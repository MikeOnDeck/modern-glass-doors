package com.github.reviversmc.modern_glass_doors.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.MaterialCategory;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ModernGlassDoorsItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		TagAppender<Item, Item> woodenDoors = valueLookupBuilder(ItemTags.WOODEN_DOORS);
		TagAppender<Item, Item> doors = valueLookupBuilder(ItemTags.DOORS);
		TagAppender<Item, Item> woodenTrapdoors = valueLookupBuilder(ItemTags.WOODEN_TRAPDOORS);
		TagAppender<Item, Item> trapdoors = valueLookupBuilder(ItemTags.TRAPDOORS);
		TagAppender<Item, Item> nonFlammableWood = valueLookupBuilder(ItemTags.NON_FLAMMABLE_WOOD);

		for (GlassDoorBlock door : ModernGlassDoorsBlocks.GLASS_DOORS) {
			if (door.getMaterialCategory() == MaterialCategory.NETHER_WOOD || door.getMaterialCategory() == MaterialCategory.WOOD) {
				woodenDoors.add(door.asItem());
			} else if (door.getMaterialCategory() == MaterialCategory.METAL) {
				doors.add(door.asItem());
			}

			if (door.getMaterialCategory() == MaterialCategory.NETHER_WOOD) {
				nonFlammableWood.add(door.asItem());
			}
		}

		for (GlassTrapdoorBlock trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			if (trapdoor.getMaterialCategory() == MaterialCategory.NETHER_WOOD || trapdoor.getMaterialCategory() == MaterialCategory.WOOD) {
				woodenTrapdoors.add(trapdoor.asItem());
			} else if (trapdoor.getMaterialCategory() == MaterialCategory.METAL) {
				trapdoors.add(trapdoor.asItem());
			}

			if (trapdoor.getMaterialCategory() == MaterialCategory.NETHER_WOOD) {
				nonFlammableWood.add(trapdoor.asItem());
			}
		}
	}
}
