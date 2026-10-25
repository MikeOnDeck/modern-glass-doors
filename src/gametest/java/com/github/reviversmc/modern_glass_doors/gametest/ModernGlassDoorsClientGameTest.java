package com.github.reviversmc.modern_glass_doors.gametest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestDedicatedServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerConnection;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.screenshot.TestScreenshotOptions;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import com.github.reviversmc.modern_glass_doors.blocks.GlassDoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.GlassTrapdoorBlock;
import com.github.reviversmc.modern_glass_doors.blocks.ModernGlassDoorsBlocks;

public class ModernGlassDoorsClientGameTest implements FabricClientGameTest {
	private static final Path SHOTS = Path.of("/opt/cursor/artifacts/screenshots");
	private static final String[] GROUP_NAMES = {
			"oak-spruce-birch",
			"acacia-jungle-dark-oak",
			"crimson-warped-iron",
			"mangrove-bamboo-cherry"
	};

	@Override
	public void runTest(ClientGameTestContext context) {
		try {
			Files.createDirectories(SHOTS);
			Scene scene;

			try (TestSingleplayerContext world = context.worldBuilder().create()) {
				prepareClient(context);
				waitReady(world);
				world.getServer().runCommand("time set 6000");
				scene = buildScene(world);
				shootDoors(context, world, scene, false);
				setDoorsOpen(world, scene, true);
				shootDoors(context, world, scene, true);
				shootTrapdoors(context, world, scene, false);
				setTrapdoorsOpen(world, scene, true);
				shootTrapdoors(context, world, scene, true);
				shootDoorConversion(context, world, scene);
				shootTrapdoorConversion(context, world, scene);
				shootRedstone(context, world, scene);
				shootWater(context, world, scene);
				assertOffhandAndCreative(world, scene);
				assertAliases(world);
				shootRecipes(context, world);
			}

			try (TestDedicatedServerContext server = context.worldBuilder().createServer()) {
				try (TestServerConnection connection = server.connect()) {
					connection.getClientWorld().waitForChunksRender(20 * 60 * 3);
					server.computeOnServer(minecraftServer -> {
						Block block = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("glassdoor", "oak_glassdoor"));

						if (block != ModernGlassDoorsBlocks.OAK_GLASS_DOOR) {
							throw new IllegalStateException("Dedicated server did not resolve glassdoor:oak_glassdoor");
						}

						return Boolean.TRUE;
					});
				}
			}
		} catch (RuntimeException | Error exception) {
			throw exception;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	private static void prepareClient(ClientGameTestContext context) {
		context.runOnClient(client -> {
			client.options.fov().set(70);
			client.options.renderDistance().set(4);
			client.options.gamma().set(1.0);
			client.options.bobView().set(false);
			client.options.guiScale().set(2);
			client.options.hideGui = false;
		});
	}

	private static Scene buildScene(TestSingleplayerContext world) {
		return world.getServer().computeOnServer(server -> {
			ServerPlayer player = player(server.getPlayerList().getPlayers());
			ServerLevel level = server.overworld();
			int originX = player.blockPosition().getX() + 20;
			int originZ = player.blockPosition().getZ() + 20;
			int ground = surfaceY(level, originX, originZ);
			clear(level, originX - 6, originZ - 6, 70, 40, ground);
			placeDoors(level, originX, ground + 1, originZ, false);
			placeTrapdoors(level, originX, ground, originZ + 16, false);
			return new Scene(ground, originX, originZ, originZ + 16);
		});
	}

	private static void shootDoors(ClientGameTestContext context, TestSingleplayerContext world, Scene scene, boolean open) {
		setHud(context, false);

		for (int group = 0; group < 4; group++) {
			lookAt(context, world, scene.originX + group * 8 + 2.5, scene.ground + 1, scene.doorZ + 4, 180.0F, 0.0F);
			shot(context, (open ? "doors-open-" : "doors-closed-") + GROUP_NAMES[group]);
		}
	}

	private static void shootTrapdoors(ClientGameTestContext context, TestSingleplayerContext world, Scene scene, boolean open) {
		setHud(context, false);

		for (int group = 0; group < 4; group++) {
			lookAt(context, world, scene.originX + group * 8 + 2.5, scene.ground + 1, scene.trapZ + 4, 180.0F, 12.0F);
			shot(context, (open ? "trapdoors-open-" : "trapdoors-closed-") + GROUP_NAMES[group]);
		}
	}

	private static void shootDoorConversion(ClientGameTestContext context, TestSingleplayerContext world, Scene scene) {
		BlockPos lower = new BlockPos(scene.originX + 48, scene.ground + 1, scene.doorZ);
		world.getServer().runOnServer(server -> {
			ServerLevel level = server.overworld();
			ServerPlayer serverPlayer = player(server.getPlayerList().getPlayers());
			serverPlayer.setGameMode(GameType.SURVIVAL);
			serverPlayer.getInventory().clearContent();
			serverPlayer.getInventory().setItem(0, new ItemStack(Items.GLASS_PANE, 16));
			serverPlayer.getInventory().setSelectedSlot(0);
			placeVanillaDoor(level, lower, Blocks.OAK_DOOR, false);
		});
		setHud(context, true);
		lookAt(context, world, lower.getX() + 0.5, scene.ground + 1, lower.getZ() + 4, 180.0F, 0.0F);
		shot(context, "convert-door-before");
		world.getServer().runOnServer(server -> convertDoor(server.overworld(), player(server.getPlayerList().getPlayers()), lower, InteractionHand.MAIN_HAND, 16, 15));
		context.waitTicks(20);
		waitReady(world);
		world.getServer().runOnServer(server -> {
			BlockState state = server.overworld().getBlockState(lower);

			if (!(state.getBlock() instanceof GlassDoorBlock) || state.getValue(DoorBlock.OPEN) || state.getValue(DoorBlock.FACING) != Direction.SOUTH) {
				throw new IllegalStateException("Converted door did not stay closed and south-facing: " + state);
			}
		});
		shot(context, "convert-door-after");
	}

	private static void shootTrapdoorConversion(ClientGameTestContext context, TestSingleplayerContext world, Scene scene) {
		BlockPos pos = new BlockPos(scene.originX + 52, scene.ground + 2, scene.trapZ);
		world.getServer().runOnServer(server -> {
			ServerLevel level = server.overworld();
			ServerPlayer serverPlayer = player(server.getPlayerList().getPlayers());
			serverPlayer.setGameMode(GameType.SURVIVAL);
			serverPlayer.getInventory().clearContent();
			serverPlayer.getInventory().setItem(0, new ItemStack(Items.GLASS_PANE, 16));
			serverPlayer.getInventory().setSelectedSlot(0);
			level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
			placeVanillaTrapdoor(level, pos, Blocks.OAK_TRAPDOOR, false, false);
		});
		setHud(context, true);
		lookAt(context, world, pos.getX() + 0.5, scene.ground + 1, pos.getZ() + 4, 180.0F, 12.0F);
		shot(context, "convert-trapdoor-before");
		world.getServer().runOnServer(server -> convertTrapdoor(server.overworld(), player(server.getPlayerList().getPlayers()), pos, InteractionHand.MAIN_HAND, 16, 15));
		context.waitTicks(5);
		waitReady(world);
		shot(context, "convert-trapdoor-after");
	}

	private static void shootRedstone(ClientGameTestContext context, TestSingleplayerContext world, Scene scene) {
		BlockPos lower = new BlockPos(scene.originX + 56, scene.ground + 1, scene.doorZ);
		world.getServer().runOnServer(server -> {
			ServerLevel level = server.overworld();
			placeDoor(level, lower, ModernGlassDoorsBlocks.IRON_GLASS_DOOR, false);
			level.setBlock(lower.east(), Blocks.REDSTONE_BLOCK.defaultBlockState(), Block.UPDATE_ALL);
		});
		context.waitTicks(10);
		world.getServer().runOnServer(server -> {
			BlockState state = server.overworld().getBlockState(lower);

			if (!state.is(ModernGlassDoorsBlocks.IRON_GLASS_DOOR) || !state.getValue(DoorBlock.OPEN) || !state.getValue(DoorBlock.POWERED)) {
				throw new IllegalStateException("Iron glass door was not opened by redstone: " + state);
			}
		});
		setHud(context, false);
		lookAt(context, world, lower.getX() - 3.5, scene.ground + 1, lower.getZ() + 0.5, -90.0F, 0.0F);
		shot(context, "redstone-iron-glass-door");
	}

	private static void shootWater(ClientGameTestContext context, TestSingleplayerContext world, Scene scene) {
		BlockPos pos = new BlockPos(scene.originX + 60, scene.ground + 1, scene.doorZ);
		world.getServer().runOnServer(server -> {
			ServerLevel level = server.overworld();

			for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					level.setBlock(pos.offset(dx, -1, dz), Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);

					if (dx == 0 && dz == 0) {
						continue;
					}

					BlockPos cell = pos.offset(dx, 0, dz);

					if (dz == 1 && dx == 0) {
						level.setBlock(cell, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
					} else if (dz == 0) {
						level.setBlock(cell, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
					} else {
						level.setBlock(cell, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
					}
				}
			}

			BlockState trapdoor = ModernGlassDoorsBlocks.OAK_GLASS_TRAPDOOR.defaultBlockState()
					.setValue(TrapDoorBlock.FACING, Direction.SOUTH)
					.setValue(TrapDoorBlock.HALF, Half.BOTTOM)
					.setValue(TrapDoorBlock.OPEN, true)
					.setValue(TrapDoorBlock.POWERED, false)
					.setValue(TrapDoorBlock.WATERLOGGED, true);
			level.setBlock(pos, trapdoor, Block.UPDATE_ALL);
			BlockState placed = level.getBlockState(pos);

			if (!placed.is(ModernGlassDoorsBlocks.OAK_GLASS_TRAPDOOR) || !placed.getValue(TrapDoorBlock.WATERLOGGED)) {
				throw new IllegalStateException("Trapdoor is not waterlogged: " + placed);
			}
		});
		setHud(context, false);
		lookAt(context, world, pos.getX() + 0.5, scene.ground + 1, pos.getZ() + 4, 180.0F, 18.0F);
		context.waitTicks(10);
		waitReady(world);
		shot(context, "waterlogged-oak-glass-trapdoor");
	}

	private static void assertOffhandAndCreative(TestSingleplayerContext world, Scene scene) {
		world.getServer().runOnServer(server -> {
			ServerLevel level = server.overworld();
			ServerPlayer serverPlayer = player(server.getPlayerList().getPlayers());
			BlockPos offhandDoor = new BlockPos(scene.originX + 40, scene.ground + 1, scene.doorZ + 8);
			placeVanillaDoor(level, offhandDoor, Blocks.SPRUCE_DOOR, false);
			serverPlayer.setGameMode(GameType.SURVIVAL);
			serverPlayer.getInventory().clearContent();
			serverPlayer.getInventory().setItem(40, new ItemStack(Items.GLASS_PANE, 5));
			convertDoor(level, serverPlayer, offhandDoor, InteractionHand.OFF_HAND, 5, 4);

			if (!serverPlayer.getMainHandItem().isEmpty()) {
				throw new IllegalStateException("Off-hand conversion used the main hand");
			}

			BlockPos creativeDoor = offhandDoor.east().east();
			placeVanillaDoor(level, creativeDoor, Blocks.BIRCH_DOOR, false);
			serverPlayer.setGameMode(GameType.CREATIVE);
			serverPlayer.getInventory().clearContent();
			serverPlayer.getInventory().setItem(0, new ItemStack(Items.GLASS_PANE, 10));
			serverPlayer.getInventory().setSelectedSlot(0);
			convertDoor(level, serverPlayer, creativeDoor, InteractionHand.MAIN_HAND, 10, 10);
		});
	}

	private static void assertAliases(TestSingleplayerContext world) {
		world.getServer().runOnServer(server -> {
			expectAlias(BuiltInRegistries.BLOCK, "oak_glassdoor", ModernGlassDoorsBlocks.OAK_GLASS_DOOR);
			expectAlias(BuiltInRegistries.BLOCK, "oak_glass_door", ModernGlassDoorsBlocks.OAK_GLASS_DOOR);
			expectAlias(BuiltInRegistries.ITEM, "cherry_glasstrapdoor", ModernGlassDoorsBlocks.CHERRY_GLASS_TRAPDOOR.asItem());
			expectAlias(BuiltInRegistries.ITEM, "iron_glass_trapdoor", ModernGlassDoorsBlocks.IRON_GLASS_TRAPDOOR.asItem());
			ServerLevel level = server.overworld();
			BlockPos pos = player(server.getPlayerList().getPlayers()).blockPosition().above(4);
			Block resolved = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("glassdoor", "warped_glassdoor"));
			level.setBlock(pos, resolved.defaultBlockState(), Block.UPDATE_ALL);

			if (!level.getBlockState(pos).is(ModernGlassDoorsBlocks.WARPED_GLASS_DOOR)) {
				throw new IllegalStateException("Placing the glassdoor alias did not create a warped glass door");
			}
		});
	}

	private static void expectAlias(net.minecraft.core.Registry<?> registry, String legacyPath, Object expected) {
		Object resolved = registry.getValue(Identifier.fromNamespaceAndPath("glassdoor", legacyPath));

		if (resolved != expected) {
			throw new IllegalStateException("Alias glassdoor:" + legacyPath + " resolved to " + resolved);
		}
	}

	private static void shootRecipes(ClientGameTestContext context, TestSingleplayerContext world) {
		setHud(context, true);
		context.runOnClient(client -> client.setScreen(new InventoryScreen(client.player)));
		context.waitTicks(5);

		for (GlassDoorBlock door : ModernGlassDoorsBlocks.GLASS_DOORS) {
			String path = BuiltInRegistries.BLOCK.getKey(door).getPath();
			craft(context, world, door.getParentDoorType().asItem(), Items.GLASS_PANE, door.asItem(), "recipe-" + path);
			craft(context, world, door.asItem(), null, door.getParentDoorType().asItem(), "recipe-" + path + "-back-to-door");
		}

		for (GlassTrapdoorBlock trapdoor : ModernGlassDoorsBlocks.GLASS_TRAPDOORS) {
			String path = BuiltInRegistries.BLOCK.getKey(trapdoor).getPath();
			craft(context, world, trapdoor.getParentTrapdoorType().asItem(), Items.GLASS_PANE, trapdoor.asItem(), "recipe-" + path);
			craft(context, world, trapdoor.asItem(), null, trapdoor.getParentTrapdoorType().asItem(), "recipe-" + path + "-back-to-trapdoor");
		}

		context.setScreen(() -> null);
	}

	private static void craft(ClientGameTestContext context, TestSingleplayerContext world, Item input, Item extra, Item expected, String name) {
		world.getServer().runOnServer(server -> {
			ServerPlayer serverPlayer = player(server.getPlayerList().getPlayers());
			serverPlayer.setGameMode(GameType.SURVIVAL);
			serverPlayer.getInventory().clearContent();
			net.minecraft.world.inventory.InventoryMenu menu = serverPlayer.inventoryMenu;

			for (int slot = 1; slot <= 4; slot++) {
				menu.getSlot(slot).set(ItemStack.EMPTY);
			}

			menu.getSlot(1).set(new ItemStack(input));

			if (extra != null) {
				menu.getSlot(2).set(new ItemStack(extra));
			}

			menu.slotsChanged(menu.getSlot(1).container);
			ItemStack result = menu.getSlot(0).getItem();

			if (!result.is(expected)) {
				throw new IllegalStateException(name + " crafted " + result + " instead of " + expected);
			}

			menu.broadcastChanges();
		});
		context.waitTicks(2);
		ItemStack seen = context.computeOnClient(client -> client.player.inventoryMenu.getSlot(0).getItem().copy());

		if (!seen.is(expected)) {
			throw new IllegalStateException(name + " client grid showed " + seen);
		}

		shot(context, name);
	}

	private static void convertDoor(ServerLevel level, Player player, BlockPos lower, InteractionHand hand, int countBefore, int countAfter) {
		BlockState before = level.getBlockState(lower);
		Direction facing = before.getValue(DoorBlock.FACING);
		DoorHingeSide hinge = before.getValue(DoorBlock.HINGE);
		boolean open = before.getValue(DoorBlock.OPEN);
		boolean powered = before.getValue(DoorBlock.POWERED);
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getCount() != countBefore) {
			throw new IllegalStateException("Expected " + countBefore + " panes, found " + stack.getCount());
		}

		InteractionResult result = before.useItemOn(stack, level, player, hand, new BlockHitResult(Vec3.atCenterOf(lower), Direction.SOUTH, lower, false));

		if (!(result instanceof InteractionResult.Success)) {
			throw new IllegalStateException("Door conversion returned " + result);
		}

		BlockState lowerState = level.getBlockState(lower);
		BlockState upperState = level.getBlockState(lower.above());

		if (!(lowerState.getBlock() instanceof GlassDoorBlock) || lowerState.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
			throw new IllegalStateException("Lower half was not replaced: " + lowerState);
		}

		if (!(upperState.getBlock() instanceof GlassDoorBlock) || upperState.getValue(DoorBlock.HALF) != DoubleBlockHalf.UPPER) {
			throw new IllegalStateException("Upper half was left behind: " + upperState);
		}

		if (lowerState.getValue(DoorBlock.FACING) != facing || lowerState.getValue(DoorBlock.HINGE) != hinge || lowerState.getValue(DoorBlock.OPEN) != open || lowerState.getValue(DoorBlock.POWERED) != powered) {
			throw new IllegalStateException("Door state was not preserved: " + lowerState);
		}

		if (upperState.getBlock() != lowerState.getBlock()) {
			throw new IllegalStateException("Door halves differ");
		}

		if (stack.getCount() != countAfter) {
			throw new IllegalStateException("Pane count is " + stack.getCount() + ", expected " + countAfter);
		}

		if (!level.getEntitiesOfClass(ItemEntity.class, new AABB(lower).inflate(4)).isEmpty()) {
			throw new IllegalStateException("Door conversion dropped an item");
		}
	}

