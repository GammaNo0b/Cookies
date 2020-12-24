
package me.gamma.cookies.objects.block.skull.storage;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.ItemSupplier;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.Utilities;



public class StorageImporter extends AbstractSkullBlock implements BlockTicker, StorageComponent {

	private final Set<Location> locations = new HashSet<>();


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_IMPORTER;
	}


	@Override
	public String getRegistryName() {
		return "storage_importer";
	}


	@Override
	public String getDisplayName() {
		return "§6Storage Importer";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.STORAGE, RecipeType.ENGINEER);

		return recipe;
	}


	@Override
	public long getDelay() {
		return 10;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.getStorageMonitor(block) != null;
	}


	@Override
	public void tick(TileState block) {
		TileState monitor = this.getStorageMonitor(block);
		for(BlockFace face : Utilities.faces) {
			Block relative = block.getBlock().getRelative(face);
			if(relative.getState() instanceof TileState) {
				TileState state = (TileState) relative.getState();
				if(state instanceof BlockInventoryHolder) {
					
				} else {
					AbstractTileStateBlock tile = CustomBlockSetup.getCustomBlockFromTileState(state);
					if(tile != null) {
						if(tile instanceof ItemSupplier) {
							ItemSupplier supplier = (ItemSupplier) tile;
							
						}
					}
				}
			}
		}
	}

}
