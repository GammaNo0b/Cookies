
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;



public class CarbonPress extends AbstractCraftingMachine {

	public CarbonPress(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();
		if(this.tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe(this, "pulverized_coal_to_coal", new ItemStack(Material.COAL), Items.PULVERIZED_COAL.get(), 160));
			recipes.add(new SimpleMachineRecipe(this, "coal_to_carbon", Items.CARBON.get(), new ItemStack(Material.COAL, 4), 200));

			if(this.tier.getTier() > 1) {
				recipes.add(new SimpleMachineRecipe(this, "carbon_to_compressed_carbon", Items.COMPRESSED_CARBON.get(), Items.CARBON.get(), 8, 400));
				recipes.add(new SimpleMachineRecipe(this, "compressed_carbon_to_carbon_chunk", Items.CARBON_CHUNK.get(), Items.COMPRESSED_CARBON.get(), 8, 600));

				if(this.tier.getTier() > 2) {
					recipes.add(new SimpleMachineRecipe(this, "carbon_chunk_to_carbonado", Items.CARBONADO.get(), Items.CARBON_CHUNK.get(), 4, 1200));

					if(this.tier.getTier() > 3) {
						recipes.add(new SimpleMachineRecipe(this, "carbonado_to_diamond", new ItemStack(Material.DIAMOND), Items.CARBONADO.get(), 2400));
					}
				}
			}
		}
		return recipes;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PISTON;
	}


	@Override
	public String getMachineRegistryName() {
		return "carbon_press";
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Carbon Press ";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CARBON_PRESS;
	}

}
