
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class AngelBlock extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ANGEL_BLOCK;
	}


	@Override
	public String getRegistryName() {
		return "angel_block";
	}


	@Override
	public String getDisplayName() {
		return "§6Angel Block";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("GFG", "FOF", "GFG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('F', Material.FEATHER);
		recipe.setIngredient('O', Material.OBSIDIAN);
		return recipe;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		Block block = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(3)).toLocation(player.getWorld()).getBlock();
		if(block.getType() == Material.AIR) {
			stack.setAmount(stack.getAmount() - 1);
			block.setType(Material.OBSIDIAN);
		}
		return super.onAirRightClick(player, stack, event);
	}

}
