
package me.gamma.cookies.objects.block.skull.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.list.TieredMaterials;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.objects.recipe.SimpleMachineRecipe;
import me.gamma.cookies.setup.CustomBlockSetup;



public class Freezer extends AbstractSkullMachine {

	public Freezer(MachineTier tier) {
		super(tier);
	}


	@Override
	public String getMachineIdentifier() {
		return "freezer";
	}


	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe("snow_to_ice", new ItemStack(Material.ICE), new ItemStack(Material.SNOW_BLOCK), 120));

			if(this.tier.getTier() > 1) {
				recipes.add(new SimpleMachineRecipe("ice_to_packed_ice", new ItemStack(Material.PACKED_ICE), new ItemStack(Material.ICE), 240));

				if(this.tier.getTier() > 2) {
					recipes.add(new SimpleMachineRecipe("packed_ice_to_blue_ice", new ItemStack(Material.BLUE_ICE), new ItemStack(Material.PACKED_ICE), 360));

					if(this.tier.getTier() > 3) {
						recipes.add(new SimpleMachineRecipe("water_bucket_to_powdered_snow", new ItemStack(Material.POWDER_SNOW_BUCKET), new ItemStack(Material.WATER_BUCKET), 480));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.PRISMARINE_SHARD);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FREEZER;
	}


	@Override
	public String getDisplayName() {
		return tier.getName() + " §bFreezer";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "MFM", "ICI");
		ItemStack center = null;
		Material cooler = null;
		switch (this.tier) {
			case BASIC:
				center = new ItemStack(Material.IRON_TRAPDOOR);
				cooler = Material.SNOW_BLOCK;
				break;
			case ADVANCED:
				center = CustomBlockSetup.FREEZER.createDefaultItemStack();
				cooler = Material.ICE;
				break;
			case IMPROVED:
				center = CustomBlockSetup.ADVANCED_FREEZER.createDefaultItemStack();
				cooler = Material.PACKED_ICE;
				break;
			case PERFECTED:
				center = CustomBlockSetup.IMPROVED_FREEZER.createDefaultItemStack();
				cooler = Material.BLUE_ICE;
				break;
			default:
				break;
		}
		recipe.setIngredient('S', TieredMaterials.getIngot(tier));
		recipe.setIngredient('F', center);
		recipe.setIngredient('I', cooler);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', TieredMaterials.getCore(tier));
		return recipe;
	}

}
