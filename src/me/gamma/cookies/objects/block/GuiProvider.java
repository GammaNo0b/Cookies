
package me.gamma.cookies.objects.block;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.managers.InventoryManager;



public interface GuiProvider {

	String getDisplayName();

	int getRows();

	Sound getSound();

	List<Player> getViewers(TileState block);


	default boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		return false;
	}


	default boolean onPlayerInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		return false;
	}


	default void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		this.getViewers(block).remove(player);
	}


	default int getIdentifierSlot() {
		return 4;
	}


	default int getFreeSlots() {
		return (this.getRows() - 2) * 7;
	}


	default Inventory createMainGui(Player player, TileState block) {
		Inventory gui = Bukkit.createInventory(null, this.getRows() * 9, this.getDisplayName());
		gui.setItem(this.getIdentifierSlot(), InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
		InventoryManager.setIdentifierStack(block.getLocation(), gui.getItem(this.getIdentifierSlot()));
		return gui;
	}


	default void openGui(Player player, TileState block) {
		this.openGui(player, block, true);
	}


	default void openGui(Player player, TileState block, boolean playsound) {
		Sound sound = this.getSound();
		if(sound != null)
			player.playSound(player.getLocation(), sound, 0.2F, 1);
		player.openInventory(this.createMainGui(player, block));
		this.getViewers(block).add(player);
	}


	default void update(TileState block) {
		for(Player player : this.getViewers(block))
			this.openGui(player, block, false);
	}


	default Location getLocation(World world, Inventory gui) {
		return InventoryManager.getIdentifierLocation(world, gui.getItem(this.getIdentifierSlot()));
	}

}
