
package me.gamma.cookies.objects.block.skull;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class LED extends AbstractSkullBlock implements PeriodicTextureChanger, Switchable {

	private Set<Location> locations = new HashSet<>();

	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isInstanceOf(block);
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public String getBlockTexture(TileState block) {
		return this.isBlockPowered(block) ? HeadTextures.LED_ON : HeadTextures.LED_OFF;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LED_OFF;
	}


	@Override
	public String getIdentifier() {
		return "led";
	}


	@Override
	public String getDisplayName() {
		return "§cLED";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape(" R ", "RWR", "AIA");
		recipe.setIngredient('R', Material.RED_STAINED_GLASS_PANE);
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		recipe.setIngredient('I', Material.IRON_INGOT);
		return recipe;
	}

}
