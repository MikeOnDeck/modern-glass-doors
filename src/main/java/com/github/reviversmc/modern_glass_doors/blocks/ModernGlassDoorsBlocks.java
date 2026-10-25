package com.github.reviversmc.modern_glass_doors.blocks;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.event.registry.FabricRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.github.reviversmc.modern_glass_doors.ModernGlassDoors;

public class ModernGlassDoorsBlocks {
	private static final String LEGACY_NAMESPACE = "glassdoor";

	public static final List<GlassDoorBlock> GLASS_DOORS = new ArrayList<>();
	public static final List<GlassTrapdoorBlock> GLASS_TRAPDOORS = new ArrayList<>();

	// Glass doors
	public static Block OAK_GLASS_DOOR;
	public static Block SPRUCE_GLASS_DOOR;
	public static Block BIRCH_GLASS_DOOR;
	public static Block ACACIA_GLASS_DOOR;
	public static Block JUNGLE_GLASS_DOOR;
	public static Block DARK_OAK_GLASS_DOOR;
	public static Block CRIMSON_GLASS_DOOR;
	public static Block WARPED_GLASS_DOOR;
	public static Block IRON_GLASS_DOOR;
	public static Block MANGROVE_GLASS_DOOR;
	public static Block BAMBOO_GLASS_DOOR;
	public static Block CHERRY_GLASS_DOOR;

	// Glass trapdoors
	public static Block OAK_GLASS_TRAPDOOR;
	public static Block SPRUCE_GLASS_TRAPDOOR;
	public static Block BIRCH_GLASS_TRAPDOOR;
	public static Block ACACIA_GLASS_TRAPDOOR;
	public static Block JUNGLE_GLASS_TRAPDOOR;
	public static Block DARK_OAK_GLASS_TRAPDOOR;
	public static Block CRIMSON_GLASS_TRAPDOOR;
	public static Block WARPED_GLASS_TRAPDOOR;
	public static Block IRON_GLASS_TRAPDOOR;
	public static Block MANGROVE_GLASS_TRAPDOOR;
	public static Block BAMBOO_GLASS_TRAPDOOR;
	public static Block CHERRY_GLASS_TRAPDOOR;

	public static void register() {
		// Glass doors
		OAK_GLASS_DOOR = registerDoor("oak", MaterialCategory.WOOD, Blocks.OAK_DOOR);
		SPRUCE_GLASS_DOOR = registerDoor("spruce", MaterialCategory.WOOD, Blocks.SPRUCE_DOOR);
		BIRCH_GLASS_DOOR = registerDoor("birch", MaterialCategory.WOOD, Blocks.BIRCH_DOOR);
		ACACIA_GLASS_DOOR = registerDoor("acacia", MaterialCategory.WOOD, Blocks.ACACIA_DOOR);
		JUNGLE_GLASS_DOOR = registerDoor("jungle", MaterialCategory.WOOD, Blocks.JUNGLE_DOOR);
		DARK_OAK_GLASS_DOOR = registerDoor("dark_oak", MaterialCategory.WOOD, Blocks.DARK_OAK_DOOR);
		CRIMSON_GLASS_DOOR = registerDoor("crimson", MaterialCategory.NETHER_WOOD, Blocks.CRIMSON_DOOR);
		WARPED_GLASS_DOOR = registerDoor("warped", MaterialCategory.NETHER_WOOD, Blocks.WARPED_DOOR);
		IRON_GLASS_DOOR = registerDoor("iron", MaterialCategory.METAL, Blocks.IRON_DOOR);
		MANGROVE_GLASS_DOOR = registerDoor("mangrove", MaterialCategory.WOOD, Blocks.MANGROVE_DOOR);
		BAMBOO_GLASS_DOOR = registerDoor("bamboo", MaterialCategory.WOOD, Blocks.BAMBOO_DOOR);
		CHERRY_GLASS_DOOR = registerDoor("cherry", MaterialCategory.WOOD, Blocks.CHERRY_DOOR);

		// Glass trapdoors
		OAK_GLASS_TRAPDOOR = registerTrapdoor("oak", MaterialCategory.WOOD, Blocks.OAK_TRAPDOOR);
		SPRUCE_GLASS_TRAPDOOR = registerTrapdoor("spruce", MaterialCategory.WOOD, Blocks.SPRUCE_TRAPDOOR);
		BIRCH_GLASS_TRAPDOOR = registerTrapdoor("birch", MaterialCategory.WOOD, Blocks.BIRCH_TRAPDOOR);
		ACACIA_GLASS_TRAPDOOR = registerTrapdoor("acacia", MaterialCategory.WOOD, Blocks.ACACIA_TRAPDOOR);
		JUNGLE_GLASS_TRAPDOOR = registerTrapdoor("jungle", MaterialCategory.WOOD, Blocks.JUNGLE_TRAPDOOR);
		DARK_OAK_GLASS_TRAPDOOR = registerTrapdoor("dark_oak", MaterialCategory.WOOD, Blocks.DARK_OAK_TRAPDOOR);
		CRIMSON_GLASS_TRAPDOOR = registerTrapdoor("crimson", MaterialCategory.NETHER_WOOD, Blocks.CRIMSON_TRAPDOOR);
		WARPED_GLASS_TRAPDOOR = registerTrapdoor("warped", MaterialCategory.NETHER_WOOD, Blocks.WARPED_TRAPDOOR);
		IRON_GLASS_TRAPDOOR = registerTrapdoor("iron", MaterialCategory.METAL, Blocks.IRON_TRAPDOOR);
		MANGROVE_GLASS_TRAPDOOR = registerTrapdoor("mangrove", MaterialCategory.WOOD, Blocks.MANGROVE_TRAPDOOR);
		BAMBOO_GLASS_TRAPDOOR = registerTrapdoor("bamboo", MaterialCategory.WOOD, Blocks.BAMBOO_TRAPDOOR);
		CHERRY_GLASS_TRAPDOOR = registerTrapdoor("cherry", MaterialCategory.WOOD, Blocks.CHERRY_TRAPDOOR);

		registerLegacyAliases();
	}

