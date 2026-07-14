
package me.gamma.cookies.object.block.machine;


import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.tile.machine.AbstractCraftingMachine;



public abstract class AbstractCraftingMachineBlock<B extends AbstractCraftingMachineBlock<B, T>, T extends AbstractCraftingMachine<T, B>> extends AbstractItemProcessingMachineBlock<B, T> {

	public AbstractCraftingMachineBlock(MachineTier tier) {
		super(tier);
	}


	/**
	 * Returns the list of machine recipes for the given block.
	 * 
	 * @param block the block position
	 * @return the list of recipes
	 */
	public List<MachineRecipe> getMachineRecipes(Block block) {
		return this.getAllMachineRecipes();
	}


	/**
	 * Returns the list of all machine recipes machines of this block can offer.
	 * 
	 * @return the list of recipes
	 */
	public abstract List<MachineRecipe> getAllMachineRecipes();


	/**
	 * Checks if the given {@link PersistentDataHolder} is a crafting machine.
	 * 
	 * @param holder the holder
	 * @return if the holder is a crafting machine
	 */
	public static boolean isCraftingMachine(PersistentDataHolder holder) {
		return Blocks.getCustomBlockFromHolder(holder) instanceof AbstractCraftingMachineBlock;
	}

}
