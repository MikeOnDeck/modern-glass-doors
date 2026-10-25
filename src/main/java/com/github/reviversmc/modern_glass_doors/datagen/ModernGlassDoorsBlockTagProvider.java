package com.github.reviversmc.modern_glass_doors.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.github.reviversmc.modern_glass_doors.ModernGlassDoors;
import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.MaterialCategory;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public static final TagKey<Block> WOODEN_GLASS_DOORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ModernGlassDoors.MOD_ID, "wooden_glass_doors"));
	public static final TagKey<Block> METAL_GLASS_DOORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ModernGlassDoors.MOD_ID, "metal_glass_doors"));
	public static final TagKey<Block> WOODEN_GLASS_TRAPDOORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ModernGlassDoors.MOD_ID, "wooden_glass_trapdoors"));
	public static final TagKey<Block> METAL_GLASS_TRAPDOORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ModernGlassDoors.MOD_ID, "metal_glass_trapdoors"));
	public static final TagKey<Block> C_METAL_DOORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "metal_doors"));
	public static final TagKey<Block> C_METAL_TRAPDOORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "metal_trapdoors"));

	public ModernGlassDoorsBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		TagAppender<Block, Block> woodenDoors = valueLookupBuilder(BlockTags.WOODEN_DOORS);
		TagAppender<Block, Block> woodenGlassDoors = valueLookupBuilder(WOODEN_GLASS_DOORS);
		TagAppender<Block, Block> axeMineable = valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE);
		TagAppender<Block, Block> doors = valueLookupBuilder(BlockTags.DOORS);
		TagAppender<Block, Block> pickaxeMineable = valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
		TagAppender<Block, Block> metalGlassDoors = valueLookupBuilder(METAL_GLASS_DOORS);
		TagAppender<Block, Block> woodenTrapdoors = valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS);
		TagAppender<Block, Block> woodenGlassTrapdoors = valueLookupBuilder(WOODEN_GLASS_TRAPDOORS);
		TagAppender<Block, Block> trapdoors = valueLookupBuilder(BlockTags.TRAPDOORS);
		TagAppender<Block, Block> metalGlassTrapdoors = valueLookupBuilder(METAL_GLASS_TRAPDOORS);

		for (GlassDoorBlock door : ModernGlassDoorsBlocks.GLASS_DOORS) {
			if (door.getMaterialCategory() == MaterialCategory.NETHER_WOOD || door.getMaterialCategory() == MaterialCategory.WOOD) {
				woodenDoors.add(door);
				woodenGlassDoors.add(door);
				axeMineable.add(door);
			} else if (door.getMaterialCategory() == MaterialCategory.METAL) {
				doors.add(door);
				pickaxeMineable.add(door);
				metalGlassDoors.add(door);
			}
		}

		for (GlassTrapdoorBlock trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			if (trapdoor.getMaterialCategory() == MaterialCategory.NETHER_WOOD || trapdoor.getMaterialCategory() == MaterialCategory.WOOD) {
				woodenTrapdoors.add(trapdoor);
				woodenGlassTrapdoors.add(trapdoor);
				axeMineable.add(trapdoor);
			} else if (trapdoor.getMaterialCategory() == MaterialCategory.METAL) {
				trapdoors.add(trapdoor);
				pickaxeMineable.add(trapdoor);
				metalGlassTrapdoors.add(trapdoor);
			}
		}

		valueLookupBuilder(C_METAL_DOORS)
				.add(Blocks.IRON_DOOR)
				.addTag(METAL_GLASS_DOORS);
		valueLookupBuilder(C_METAL_TRAPDOORS)
				.add(Blocks.IRON_TRAPDOOR)
				.addTag(METAL_GLASS_TRAPDOORS);
	}
}
