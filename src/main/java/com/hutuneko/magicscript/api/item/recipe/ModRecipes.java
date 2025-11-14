package com.hutuneko.magicscript.api.item.recipe;

import com.hutuneko.magicscript.Magicscript;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Magicscript.MODID);

    public static final RegistryObject<RecipeSerializer<MagicScriptBookRecipe>> MAGIC_SCRIPT_BOOK_SERIALIZER =
            SERIALIZERS.register("magic_script_book", MagicScriptBookRecipeSerializer::new);
}
