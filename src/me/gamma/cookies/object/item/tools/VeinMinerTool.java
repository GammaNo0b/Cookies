
package me.gamma.cookies.object.item.tools;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Sets;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.DebugUtils;



public abstract class VeinMinerTool extends AbstractCustomItem {

	/**
	 * Checks if the given material will trigger vein mining when mined.
	 * 
	 * @param material the material
	 * @return if the blomaterialck will trigger vein mining
	 */
	protected abstract boolean isValidMaterial(Material material);

	/**
	 * Returns a set containing the next blocks to be broken automatically.
	 * 
	 * @param block the first block broken
	 * @return the next blocks to be broken
	 */
	protected abstract Set<Block> getNextGeneration(Block block);


	@Override
	public boolean onBlockBreak(Player player, ItemStack stack, Block block, BlockBreakEvent event) {
		Material type = block.getType();
		if(this.isValidMaterial(type))
			this.breakBlocksRecursively(type, player, stack, new HashSet<>(), Sets.newHashSet(block));
		return false;
	}


	/**
	 * Breaks blocks recursively until either no more blocks are found or the tool has no more uses left.
	 * 
	 * @param original   the originally broken block material
	 * @param player     the player
	 * @param tool       the tool the block got broken with
	 * @param broken     the set containing all already broken blocks
	 * @param generation the set containing the next blocks to be broken
	 */
	private void breakBlocksRecursively(Material original, Player player, ItemStack tool, Set<Block> broken, Set<Block> generation) {
		Set<Block> nextGeneration = new HashSet<>();
		for(Block block : generation) {
			if(tool.getType() == Material.AIR)
				return;

			if(block.getType() != original)
				continue;

			if(!broken.add(block))
				continue;

			System.out.println(DebugUtils.toString(tool, 0, 1));
			player.breakBlock(block);
			System.out.println(DebugUtils.toString(tool, 0, 1));

			nextGeneration.addAll(this.getNextGeneration(block));
		}
	}

}
