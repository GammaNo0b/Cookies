
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.tile.machine.Dryer;



public class DryerBlock extends AbstractCraftingMachineBlock<DryerBlock, Dryer> {

	public DryerBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getAllMachineRecipes() {
		return new ArrayList<>();
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Dryer";
	}


	@Override
	public String getMachineRegistryName() {
		return "dryer";
	}


	@Override
	public DryerBlock castCustomBlock() {
		return this;
	}


	@Override
	public Dryer createNewTileEntity(Block block) {
		return new Dryer(this, block);
	}

}
