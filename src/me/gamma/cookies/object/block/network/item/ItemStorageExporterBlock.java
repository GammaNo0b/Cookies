
package me.gamma.cookies.object.block.network.item;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.AbstractStorageInterfaceBlock;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.item.ItemStorageExporter;



public class ItemStorageExporterBlock extends AbstractStorageInterfaceBlock<ItemStack, ItemStorageExporterBlock, ItemStorageExporter> {

	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public String getFilterTitle() {
		return "§c<- §8Storage Exporter §c->";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_EXPORTER;
	}


	@Override
	public String getIdentifier() {
		return "item_storage_exporter";
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ItemStorageExporter exporter = this.getTileEntity(block);
		if(exporter == null)
			return true;

		if(!exporter.canAccess(player.getUniqueId())) {
			player.sendMessage("§cYou cannot access this item exporter!");
			return true;
		}

		ItemFilterGui.open(player, exporter, null, Material.RED_STAINED_GLASS_PANE);

		return true;
	}


	@Override
	public ItemStorageExporterBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemStorageExporter createNewTileEntity(Block block) {
		return new ItemStorageExporter(this, block);
	}

}
