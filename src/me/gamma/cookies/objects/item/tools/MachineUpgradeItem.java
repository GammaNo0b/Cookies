
package me.gamma.cookies.objects.item.tools;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class MachineUpgradeItem extends AbstractCustomItem {

	private String identifier;
	private String name;
	private int customModelData;
	private ItemStack ingredient1;
	private ItemStack ingredient2;
	private ItemStack ingredient3;
	private ItemStack ingredient4;
	
	
	public MachineUpgradeItem(String name, int customModelData, ItemStack ingredient) {
		this(name, customModelData, ingredient, ingredient, ingredient, ingredient);
	}
	
	
	public MachineUpgradeItem(String name, int customModelData, ItemStack ingredientHorizontal, ItemStack ingredientVertical) {
		this(name, customModelData, ingredientVertical, ingredientHorizontal, ingredientHorizontal, ingredientVertical);
	}

	public MachineUpgradeItem(String name, int customModelData, ItemStack ingredient1, ItemStack ingredient2, ItemStack ingredient3, ItemStack ingredient4) {
		this.identifier = "upgrade_" + name.replaceAll("§[0-9a-fk-or]", "").replace(' ', '_').toLowerCase();
		this.name = name;
		this.customModelData = customModelData;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
		this.ingredient4 = ingredient4;
	}


	@Override
	public String getRegistryName() {
		return identifier;
	}


	@Override
	public String getDisplayName() {
		return name;
	}


	@Override
	public Material getMaterial() {
		return Material.PAPER;
	}


	@Override
	public int getCustomModelData() {
		return customModelData;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.get(), RecipeCategory.TECHNICAL_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape("G1G", "2R3", "G4G");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('R', CustomItemSetup.UPGRADE_BASE.get());
		recipe.setIngredient('1', ingredient1);
		recipe.setIngredient('2', ingredient2);
		recipe.setIngredient('3', ingredient3);
		recipe.setIngredient('4', ingredient4);
		return recipe;
	}

}
