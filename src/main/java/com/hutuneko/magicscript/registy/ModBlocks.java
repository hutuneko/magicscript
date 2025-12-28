package com.hutuneko.magicscript.registy;

import com.hutuneko.magicscript.Magicscript;
import com.hutuneko.magicscript.block.ScriptEnchanterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Magicscript.MODID);
    public static final RegistryObject<Block> SCRIPT_ENCHANTER =
            BLOCKS.register("script_enchanter",
                    () -> new ScriptEnchanterBlock());
}
