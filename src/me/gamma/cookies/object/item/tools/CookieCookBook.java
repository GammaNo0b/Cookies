
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.gui.book.CookieMenuBook;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.PropertyBuilder;



public class CookieCookBook extends AbstractCustomItem {

	private static BooleanProperty OPEN_ON_EAT = new BooleanProperty("eat");

	@Override
	public String getIdentifier() {
		return "cookie_cook_book";
	}


	@Override
	public String getTitle() {
		return "§6Cookie Cook Book";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Collection of all recipes written down in a cookie.");
	}


	@Override
	public Material getMaterial() {
		return Material.COOKIE;
	}
	
	@Override
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return super.buildItemProperties(builder).add(OPEN_ON_EAT, true);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return this.openCookieCookBook(player, stack);
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return this.openCookieCookBook(player, stack);
	}


	private boolean openCookieCookBook(Player player, ItemStack stack) {
		if(player.isSneaking()) {
			ItemMeta meta = stack.getItemMeta();
			if(OPEN_ON_EAT.toggle(meta)) {
				player.sendMessage("§aOpen on Eat enabled!");
			} else {
				player.sendMessage("§cOpen on Eat disabled!");
			}
			stack.setItemMeta(meta);
			return true;
		} else if(!OPEN_ON_EAT.fetch(stack.getItemMeta())) {
			CookieMenuBook.openBook(player, false);
			return true;
		}
		return false;
	}


	@Override
	public boolean onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		CookieMenuBook.openBook(player, false);
		return true;
	}

}