	private static Block registerDoor(String material, MaterialCategory materialCategory, Block parentDoorType) {
		if (!(parentDoorType instanceof DoorBlock parentDoor)) {
			throw new IllegalArgumentException(material);
		}

		Identifier id = Identifier.fromNamespaceAndPath(ModernGlassDoors.MOD_ID, material + "_glass_door");
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		GlassDoorBlock door = new GlassDoorBlock(materialCategory, parentDoor, BlockBehaviour.Properties.ofFullCopy(parentDoor).setId(blockKey));

		GLASS_DOORS.add(door);
		Registry.register(BuiltInRegistries.BLOCK, blockKey, door);
		Registry.register(BuiltInRegistries.ITEM, itemKey, new DoubleHighBlockItem(door, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		ItemGroupEvents.modifyEntriesEvent(ModernGlassDoors.ITEM_GROUP).register(entries -> entries.accept(door));
		return door;
	}

	private static Block registerTrapdoor(String material, MaterialCategory materialCategory, Block parentTrapdoorType) {
		if (!(parentTrapdoorType instanceof TrapDoorBlock parentTrapdoor)) {
			throw new IllegalArgumentException(material);
		}

		Identifier id = Identifier.fromNamespaceAndPath(ModernGlassDoors.MOD_ID, material + "_glass_trapdoor");
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		GlassTrapdoorBlock trapdoor = new GlassTrapdoorBlock(materialCategory, parentTrapdoor, BlockBehaviour.Properties.ofFullCopy(parentTrapdoor).setId(blockKey));

		GLASS_TRAPDOORS.add(trapdoor);
		Registry.register(BuiltInRegistries.BLOCK, blockKey, trapdoor);
		Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(trapdoor, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		ItemGroupEvents.modifyEntriesEvent(ModernGlassDoors.ITEM_GROUP).register(entries -> entries.accept(trapdoor));
		return trapdoor;
	}

	/**
	 * Maps predecessor {@code glassdoor} ids onto the 5.3.0 ids via Fabric registry aliases.
	 */
	private static void registerLegacyAliases() {
		for (Block door : GLASS_DOORS) {
			registerGlassdoorAlias(BuiltInRegistries.BLOCK, BuiltInRegistries.BLOCK.getKey(door), "_glass_door", "_glassdoor");
			registerGlassdoorAlias(BuiltInRegistries.ITEM, BuiltInRegistries.ITEM.getKey(door.asItem()), "_glass_door", "_glassdoor");
		}

		for (Block trapdoor : GLASS_TRAPDOORS) {
			registerGlassdoorAlias(BuiltInRegistries.BLOCK, BuiltInRegistries.BLOCK.getKey(trapdoor), "_glass_trapdoor", "_glasstrapdoor");
			registerGlassdoorAlias(BuiltInRegistries.ITEM, BuiltInRegistries.ITEM.getKey(trapdoor.asItem()), "_glass_trapdoor", "_glasstrapdoor");
		}
	}

	private static void registerGlassdoorAlias(Registry<?> registry, Identifier currentId, String currentSuffix, String legacySuffix) {
		FabricRegistry fabricRegistry = (FabricRegistry) registry;
		String path = currentId.getPath();
		fabricRegistry.addAlias(Identifier.fromNamespaceAndPath(LEGACY_NAMESPACE, path.replace(currentSuffix, legacySuffix)), currentId);
		fabricRegistry.addAlias(Identifier.fromNamespaceAndPath(LEGACY_NAMESPACE, path), currentId);
	}
}