	private static void convertTrapdoor(ServerLevel level, Player player, BlockPos pos, InteractionHand hand, int countBefore, int countAfter) {
		BlockState before = level.getBlockState(pos);
		ItemStack stack = player.getItemInHand(hand);
		InteractionResult result = before.useItemOn(stack, level, player, hand, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
		BlockState after = level.getBlockState(pos);

		if (!(result instanceof InteractionResult.Success) || !(after.getBlock() instanceof GlassTrapdoorBlock)) {
			throw new IllegalStateException("Trapdoor conversion failed: " + result + " " + after);
		}

		if (after.getValue(TrapDoorBlock.FACING) != before.getValue(TrapDoorBlock.FACING)
				|| after.getValue(TrapDoorBlock.HALF) != before.getValue(TrapDoorBlock.HALF)
				|| after.getValue(TrapDoorBlock.OPEN) != before.getValue(TrapDoorBlock.OPEN)
				|| after.getValue(TrapDoorBlock.POWERED) != before.getValue(TrapDoorBlock.POWERED)
				|| after.getValue(TrapDoorBlock.WATERLOGGED) != before.getValue(TrapDoorBlock.WATERLOGGED)) {
			throw new IllegalStateException("Trapdoor state was not preserved: " + after);
		}

		if (stack.getCount() != countAfter || stack.getCount() == countBefore && countBefore != countAfter) {
			throw new IllegalStateException("Pane count is " + stack.getCount());
		}

		if (!level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(3)).isEmpty()) {
			throw new IllegalStateException("Trapdoor conversion dropped an item");
		}
	}

