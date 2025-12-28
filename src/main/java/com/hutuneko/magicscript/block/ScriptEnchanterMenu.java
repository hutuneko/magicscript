
package com.hutuneko.magicscript.block;

import com.hutuneko.magicscript.item.MagicWritableBook;
import com.hutuneko.magicscript.registy.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

public class ScriptEnchanterMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public final static HashMap<String, Object> guistate = new HashMap<>();
    public final Level world;
    public final Player entity;
    private final ContainerLevelAccess access = ContainerLevelAccess.NULL;
    private IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private final boolean bound = false;
    private Supplier<Boolean> boundItemMatcher = null;
    private Entity boundEntity = null;
    private BlockEntity boundBlockEntity = null;

    // クライアント側用コンストラクタ (FriendlyByteBuf extraData を引数に持つ)
    public ScriptEnchanterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        // extraDataから送られてきた座標を読み取って、サーバー用コンストラクタに渡す
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    // サーバー・クライアント共通のメインコンストラクタ
    public ScriptEnchanterMenu(int id, Inventory inv, BlockEntity blockEntity) {
        super(ModMenus.SCRIPT_ENCHANTER_MENU.get(), id);
        this.world = inv.player.level();
        this.entity = inv.player;
        // BlockEntityの有無を確認
        if (blockEntity instanceof ScriptEnchanterBlockEntity enchanter) {
            boundBlockEntity = blockEntity;
            enchanter.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                this.internal = handler;
                // 魔法の本を入れるスロット (座標 80, 35)
                this.addSlot(new SlotItemHandler(handler, 0, 224, 44));
            });
        } else {
            // blockEntityがnull（クライアント側での初期化失敗時など）のフォールバック
            this.internal = new ItemStackHandler(1) {
                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    // 魔法の本（あるいはWritableBook）以外は入らないように制限
                    return stack.getItem() instanceof MagicWritableBook;
                }
            };
        }

        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 192 + sj * 18, 156 + si * 18));

        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 192 + si * 18, 214));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.bound) {
            if (this.boundItemMatcher != null)
                return this.boundItemMatcher.get();
            else if (this.boundBlockEntity != null)
                return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
            else if (this.boundEntity != null)
                return this.boundEntity.isAlive();
        }
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                if (index < 2 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 2 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemstack1, 2, 2 + 27, false))
                        return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(@NotNull ItemStack stack, int x, int y, boolean b) {
        boolean flag = false;
        int i = x;
        if (b) {
            i = y - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (b) {
                    if (i < x) {
                        break;
                    }
                } else if (i >= y) {
                    break;
                }
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.set(itemstack);
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.set(itemstack);
                        flag = true;
                    }
                }
                if (b) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        if (!stack.isEmpty()) {
            if (b) {
                i = y - 1;
            } else {
                i = x;
            }
            while (true) {
                if (b) {
                    if (i < x) {
                        break;
                    }
                } else if (i >= y) {
                    break;
                }
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                if (b) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        return flag;
    }

    public Map<Integer, Slot> get() {
        return customSlots;
    }
    public BlockPos getPos(){
        return boundBlockEntity.getBlockPos();
    }
    private final ItemStackHandler ItemHandler = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // 魔法の本（あるいはWritableBook）以外は入らないように制限
            return stack.getItem() instanceof MagicWritableBook;
        }
    };
}
