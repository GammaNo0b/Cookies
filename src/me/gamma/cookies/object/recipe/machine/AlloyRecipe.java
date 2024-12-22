
package me.gamma.cookies.object.recipe.machine;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.AbstractMachine;



public class AlloyRecipe extends AdvancedMachineRecipe {

	public AlloyRecipe(AbstractMachine machine, String identifier, ItemStack result, int duration, ItemStack... ingredients) {
		super(machine, identifier, result, duration);
		this.setIngredients(ingredients);
	}


	public AlloyRecipe(AbstractMachine machine, String identifier, ItemStack result, int duration, Material... ingredients) {
		super(machine, identifier, result, duration);
		this.setIngredients(ingredients);
	}

}
