
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.BLOCKS;
import static me.gamma.cookies.init.Registries.BOOKS;
import static me.gamma.cookies.init.Registries.INVENTORY_HANDLERS;
import static me.gamma.cookies.init.Registries.ITEMS;

import me.gamma.cookies.object.block.machine.StarMaker;
import me.gamma.cookies.object.gui.InventoryHandler;
import me.gamma.cookies.object.gui.util.ColorWheel;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;



public class Inventories {

	public static StarMaker.ColorSelectionInventory COLOR_SELECTION_PANEL;
	public static ColorWheel COLOR_WHEEL;
	public static ItemFilterGui ITEM_FILTER;
	public static MachineUpgradeGui MACHINE_UPGRADES;

	public static void init() {
		BLOCKS.filterByClass(InventoryHandler.class).forEach(INVENTORY_HANDLERS::register);
		ITEMS.filterByClass(InventoryHandler.class).forEach(INVENTORY_HANDLERS::register);
		BOOKS.forEach(INVENTORY_HANDLERS::register);

		COLOR_SELECTION_PANEL = INVENTORY_HANDLERS.register(new StarMaker.ColorSelectionInventory());
		COLOR_WHEEL = INVENTORY_HANDLERS.register(new ColorWheel());
		ITEM_FILTER = INVENTORY_HANDLERS.register(new ItemFilterGui());
		MACHINE_UPGRADES = INVENTORY_HANDLERS.register(new MachineUpgradeGui());
	}

}
