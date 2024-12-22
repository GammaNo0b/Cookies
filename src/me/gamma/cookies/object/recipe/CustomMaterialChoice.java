
package me.gamma.cookies.object.recipe;


import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import me.gamma.cookies.util.ItemUtils;



public class CustomMaterialChoice extends RecipeChoice.MaterialChoice {

	public CustomMaterialChoice(Material choice) {
		super(Arrays.asList(choice));
	}


	public CustomMaterialChoice(Material... choices) {
		super(Arrays.asList(choices));
	}
	
	public CustomMaterialChoice(Tag<Material> choices) {
		super(choices);
	}


	@Override
	public boolean test(ItemStack t) {
		return !ItemUtils.isCustomItem(t) && super.test(t);
	}


	@Override
	public CustomMaterialChoice clone() {
		return (CustomMaterialChoice) super.clone();
	}

}
