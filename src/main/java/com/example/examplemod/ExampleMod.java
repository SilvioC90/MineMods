package com.example.examplemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.examplemod.blocks.ModBlocks;
import com.example.examplemod.blocks.TestBlock;
import com.example.examplemod.containers.TestBlockContainer;
import com.example.examplemod.items.CustomPickaxeItem;
import com.example.examplemod.items.TestItem;
import com.example.examplemod.materials.ModToolMaterials;
import com.example.examplemod.setup.ClientProxy;
import com.example.examplemod.setup.IProxy;
import com.example.examplemod.setup.ModSetup;
import com.example.examplemod.setup.ServerProxy;
import com.example.examplemod.tiles.TestBlockTile;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod {
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "examplemod";
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
	public static ModSetup setup = new ModSetup();

	public ExampleMod() {
		LOGGER.info("Setting up mod!");
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	private void setup(final FMLCommonSetupEvent event) {
		setup.init();
		proxy.init();
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			blockRegistryEvent.getRegistry().register(new TestBlock());
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> blockRegistryEvent) {
			Item.Properties properties = new Item.Properties().group(setup.itemGroup);
			blockRegistryEvent.getRegistry().register(
					new BlockItem(ModBlocks.TESTBLOCK, properties).setRegistryName("examplemod", "testblock"));

			blockRegistryEvent.getRegistry().register(new CustomPickaxeItem(ModToolMaterials.tutorial, -2, 6.0f,
					new Item.Properties().group(setup.itemGroup)).setRegistryName("examplemod", "testpickaxe"));

			blockRegistryEvent.getRegistry().register(new TestItem());
		}

		@SubscribeEvent
		public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
			event.getRegistry().register(TileEntityType.Builder.create(TestBlockTile::new, ModBlocks.TESTBLOCK)
					.build(null).setRegistryName("examplemod", "testblock"));

		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
			event.getRegistry().register(IForgeContainerType.create((windowID, inv, data) -> {
				BlockPos blockPos = data.readBlockPos();
				return new TestBlockContainer(ModBlocks.TESTBLOCK_CONTAINER, windowID, proxy.getClientWorld(), blockPos,
						inv, proxy.getClientPlayer());
			}).setRegistryName("examplemod", "testblock"));
		}
	}
}
