package com.hutuneko.magicscript;

import com.hutuneko.magicscript.api.item.recipe.ModRecipes;
import com.hutuneko.magicscript.api.magic.MagicAPIRegistry;
import com.hutuneko.magicscript.api.magic.MagicScriptRegistry;
import com.hutuneko.magicscript.item.MagicItems;
import com.hutuneko.magicscript.magics.FireballFunction;
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
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> MagicScriptRegistry.scan(Magicscript.MODID));
    }
}
