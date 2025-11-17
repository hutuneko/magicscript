package com.hutuneko.magicscript.item;

import com.hutuneko.magicscript.api.MagicScriptAPI;
import com.hutuneko.magicscript.api.script.MagicScriptEngine;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MagicScriptBook extends WrittenBookItem {
    public MagicScriptBook(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(!player.isShiftKeyDown()){
            if (!player.level().isClientSide()){
                ServerPlayer sp = (ServerPlayer) player;

                String script = MagicScriptAPI.pageJson(stack);
                if (script == null || script.isEmpty()) {
                    player.displayClientMessage(Component.literal("スクリプトが書かれていません"), true);
                    return InteractionResultHolder.fail(stack);
                }
                System.out.println(script);
                MagicScriptEngine.execute(sp, script);
            }
        }else {
            super.use(level,player,hand);
        }
        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide());
    }
}
