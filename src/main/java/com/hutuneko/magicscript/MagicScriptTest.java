package com.hutuneko.magicscript;

import com.hutuneko.magicscript.antlr4.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MagicScriptTest {
    public static void main(String[] args) throws Exception {
        String code = "var x = 10; if (x > 5) { x = x + 1; }";

        // ANTLRの標準処理
        CharStream input = CharStreams.fromString(code);
        MagicScriptLexer lexer = new MagicScriptLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MagicScriptParser parser = new MagicScriptParser(tokens);

        // エラーハンドリングも追加しておくと便利
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.err.println("構文エラー: line " + line + ":" + charPositionInLine + " " + msg);
            }
        });

        // 構文木生成
        ParseTree tree = parser.program();

        // 結果を出力
        System.out.println(tree.toStringTree(parser));
    }
}
