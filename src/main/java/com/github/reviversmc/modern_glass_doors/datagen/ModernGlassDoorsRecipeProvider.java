package com.github.reviversmc.modern_glass_doors.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsRecipeProvider extends FabricRecipeProvider {
	public ModernGlassDoorsRecipeProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void buildRecipes(RecipeOutput exporter) {
		for (GlassDoorBlock door : ModernGlassDoorsBlocks.GLASS_DOORS) {
			createGlassDoorRecipe(exporter, door, door.getParentDoorType());
			createDoorFromGlassDoorRecipe(exporter, door.getParentDoorType(), door);
		}

		for (GlassTrapdoorBlock trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			createGlassDoorRecipe(exporter, trapdoor, trapdoor.getParentTrapdoorType());
			createDoorFromGlassDoorRecipe(exporter, trapdoor.getParentTrapdoorType(), trapdoor);
		}
	}

	private static void createGlassDoorRecipe(RecipeOutput exporter, ItemLike output, ItemLike input) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, output)
				.requires(input)
				.requires(Blocks.GLASS_PANE)
				.unlockedBy(getHasName(input), has(input))
				.unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE))
				.save(exporter);
	}

	private static void createDoorFromGlassDoorRecipe(RecipeOutput exporter, ItemLike output, ItemLike input) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, output)
				.requires(input)
				.unlockedBy(getHasName(input), has(input))
				.save(exporter, getConversionRecipeName(output, input));
	}
}
