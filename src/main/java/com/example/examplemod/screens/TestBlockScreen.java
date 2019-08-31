package com.example.examplemod.screens;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.containers.TestBlockContainer;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TestBlockScreen extends ContainerScreen<TestBlockContainer> {

	private ResourceLocation GUI = new ResourceLocation(ExampleMod.MODID, "textures/gui/testblock_gui.png");

	public TestBlockScreen(TestBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	public void render(int mouseX, int mousey, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mousey, partialTicks);
		this.renderHoveredToolTip(mouseX, mousey);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
		this.font.drawString("Energy: " + container.getEnergy(), 8.0F, 16.0F, 4210752);

		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F,
				(float) (this.ySize - 96 + 2), 4210752);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
	}
}
