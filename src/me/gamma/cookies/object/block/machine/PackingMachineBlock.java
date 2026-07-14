
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.tile.machine.PackingMachine;



public class PackingMachineBlock extends AbstractCraftingMachineBlock<PackingMachineBlock, PackingMachine> {

	public PackingMachineBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public String getTitle() {
		return "§6Packing Machine";
	}


	@Override
	public String getMachineRegistryName() {
		return "packing_machine";
	}


	@Override
	public List<MachineRecipe> getAllMachineRecipes() {
		return new ArrayList<>();
	}


	@Override
	public PackingMachineBlock castCustomBlock() {
		return this;
	}


	@Override
	public PackingMachine createNewTileEntity(Block block) {
		return new PackingMachine(this, block);
	}

}
