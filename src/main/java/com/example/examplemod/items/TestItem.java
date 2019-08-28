package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;

import net.minecraft.item.Item;

public class TestItem extends Item {

	public TestItem() {
		super(new Item.Properties().maxStackSize(1).group(ExampleMod.setup.itemGroup));
		setRegistryName("testitem");
	}
}
