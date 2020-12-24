
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;
import me.gamma.cookies.util.Utilities;



public class Fruit extends EdibleSkullItem implements Food {

	private String identifier;
	private String name;
	private String texture;
	private Material leave;
	private int blocksToBreak;

	public Fruit(String identifier, String name, String texture, Material leave, int blocksToBreak) {
		this.identifier = identifier;
		this.name = name;
		this.texture = texture;
		this.leave = leave;
		this.blocksToBreak = blocksToBreak;
	}


	@Override
	protected String getBlockTexture() {
		return texture;
	}


	@Override
	public String getIdentifier() {
		return identifier;
	}


	@Override
	public String getDisplayName() {
		return name;
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Drops from §2" + Utilities.toCapitalWords(leave.name().replace('_', ' ')) + "§7!");
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(createDefaultItemStack(), RecipeCategory.PLANTS);
	}


	@Override
	public int getHunger() {
		return 4;
	}


	@Override
	public int getSaturation() {
		return 4;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onLeaveBreak(BlockBreakEvent event) {
				if(event.getBlock().getType() == leave) {
					if(new Random().nextInt(blocksToBreak) == 0) {
						Utilities.dropItem(createDefaultItemStack(), event.getBlock().getLocation().add(0.5D, 0.5D, 0.5D));
					}
				}
			}

		};
	}


	public Drink createDrink(Color color) {
		return new Drink(name + " Juice", identifier + "_juice", getHunger() + 2, getSaturation() + 4, createDefaultItemStack(), color);
	}

}
