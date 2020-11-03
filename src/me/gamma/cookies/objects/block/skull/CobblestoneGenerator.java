
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public class CobblestoneGenerator extends AbstractSkullBlock implements BlockTicker, Switchable {

	public static Set<Location> locations = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.COBBLESTONE_GENERATOR;
	}


	@Override
	public String getDisplayName() {
		return "�9Cobblestone �6Generator";
	}


	@Override
	public String getIdentifier() {
		return "cobblestone_generator";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("�4UncreativeDescriptionException! �cDescription was not creative enough!", "�5�o  Bruh, didn't even know that i coded that in!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("CCC", "WGL", "CCC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		return recipe;
	}


	@Override
	public long getDelay() {
		return ConfigValues.COBBLESTONE_GENERATOR_DELAY;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public void tick(TileState block) {
		ItemStack rest = Utilities.transferItem(new ItemStack(Material.COBBLESTONE), block.getBlock(), Utilities.faces);
		if(rest != null) {
			block.getWorld().dropItem(block.getLocation().add(0.0D, 0.5D, 0.0D), rest);
		}
	}

}
