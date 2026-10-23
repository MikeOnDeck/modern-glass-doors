package com.github.reviversmc.modern_glass_doors.blocks;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
						.setValue(DoorBlock.OPEN, state.getValue(DoorBlock.OPEN))
						.setValue(DoorBlock.POWERED, state.getValue(DoorBlock.POWERED))
						.setValue(DoorBlock.HALF, state.getValue(DoorBlock.HALF));
			}
		}

		return state;
	}

	public final MaterialCategory materialCategory;
	public final DoorBlock parentDoorType;

	GlassDoorBlock(MaterialCategory materialCategory, DoorBlock parentDoorType, BlockBehaviour.Properties properties) {
		super(parentDoorType.type(), properties);

		this.materialCategory = materialCategory;
		this.parentDoorType = parentDoorType;
	}

	public MaterialCategory getMaterialCategory() {
		return materialCategory;
	}

	public DoorBlock getParentDoorType() {
		return parentDoorType;
	}
}
