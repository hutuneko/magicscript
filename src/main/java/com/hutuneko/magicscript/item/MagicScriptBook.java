package com.hutuneko.magicscript.item;

import com.hutuneko.magicscript.api.MagicScriptAPI;
import com.hutuneko.magicscript.api.script.MagicScriptEngine;
import com.hutuneko.magicscript.block.ScriptEnchanterBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class MagicScriptBook extends WrittenBookItem {
    public MagicScriptBook(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockState state = level.getBlockState(hit.getBlockPos());
            if (state.getBlock() instanceof ScriptEnchanterBlock) {
                return InteractionResultHolder.pass(player.getItemInHand(hand)); // アイテムは何もしない
            }
        }else if (!player.isShiftKeyDown()) {
            if (!player.level().isClientSide()) {
                ServerPlayer sp = (ServerPlayer) player;

                String script = MagicScriptAPI.page(stack);
                if (script == null || script.isEmpty()) {
                    player.displayClientMessage(Component.literal("スクリプトが書かれていません"), true);
                    return InteractionResultHolder.fail(stack);
                }

                MagicScriptEngine.execute(sp, script);
            }
        } else {
            super.use(level, player, hand);
        }

        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide());
    }
}
