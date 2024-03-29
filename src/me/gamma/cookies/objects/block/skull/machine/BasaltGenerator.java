
package me.gamma.cookies.objects.block.skull.machine;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public class BasaltGenerator extends AbstractSkullBlock implements BlockTicker, Switchable {

	private final Set<Location> locations = new HashSet<>();

	public BasaltGenerator() {
		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COBBLESTONE_GENERATOR;
	}


	@Override
	public String getDisplayName() {
		return "�7Basalt �6Generator";
	}


	@Override
	public String getRegistryName() {
		return "basalt_generator";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("CCC", "WGL", "CSC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		recipe.setIngredient('S', Material.SOUL_SOIL);
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
		ItemStack rest = Utilities.transferItem(new ItemStack(Material.BASALT), block.getBlock(), Utilities.faces);
		if(rest != null) {
			block.getWorld().dropItem(block.getLocation().add(0.5D, 0.5D, 0.5D), rest);
		}
	}

}
