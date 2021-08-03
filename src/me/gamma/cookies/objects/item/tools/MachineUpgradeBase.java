
package me.gamma.cookies.objects.item.tools;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class MachineUpgradeBase extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "machine_upgrade_base";
	}


	@Override
	public String getDisplayName() {
		return "§cMachine Upgrade Base";
	}


	@Override
	public Material getMaterial() {
		return Material.PAPER;
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.UPGRADE_BASE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.get(), RecipeCategory.TECHNICAL_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape("RR", "RR");
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}

}
