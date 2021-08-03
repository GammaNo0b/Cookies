
package me.gamma.cookies.objects.recipe;


import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.util.ItemBuilder;



public class SimpleMachineRecipe implements MachineRecipe {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.##");

	private String identifier;
	private ItemStack result;
	private ItemStack ingredient;
	private int duration;

	public SimpleMachineRecipe(String identifier, ItemStack result, ItemStack ingredient, int duration) {
		this.identifier = identifier;
		this.result = result;
		this.ingredient = ingredient;
		this.duration = duration;
	}


	public SimpleMachineRecipe(String identifier, ItemStack result, int amount, ItemStack ingredient, int duration) {
		this(identifier, new ItemBuilder(result).setAmount(amount).build(), ingredient, duration);
	}


	public SimpleMachineRecipe(String identifier, ItemStack result, RecipeCategory category, ItemStack ingredient, int duration) {
		this(identifier, result, ingredient, duration);
		category.registerRecipe(this);
	}


	public SimpleMachineRecipe(String identifier, ItemStack result, int amount, RecipeCategory category, ItemStack ingredient, int duration) {
		this(identifier, new ItemBuilder(result).setAmount(amount).build(), category, ingredient, duration);
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public ItemStack getResult() {
		return result;
	}


	@Override
	public ItemStack[] getExtraResults() {
		return new ItemStack[0];
	}


	@Override
	public int getDuration() {
		return this.duration;
	}


	@Override
	public ItemStack[] getIngredients() {
		return new ItemStack[] {
			this.ingredient
		};
	}


	@Override
	public ItemStack createIcon() {
		return this.result;
	}


	@Override
	public Inventory display(String title) {
		Inventory gui = Bukkit.createInventory(null, 3 * 9, title);
		final ItemStack filler = InventoryManager.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		for(int i = 0; i < gui.getSize(); i++)
			gui.setItem(i, filler);
		gui.setItem(12, this.ingredient);
		gui.setItem(13, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9--- §3" + FORMAT.format(this.getDuration() / 20.0D) + "s §9-->").build());
		gui.setItem(14, this.result);
		return gui;
	}

}
