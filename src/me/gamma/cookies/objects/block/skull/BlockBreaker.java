
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public class BlockBreaker extends AbstractSkullBlock implements BlockTicker, Switchable {

	public static final List<Material> bannedBreakBlocks = Arrays.asList(Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.WATER, Material.LAVA, Material.AIR, Material.SPAWNER);

	public static Set<Location> locations = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.BLOCK_BREAKER;
	}


	@Override
	public String getDisplayName() {
		return "§4Block §cBreaker";
	}


	@Override
	public String getIdentifier() {
		return "block_breaker";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Breaks the block in front of it.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("IHI", "GCG", "IAI");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('H', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('C', CustomBlockSetup.STORAGE_CONNECTOR.createDefaultItemStack());
		recipe.setIngredient('A', CustomBlockSetup.ITEM_ABSORBER.createDefaultItemStack());
		return recipe;
	}


	@Override
	public long getDelay() {
		return ConfigValues.BLOCK_BREAKER_DELAY;
	}


	@Override
	public boolean isActiveOnRedstone() {
		return true;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@SuppressWarnings("deprecation")
	@Override
	public void tick(TileState block) {
		BlockFace rotation = ((Skull) block).getRotation();
		Block front = block.getBlock().getRelative(rotation);
		if(!bannedBreakBlocks.contains(front.getType())) {
			for(ItemStack item : front.getDrops()) {
				ejectCollectedItems(block, rotation.getOppositeFace(), item);
			}
			front.setType(Material.AIR);
		}
	}


	private void ejectCollectedItems(TileState block, BlockFace oppositeDirection, ItemStack item) {
		if((item = Utilities.transferItem(item, block.getBlock(), Utilities.faces)) != null) {
			Item entityItem = block.getWorld().dropItem(block.getBlock().getRelative(oppositeDirection).getLocation().add(0.5, 0.25, 0.5), item);
			entityItem.setVelocity(oppositeDirection.getDirection());
		}
	}

}
