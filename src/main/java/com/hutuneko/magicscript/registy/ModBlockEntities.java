package com.hutuneko.magicscript.registy;

import com.hutuneko.magicscript.Magicscript;
import com.hutuneko.magicscript.block.ScriptEnchanterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Magicscript.MODID);
    public static final RegistryObject<BlockEntityType<ScriptEnchanterBlockEntity>> SCRIPT_ENCHANTER =
            BLOCK_ENTITIES.register("script_enchanter",
                    () -> BlockEntityType.Builder.of(
                            ScriptEnchanterBlockEntity::new,
                            ModBlocks.SCRIPT_ENCHANTER.get()
                    ).build(null)
            );
}
