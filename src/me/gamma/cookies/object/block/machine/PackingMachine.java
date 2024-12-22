
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;

import me.gamma.cookies.object.recipe.machine.MachineRecipe;



public class PackingMachine extends AbstractCraftingMachine {

	public PackingMachine(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		return new ArrayList<>();
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PISTON;
	}


	@Override
	public String getTitle() {
		return "§6Packing Machine";
	}


	@Override
	public String getMachineRegistryName() {
		return "packing_machine";
	}

}
