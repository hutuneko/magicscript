package com.hutuneko.magicscript.api.script;

import com.hutuneko.magicscript.antlr4.MagicScriptLexer;
import com.hutuneko.magicscript.antlr4.MagicScriptParser;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class MagicScriptEngine {

    public static void execute(ServerPlayer player, String sourceLines) {

        try {
            // 全文を ANTLR に食わせる
            MagicScriptLexer lexer = new MagicScriptLexer(CharStreams.fromString(sourceLines));
            MagicScriptParser parser = new MagicScriptParser(new CommonTokenStream(lexer));

            MagicScriptParser.ProgramContext tree = parser.program();

            MagicScriptInterpreter interpreter = new MagicScriptInterpreter(player);
            interpreter.visit(tree);

        } catch (Exception e) {
            player.sendSystemMessage(Component.literal("§c[MagicScript] Error: " + e.getMessage()));
        }
    }
}

