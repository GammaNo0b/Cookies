
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.util.ItemBuilder;



public abstract class ImprovedMachine extends AbstractMachine {

	@Override
	public Inventory createInventory(TileState block) {
		Inventory gui = Bukkit.createInventory(null, this.getRows() * 9, this.getDisplayName());
		gui.setItem(40, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
		for(int i : this.getOutputSlots())
			gui.setItem(i, new ItemBuilder(Material.BARRIER).setName(" ").build());
		for(int i = 0; i <= 3; i++) {
			gui.setItem(i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(i + 36, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i <= 3; i++) {
			gui.setItem((i + 1) * 9, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem((i + 1) * 9 + 3, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 5; i <= 8; i++) {
			gui.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(i + 36, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i <= 3; i++) {
			gui.setItem((i + 1) * 9 + 5, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem((i + 1) * 9 + 8, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
		}
		gui.setItem(13, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(31, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(this.getProgressSlot(), this.getProgressIcon());
		Location location = block.getLocation();
		gui.setItem(this.getIdentifierSlot(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§9Location: §3" + location.getBlockX() + " : " + location.getBlockY() + " : " + location.getBlockZ()).build());
		return gui;
	}


	@Override
	public List<Integer> getInputSlots() {
		return Arrays.asList(10, 11, 19, 20, 28, 29);
	}


	@Override
	public List<Integer> getOutputSlots() {
		return Arrays.asList(15, 16, 24, 25, 33, 34);
	}


	@Override
	public int getRows() {
		return 5;
	}
	
	
	@Override
	public int getProgressSlot() {
		return 22;
	}

}