	private static void placeDoors(ServerLevel level, int originX, int y, int z, boolean open) {
		for (int index = 0; index < ModernGlassDoorsBlocks.GLASS_DOORS.size(); index++) {
			int group = index / 3;
			int column = index % 3;
			placeDoor(level, new BlockPos(originX + group * 8 + column * 2, y, z), ModernGlassDoorsBlocks.GLASS_DOORS.get(index), open);
		}
	}

	private static void placeTrapdoors(ServerLevel level, int originX, int ground, int z, boolean open) {
		for (int index = 0; index < ModernGlassDoorsBlocks.GLASS_TRAPDOORS.size(); index++) {
			int group = index / 3;
			int column = index % 3;
			BlockPos pos = new BlockPos(originX + group * 8 + column * 2, ground + 2, z);
			level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
			placeTrapdoor(level, pos, ModernGlassDoorsBlocks.GLASS_TRAPDOORS.get(index), open, false);
		}
	}

	private static void setDoorsOpen(TestSingleplayerContext world, Scene scene, boolean open) {
		world.getServer().runOnServer(server -> placeDoors(server.overworld(), scene.originX, scene.ground + 1, scene.doorZ, open));
	}

	private static void setTrapdoorsOpen(TestSingleplayerContext world, Scene scene, boolean open) {
		world.getServer().runOnServer(server -> placeTrapdoors(server.overworld(), scene.originX, scene.ground, scene.trapZ, open));
	}

