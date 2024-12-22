
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;
import me.gamma.cookies.util.ItemBuilder;



public class Freezer extends AbstractCraftingMachine {

	public Freezer(MachineTier tier) {
		super(tier);
	}


	@Override
	public String getMachineRegistryName() {
		return "freezer";
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe(this, "water_bottle_to_snow_ball", new ItemStack(Material.SNOWBALL), new ItemBuilder(Material.POTION).setBasePotionType(PotionType.WATER).build(), 200));
			recipes.add(new SimpleMachineRecipe(this, "snow_to_ice", new ItemStack(Material.ICE), new ItemStack(Material.SNOW_BLOCK), 200));

			if(this.tier.getTier() > 1) {
				recipes.add(new SimpleMachineRecipe(this, "ice_to_packed_ice", new ItemStack(Material.PACKED_ICE), new ItemStack(Material.ICE), 400));

				if(this.tier.getTier() > 2) {
					recipes.add(new SimpleMachineRecipe(this, "packed_ice_to_blue_ice", new ItemStack(Material.BLUE_ICE), new ItemStack(Material.PACKED_ICE), 600));

					if(this.tier.getTier() > 3) {
						recipes.add(new SimpleMachineRecipe(this, "water_bucket_to_powdered_snow", new ItemStack(Material.POWDER_SNOW_BUCKET), new ItemStack(Material.WATER_BUCKET), 1200));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Freezer";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FREEZER;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PRISMARINE_SHARD;
	}

}
