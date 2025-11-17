package com.hutuneko.magicscript.api;

import net.minecraft.core.BlockPos;

public class MagicPos {
    public int x;
    public int y;
    public int z;

    public MagicPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }
}
