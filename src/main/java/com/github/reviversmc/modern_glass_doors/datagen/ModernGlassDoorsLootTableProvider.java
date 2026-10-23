package com.github.reviversmc.modern_glass_doors.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsLootTableProvider extends FabricBlockLootTableProvider {
	public ModernGlassDoorsLootTableProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generate() {
		for (Block door: ModernGlassDoorsBlocks.GLASS_DOORS) {
			add(door, createDoorTable(door));
		}

		for (Block trapdoor: ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			dropSelf(trapdoor);
		}
	}
}
