
package me.gamma.cookies.objects.block.skull.machine;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.Utilities;



public class Gammabot extends AbstractGuiProvidingSkullBlock {

	public static final ItemStackProperty SCRIPT = new ItemStackProperty("script");

	@Override
	public String getBlockTexture() {
		return HeadTextures.ANDROID;
	}


	@Override
	public String getRegistryName() {
		return "gammabot";
	}


	@Override
	public String getDisplayName() {
		return "§dGammabot";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("SWS", "HCT", "MEM");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('H', Material.CHEST);
		recipe.setIngredient('C', CustomBlockSetup.ANDROID_CORE.createDefaultItemStack());
		recipe.setIngredient('T', Material.CRAFTING_TABLE);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('E', CustomBlockSetup.ELECTRICAL_CIRCUIT.createDefaultItemStack());
		return recipe;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		((Rotatable) block.getBlockData()).setRotation(BlockFace.SOUTH);
		SCRIPT.storeEmpty(block);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		Utilities.dropItem(SCRIPT.fetch(block), block.getLocation().add(0.5D, 0.5D, 0.5D));
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public int getRows() {
		return 5;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		ItemStack filler = InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			if(i != 4)
				gui.setItem(i, filler);
			gui.setItem(i + 36, filler);
		}
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		return super.onMainInventoryInteract(player, block, gui, event);
	}


	@Override
	public void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		SCRIPT.store(block, gui.getItem(10));
		block.update();
		super.onInventoryClose(player, block, gui, event);
	}

}
