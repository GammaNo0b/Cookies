
package me.gamma.cookies.objects.recipe;


import org.bukkit.inventory.ItemStack;



public class AlloyRecipe extends AdvancedMachineRecipe {

	public AlloyRecipe(String identifier, ItemStack result, int duration, ItemStack... ingredients) {
		super(identifier, result, duration);
		this.setIngredients(ingredients);
	}

	public AlloyRecipe(String identifier, ItemStack result, RecipeCategory category, int duration, ItemStack... ingredients) {
		super(identifier, result, category, duration);
		this.setIngredients(ingredients);
	}
	
	

}
