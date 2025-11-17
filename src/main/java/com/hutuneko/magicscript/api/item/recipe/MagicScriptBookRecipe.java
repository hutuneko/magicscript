package com.hutuneko.magicscript.api.item.recipe;

import com.hutuneko.magicscript.item.MagicItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MagicScriptBookRecipe implements CraftingRecipe {

    public static final ResourceLocation ID = new ResourceLocation("magicscript:script_book");

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        boolean foundBook = false;
        boolean foundCatalyst = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.is(Items.WRITTEN_BOOK)) {
                foundBook = true;
            } else if (stack.is(Items.AMETHYST_SHARD)) {
                foundCatalyst = true;
            }
        }

        return foundBook && foundCatalyst;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer inv, @NotNull RegistryAccess access) {
        ItemStack book = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.is(Items.WRITTEN_BOOK)) {
                book = stack;
                break;
            }
        }

        if (book.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = new ItemStack(MagicItems.MAGIC_SCRIPTBOOK.get());

        // NBTコピー
        if (book.hasTag()) {
            if (book.getTag() != null) {
//                String json = Component.Serializer.toJson(Component.literal(book.getTag().get("pages").toString()));
//                System.out.println(json);
                CompoundTag tag = book.getTag();
                result.setTag(tag);
            }
        }

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.MAGIC_SCRIPT_BOOK_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return new ItemStack(MagicItems.MAGIC_SCRIPTBOOK.get());
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

}
