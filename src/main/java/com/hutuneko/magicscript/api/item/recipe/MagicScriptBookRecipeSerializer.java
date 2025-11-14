package com.hutuneko.magicscript.api.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class MagicScriptBookRecipeSerializer implements RecipeSerializer<MagicScriptBookRecipe> {

    @Override
    public @NotNull MagicScriptBookRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        return new MagicScriptBookRecipe();
    }

    @Override
    public MagicScriptBookRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        return new MagicScriptBookRecipe();
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull MagicScriptBookRecipe recipe) {
        // 何も送らなくていい
    }
}
