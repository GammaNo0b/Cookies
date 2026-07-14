
package me.gamma.cookies.object.block.network.item;


import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.item.Trashcan;



public class TrashcanBlock extends AbstractCustomTileBlock<TrashcanBlock, Trashcan> {

	@Override
	public String getIdentifier() {
		return "trashcan";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.TRASHCAN;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		player.openInventory(Bukkit.createInventory(null, 36, "§8Trashcan"));

		return true;
	}


	@Override
	public TrashcanBlock castCustomBlock() {
		return this;
	}


	@Override
	public Trashcan createNewTileEntity(Block block) {
		return new Trashcan(this, block);
	}

}
