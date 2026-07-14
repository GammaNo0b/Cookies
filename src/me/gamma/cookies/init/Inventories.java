
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.BLOCKS;
import static me.gamma.cookies.init.Registries.BOOKS;
import static me.gamma.cookies.init.Registries.INVENTORY_HANDLERS;
import static me.gamma.cookies.init.Registries.ITEMS;

import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.gui.BlockFaceConfigs;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.gui.util.ColorGrid;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.tile.machine.StarMaker;



public class Inventories {

	public static StarMaker.ColorSelectionInventory COLOR_SELECTION_PANEL;
	public static ColorGrid COLOR_WHEEL;
	public static ItemFilterGui ITEM_FILTER;
	public static MachineUpgradeGui MACHINE_UPGRADES;
	public static BlockFaceConfigs BLOCK_FACE_CONFIGS;
	public static BlockFaceConfig BLOCK_FACE_CONFIG;

	public static void init() {
		BLOCKS.filterByClass(InventoryProvider.class).forEach(INVENTORY_HANDLERS::register);
		ITEMS.filterByClass(InventoryProvider.class).forEach(INVENTORY_HANDLERS::register);
		BOOKS.forEach(INVENTORY_HANDLERS::register);

		COLOR_SELECTION_PANEL = INVENTORY_HANDLERS.register(new StarMaker.ColorSelectionInventory());
		COLOR_WHEEL = INVENTORY_HANDLERS.register(new ColorGrid());
		ITEM_FILTER = INVENTORY_HANDLERS.register(new ItemFilterGui());
		MACHINE_UPGRADES = INVENTORY_HANDLERS.register(new MachineUpgradeGui());
		BLOCK_FACE_CONFIGS = INVENTORY_HANDLERS.register(new BlockFaceConfigs());
		BLOCK_FACE_CONFIG = INVENTORY_HANDLERS.register(new BlockFaceConfig());
	}

}
