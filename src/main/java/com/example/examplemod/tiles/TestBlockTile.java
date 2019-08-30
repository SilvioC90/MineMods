package com.example.examplemod.tiles;

import static com.example.examplemod.blocks.ModBlocks.TESTBLOCK_TILE;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TestBlockTile extends TileEntity implements ITickableTileEntity {

	private ItemStackHandler handler;

	public TestBlockTile() {
		super(TESTBLOCK_TILE);
	}

	@Override
	public void tick() {
	}

	@Override
	public void read(CompoundNBT compound) {
		CompoundNBT compoundNBT = compound.getCompound("inv");
		getHandler().deserializeNBT(compoundNBT);
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT compoundNBT = getHandler().serializeNBT();
		compound.put("inv", compoundNBT);
		return super.write(compound);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return LazyOptional.of(() -> (T) getHandler());
		}
		return super.getCapability(cap, side);
	}

	public ItemStackHandler getHandler() {
		if (handler == null) {
			handler = new ItemStackHandler(1) {

				@Override
				public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
					return (stack.getItem() == Items.DIAMOND);
				}

				@Override
				public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
					if (stack.getItem() != Items.DIAMOND) {
						return stack;
					} else {
						return super.insertItem(slot, stack, simulate);
					}
				}
			};
		}
		return handler;
	}
}
