
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.hutuneko.magicscript.registy;

import com.hutuneko.magicscript.client.block.gui.ScriptEnchanterScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenus.SCRIPT_ENCHANTER_MENU.get(), ScriptEnchanterScreen::new));

    }
}
