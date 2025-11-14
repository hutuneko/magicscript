package com.hutuneko.magicscript.api.script;

import com.hutuneko.magicscript.antlr4.*;
import com.hutuneko.magicscript.api.magic.MagicAPIRegistry;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MagicScriptInterpreter extends MagicScriptBaseVisitor<Object> {

    private final MagicScriptContext context;
    private final ServerPlayer player;

    public MagicScriptInterpreter(ServerPlayer player) {
        this.player = player;
        this.context = new MagicScriptContext();
    }

    // 変数宣言
    @Override
    public Object visitVariableDeclaration(MagicScriptParser.VariableDeclarationContext ctx) {
        String name = ctx.Identifier().getText();
        Object value = visit(ctx.expression());
        context.define(name, value);
        return null;
    }

    // 代入
    @Override
    public Object visitAssignment(MagicScriptParser.AssignmentContext ctx) {
        String name = ctx.Identifier().getText();
        Object value = visit(ctx.expression());
        context.set(name, value);
        return value;
    }

    // if 文
    @Override
    public Object visitIfStatement(MagicScriptParser.IfStatementContext ctx) {
        boolean cond = (boolean) visit(ctx.expression());
        if (cond) {
            visit(ctx.statement(0));
        } else if (ctx.statement().size() > 1) {
            visit(ctx.statement(1));
        }
        return null;
    }

    // while
    @Override
    public Object visitWhileStatement(MagicScriptParser.WhileStatementContext ctx) {
        while ((boolean) visit(ctx.expression())) {
            visit(ctx.statement());
        }
        return null;
    }

    // 関数呼び出し（魔法発動）
    @Override
    public Object visitFunctionCall(MagicScriptParser.FunctionCallContext ctx) {
        String name = ctx.Identifier().getText();

        List<Object> args = new ArrayList<>();
        if (ctx.argumentList() != null) {
            for (var e : ctx.argumentList().expression()) {
                args.add(visit(e));
            }
        }

        return MagicAPIRegistry.call(player, name, args);
    }

    // リテラル
    @Override
    public Object visitLiteralExpr(MagicScriptParser.LiteralExprContext ctx) {
        return MagicLiteralConverter.convert(ctx.literal());
    }

    // 2項演算
    @Override
    public Object visitAddSubExpr(MagicScriptParser.AddSubExprContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));
        return MagicBinary.compute(left, right, ctx.op.getText());
    }
}
