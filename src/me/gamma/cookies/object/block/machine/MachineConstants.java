
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.InventoryUtils;



public class MachineConstants {

	public static final int GUI_ROWS = 5;
	public static final int IDENTIFIER_SLOT = 1;
	public static final int RECIPES_SLOT = 6;
	public static final int PROGRESS_SLOT = 22;
	public static final int ENERGY_LEVEL_SLOT = 40;
	public static final int REDSTONE_MODE_SLOT = 31;
	public static final int INPUT_MODE_SLOT = 37;
	public static final int OUTPUT_MODE_SLOT = 43;
	public static final int BLOCK_FACE_CONFIG_SLOT = 4;
	public static final int UPGRADE_SLOT = 13;

	public static final ItemStack BORDER_MATERIAL = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
	public static final ItemStack INPUT_BORDER_MATERIAL = InventoryUtils.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
	public static final ItemStack OUTPUT_BORDER_MATERIAL = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
	public static final ItemStack FILLER_MATERIAL = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

	public static final int[][] inputSlots = { { 19 }, { 19, 20 }, { 10, 11, 19, 20, 28, 29 }, { 9, 10, 11, 18, 19, 20, 27, 28, 29 } };
	public static final int[][] outputSlots = { { 25 }, { 24, 25 }, { 15, 16, 24, 25, 33, 34 }, { 15, 16, 17, 24, 25, 26, 33, 34, 35 } };
	public static final int[][] inputBorderSlots = { { 9, 10, 11, 18, 20, 27, 28, 29 }, { 9, 10, 11, 12, 18, 21, 27, 28, 29, 30 }, { 9, 12, 18, 21, 27, 30 }, { 12, 21, 30 } };
	public static final int[][] outputBorderSlots = { { 15, 16, 17, 24, 26, 33, 34, 35 }, { 14, 15, 16, 17, 23, 26, 32, 33, 34, 35 }, { 14, 17, 23, 26, 32, 35 }, { 14, 23, 32 } };
	public static final int[][] fillerSlots = { { 12, 14, 21, 23, 30, 32 }, {}, {}, {} };

	public static int[] getInputSlots(MachineTier tier) {
		return inputSlots[tier.ordinal()];
	}


	public static int[] getOutputSlots(MachineTier tier) {
		return outputSlots[tier.ordinal()];
	}


	public static Inventory createGui(AbstractGuiMachine machine, TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(machine, block);
		setupInventory(gui, machine.getTier());
		return gui;
	}


	public static void setupInventory(Inventory gui, MachineTier tier) {
		InventoryUtils.fillTopBottom(gui, BORDER_MATERIAL);
		int i = tier.ordinal();
		for(int slot : inputBorderSlots[i])
			gui.setItem(slot, INPUT_BORDER_MATERIAL);
		for(int slot : outputBorderSlots[i])
			gui.setItem(slot, OUTPUT_BORDER_MATERIAL);
		for(int slot : fillerSlots[i])
			gui.setItem(slot, FILLER_MATERIAL);
	}

}
