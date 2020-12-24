
package me.gamma.cookies.objects.block;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.util.ItemBuilder;



public interface GuiProvider {

	String getDisplayName();


	int getRows();


	Sound getSound();


	default boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		return false;
	}


	default boolean onPlayerInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		return false;
	}


	default void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {}


	default int getIdentifierSlot() {
		return 4;
	}


	default int getFreeSlots() {
		return (this.getRows() - 2) * 7;
	}


	default Inventory createMainGui(Player player, TileState block) {
		Inventory gui = Bukkit.createInventory(null, this.getRows() * 9, this.getDisplayName());
		Location location = block.getLocation();
		gui.setItem(this.getIdentifierSlot(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§9Location: §3" + location.getBlockX() + " : " + location.getBlockY() + " : " + location.getBlockZ()).build());
		return gui;
	}


	default void openGui(Player player, TileState block) {
		player.playSound(player.getLocation(), this.getSound(), 0.2F, 1);
		player.openInventory(this.createMainGui(player, block));
	}

}
