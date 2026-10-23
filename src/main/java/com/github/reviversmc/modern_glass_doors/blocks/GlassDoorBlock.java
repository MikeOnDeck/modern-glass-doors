package com.github.reviversmc.modern_glass_doors.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GlassDoorBlock extends DoorBlock {
	public static BlockState copyState(BlockState state) {
		if (!(state.getBlock() instanceof DoorBlock)) {
			return state;
		}

		for (GlassDoorBlock door : ModernGlassDoorsBlocks.GLASS_DOORS) {
			if (door.getParentDoorType().equals(state.getBlock())) {
				return door.defaultBlockState()
						.setValue(DoorBlock.FACING, state.getValue(DoorBlock.FACING))
						.setValue(DoorBlock.HINGE, state.getValue(DoorBlock.HINGE))
						.setValue(DoorBlock.OPEN, state.getValue(DoorBlock.OPEN));
			}
		}

		return state;
	}

	public final MaterialCategory materialCategory;
	public final DoorBlock parentDoorType;

	GlassDoorBlock(MaterialCategory materialCategory, DoorBlock parentDoorType) {
		super(parentDoorType.type(), Block.Properties.ofFullCopy(parentDoorType));

		this.materialCategory = materialCategory;
		this.parentDoorType = (DoorBlock) parentDoorType;
	}

	public MaterialCategory getMaterialCategory() {
		return materialCategory;
	}

	public DoorBlock getParentDoorType() {
		return parentDoorType;
	}
}
