package com.github.reviversmc.modern_glass_doors.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;

@Mixin(BlockBehaviour.class)
public class DoorBlockMixin {
	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	private void modern_glass_doors_convertToGlassDoor(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> callback) {
		if (!(state.getBlock() instanceof DoorBlock) || state.getBlock() instanceof GlassDoorBlock || stack.getItem() != Items.GLASS_PANE) {
			return;
		}

		BlockPos lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
		BlockState lowerState = level.getBlockState(lower);

		if (!(lowerState.getBlock() instanceof DoorBlock) || lowerState.getBlock() instanceof GlassDoorBlock) {
			return;
		}

		BlockState copied = GlassDoorBlock.copyState(lowerState);

		if (!(copied.getBlock() instanceof GlassDoorBlock)) {
			return;
		}

		// Known-shape skips the vertical shape update. A door half whose partner is still air is destroyed, which drops an item and plays break particles.
		int flags = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS;
		level.setBlock(lower, Blocks.AIR.defaultBlockState(), flags);
		level.setBlock(lower.above(), Blocks.AIR.defaultBlockState(), flags);
		level.setBlock(lower, copied.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER), flags);
		level.setBlock(lower.above(), copied.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), flags);
		level.updateNeighborsAt(lower, copied.getBlock());
		level.updateNeighborsAt(lower.above(), copied.getBlock());

		if (player != null && !player.isCreative()) {
			stack.shrink(1);
		}

		callback.setReturnValue(InteractionResult.SUCCESS);
	}
}
