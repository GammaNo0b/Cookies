
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class MultiBlockBook extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "multi_block_book";
	}


	@Override
	public String getTitle() {
		return "§dMulti Block Book";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Collection of multiblock building instructions.");
	}


	@Override
	public Material getMaterial() {
		return Material.BOOK;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		me.gamma.cookies.object.gui.book.MultiBlockBook.openBook(player);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		me.gamma.cookies.object.gui.book.MultiBlockBook.openBook(player);
		return true;
	}

}
