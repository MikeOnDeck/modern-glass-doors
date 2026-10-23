package com.github.reviversmc.modern_glass_doors.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.DoorBlock;

// TODO(#5): Port glass-pane conversion inject to DoorBlock.useItemOn (1.21.11 API).
@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {
}
