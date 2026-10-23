package com.github.reviversmc.modern_glass_doors;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, ModernGlassDoorsBlocks.GLASS_DOORS.toArray(Block[]::new));
		BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, ModernGlassDoorsBlocks.GLASS_TRAPDOORS.toArray(Block[]::new));
	}
}
