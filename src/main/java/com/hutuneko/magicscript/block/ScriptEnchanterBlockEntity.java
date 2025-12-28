package com.hutuneko.magicscript.block;

import com.hutuneko.magicscript.registy.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptEnchanterBlockEntity extends BlockEntity implements MenuProvider {
    // ItemStackフィールドの代わりに、Handlerを使う（同期に強い）
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ScriptEnchanterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCRIPT_ENCHANTER.get(), pos, state);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Script Enchanter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new ScriptEnchanterMenu(id, inv, this);
    }

    // Capabilityの追加 (外部からアイテムを出し入れ可能にする)
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return LazyOptional.of(() -> itemHandler).cast();
        return super.getCapability(cap, side);
    }
    // ScriptEnchanterBlockEntity.java に追加
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        // 保存されたアイテムデータを読み込む
        if (tag.contains("Inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        // アイテムデータをNBTに書き込む
        tag.put("Inventory", itemHandler.serializeNBT());
    }
    public void updateScript(String newCode) {
        ItemStack bookStack = itemHandler.getStackInSlot(0); // 0番スロットから取得
        if (!bookStack.isEmpty()) {
            CompoundTag tag = bookStack.getOrCreateTag();
            ListTag pages = new ListTag();
            pages.add(StringTag.valueOf("{\"text\":\"" + newCode + "\"}"));
            tag.put("pages", pages);
            setChanged();
            // クライアントへ通知
            if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}