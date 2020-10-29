package me.gamma.cookies.objects.block.skull;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.util.ItemBuilder;


public abstract class AdvancedMachine extends AbstractMachine {

	@Override
	public Inventory createInventory(TileState block) {
		Inventory gui = Bukkit.createInventory(null, this.getRows() * 9, this.getDisplayName());
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i : this.getOutputSlots())
			gui.setItem(i, new ItemBuilder(Material.BARRIER).setName(" ").build());
		for(int i = 9; i <= 12; i++) {
			gui.setItem(i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(i + 18, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		}
		gui.setItem(18, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(21, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		for(int i = 14; i <= 17; i++) {
			gui.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(i + 18, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
		}
		gui.setItem(23, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(26, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(13, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(31, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(this.getProgressSlot(), this.getProgressIcon());
		Location location = block.getLocation();
		gui.setItem(this.getIdentifierSlot(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§9Location: §3" + location.getBlockX() + " : " + location.getBlockY() + " : " + location.getBlockZ()).build());
		return gui;
	}


	@Override
	public int getProgressSlot() {
		return 22;
	}


	@Override
	public int getRows() {
		return 5;
	}


	@Override
	public List<Integer> getInputSlots() {
		return Arrays.asList(19, 20);
	}


	@Override
	public List<Integer> getOutputSlots() {
		return Arrays.asList(24, 25);
	}

}
