package com.hutuneko.magicscript.api.script;

import com.hutuneko.magicscript.antlr4.MagicScriptLexer;
import com.hutuneko.magicscript.antlr4.MagicScriptParser;
import net.minecraft.server.level.ServerPlayer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class MagicScriptEngine {

    public static void run(ServerPlayer player, String script) {
        MagicScriptLexer lexer = new MagicScriptLexer(CharStreams.fromString(script));
        MagicScriptParser parser = new MagicScriptParser(new CommonTokenStream(lexer));

        ParseTree tree = parser.program();

        MagicScriptInterpreter interpreter = new MagicScriptInterpreter(player);
        interpreter.visit(tree);
    }
}
