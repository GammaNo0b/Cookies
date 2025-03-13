
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemBuilder;



public interface BlockFaceConfigurable {

	ItemStack BLOCK_FACE_CONFIG_ICON = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§eOpen Block Face Config").build();

	/**
	 * Utility method to list all block face configurations.
	 * 
	 * @param configs the configurations
	 */
	default void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {}


	/**
	 * Opens a block face config selector gui of the given block for the given player.
	 * 
	 * @param player the player
	 * @param block  the block
	 */
	default void openBlockFaceConfig(HumanEntity player, TileState block) {
		ArrayList<BlockFaceConfig.Config> configs = new ArrayList<>();
		this.listBlockFaceProperties(configs);
		BlockFaceConfig.openBlockFaceConfigs(player, block, "§8Block Face Configuration", configs);
	}


	/**
	 * Checks if the given face of the block is enabled in the given flags byte.
	 * 
	 * @param flags the flags
	 * @param block the block
	 * @param face  the checked face
	 * @return if the face is enabled
	 */
	public static boolean isFaceEnabled(byte flags, TileState block, BlockFace face) {
		for(BlockUtils.BlockFaceDirection direction : BlockUtils.BlockFaceDirection.values())
			if(direction.getFacing(BlockUtils.getFacing(block.getBlockData())) == face)
				return ((flags >> direction.ordinal()) & 1) == 1;
		return false;
	}

}
