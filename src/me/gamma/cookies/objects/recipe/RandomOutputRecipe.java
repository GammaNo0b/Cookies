
package me.gamma.cookies.objects.recipe;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class RandomOutputRecipe implements MachineRecipe {

	private static final DecimalFormat CHANCE = new DecimalFormat("0.00%");
	private static final DecimalFormat DURATION = new DecimalFormat("0.0s");

	private Random r;
	private int maxChance;
	private Map<ItemStack, Integer> outputs = new HashMap<>();
	private ItemStack ingredient;
	private String identifier;
	private int duration;

	public RandomOutputRecipe(String identifier, ItemStack ingredient, int duration) {
		this(identifier, ingredient, duration, new Random());
	}


	public RandomOutputRecipe(String identifier, ItemStack ingredient, int duration, long seed) {
		this(identifier, ingredient, duration, new Random(seed));
	}


	public RandomOutputRecipe(String identifier, ItemStack ingredient, int duration, Random random) {
		this.identifier = identifier;
		this.duration = duration;
		this.ingredient = ingredient;
		this.r = random;
		this.maxChance = 0;
	}


	public RandomOutputRecipe addOutput(Material output, int chance) {
		return this.addOutput(new ItemStack(output), chance);
	}


	public RandomOutputRecipe addOutput(ItemStack output, int chance) {
		this.outputs.put(output, chance);
		this.maxChance += chance;
		return this;
	}


	@Override
	public ItemStack getResult() {
		int n = r.nextInt(maxChance);
		int currentChance = 0;
		for(ItemStack stack : outputs.keySet()) {
			int chance = outputs.get(stack);
			if(currentChance <= n && n < currentChance + chance) {
				return stack;
			}
			currentChance += chance;
		}
		return null;
	}


	@Override
	public String getIdentifier() {
		return identifier;
	}


	@Override
	public int getDuration() {
		return duration;
	}


	@Override
	public ItemStack[] getIngredients() {
		return new ItemStack[] {
			ingredient
		};
	}


	@Override
	public ItemStack[] getExtraResults() {
		return new ItemStack[0];
	}


	@Override
	public ItemStack createIcon() {
		return new ItemBuilder(ingredient).setName((ingredient.hasItemMeta() && ingredient.getItemMeta().hasDisplayName() ? ingredient.getItemMeta().getDisplayName() : "§9" + Utilities.toCapitalWords(ingredient.getType().name().replace('_', ' '))) + "§9 - §3" + DURATION.format(this.duration / 20.0D)).build();
	}


	@Override
	public Inventory display(String title) {
		int rows = Math.min(4, (outputs.size() + 6) / 7) + 2;
		Inventory gui = Bukkit.createInventory(null, rows * 9, title);
		final ItemStack filler = InventoryManager.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		final ItemStack border = InventoryManager.filler(Material.BLUE_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, border);
			gui.setItem(gui.getSize() - i - 1, border);
		}
		for(int i = 1; i < rows - 1; i++) {
			gui.setItem(i * 9, border);
			gui.setItem(i * 9 + 8, border);
		}
		gui.setItem(gui.getSize() - 5, this.createIcon());
		List<ItemStack> results = outputs.keySet().stream().sorted((item1, item2) -> outputs.getOrDefault(item2, 0).compareTo(outputs.getOrDefault(item1, 0))).collect(Collectors.toList());
		for(int i = 0; i < (rows - 2) * 7; i++) {
			int row = i / 7 + 1;
			int column = i % 7 + 1;
			int index = row * 9 + column;
			if(i < results.size()) {
				ItemStack result = results.get(i);
				String chance = " §2: " + CHANCE.format(Math.round(outputs.getOrDefault(result, 0) * 1000.0D / maxChance) / 1000.0D);
				if(result == null || result.getType() == Material.AIR) {
					gui.setItem(index, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§7Air" + chance).build());
				} else {
					gui.setItem(index, new ItemBuilder(result).setName((result.getItemMeta().hasDisplayName() ? result.getItemMeta().getDisplayName() : "§7" + Utilities.toCapitalWords(result.getType().name().replace('_', ' '))) + chance).build());
				}
			} else {
				gui.setItem(index, filler);
			}
		}
		return gui;
	}

}
