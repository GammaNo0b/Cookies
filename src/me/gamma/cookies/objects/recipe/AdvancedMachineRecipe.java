
package me.gamma.cookies.objects.recipe;


import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class AdvancedMachineRecipe implements MachineRecipe {

	private String identifier;
	private ItemStack result;
	private ItemStack[] extra;
	private ItemStack[] ingredients;
	private int duration;

	public AdvancedMachineRecipe(String identifier, ItemStack result, int duration, ItemStack... extra) {
		this.identifier = identifier;
		this.result = result;
		this.duration = duration;
		this.extra = extra;
	}


	public AdvancedMachineRecipe(String identifier, ItemStack result, RecipeCategory category, int duration, ItemStack... extra) {
		this(identifier, result, duration, extra);
		category.registerRecipe(this);
	}


	public AdvancedMachineRecipe setIngredients(ItemStack... ingredients) {
		this.ingredients = ingredients;
		return this;
	}


	@Override
	public ItemStack getResult() {
		return this.result;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public int getDuration() {
		return this.duration;
	}


	@Override
	public ItemStack[] getIngredients() {
		return this.ingredients;
	}


	@Override
	public ItemStack[] getExtraResults() {
		return this.extra;
	}
	
	@Override
	public ItemStack createIcon() {
		return this.result;
	}
	
	@Override
	public Inventory display(String title) {
		Inventory gui = Bukkit.createInventory(null, 6 * 9, title);
		
		return gui;
	}

}
