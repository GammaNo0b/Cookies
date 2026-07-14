
package me.gamma.cookies.object.block;


import static me.gamma.cookies.object.tile.ClownfishChest.KEY_INVENTORY;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.ClownfishChest;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ClownfishChestBlock extends AbstractCustomTileBlock<ClownfishChestBlock, ClownfishChest> {

	@Override
	public String getIdentifier() {
		return "clownfish_chest";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CLOWNFISH_CHEST;
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		Pair<Inventory, String> pair = PersistentDataUtils.getInventory(tileData, KEY_INVENTORY);
		if(pair != null)
			PersistentDataUtils.setInventory(itemData, KEY_INVENTORY, pair.left, pair.right);
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ClownfishChest chest = this.getTileEntity(block);
		if(chest != null)
			player.openInventory(chest.getInventory());

		return true;
	}


	@Override
	public ClownfishChestBlock castCustomBlock() {
		return this;
	}


	@Override
	public ClownfishChest createNewTileEntity(Block block) {
		return new ClownfishChest(this, block);
	}

}
