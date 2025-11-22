package com.hutuneko.magicscript.block;

import com.hutuneko.magicscript.Magicscript;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Magicscript.MODID);

    public static final RegistryObject<Block> MAGIC_SCRIPT_TERMINAL =
            BLOCKS.register("magic_script_terminal",
                    () -> new MagicScriptTerminalBlock(BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .noOcclusion()
                    ));
}
