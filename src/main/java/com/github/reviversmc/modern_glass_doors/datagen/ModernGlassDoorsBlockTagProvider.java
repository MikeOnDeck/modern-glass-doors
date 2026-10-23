package com.github.reviversmc.modern_glass_doors.datagen;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import com.github.reviversmc.modern_glass_doors.ModernGlassDoors;
import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.MaterialCategory;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public static final TagKey<Block> WOODEN_GLASS_DOORS = TagKey.create(Registries.BLOCK, new ResourceLocation(ModernGlassDoors.MOD_ID, "wooden_glass_doors"));
	public static final TagKey<Block> METAL_GLASS_DOORS = TagKey.create(Registries.BLOCK, new ResourceLocation(ModernGlassDoors.MOD_ID, "metal_glass_doors"));
	public static final TagKey<Block> WOODEN_GLASS_TRAPDOORS = TagKey.create(Registries.BLOCK, new ResourceLocation(ModernGlassDoors.MOD_ID, "wooden_glass_trapdoors"));
	public static final TagKey<Block> METAL_GLASS_TRAPDOORS = TagKey.create(Registries.BLOCK, new ResourceLocation(ModernGlassDoors.MOD_ID, "metal_glass_trapdoors"));
	public static final TagKey<Block> C_METAL_DOORS = TagKey.create(Registries.BLOCK, new ResourceLocation("c:metal_doors"));
	public static final TagKey<Block> C_METAL_TRAPDOORS = TagKey.create(Registries.BLOCK, new ResourceLocation("c:metal_trapdoors"));

	public ModernGlassDoorsBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		List<FabricTagBuilder> woodenGlassDoorTagBuilders = Arrays.asList(
				tag(BlockTags.WOODEN_DOORS),
				tag(WOODEN_GLASS_DOORS));

		List<FabricTagBuilder> metalGlassDoorTagBuilders = Arrays.asList(
				tag(BlockTags.DOORS),
				tag(BlockTags.MINEABLE_WITH_PICKAXE),
				tag(METAL_GLASS_DOORS));

		List<FabricTagBuilder> woodenGlassTrapdoorTagBuilders = Arrays.asList(
				tag(BlockTags.WOODEN_TRAPDOORS),
				tag(WOODEN_GLASS_TRAPDOORS));

		List<FabricTagBuilder> metalGlassTrapdoorTagBuilders = Arrays.asList(
				tag(BlockTags.TRAPDOORS),
				tag(BlockTags.MINEABLE_WITH_PICKAXE),
				tag(METAL_GLASS_TRAPDOORS));

		MaterialCategory material;

		for (GlassDoorBlock door: ModernGlassDoorsBlocks.GLASS_DOORS) {
			material = door.getMaterialCategory();

			if (material == MaterialCategory.NETHER_WOOD || material == MaterialCategory.WOOD) {
				for (FabricTagBuilder tagBuilder : woodenGlassDoorTagBuilders) {
					tagBuilder.add(door);
				}
			} else if (material == MaterialCategory.METAL) {
				for (FabricTagBuilder tagBuilder : metalGlassDoorTagBuilders) {
					tagBuilder.add(door);
				}
			}
		}

		for (GlassTrapdoorBlock trapdoor: ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			material = trapdoor.getMaterialCategory();

			if (material == MaterialCategory.NETHER_WOOD || material == MaterialCategory.WOOD) {
				for (FabricTagBuilder tagBuilder : woodenGlassTrapdoorTagBuilders) {
					tagBuilder.add(trapdoor);
				}
			} else if (material == MaterialCategory.METAL) {
				for (FabricTagBuilder tagBuilder : metalGlassTrapdoorTagBuilders) {
					tagBuilder.add(trapdoor);
				}
			}
		}

		tag(C_METAL_DOORS)
				.add(Blocks.IRON_DOOR)
				.addTag(METAL_GLASS_DOORS);
		tag(C_METAL_TRAPDOORS)
				.add(Blocks.IRON_TRAPDOOR)
				.addTag(METAL_GLASS_TRAPDOORS);
	}
}
