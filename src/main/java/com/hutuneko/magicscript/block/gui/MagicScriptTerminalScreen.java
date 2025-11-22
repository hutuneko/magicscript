package com.hutuneko.magicscript.block.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.NotNull;

public class MagicScriptTerminalScreen extends AbstractContainerScreen<MagicScriptTerminalMenu> {
	private final static HashMap<String, Object> guistate = MagicScriptTerminalMenu.guistate;
    EditBox terminal;

	public MagicScriptTerminalScreen(MagicScriptTerminalMenu container, Inventory inventory, Component text) {
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
	public void containerTick() {
		super.containerTick();
		terminal.tick();
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

	@Override
	public void init() {
		super.init();
		terminal = new EditBox(this.font, this.leftPos + 8, this.topPos + 9, 118, 18, Component.translatable("gui.magicsprict.magic_script_terminal.terminal"));
		terminal.setMaxLength(32767);
		guistate.put("text:terminal", terminal);
		this.addWidget(this.terminal);
	}
}
