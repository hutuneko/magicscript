package com.hutuneko.magicscript.magics;

import com.hutuneko.magicscript.api.MagicPos;
import com.hutuneko.magicscript.api.magic.IMagicFunction;
import com.hutuneko.magicscript.api.magic.MagicScript;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

import java.util.List;

@MagicScript("lightning")
public class LightningFunction implements IMagicFunction {
    @Override
    public Object invoke(ServerPlayer player, List<Object> args) {
        // 引数は 1 つ（Pos のみ）
        if (args.size() != 1) {
            return null;
        }

        Object posObj = args.get(0);

        if (!(posObj instanceof MagicPos pos)) {
            return null;
        }

        int x = pos.x;
        int y = pos.y;
        int z = pos.z;
        Level level = player.level();
        if (level.isClientSide()) return null;

        ServerLevel sl = (ServerLevel) level;

        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
        if (bolt == null) return null;

        bolt.setPos(x, y, z);
        sl.addFreshEntity(bolt);

        return null;
    }

}
