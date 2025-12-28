package com.hutuneko.magicscript.network;

import com.hutuneko.magicscript.block.ScriptEnchanterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveScriptPacket {
    private final BlockPos pos;
    private final String script;

    public SaveScriptPacket(BlockPos pos, String script) {
        this.pos = pos;
        this.script = script;
    }

    // バッファへの書き込み（送信準備）
    public static void encode(SaveScriptPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeUtf(msg.script);
    }

    // バッファからの読み込み（受信処理）
    public static SaveScriptPacket decode(FriendlyByteBuf buf) {
        return new SaveScriptPacket(buf.readBlockPos(), buf.readUtf());
    }

    public static void handle(SaveScriptPacket msg, Supplier<NetworkEvent.Context> ctxGetter) {
        NetworkEvent.Context ctx = ctxGetter.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                BlockEntity be = player.level().getBlockEntity(msg.pos);
                if (be instanceof ScriptEnchanterBlockEntity enchanter) {
                    // BlockEntityに保存するメソッドを呼び出し
                    enchanter.updateScript(msg.script);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}