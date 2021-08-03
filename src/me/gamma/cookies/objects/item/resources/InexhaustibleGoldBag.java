
package me.gamma.cookies.objects.item.resources;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class InexhaustibleGoldBag extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "inexhaustible_gold_bag";
	}


	@Override
	public String getDisplayName() {
		return "§6Inexhaustible Gold Bag";
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER;
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Provides the owner an endless amount of gold coins.");
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.INEXHAUSTIBLE_GOLD_BAG;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.MAGIC);
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.giveGold(player);
		event.setCancelled(true);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.giveGold(player);
		event.setCancelled(true);
	}


	private void giveGold(Player player) {
		Random r = new Random();
		Utilities.giveItemToPlayer(player, new ItemBuilder(CustomItemSetup.GOLD_COIN.createDefaultItemStack()).setAmount(r.nextInt(2)).build());
		Utilities.giveItemToPlayer(player, new ItemBuilder(CustomItemSetup.SILVER_COIN.createDefaultItemStack()).setAmount(r.nextInt(3) + 1).build());
		Utilities.giveItemToPlayer(player, new ItemBuilder(CustomItemSetup.BRONZE_COIN.createDefaultItemStack()).setAmount(r.nextInt(4) + 2).build());
	}

}
