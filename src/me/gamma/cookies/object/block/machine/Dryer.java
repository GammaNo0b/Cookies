
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;

import me.gamma.cookies.object.recipe.machine.MachineRecipe;



public class Dryer extends AbstractCraftingMachine {

	public Dryer(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		return new ArrayList<>();
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FLINT_AND_STEEL;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Dryer";
	}


	@Override
	public String getMachineRegistryName() {
		return "dryer";
	}

}
