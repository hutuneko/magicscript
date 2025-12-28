package com.hutuneko.magicscript;

import com.hutuneko.magicscript.api.item.recipe.ModRecipes;
import com.hutuneko.magicscript.api.magic.MagicScriptRegistry;
import com.hutuneko.magicscript.client.block.gui.ScriptEnchanterScreen;
import com.hutuneko.magicscript.registy.ModBlockEntities;
import com.hutuneko.magicscript.registy.ModBlocks;
import com.hutuneko.magicscript.registy.MagicItems;
import com.hutuneko.magicscript.registy.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Magicscript.MODID)
public class Magicscript {

    public static final String MODID = "magicscript";
    public Magicscript() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MagicItems.ITEMS.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.REGISTRY.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MagicScriptRegistry.scan(Magicscript.MODID);
        });
    }
}
