
package me.gamma.cookies.object.block.network.item;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.AbstractStorageInterfaceBlock;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.item.ItemStorageImporter;



public class ItemStorageImporterBlock extends AbstractStorageInterfaceBlock<ItemStack, ItemStorageImporterBlock, ItemStorageImporter> {

	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public String getFilterTitle() {
		return "§2-> §8Storage Importer §2<-";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_IMPORTER;
	}


	@Override
	public String getIdentifier() {
		return "item_storage_importer";
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ItemStorageImporter importer = this.getTileEntity(block);
		if(importer == null)
			return true;

		if(!importer.canAccess(player.getUniqueId())) {
			player.sendMessage("§cYou cannot access this storage importer!");
			return true;
		}

		ItemFilterGui.open(player, importer, null, Material.GREEN_STAINED_GLASS_PANE);

		return true;
	}


	@Override
	public ItemStorageImporterBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemStorageImporter createNewTileEntity(Block block) {
		return new ItemStorageImporter(this, block);
	}

}
