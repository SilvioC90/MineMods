package com.example.examplemod.containers;

import com.example.examplemod.blocks.ModBlocks;
import com.example.examplemod.tools.CustomEnergyStorage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
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

		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return getEnergy();
			}

			@Override
			public void set(int value) {
				tileEntity.getCapability(CapabilityEnergy.ENERGY)
						.ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
			}
		});
	}

	public int getEnergy() {
		return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), player,
				ModBlocks.TESTBLOCK);
	}

	// Permette il trasferimento degli stack di oggetti dall'inventario al blocco
	// facendo shift + click
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			itemStack = stack.copy();
			if (index == 0) {
				if (!this.mergeItemStack(stack, 1, 37, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack, itemStack);
			} else {
				if (stack.getItem() == Items.DIAMOND) {
					if (!this.mergeItemStack(stack, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index < 28) {
					if (!this.mergeItemStack(stack, 28, 37, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index < 37 && !this.mergeItemStack(stack, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stack.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, stack);
		}

		return itemStack;
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
