
package me.gamma.cookies.util;


import java.util.function.UnaryOperator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Snow;
import org.bukkit.craftbukkit.v1_21_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.property.Properties;



public class BlockUtils {

	/**
	 * All {@link BlockFace} fields where {@link BlockFace#isCartesian()} returns true.
	 */
	public static final BlockFace[] cartesian = ArrayUtils.filter(BlockFace.values(), BlockFace::isCartesian, BlockFace[]::new);

	/**
	 * All {@link BlockUtils#cartesian} fields that only move along the x or z axis.
	 */
	public static final BlockFace[] horizontal = ArrayUtils.filter(cartesian, face -> face.getModY() == 0, BlockFace[]::new);

	/**
	 * All block materials that get's replaces when placing a block on their location.
	 */
	public static final Material[] replaceable = ArrayUtils.array(Material.AIR, Material.WATER, Material.LAVA, Material.SHORT_GRASS, Material.FERN, Material.SEAGRASS, Material.TALL_GRASS, Material.LARGE_FERN, Material.TALL_SEAGRASS, Material.CRIMSON_ROOTS, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.DEAD_BUSH, Material.HANGING_ROOTS, Material.VINE, Material.GLOW_LICHEN);

	private static final BlockFace[][][] facingByMod = new BlockFace[5][3][5];
	private static final BlockFace[][][] rotatedClockwise = new BlockFace[5][3][5];
	private static final BlockFace[][][] rotatedCounterClockwise = new BlockFace[5][3][5];
	private static final BlockFace[][][] closestCartesian = new BlockFace[5][3][5];
	static {
		for(BlockFace face : BlockFace.values()) {
			int x = face.getModX();
			int y = face.getModY();
			int z = face.getModZ();

			facingByMod[x + 2][y + 1][z + 2] = face;
			rotatedClockwise[z + 2][y + 1][-x + 2] = face;
			rotatedCounterClockwise[-z + 2][y + 1][x + 2] = face;

			if(face.isCartesian()) {
				closestCartesian[x + 2][y + 1][z + 2] = face;
			} else if(Math.abs(x) == 2) {
				closestCartesian[x + 2][y + 1][z + 2] = facingByMod[x / 2 + 2][1][2];
			} else if(Math.abs(z) == 2) {
				closestCartesian[x + 2][y + 1][z + 2] = facingByMod[2][1][z / 2 + 2];
			} else if(face == BlockFace.SELF) {
				closestCartesian[x + 2][y + 1][z + 2] = face;
			} else {
				if(x == z) {
					closestCartesian[x + 2][y + 1][z + 2] = facingByMod[x + 2][1][2];
				} else {
					closestCartesian[x + 2][y + 1][z + 2] = facingByMod[2][1][z + 2];
				}
			}
		}
	}

	/**
	 * Returns the facing with the given mod values.
	 * 
	 * @param modX the modX
	 * @param modY the modY
	 * @param modZ the modZ
	 * @return the corresponding facing
	 */
	public static BlockFace getFacingByMod(int modX, int modY, int modZ) {
		return facingByMod[modX + 2][modY + 1][modZ + 2];
	}


	/**
	 * Rotates the given {@link BlockFace} along the y axis in a clockwise direction.
	 * 
	 * @param facing the {@link BlockFace} to be rotated
	 * @return the rotated {@link BlockFace}
	 */
	public static BlockFace rotateYClockwise(BlockFace face) {
		return rotatedClockwise[face.getModX() + 2][face.getModY() + 1][face.getModZ() + 2];
	}


	/**
	 * Rotates the given {@link BlockFace} along the y axis in a counter-clockwise direction.
	 * 
	 * @param facing the {@link BlockFace} to be rotated
	 * @return the rotated {@link BlockFace}
	 */
	public static BlockFace rotateYCounterClockwise(BlockFace face) {
		return rotatedCounterClockwise[face.getModX() + 2][face.getModY() + 1][face.getModZ() + 2];
	}


	/**
	 * Returns the closest cartesion block face to the given face.
	 * 
	 * @param face the block face
	 * @return the closest cartesion block face
	 */
	public static BlockFace getClosestCartesianFacing(BlockFace face) {
		return closestCartesian[face.getModX() + 2][face.getModY() + 1][face.getModZ() + 2];
	}


	/**
	 * Returns the facing of the given block data or null if none could be determined.
	 * 
	 * @param data the block data
	 * @return the facing
	 */
	public static BlockFace getFacing(BlockData data) {
		if(data instanceof Directional directional) {
			return directional.getFacing();
		} else if(data instanceof Rotatable rotatable) {
			return rotatable.getRotation().getOppositeFace();
		}
		return null;
	}


