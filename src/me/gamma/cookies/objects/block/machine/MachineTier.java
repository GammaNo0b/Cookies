
package me.gamma.cookies.objects.block.machine;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;



public enum MachineTier {

	BASIC("§aBasic", MachineInventorySupplier.BASIC_INVENTORY_SUPPLIER, Arrays.asList(19), Arrays.asList(25), 22, 40, 31, 13, 1, 5, 1),
	ADVANCED("§eAdvanced", MachineInventorySupplier.ADVANCED_INVENTORY_SUPPLIER, Arrays.asList(19, 20), Arrays.asList(24, 25), 22, 40, 31, 13, 2, 5, 4),
	IMPROVED("§cImproved", MachineInventorySupplier.IMPROVED_INVENTORY_SUPPLIER, Arrays.asList(10, 11, 19, 20, 28, 29), Arrays.asList(15, 16, 24, 25, 33, 34), 22, 40, 31, 13, 3, 5, 10),
	PERFECTED("§5Perfected", MachineInventorySupplier.PERFECTED_INVENTORY_SUPPLIER, Arrays.asList(9, 10, 11, 18, 19, 20, 27, 28, 29), Arrays.asList(15, 16, 17, 24, 25, 26, 33, 34, 35), 22, 40, 31, 13, 5, 5, 20);

	private final String name;
	private final MachineInventorySupplier inventorySupplier;
	private final List<Integer> inputSlots;
	private final List<Integer> outputSlots;
	private final int progressSlot;
	private final int recipeBookSlot;
	private final int redstoneModeSlot;
	private final int machineUpgradesSlot;
	private final int machineUpgradeSlots;
	private final int rows;
	private final int speed;

	private MachineTier(String name, MachineInventorySupplier inventorySupplier, List<Integer> inputSlots, List<Integer> outputSlots, int progressSlot, int recipeBookSlot, int redstoneModeSlot, int machineUpgradesSlot, int machineUpgradeSlots, int rows, int speed) {
		this.name = name;
		this.inventorySupplier = inventorySupplier;
		this.inputSlots = inputSlots;
		this.outputSlots = outputSlots;
		this.progressSlot = progressSlot;
		this.recipeBookSlot = recipeBookSlot;
		this.redstoneModeSlot = redstoneModeSlot;
		this.machineUpgradesSlot = machineUpgradesSlot;
		this.machineUpgradeSlots = machineUpgradeSlots;
		this.rows = rows;
		this.speed = speed;
	}


	public String getName() {
		return name;
	}


	public String getDescripition() {
		return String.format("§9Tier §1(§3%d§1):   %s", getTier(), name);
	}


	public int getTier() {
		return ordinal() + 1;
	}


	public List<Integer> getDefaultInputSlots() {
		return inputSlots;
	}


	public List<Integer> getDefaultOutputSlots() {
		return outputSlots;
	}


	public int getDefaultProgressSlot() {
		return progressSlot;
	}


	public int getDefaultRecipeBookSlot() {
		return recipeBookSlot;
	}


	public int getDefaultRedstoneModeSlot() {
		return redstoneModeSlot;
	}


	public int getDefaultMachineUpgradesSlot() {
		return machineUpgradesSlot;
	}
	
	
	public int getUpgradeSlots() {
		return machineUpgradeSlots;
	}


	public int getDefaultRowCount() {
		return rows;
	}


	public int getDefaultSpeed() {
		return speed;
	}


	public Inventory createInventory(Location location, Machine machine) {
		return this.inventorySupplier.create(location, machine);
	}


	public MachineTier increase() {
		return byTier(getTier() + 1);
	}


	public MachineTier decrease() {
		return byTier(getTier() + values().length - 1);
	}


	public static MachineTier byTier(int tier) {
		return values()[(tier - 1) % values().length];
	}

}
