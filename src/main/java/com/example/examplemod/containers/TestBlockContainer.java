package com.example.examplemod.containers;

import com.example.examplemod.blocks.ModBlocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TestBlockContainer extends Container {
	private TileEntity tileEntity;
	private PlayerEntity player;
	private IItemHandler playerInv;

	public TestBlockContainer(ContainerType<?> type, int id, World world, BlockPos blockPos, PlayerInventory playerInv,
			PlayerEntity player) {
		super(type, id);

		this.player = player;
		this.playerInv = new InvWrapper(playerInv);

		tileEntity = world.getTileEntity(blockPos);
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, 0, 80, 28));
		});

		layoutPlayerInventorySlots(8, 84);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), player,
				ModBlocks.TESTBLOCK);
	}

	private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			addSlot(new SlotItemHandler(handler, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount,
			int dy) {
		for (int i = 0; i < verAmount; i++) {
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	private void layoutPlayerInventorySlots(int leftCol, int topRow) {
		addSlotBox(playerInv, 9, leftCol, topRow, 9, 18, 3, 18);
		topRow += 58;
		addSlotRange(playerInv, 0, leftCol, topRow, 9, 18);
	}
}
