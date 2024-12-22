
package me.gamma.cookies.object.gui.task;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.recipe.machine.RandomOutputRecipe;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class RandomOutputRecipeInventoryTask extends RecipeInventoryTask<RandomOutputRecipe> {

	private static final DecimalFormat CHANCE = new DecimalFormat(" §8- §a0.00§2%");

	public RandomOutputRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, RandomOutputRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		this.inventory.setItem(22, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§9--- §3" + Utils.formatTicks(this.recipe.getDuration()) + " §9-->").build());
	}


	@Override
	protected void updateInventory() {
		this.inventory.setItem(20, this.getItemFromItemChoice(this.recipe.getIngredients()[0]));
		List<Map.Entry<ItemStack, Integer>> entries = this.recipe.getOutputs().entrySet().stream().filter(e -> !ItemUtils.isEmpty(e.getKey())).sorted((e1, e2) -> e2.getValue() - e1.getValue()).toList();
		int pages = (entries.size() + 8) / 9;
		List<Map.Entry<ItemStack, Integer>> items = CollectionUtils.subList(entries, (this.cycle % pages) * 9, 9);
		for(int i = 0; i < items.size(); i++) {
			int r = i / 3;
			int c = i - 3 * r;
			Map.Entry<ItemStack, Integer> entry = items.get(i);
			ItemStack stack = entry.getKey().clone();
			ItemMeta meta = stack.getItemMeta();
			String name = meta.hasDisplayName() ? meta.getDisplayName() : "§f" + Utils.toCapitalWords(stack.getType());
			meta.setDisplayName(name + CHANCE.format(1.0D * entry.getValue() / this.recipe.getMaxChance()));
			stack.setItemMeta(meta);
			this.inventory.setItem(r * 9 + c + 14, stack);
		}
	}

}
