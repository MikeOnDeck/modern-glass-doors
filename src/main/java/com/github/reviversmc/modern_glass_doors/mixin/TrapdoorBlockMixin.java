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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;

@Mixin(BlockBehaviour.class)
public class TrapdoorBlockMixin {
	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	private void modern_glass_doors_convertToGlassTrapdoor(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> callback) {
		if (!(state.getBlock() instanceof TrapDoorBlock) || state.getBlock() instanceof GlassTrapdoorBlock || stack.getItem() != Items.GLASS_PANE) {
			return;
		}

		BlockState copied = GlassTrapdoorBlock.copyState(state);

		if (!(copied.getBlock() instanceof GlassTrapdoorBlock)) {
			return;
		}

		level.setBlock(pos, copied, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);

		if (copied.getValue(TrapDoorBlock.WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		if (player != null && !player.isCreative()) {
			stack.shrink(1);
		}

		callback.setReturnValue(InteractionResult.SUCCESS);
	}
}
