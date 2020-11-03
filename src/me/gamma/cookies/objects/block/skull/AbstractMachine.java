
package me.gamma.cookies.objects.block.skull;


import java.util.HashMap;
import java.util.HashSet;
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
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.objects.block.Machine;
import me.gamma.cookies.objects.recipe.MachineRecipe;



public abstract class AbstractMachine extends AbstractGuiProvidingSkullBlock implements Machine {

	private Set<Location> locations = new HashSet<>();
	private Map<Location, Inventory> machineMap = new HashMap<>();
	private Map<String, MachineRecipe> recipeMap = this.createRecipeMap();

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
		return locations;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		MACHINE_IDENTIFIER.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		this.onBlockPlace(block);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		this.onBlockBreak(block);
		return super.onBlockBreak(player, block, event);
	}


	public static boolean isMachine(PersistentDataHolder holder) {
		return MACHINE_IDENTIFIER.isPropertyOf(holder);
	}

}
