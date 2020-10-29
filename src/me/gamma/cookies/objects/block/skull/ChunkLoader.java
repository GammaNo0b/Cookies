
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;



public class ChunkLoader extends AbstractSkullBlock {

	private static Set<Chunk> loadedChunks = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.CHUNK_LOADER;
	}


	@Override
	public String getDisplayName() {
		return "§bChunk §6Loader";
	}


	@Override
	public String getIdentifier() {
		return "chunk_loader";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Loads Chunks.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("NSN", "RMT", "IDI");
		recipe.setIngredient('N', Material.NETHERITE_INGOT);
		recipe.setIngredient('S', Material.NETHER_STAR);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('M', CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('D', Material.DIAMOND);
		return recipe;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		Chunk chunk = block.getChunk();
		if(!loadedChunks.add(chunk)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§eThis Chunk already contains a Chunk Loader!");
			return;
		}
		chunk.setForceLoaded(true);
		event.getPlayer().sendMessage("§aChunkloader activated!");
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		Chunk chunk = block.getChunk();
		if(loadedChunks.remove(chunk)) {
			chunk.setForceLoaded(false);
			event.getPlayer().sendMessage("§cChunkloader deactivated!");
		}
		return super.onBlockBreak(player, block, event);
	}

}
