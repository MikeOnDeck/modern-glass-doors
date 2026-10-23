package com.github.reviversmc.modern_glass_doors.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GlassTrapdoorBlock extends TrapDoorBlock {
	public static BlockState copyState(BlockState state) {
		if (!(state.getBlock() instanceof TrapDoorBlock)) {
			return state;
		}

		for (GlassTrapdoorBlock trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			if (trapdoor.getParentTrapdoorType().equals(state.getBlock())) {
				return trapdoor.defaultBlockState()
						.setValue(TrapDoorBlock.FACING, state.getValue(TrapDoorBlock.FACING))
						.setValue(TrapDoorBlock.OPEN, state.getValue(TrapDoorBlock.OPEN))
						.setValue(TrapDoorBlock.HALF, state.getValue(TrapDoorBlock.HALF))
						.setValue(TrapDoorBlock.POWERED, state.getValue(TrapDoorBlock.POWERED))
						.setValue(TrapDoorBlock.WATERLOGGED, state.getValue(TrapDoorBlock.WATERLOGGED));
			}
		}

		return state;
	}

	private final MaterialCategory materialCategory;
	private final TrapDoorBlock parentTrapdoorType;

	GlassTrapdoorBlock(MaterialCategory materialCategory, TrapDoorBlock parentTrapdoorType) {
		super(parentTrapdoorType.type, Block.Properties.ofFullCopy(parentTrapdoorType));

		this.materialCategory = materialCategory;
		this.parentTrapdoorType = (TrapDoorBlock) parentTrapdoorType;
	}

	public MaterialCategory getMaterialCategory() {
		return materialCategory;
	}

	public TrapDoorBlock getParentTrapdoorType() {
		return parentTrapdoorType;
	}
}
