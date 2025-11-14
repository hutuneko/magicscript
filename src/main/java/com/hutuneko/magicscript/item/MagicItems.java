package com.hutuneko.magicscript.item;

import com.hutuneko.magicscript.Magicscript;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MagicItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Magicscript.MODID);
    public static final RegistryObject<Item> MAGIC_SCRIPTBOOK = ITEMS.register("magic_scriptbook",
            () -> new MagicScriptBook(new Item.Properties().stacksTo(1))
    );

}
