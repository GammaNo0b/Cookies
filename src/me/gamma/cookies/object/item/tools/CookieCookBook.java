
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.gui.book.CookieMenuBook;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CookieCookBook extends AbstractCustomItem {

	private static final String KEY_OPEN_ON_EAT = "openoneat";

	@Override
	public String getIdentifier() {
		return "cookie_cook_book";
	}


	@Override
	public String getTitle() {
		return "§6Cookie Cook Book";
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection(null, true).add("§7Collection of all recipes written down in a cookie.");
	}


	@Override
	public Material getMaterial() {
		return Material.COOKIE;
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		customData.setBoolean(KEY_OPEN_ON_EAT, true);
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
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return true;

		boolean openOnEat = data.getData().getBoolean(KEY_OPEN_ON_EAT, true);
		if(player.isSneaking()) {
			if(openOnEat) {
				player.sendMessage("§aOpen on Eat enabled!");
			} else {
				player.sendMessage("§cOpen on Eat disabled!");
			}
			data.getData().setBoolean(KEY_OPEN_ON_EAT, !openOnEat);
			data.save();
			return true;
		} else if(!openOnEat) {
			Utils.runLater(() -> CookieMenuBook.openBook(player, false));
			return true;
		}
		return false;
	}


	@Override
	public boolean onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		Utils.runLater(() -> CookieMenuBook.openBook(player, false));
		return true;
	}

}
