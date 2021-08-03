
package me.gamma.cookies.objects.block.skull.machine;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.machine.Machine;
import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.recipe.MachineRecipe;



public abstract class AbstractSkullMachine extends AbstractGuiProvidingSkullBlock implements Machine {

	protected final MachineTier tier;
	private final Set<Location> locations = new HashSet<>();
	private final Map<Location, Inventory> machineMap = new HashMap<>();
	private final Map<String, MachineRecipe> recipeMap = new HashMap<>();

	protected AbstractSkullMachine(MachineTier tier) {
		this.tier = tier;

		register();
	}


	@Override
	public MachineTier getTier() {
		return this.tier;
	}


	@Override
	public Map<Location, Inventory> getMachineMap() {
		return this.machineMap;
	}


	@Override
	public Map<String, MachineRecipe> getRecipeMap() {
		return this.recipeMap;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public String getRegistryName() {
		return Machine.super.getRegistryName();
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		MACHINE_IDENTIFIER.storeEmpty(meta);
		stack.setItemMeta(meta);
		Machine.super.createDefaultItemStack(stack);
		return stack;
	}


	@Override
	public ItemStack createIcon() {
		return this.createDefaultItemStack();
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		Machine.super.onBlockPlace(block, usedItem);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack stack = super.onBlockBreak(player, block, event);
		this.onBlockBreak(block, stack);
		return stack;
	}


	@Override
	public List<Integer> getInputSlots() {
		return this.getTier().getDefaultInputSlots();
	}


	@Override
	public List<Integer> getOutputSlots() {
		return this.getTier().getDefaultOutputSlots();
	}


	@Override
	public int getProgressSlot() {
		return this.getTier().getDefaultProgressSlot();
	}


	@Override
	public int getRecipeBookSlot() {
		return this.getTier().getDefaultRecipeBookSlot();
	}


	@Override
	public int getRedstoneModeSlot() {
		return this.getTier().getDefaultRedstoneModeSlot();
	}


	@Override
	public int getMachineUpgradesSlot() {
		return this.getTier().getDefaultMachineUpgradesSlot();
	}


	@Override
	public int getUpgradeSlots() {
		return this.getTier().getUpgradeSlots();
	}


	@Override
	public int getRows() {
		return this.getTier().getDefaultRowCount();
	}


	@Override
	public int getSpeed() {
		return this.getTier().getDefaultSpeed();
	}

}
