package com.hutuneko.magicscript.api.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;

public class MagicAPIRegistry {

    private static final Map<String, IMagicFunction> FUNCTIONS = new HashMap<>();

    public static void register(String name, IMagicFunction func) {
        FUNCTIONS.put(name, func);
    }

    public static Object call(ServerPlayer player, String name, List<Object> args) {
        IMagicFunction func = FUNCTIONS.get(name);
        if (func == null)
            throw new RuntimeException("Unknown magic function: " + name);

        return func.invoke(player, args);
    }
}
