
package me.gamma.cookies.objects.block.machine;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.util.ItemBuilder;



public interface MachineInventorySupplier {

	public static final MachineInventorySupplier BASIC_INVENTORY_SUPPLIER = new MachineInventorySupplier() {

		@Override
		public Inventory create(Location location, Machine machine) {
			Inventory gui = Bukkit.createInventory(null, machine.getRows() * 9, machine.getDisplayName());
			for(int i = 0; i < 9; i++) {
				gui.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
			}
			for(int i = 9; i <= 11; i++) {
				gui.setItem(i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(i + 18, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			}
			gui.setItem(18, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(20, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			for(int i = 15; i <= 17; i++) {
				gui.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(i + 18, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			}
			gui.setItem(24, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(26, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			for(int i = 12; i <= 14; i++) {
				gui.setItem(i, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(i + 18, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
			}
			gui.setItem(21, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(23, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
			return gui;

		}

	};

	public static final MachineInventorySupplier ADVANCED_INVENTORY_SUPPLIER = new MachineInventorySupplier() {

		@Override
		public Inventory create(Location location, Machine machine) {
			Inventory gui = Bukkit.createInventory(null, machine.getRows() * 9, machine.getDisplayName());
			for(int i = 0; i < 9; i++) {
				gui.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
			}
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
			return gui;
		}

	};

	public static final MachineInventorySupplier IMPROVED_INVENTORY_SUPPLIER = new MachineInventorySupplier() {

		@Override
		public Inventory create(Location location, Machine machine) {
			Inventory gui = Bukkit.createInventory(null, machine.getRows() * 9, machine.getDisplayName());
			gui.setItem(40, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
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
			return gui;
		}

	};

	public static final MachineInventorySupplier PERFECTED_INVENTORY_SUPPLIER = new MachineInventorySupplier() {

		@Override
		public Inventory create(Location location, Machine machine) {
			Inventory gui = Bukkit.createInventory(null, machine.getRows() * 9, machine.getDisplayName());
			gui.setItem(40, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
			for(int i = 0; i <= 3; i++) {
				gui.setItem(i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(i + 36, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			}
			for(int i = 0; i <= 3; i++) {
				gui.setItem((i + 1) * 9 + 3, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			}
			for(int i = 5; i <= 8; i++) {
				gui.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(i + 36, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			}
			for(int i = 0; i <= 3; i++) {
				gui.setItem((i + 1) * 9 + 5, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(" ").build());
			}
			gui.setItem(13, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(31, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
			return gui;
		}

	};

	Inventory create(Location location, Machine machine);

}
