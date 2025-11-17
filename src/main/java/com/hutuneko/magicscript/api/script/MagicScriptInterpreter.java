package com.hutuneko.magicscript.api.script;

import com.hutuneko.magicscript.antlr4.*;
import com.hutuneko.magicscript.api.MagicPos;
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
        String typeName = ctx.type() != null ? ctx.type().getText() : null;
        String name = ctx.Identifier().getText();
        Object value = visit(ctx.expression());

        if ("pos".equals(typeName)) {
            if (!(value instanceof MagicPos))
                throw new RuntimeException("Expected pos value");
        }
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

        var argList = ctx.argumentList();
        if (argList != null) {
            for (var expr : argList.expr) {
                args.add(visit(expr));
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
    @Override
    public Object visitLogicAndExpr(MagicScriptParser.LogicAndExprContext ctx) {
        Object left = visit(ctx.expression(0));
        if (!(left instanceof Boolean))
            throw new RuntimeException("Left side of && is not boolean");

        boolean l = (boolean) left;
        if (!l) return false; // 短絡評価

        Object right = visit(ctx.expression(1));
        if (!(right instanceof Boolean))
            throw new RuntimeException("Right side of && is not boolean");

        return right;
    }
    @Override
    public Object visitLogicOrExpr(MagicScriptParser.LogicOrExprContext ctx) {
        Object left = visit(ctx.expression(0));
        if (!(left instanceof Boolean))
            throw new RuntimeException("Left side of || is not boolean");

        boolean l = (boolean) left;
        if (l) return true; // 短絡評価

        Object right = visit(ctx.expression(1));
        if (!(right instanceof Boolean))
            throw new RuntimeException("Right side of || is not boolean");

        return right;
    }
    @Override
    public Object visitPosLiteralExpr(MagicScriptParser.PosLiteralExprContext ctx) {

        MagicScriptParser.PosLiteralContext p = ctx.posLiteral();

        Object xObj = visit(p.expression(0));
        Object yObj = visit(p.expression(1));
        Object zObj = visit(p.expression(2));

        int x = toInt(xObj);
        int y = toInt(yObj);
        int z = toInt(zObj);

        System.out.println("RAW X = " + xObj);
        System.out.println("INT X = " + x);

        return new MagicPos(x, y, z);
    }

    private int toInt(Object val) {
        if (val instanceof Integer i) return i;
        if (val instanceof Double d) return (int)d.doubleValue();
        if (val instanceof Float f) return (int)f.floatValue();
        if (val instanceof String s) return Integer.parseInt(s);
        throw new RuntimeException("Cannot convert: " + val);
    }


    @Override
    public Object visitUnaryMinusExpr(MagicScriptParser.UnaryMinusExprContext ctx) {
        Object value = visit(ctx.expression());
        if (value instanceof Number num) {
            // int と float の両方に対応
            if (value instanceof Integer) {
                return -num.intValue();
            }
            return -num.doubleValue();
        }
        throw new RuntimeException("Unary minus on non-number: " + value);
    }
    @Override
    public Object visitIdentifierExpr(MagicScriptParser.IdentifierExprContext ctx) {
        String name = ctx.Identifier().getText();

        if (!context.contains(name)) {
            throw new RuntimeException("Undefined variable: " + name);
        }

        return context.get(name);
    }


}