	private static void placeDoor(ServerLevel level, BlockPos lower, Block door, boolean open) {
		BlockState state = door.defaultBlockState()
				.setValue(DoorBlock.FACING, Direction.SOUTH)
				.setValue(DoorBlock.HINGE, DoorHingeSide.LEFT)
				.setValue(DoorBlock.OPEN, open)
				.setValue(DoorBlock.POWERED, false)
				.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
		level.setBlock(lower, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_SUPPRESS_DROPS);
		level.setBlock(lower.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_SUPPRESS_DROPS);
		level.setBlock(lower, state, Block.UPDATE_ALL);
		level.setBlock(lower.above(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
	}

	private static void placeVanillaDoor(ServerLevel level, BlockPos lower, Block door, boolean open) {
		placeDoor(level, lower, door, open);
	}

	private static void placeTrapdoor(ServerLevel level, BlockPos pos, Block trapdoor, boolean open, boolean waterlogged) {
		level.setBlock(pos, trapdoor.defaultBlockState()
				.setValue(TrapDoorBlock.FACING, Direction.SOUTH)
				.setValue(TrapDoorBlock.HALF, Half.BOTTOM)
				.setValue(TrapDoorBlock.OPEN, open)
				.setValue(TrapDoorBlock.POWERED, false)
				.setValue(TrapDoorBlock.WATERLOGGED, waterlogged), Block.UPDATE_ALL);
	}

	private static void placeVanillaTrapdoor(ServerLevel level, BlockPos pos, Block trapdoor, boolean open, boolean waterlogged) {
		placeTrapdoor(level, pos, trapdoor, open, waterlogged);
	}

	private static void clear(ServerLevel level, int x, int z, int width, int depth, int ground) {
		for (int dx = 0; dx < width; dx++) {
			for (int dz = 0; dz < depth; dz++) {
				level.setBlock(new BlockPos(x + dx, ground, z + dz), Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS);

				for (int dy = 1; dy <= 6; dy++) {
					level.setBlock(new BlockPos(x + dx, ground + dy, z + dz), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
				}
			}
		}
	}

	private static int surfaceY(ServerLevel level, int x, int z) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, level.getMaxY(), z);

		for (int y = level.getMaxY(); y >= level.getMinY(); y--) {
			pos.setY(y);

			if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP)) {
				return y;
			}
		}

