package com.example.examplemod.setup;

import net.minecraft.world.World;

public interface IProxy {

	public void init();

	World getClientWorld();
}
