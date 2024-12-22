
package me.gamma.cookies.object.gui.task;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.math.MathHelper;



public abstract class RecipeInventoryTask<R extends Recipe> extends InventoryTask {

	protected final R recipe;
	protected final ResultChoice resultchoice;

	public RecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, R recipe) {
		super(inventory, 20);
		this.resultchoice = resultchoice;
		this.recipe = recipe;
	}


	protected final ItemStack getItemFromItemChoice(RecipeChoice choice) {
		return getItemFromItemChoice(choice, this.getResultChoice(), this.cycle);
	}


	protected ResultChoice getResultChoice() {
		return this.resultchoice;
	}


	public static ItemStack getRawItemFromItemChoice(RecipeChoice choice, ResultChoice chooser, int cycle) {
		List<ItemStack> choices = RecipeUtils.getItemsFromChoice(choice);
		int size = choices.size();
		if(size <= 0)
			return null;

		return chooser.chooseItem(choices, size, cycle);
	}


	public static ItemStack getItemFromItemChoice(RecipeChoice choice, ResultChoice chooser, int cycle) {
		List<ItemStack> choices = RecipeUtils.getItemsFromChoice(choice);
		int size = choices.size();
		if(size <= 0)
			return null;

		ItemStack stack = chooser.chooseItem(choices, size, cycle).clone();
		if(size == 1)
			return stack;

		ItemMeta meta = stack.getItemMeta();
		boolean haslore = meta.hasLore();
		List<String> lore = haslore ? meta.getLore() : new ArrayList<>();

		int i = 0;
		lore.add(i++, "§8Accepts:");
		for(ItemStack other : choices) {
			char color = stack.equals(other) ? '8' : '7';
			lore.add(i++, "  §" + color + "- §f" + ItemUtils.getItemName(other));
		}
		if(haslore)
			lore.add(i, "");

		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}

	public enum ResultChoice implements Chooser {

		ORDERED(new Chooser() {
			@Override
			public <T> T chooseItem(List<? extends T> list, int size, int cycle) {
				return list.get(cycle % size);
			}
		}),
		RANDOM(new Chooser() {
			@Override
			public <T> T chooseItem(List<? extends T> list, int size, int cycle) {
				return list.get(MathHelper.random.nextInt(size));
			}
		});

		private final Chooser chooser;

		private ResultChoice(Chooser chooser) {
			this.chooser = chooser;
		}


		@Override
		public <T> T chooseItem(List<? extends T> list, int size, int cycle) {
			return this.chooser.chooseItem(list, size, cycle);
		}


		public String getId() {
			return this.name().toLowerCase();
		}


		public ItemStack createIcon() {
			return new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§3" + Utils.toCapitalWords(this)).build();
		}


		public static ResultChoice byId(String id) {
			for(ResultChoice choice : values())
				if(id.equals(choice.getId()))
					return choice;

			return null;
		}

	}

	private interface Chooser {

		<T> T chooseItem(List<? extends T> list, int size, int cycle);

	}

}
