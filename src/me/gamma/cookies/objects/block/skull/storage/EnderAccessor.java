
package me.gamma.cookies.objects.block.skull.storage;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.ItemConsumer;
import me.gamma.cookies.objects.block.ItemProvider;
import me.gamma.cookies.objects.block.ItemSupplier;
import me.gamma.cookies.objects.block.Ownable;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class EnderAccessor extends AbstractSkullBlock implements Ownable, ItemConsumer, ItemSupplier {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ENDER_ACCESSOR;
	}


	@Override
	public String getRegistryName() {
		return "ender_accessor";
	}


	@Override
	public String getDisplayName() {
		return "§3Ender Accessor";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.STORAGE, RecipeType.CUSTOM);
		recipe.setShape("OCO", "CEC", "OCO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('C', Material.CRYING_OBSIDIAN);
		recipe.setIngredient('E', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
		return recipe;
	}


	@Override
	public List<ItemProvider> getOutputStackHolders(TileState block) {
		return ItemProvider.createItemProviders(this.getPlayer(block).getEnderChest());
	}


	@Override
	public List<ItemProvider> getInputStackHolders(TileState block) {
		return ItemProvider.createItemProviders(this.getPlayer(block).getEnderChest());
	}


	private Player getPlayer(TileState block) {
		return Bukkit.getPlayer(this.getOwner(block));
	}

}
