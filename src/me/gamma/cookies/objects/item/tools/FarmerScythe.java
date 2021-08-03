
package me.gamma.cookies.objects.item.tools;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.block.impl.CraftBeetroot;
import org.bukkit.craftbukkit.v1_17_R1.block.impl.CraftCarrots;
import org.bukkit.craftbukkit.v1_17_R1.block.impl.CraftCrops;
import org.bukkit.craftbukkit.v1_17_R1.block.impl.CraftNetherWart;
import org.bukkit.craftbukkit.v1_17_R1.block.impl.CraftPotatoes;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;



public class FarmerScythe extends AbstractCustomItem {

	private static final List<Class<? extends Ageable>> harvestableCrops = Arrays.asList(CraftBeetroot.class, CraftCarrots.class, CraftCrops.class, CraftNetherWart.class, CraftPotatoes.class);

	@Override
	public String getRegistryName() {
		return "farmers_scythe";
	}


	@Override
	public String getDisplayName() {
		return "§6Farmer's Scythe";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Harvests only fully grown crops in a 3x3 area!");
	}


	@Override
	public Material getMaterial() {
		return Material.IRON_HOE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.TOOLS, RecipeType.CUSTOM);
		recipe.setShape(" II", "IS ", " S ");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.harvest(player, block);
	}


	@Override
	public void onBlockBreak(Player player, ItemStack stack, BlockBreakEvent event) {
		event.setCancelled(true);
		final Block block = event.getBlock();
		this.harvest(player, block);
	}


	private void harvest(Player player, Block block) {
		if(harvestableCrops.contains(block.getBlockData().getClass())) {
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					this.harvestCrop(player, block.getRelative(i, 0, j));
				}
			}
		}
	}


	private void harvestCrop(Player player, Block block) {
		BlockData data = block.getBlockData();
		if(harvestableCrops.contains(data.getClass())) {
			Ageable crop = (Ageable) data;
			if(crop.getAge() == crop.getMaximumAge()) {
				for(ItemStack drop : block.getDrops(player.getInventory().getItemInMainHand(), player))
					Utilities.dropItem(drop, player.getLocation().subtract(0.5D, 0.0D, 0.5D));
				crop.setAge(0);
				block.setBlockData(crop);
			}
		}
	}

}
