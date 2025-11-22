package com.hutuneko.magicscript.block;

import com.hutuneko.magicscript.block.gui.MagicScriptTerminalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MagicScriptTerminalBlockEntity extends BlockEntity implements MenuProvider {

    private String code = "";

    public MagicScriptTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_SCRIPT_TERMINAL.get(), pos, state);
    }

    // GUI のタイトル
    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("MagicScript Terminal");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new MagicScriptTerminalMenu(id, inv);
    }

    // NBT 保存
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Code", code);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        code = tag.getString("Code");
    }

    public String getCode() { return code; }
    public void setCode(String c) { this.code = c; setChanged(); }
}

