
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.gui.book.VanillaRecipeBook;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class VanillaRecipeBookItem extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "vanilla_recipe_book";
	}


	@Override
	public String getTitle() {
		return "§aVanilla Recipe Book";
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection(null, true).add("§7Lists and displays all vanilla recipes.");
	}


	@Override
	public Material getMaterial() {
		return Material.KNOWLEDGE_BOOK;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		VanillaRecipeBook.openBook(player);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		VanillaRecipeBook.openBook(player);
		return true;
	}

}