		throw new IllegalStateException("No ground at " + x + ", " + z);
	}

	private static void lookAt(ClientGameTestContext context, TestSingleplayerContext world, double x, double y, double z, float yaw, float pitch) {
		world.getServer().runOnServer(server -> {
			ServerPlayer serverPlayer = player(server.getPlayerList().getPlayers());
			serverPlayer.teleportTo(server.overworld(), x, y, z, Set.of(), yaw, pitch, true);
		});
		context.waitTicks(5);
		waitReady(world);
		context.runOnClient(client -> {
			client.player.setYRot(yaw);
			client.player.setXRot(pitch);
			client.player.setYHeadRot(yaw);
		});
		context.waitTick();
	}

	private static void waitReady(TestSingleplayerContext world) {
		world.getClientWorld().waitForChunksRender(20 * 60 * 3);
	}

	private static void setHud(ClientGameTestContext context, boolean visible) {
		context.runOnClient(client -> client.options.hideGui = !visible);
	}

	private static void shot(ClientGameTestContext context, String name) {
		context.takeScreenshot(TestScreenshotOptions.of(name).disableCounterPrefix().withSize(1280, 720).withDestinationDir(SHOTS));
	}

	private static ServerPlayer player(List<ServerPlayer> players) {
		if (players.isEmpty()) {
			throw new IllegalStateException("No player");
		}

		return players.get(0);
	}

	private record Scene(int ground, int originX, int doorZ, int trapZ) {
	}
}
