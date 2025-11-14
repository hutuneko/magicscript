package com.hutuneko.magicscript.api.script;

public class MagicBinary {

    public static Object compute(Object left, Object right, String op) {
        // null safety
        if (left == null || right == null) {
            switch (op) {
                case "==" -> {
                    return left == right;
                }
                case "!=" -> {
                    return left != right;
                }
                default -> throw new RuntimeException("Null operand for operator " + op);
            }
        }

        // String concatenation for +
        if (op.equals("+") && (left instanceof String || right instanceof String)) {
            return left + String.valueOf(right);
        }

        // Numbers handling
        if (isNumber(left) && isNumber(right)) {
            double ld = toDouble(left);
            double rd = toDouble(right);

            return switch (op) {
                case "+" -> boxNumber(ld + rd);
                case "-" -> boxNumber(ld - rd);
                case "*" -> boxNumber(ld * rd);
                case "/" -> {
                    if (rd == 0) throw new RuntimeException("Division by zero");
                    yield boxNumber(ld / rd);
                }
                case ">" -> ld > rd;
                case "<" -> ld < rd;
                case ">=" -> ld >= rd;
                case "<=" -> ld <= rd;
                case "==" -> ld == rd;
                case "!=" -> ld != rd;
                default -> throw new RuntimeException("Unsupported numeric operator: " + op);
            };
        }

        // Boolean ops
        if (left instanceof Boolean && right instanceof Boolean) {
            boolean lb = (Boolean) left;
            boolean rb = (Boolean) right;
            return switch (op) {
                case "==" -> lb == rb;
                case "!=" -> lb != rb;
                default -> throw new RuntimeException("Unsupported boolean operator: " + op);
            };
        }

        // Fallback: equality for non-numeric, non-boolean
        return switch (op) {
            case "==" -> left.equals(right);
            case "!=" -> !left.equals(right);
            default -> throw new RuntimeException("Operator " + op + " not supported for types: "
                    + left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName());
        };
    }

    private static boolean isNumber(Object o) {
        return o instanceof Number;
    }

    private static double toDouble(Object o) {
        return ((Number) o).doubleValue();
    }

    private static Object boxNumber(double d) {
        if (d == Math.rint(d)) {
            long l = (long) d;
            if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) return (int) l;
            return l;
        }
        return d;
    }
}