	/**
	 * Gets the facing of the given block, applies the transformer and sets the new transformed facing.
	 * 
	 * @param block the block
	 */
	public static void getSetFacing(Block block, UnaryOperator<BlockFace> transformer) {
		BlockData data = block.getBlockData();

		if(data instanceof Directional directional) {
			BlockFace facing = directional.getFacing();
			BlockFace transformed = transformer.apply(facing);
			if(facing == transformed)
				return;

			directional.setFacing(transformed);
		} else if(data instanceof Rotatable rotatable) {
			BlockFace facing = rotatable.getRotation().getOppositeFace();
			BlockFace transformed = transformer.apply(facing);
			if(facing == transformed)
				return;

			rotatable.setRotation(transformed);
		} else {
			return;
		}

		block.setBlockData(data);
	}

	/**
	 * Used to transform directions like left, right into cardinal directions based on the facing of a block.
	 */
	public static enum BlockFaceDirection {

		FRONT(UnaryOperator.identity()),
		BACK(BlockFace::getOppositeFace),
		TOP(BlockFace.UP),
		BOTTOM(BlockFace.DOWN),
		LEFT(BlockUtils::rotateYClockwise),
		RIGHT(BlockUtils::rotateYCounterClockwise);

		private final UnaryOperator<BlockFace> transformer;

		private BlockFaceDirection(BlockFace facing) {
			this(__ -> facing);
		}


		private BlockFaceDirection(UnaryOperator<BlockFace> transformer) {
			this.transformer = transformer;
		}


		public BlockFace getFacing(BlockFace rotation) {
			return this.transformer.apply(rotation);
		}

	}

	/**
	 * Checks if the given type is a block and a fluid like water and lava.
	 * 
	 * @param material the type
	 * @return if the type is a fluid
	 */
	public static boolean isFluid(Material material) {
		return material == Material.WATER || material == Material.LAVA;
	}


	/**
	 * Checks if the given block state is a block that get's replaced when a new block get's placed.
	 * 
	 * @param state the block state
	 * @return if the block state can get replaced
	 */
	public static boolean isReplaceable(BlockState state) {
		return ArrayUtils.contains(replaceable, state.getType()) || state.getBlockData() instanceof Snow snow && snow.getLayers() == snow.getMinimumLayers();
	}


	/**
	 * Get's the block where a new block can be placed or null if no block can be placed.
	 * 
	 * @param clicked the clicked block
	 * @param face    the block face on which the block was clicked
	 * @return the block where the player can place or null
	 */
	public static Block getBlockToPlace(Block clicked, BlockFace face) {
		if(isReplaceable(clicked.getState()))
			return clicked;

		Block block = clicked.getRelative(face);
		if(isReplaceable(block.getState()))
			return block;

		return null;
	}


	/**
	 * Returns the number of ticks it is required to harvest the given block with the given tool. Returns -1 if the block is unbreakable and 0 if the
	 * material is actually no block.
	 * 
	 * @param block the block type
	 * @param tool  the tool
	 * @return the number of ticks
	 */
	public static int getBlockBreakTime(Block block, ItemStack tool) {
		Material type = block.getType();
		float hardness = type.getHardness();
		if(hardness <= 0.0F)
			return Math.round(hardness);

		if(ItemUtils.isEmpty(tool) || !block.isPreferredTool(tool))
			return Math.round(6.0F * hardness);

		float speedMultiplier = CraftItemStack.asNMSCopy(tool).a(((CraftBlock) block).getNMS());

		int efficiency = tool.getEnchantmentLevel(Enchantment.EFFICIENCY);
		if(efficiency > 0)
			speedMultiplier += 1 + efficiency * efficiency;

		float damage = speedMultiplier / (hardness * 30);
		if(damage > 1)
			return 0;

		return (int) Math.ceil(1.0F / damage);
	}


	/**
	 * Returns the {@link TileState} at the given location or null of none found.
	 * 
	 * @param location the location
	 * @return the tile state
	 */
	public static TileState getTileState(Location location) {
		return location == null ? null : getTileState(location.getBlock());
	}


	/**
	 * Returns the {@link TileState} at the given block or null of none found.
	 * 
	 * @param block the block
	 * @return the tile state
	 */
	public static TileState getTileState(Block block) {
		return block == null ? null : getTileState(block.getState());
	}


	/**
	 * Returns the {@link TileState} if the given {@link BlockState} is one.
	 * 
	 * @param state the block state
	 * @return the casted tile state or null
	 */
	public static TileState getTileState(BlockState state) {
		return state instanceof TileState tile ? tile : null;
	}


	/**
	 * Checks if the given block is a custom block and stores data from this Cookie plugin.
	 * 
	 * @param block the block to be checked
	 * @return if the block is custom
	 */
	public static boolean isCustomBlock(TileState block) {
		return Properties.IDENTIFIER.isPropertyOf(block);
	}


	/**
	 * Checks if the given block is a custom block and stores data from this Cookie plugin.
	 * 
	 * @param block the block to be checked
	 * @return if the block is custom
	 */
	public static boolean isCustomBlock(Block block) {
		return block.getState() instanceof TileState state && isCustomBlock(state);
	}

}
