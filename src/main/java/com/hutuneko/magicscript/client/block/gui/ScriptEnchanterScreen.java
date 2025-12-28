package com.hutuneko.magicscript.client.block.gui;

import com.hutuneko.magicscript.api.MagicScriptAPI;
import com.hutuneko.magicscript.block.ScriptEnchanterMenu;
import com.hutuneko.magicscript.network.Net;
import com.hutuneko.magicscript.network.SaveScriptPacket;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ScriptEnchanterScreen extends AbstractContainerScreen<ScriptEnchanterMenu> {
    private final static HashMap<String, Object> guistate = ScriptEnchanterMenu.guistate;
    MultiLineEditBox terminal;

    public ScriptEnchanterScreen(ScriptEnchanterMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 358;
        this.imageHeight = 240;
    }

    private static final ResourceLocation texture = new ResourceLocation("magicsprict:textures/screens/magic_script_terminal.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        terminal.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            if (this.minecraft != null) {
                if (this.minecraft.player != null) {
                    this.minecraft.player.closeContainer();
                }
            }
            return true;
        }
        if (terminal.isFocused())
            return terminal.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        String terminalValue = terminal.getValue();
        super.resize(minecraft, width, height);
        terminal.setValue(terminalValue);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    // ScriptEnchanterScreen.java の init メソッド内
    @Override
    public void init() {
        super.init();
        terminal = new MultiLineEditBox(this.font, this.leftPos + 8, this.topPos + 9, 118, 120, Component.literal("terminal"),Component.literal(""));

        // Menu 経由でスロット 0 のアイテムを取得し、中身を表示する
        ItemStack book = this.menu.getSlot(0).getItem();
        String currentCode = MagicScriptAPI.pageJson(book);
        if (currentCode != null) {
            terminal.setValue(currentCode);
        }

        this.addWidget(this.terminal);
    }
    private ItemStack lastStack = ItemStack.EMPTY;

    @Override
    public void containerTick() {
        super.containerTick();
        terminal.tick();

        // スロット0のアイテムを取得
        ItemStack currentStack = this.menu.getSlot(0).getItem();
        // 前回のチェック時とアイテムが変わっていたら更新
        if (!ItemStack.matches(lastStack, currentStack)) {
            this.lastStack = currentStack.copy();
            updateTerminalFromBook(currentStack);
        }
        Net.CHANNEL.sendToServer(new SaveScriptPacket(this.menu.getPos(), terminal.getValue()));
        currentStack = this.menu.getSlot(0).getItem();
        this.lastStack = currentStack.copy();
    }

    private void updateTerminalFromBook(ItemStack stack) {
        String currentCode = MagicScriptAPI.pageJson(stack);
        if (currentCode != null) {
            terminal.setValue(currentCode);
        } else {
            terminal.setValue(""); // 本がない、またはコードがない場合は空にする
        }
    }
    public void setTerminal(){
        ItemStack book = this.menu.getSlot(0).getItem();
        String currentCode = MagicScriptAPI.pageJson(book);
        if (currentCode != null) {
            terminal.setValue(currentCode);
        }
    }
}
