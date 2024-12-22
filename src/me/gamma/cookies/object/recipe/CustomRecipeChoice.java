
package me.gamma.cookies.object.recipe;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import me.gamma.cookies.object.item.AbstractCustomItem;



public class CustomRecipeChoice implements RecipeChoice {

	private final List<AbstractCustomItem> list;

	public CustomRecipeChoice(AbstractCustomItem... items) {
		this.list = Arrays.asList(items);
	}


	public CustomRecipeChoice(List<AbstractCustomItem> items) {
		this.list = items;
	}


	@Deprecated
	@Override
	public ItemStack getItemStack() {
		return list.get(0).get();
	}


	@Override
	public boolean test(ItemStack itemStack) {
		for(AbstractCustomItem item : this.list)
			if(item.isInstanceOf(itemStack))
				return true;

		return false;
	}


	@Override
	public CustomRecipeChoice clone() {
		return new CustomRecipeChoice(this.list);
	}


	public List<AbstractCustomItem> getChoices() {
		return Collections.unmodifiableList(this.list);
	}

}
