package com.hutuneko.magicscript.api.magic;

import net.minecraft.server.level.ServerPlayer;
import java.util.List;

public interface IMagicFunction {
    Object invoke(ServerPlayer player, List<Object> args);
}
