package com.github.reviversmc.modern_glass_doors;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoors implements ModInitializer {
	// Hardcoded Mod Identification
	public static final String MOD_ID = "modern_glass_doors";

	// ItemGroup Declaration
	public static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "blocks"));

	@Override
	public void onInitialize() {
		ModernGlassDoorsBlocks.register();

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP, FabricItemGroup.builder()
				.title(Component.translatable("itemGroup." + MOD_ID + ".blocks"))
				.icon(() -> new ItemStack(ModernGlassDoorsBlocks.OAK_GLASS_DOOR.asItem()))
				.build()
		);
	}
}
