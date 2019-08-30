package com.example.examplemod.tiles;

import static com.example.examplemod.blocks.ModBlocks.TESTBLOCK_TILE;

import javax.annotation.Nonnull;

import com.example.examplemod.blocks.ModBlocks;
import com.example.examplemod.containers.TestBlockContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("unchecked")
public class TestBlockTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

	public TestBlockTile() {
		super(TESTBLOCK_TILE);
	}

	@Override
	public void tick() {
	}

	@Override
	public void read(CompoundNBT compound) {
		CompoundNBT compoundNBT = compound.getCompound("inv");
		handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compoundNBT));
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		handler.ifPresent(h -> {
			CompoundNBT compoundNBT = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("inv", compoundNBT);
		});
		return super.write(compound);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	public IItemHandler createHandler() {
		return new ItemStackHandler(1) {

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

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
		return new TestBlockContainer(ModBlocks.TESTBLOCK_CONTAINER, id, world, pos, inv, player);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent(getType().getRegistryName().getPath());
	}

}
