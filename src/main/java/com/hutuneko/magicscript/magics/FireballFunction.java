package com.hutuneko.magicscript.magics;

import com.hutuneko.magicscript.api.magic.IMagicFunction;
import com.hutuneko.magicscript.api.magic.MagicScript;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
@MagicScript("fireball")
public class FireballFunction implements IMagicFunction {

    @Override
    public Object invoke(ServerPlayer player, List<Object> args) {

        Level level = player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        level.explode(player, x, y, z, 2.0F, Level.ExplosionInteraction.MOB);

        return null;
    }
}
