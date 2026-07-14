
package me.gamma.cookies.object.block.network.item;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.block.network.AbstractStorageComponentBlock;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.item.ItemStorageReader;



public class ItemStorageReaderBlock extends AbstractStorageComponentBlock<ItemStack, ItemStorageReaderBlock, ItemStorageReader> implements Cartesian {

	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public String getIdentifier() {
		return "item_storage_reader";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_READER;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ItemStorageReader reader = this.getTileEntity(block);
		if(reader == null)
			return true;

		if(!reader.canAccess(player.getUniqueId())) {
			player.sendMessage("§cYou do not own this storage reader.");
			return true;
		}

		ItemFilterGui.open(player, reader, null, Material.ORANGE_STAINED_GLASS_PANE);

		return true;
	}


	@Override
	public ItemStorageReaderBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemStorageReader createNewTileEntity(Block block) {
		return new ItemStorageReader(this, block);
	}

}
