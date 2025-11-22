package com.hutuneko.magicscript;

import com.hutuneko.magicscript.api.item.recipe.ModRecipes;
import com.hutuneko.magicscript.api.magic.MagicScriptRegistry;
import com.hutuneko.magicscript.block.ModBlockEntities;
import com.hutuneko.magicscript.block.ModBlocks;
import com.hutuneko.magicscript.block.gui.ModMenus;
import com.hutuneko.magicscript.item.MagicItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Magicscript.MODID)
public class Magicscript {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "magicscript";
    public Magicscript() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MagicItems.ITEMS.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.REGISTRY.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> MagicScriptRegistry.scan(Magicscript.MODID));
    }
}
