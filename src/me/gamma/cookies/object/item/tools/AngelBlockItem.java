
package me.gamma.cookies.object.item.tools;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.BlockUtils;



public class AngelBlockItem extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "angel_block";
	}


	@Override
	public String getTitle() {
		return "§6Angel Block";
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.ANGEL_BLOCK;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		Block block = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(4)).toLocation(player.getWorld()).getBlock();
		if(BlockUtils.isReplaceable(block.getState()))
			Blocks.ANGEL_BLOCK.place(block);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		Block target = BlockUtils.getBlockToPlace(block, event.getBlockFace());
		if(target != null)
			Blocks.ANGEL_BLOCK.place(player, stack, target);
		return true;
	}

}
