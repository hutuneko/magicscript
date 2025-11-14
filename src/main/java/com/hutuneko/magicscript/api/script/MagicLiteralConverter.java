package com.hutuneko.magicscript.api.script;

import com.hutuneko.magicscript.antlr4.MagicScriptParser;

public class MagicLiteralConverter {

    public static Object convert(MagicScriptParser.LiteralContext ctx) {
        if (ctx == null) return null;

        if (ctx.Number() != null) {
            String num = ctx.Number().getText();
            if (num.contains(".")) {
                try {
                    double d = Double.parseDouble(num);
                    return boxNumber(d);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number literal: " + num);
                }
            } else {
                try {
                    long l = Long.parseLong(num);
                    if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
                        return (int) l;
                    } else {
                        return l;
                    }
                } catch (NumberFormatException e) {
                    try {
                        double d = Double.parseDouble(num);
                        return boxNumber(d);
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException("Invalid number literal: " + num);
                    }
                }
            }
        }

        if (ctx.StringLiteral() != null) {
            String raw = ctx.StringLiteral().getText();
            // remove surrounding quotes (lexer includes them)
            if (raw.length() >= 2 && raw.charAt(0) == '"' && raw.charAt(raw.length() - 1) == '"') {
                raw = raw.substring(1, raw.length() - 1);
            }
            return unescapeString(raw);
        }

        if (ctx.BooleanLiteral() != null) {
            String b = ctx.BooleanLiteral().getText();
            return Boolean.parseBoolean(b);
        }

        throw new RuntimeException("Unknown literal type: " + ctx.getText());
    }

    private static Object boxNumber(double d) {
        if (d == Math.rint(d)) {
            // integral
            long l = (long) d;
            if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) return (int) l;
            return l;
        }
        return d;
    }

    private static String unescapeString(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                switch (n) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    default -> sb.append(n); // unknown escape, keep char
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
