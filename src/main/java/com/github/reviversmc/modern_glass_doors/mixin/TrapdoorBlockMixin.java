package com.github.reviversmc.modern_glass_doors.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.TrapDoorBlock;

// TODO(#6): Port glass-pane conversion inject to TrapDoorBlock.useItemOn (1.21.11 API).
@Mixin(TrapDoorBlock.class)
public abstract class TrapdoorBlockMixin {
}
