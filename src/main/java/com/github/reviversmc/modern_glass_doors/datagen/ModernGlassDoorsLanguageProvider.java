package com.github.reviversmc.modern_glass_doors.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

import com.github.reviversmc.modern_glass_doors.ModernGlassDoors;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsLanguageProvider extends FabricLanguageProvider {
	public ModernGlassDoorsLanguageProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add("itemGroup." + ModernGlassDoors.MOD_ID + ".blocks", "Modern Glass Doors");
		add(translationBuilder, ModernGlassDoorsBlocks.OAK_GLASS_DOOR, "Oak Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.SPRUCE_GLASS_DOOR, "Spruce Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.BIRCH_GLASS_DOOR, "Birch Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.ACACIA_GLASS_DOOR, "Acacia Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.JUNGLE_GLASS_DOOR, "Jungle Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.DARK_OAK_GLASS_DOOR, "Dark Oak Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.CRIMSON_GLASS_DOOR, "Crimson Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.WARPED_GLASS_DOOR, "Warped Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.IRON_GLASS_DOOR, "Iron Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.MANGROVE_GLASS_DOOR, "Mangrove Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.BAMBOO_GLASS_DOOR, "Bamboo Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.CHERRY_GLASS_DOOR, "Cherry Glass Door");
		add(translationBuilder, ModernGlassDoorsBlocks.OAK_GLASS_TRAPDOOR, "Oak Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.SPRUCE_GLASS_TRAPDOOR, "Spruce Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.BIRCH_GLASS_TRAPDOOR, "Birch Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.ACACIA_GLASS_TRAPDOOR, "Acacia Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.JUNGLE_GLASS_TRAPDOOR, "Jungle Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.DARK_OAK_GLASS_TRAPDOOR, "Dark Oak Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.CRIMSON_GLASS_TRAPDOOR, "Crimson Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.WARPED_GLASS_TRAPDOOR, "Warped Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.IRON_GLASS_TRAPDOOR, "Iron Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.MANGROVE_GLASS_TRAPDOOR, "Mangrove Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.BAMBOO_GLASS_TRAPDOOR, "Bamboo Glass Trapdoor");
		add(translationBuilder, ModernGlassDoorsBlocks.CHERRY_GLASS_TRAPDOOR, "Cherry Glass Trapdoor");
	}

	private static void add(TranslationBuilder translationBuilder, Block block, String name) {
		translationBuilder.add(block, name);
	}
}
