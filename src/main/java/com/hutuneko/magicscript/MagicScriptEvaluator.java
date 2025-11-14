package com.hutuneko.magicscript;

import com.hutuneko.magicscript.antlr4.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.HashMap;
import java.util.Map;

public class MagicScriptEvaluator extends MagicScriptBaseVisitor<Object> {
    private final Map<String, Object> variables = new HashMap<>();

    @Override
    public Object visitVariableDeclaration(MagicScriptParser.VariableDeclarationContext ctx) {
        String name = ctx.getText();
        Object value = visit(ctx.expression());
        variables.put(name, value);
        return null;
    }

    @Override
    public Object visitAssignment(MagicScriptParser.AssignmentContext ctx) {
        String name = ctx.getText();
        Object value = visit(ctx.expression());
        variables.put(name, value);
        return null;
    }

    @Override
    public Object visitLiteral(MagicScriptParser.LiteralContext ctx) {
        return Integer.parseInt(ctx.getText());
    }

//    @Override
    public Object visitExpression(MagicScriptParser.ExpressionContext ctx) {
        // 簡易的な二項演算子処理の例
        if (ctx.children.size() == 3) {
            Object left = visit(ctx.getChild(0));
            String op = ctx.getChild(1).getText();
            Object right = visit(ctx.getChild(2));
            if (op.equals("+")) return (int) left + (int) right;
            if (op.equals(">")) return (int) left > (int) right;
        }
        return visitChildren(ctx);
    }
}
