
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.hutuneko.magicscript.block.gui;

import com.hutuneko.magicscript.Magicscript;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;


public class ModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Magicscript.MODID);
	public static final RegistryObject<MenuType<MagicScriptTerminalMenu>> MAGIC_SCRIPT_TERMINAL = REGISTRY.register("magic_script_terminal", () -> IForgeMenuType.create(MagicScriptTerminalMenu::new));
}
