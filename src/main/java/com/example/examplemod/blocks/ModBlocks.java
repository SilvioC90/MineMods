package com.example.examplemod.blocks;

import com.example.examplemod.containers.TestBlockContainer;
import com.example.examplemod.tiles.TestBlockTile;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

	@ObjectHolder("examplemod:testblock")
	public static TestBlock TESTBLOCK;

	@ObjectHolder("examplemod:testblock")
	public static TileEntityType<TestBlockTile> TESTBLOCK_TILE;

	@ObjectHolder("examplemod:testblock")
	public static ContainerType<TestBlockContainer> TESTBLOCK_CONTAINER;
}
