package com.github.reviversmc.modern_glass_doors.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.world.level.block.Block;

import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsModelProvider extends FabricModelProvider {
	public ModernGlassDoorsModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generator) {
		for (Block door : ModernGlassDoorsBlocks.GLASS_DOORS) {
			generator.createDoor(door);
		}

		for (Block trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			generator.createOrientableTrapdoor(trapdoor);
		}
	}

	@Override
	public void generateItemModels(ItemModelGenerators generator) {
	}
}
