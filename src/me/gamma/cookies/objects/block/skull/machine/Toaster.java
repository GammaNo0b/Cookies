
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public class Toaster extends AbstractSkullBlock implements BlockTicker {

	private static final IntegerProperty TOAST1_TICKS = new IntegerProperty("toast1ticks");
	private static final IntegerProperty TOAST2_TICKS = new IntegerProperty("toast2ticks");

	private final Set<Location> locations = new HashSet<>();

	public Toaster() {
		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.TOASTER;
	}


	@Override
	public String getRegistryName() {
		return "toaster";
	}


	@Override
	public String getDisplayName() {
		return "§6Toaster";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Toastes Bread into Toast!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("S  ", "ABA", "IPI");
		recipe.setIngredient('S', Material.STONE_BUTTON);
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		recipe.setIngredient('B', Material.IRON_BARS);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('P', Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
		return recipe;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		TOAST1_TICKS.storeEmpty(block);
		TOAST2_TICKS.storeEmpty(block);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(this.hasToast(block, true))
			block.getWorld().dropItemNaturally(block.getLocation().add(0.5D, 0.5D, 0.5D), new ItemStack(Material.BREAD));
		if(this.hasToast(block, false))
			block.getWorld().dropItemNaturally(block.getLocation().add(0.5D, 0.5D, 0.5D), new ItemStack(Material.BREAD));
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		ItemStack stack = event.getItem();
		if(stack != null && stack.getType() == Material.BREAD && !AbstractCustomItem.isCustomItem(stack)) {
			if(!this.hasToast(block, true)) {
				stack.setAmount(stack.getAmount() - 1);
				this.setToast(block, true);
			} else if(!this.hasToast(block, false)) {
				stack.setAmount(stack.getAmount() - 1);
				this.setToast(block, false);
			}
		}
		return false;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.hasToast(block);
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public void tick(TileState block) {
		if(hasToast(block, true))
			this.tickToast(block, TOAST1_TICKS);
		if(hasToast(block, false))
			this.tickToast(block, TOAST2_TICKS);
		block.update();
	}


	private void tickToast(TileState block, IntegerProperty property) {
		int ticks = property.fetch(block);
		if(--ticks <= 0) {
			ItemStack stack = CustomItemSetup.TOAST.createDefaultItemStack();
			stack = Utilities.transferItem(stack, block.getBlock());
			if(!Utilities.isEmpty(stack)) {
				block.getWorld().dropItem(block.getLocation().add(0.5D, 0.5D, 0.5D), CustomItemSetup.TOAST.createDefaultItemStack());
			}
		}
		property.store(block, ticks);
	}


	public void setToast(TileState block, boolean firstSlot) {
		(firstSlot ? TOAST1_TICKS : TOAST2_TICKS).store(block, ConfigValues.TOASTER_TICKS);
		block.update();
	}


	public boolean hasToast(TileState block, boolean firstSlot) {
		return (firstSlot ? TOAST1_TICKS : TOAST2_TICKS).fetch(block) > 0;
	}


	public boolean hasToast(TileState block) {
		return this.hasToast(block, true) || this.hasToast(block, false);
	}

}
