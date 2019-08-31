package com.example.examplemod.tools;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

	public CustomEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void addEnergy(int energy) {
		this.energy += energy;
		if (this.energy > getMaxEnergyStored()) {
			this.energy = getEnergyStored();
		}
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT compoundNBT = new CompoundNBT();
		compoundNBT.putInt("energy", getEnergyStored());
		return compoundNBT;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setEnergy(nbt.getInt("energy"));
	}
}
