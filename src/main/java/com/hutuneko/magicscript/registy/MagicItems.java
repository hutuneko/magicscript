package com.hutuneko.magicscript.registy;

import com.hutuneko.magicscript.Magicscript;
import com.hutuneko.magicscript.block.ScriptEnchanterBlock;
import com.hutuneko.magicscript.item.MagicScriptBook;
import net.minecraft.world.item.BlockItem;
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
    public static final RegistryObject<Item> SCRIPT_ENCHANTER = ITEMS.register("script_enchanter",
            () -> new BlockItem(ModBlocks.SCRIPT_ENCHANTER.get(),new Item.Properties().stacksTo(1))
    );
}
