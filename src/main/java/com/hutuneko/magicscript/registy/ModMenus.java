
package com.hutuneko.magicscript.registy;

import com.hutuneko.magicscript.Magicscript;
import com.hutuneko.magicscript.block.ScriptEnchanterMenu;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;


public class ModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Magicscript.MODID);
    public static final RegistryObject<MenuType<ScriptEnchanterMenu>> SCRIPT_ENCHANTER_MENU = REGISTRY.register("script_enchanter", () -> IForgeMenuType.create(ScriptEnchanterMenu::new));
}
