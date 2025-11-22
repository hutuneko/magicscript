package com.hutuneko.magicscript.block;

import com.hutuneko.magicscript.Magicscript;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Magicscript.MODID);

    public static final RegistryObject<BlockEntityType<MagicScriptTerminalBlockEntity>> MAGIC_SCRIPT_TERMINAL =
            BLOCK_ENTITIES.register("magic_script_terminal",
                    () -> BlockEntityType.Builder.of(
                            MagicScriptTerminalBlockEntity::new,
                            ModBlocks.MAGIC_SCRIPT_TERMINAL.get()
                    ).build(null)
            );
}
