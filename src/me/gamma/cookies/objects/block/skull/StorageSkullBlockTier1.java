
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.list.HeadTextures;



public class StorageSkullBlockTier1 extends AbstractStorageSkullBlock {

	@Override
	public int getTier() {
		return 1;
	}


	@Override
	public ItemStack getMaterialIngredient() {
		return new ItemStack(Material.DARK_OAK_PLANKS);
	}


	@Override
	public ItemStack getMiddleIngredient() {
		return new ItemStack(Material.DARK_OAK_LOG);
	}


	@Override
	public ItemStack getCenterIngredient() {
		return new ItemStack(Material.IRON_INGOT);
	}


	@Override
	public ItemStack getPreviousTierStorageIngredient() {
		return new ItemStack(Material.CHEST);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.BROWN_STORAGE_CRATE;
	}

}
