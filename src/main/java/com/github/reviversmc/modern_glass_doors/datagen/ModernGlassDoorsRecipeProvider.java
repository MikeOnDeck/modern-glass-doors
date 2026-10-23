package com.github.reviversmc.modern_glass_doors.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsRecipeProvider extends FabricRecipeProvider {
	public ModernGlassDoorsRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public String getName() {
		return "Modern Glass Doors Recipes";
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				for (GlassDoorBlock door : ModernGlassDoorsBlocks.GLASS_DOORS) {
					createGlassDoorRecipe(exporter, door, door.getParentDoorType());
					createDoorFromGlassDoorRecipe(exporter, door.getParentDoorType(), door);
				}

				for (GlassTrapdoorBlock trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
					createGlassDoorRecipe(exporter, trapdoor, trapdoor.getParentTrapdoorType());
					createDoorFromGlassDoorRecipe(exporter, trapdoor.getParentTrapdoorType(), trapdoor);
				}
			}

			private void createGlassDoorRecipe(RecipeOutput recipeOutput, ItemLike output, ItemLike input) {
				shapeless(RecipeCategory.REDSTONE, output)
						.requires(input)
						.requires(Blocks.GLASS_PANE)
						.unlockedBy(getHasName(input), has(input))
						.unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE))
						.save(recipeOutput);
			}

			private void createDoorFromGlassDoorRecipe(RecipeOutput recipeOutput, ItemLike output, ItemLike input) {
				shapeless(RecipeCategory.REDSTONE, output)
						.requires(input)
						.unlockedBy(getHasName(input), has(input))
						.save(recipeOutput, getConversionRecipeName(output, input));
			}
		};
	}
}
