
package me.gamma.cookies.objects.block.skull.machine;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.block.skull.PeriodicTextureChanger;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class LED extends AbstractSkullBlock implements PeriodicTextureChanger, Switchable {

	private Set<Location> locations = new HashSet<>();

	private final String texturePowered;
	private final String registryName;
	private final String name;
	private final Material glass;

	public LED(String texturePowered, String registryName, String name, Material glass) {
		register();
		this.texturePowered = texturePowered;
		this.registryName = registryName;
		this.name = name;
		this.glass = glass;
	}


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
		return this.isBlockPowered(block) ? this.texturePowered : HeadTextures.LED_OFF;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LED_OFF;
	}


	@Override
	public String getRegistryName() {
		return this.registryName;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape(" R ", "RWR", "AIA");
		recipe.setIngredient('R', this.glass);
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		recipe.setIngredient('I', Material.IRON_INGOT);
		return recipe;
	}

}
