
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.property.Properties;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class WirelessRedstoneTransmitter extends AbstractSkullBlock implements Switchable, BlockTicker {

	private static final ByteProperty FREQUENCY = Properties.REDSTONE_FREQUENCY;
	private static final BooleanProperty LAST_POWERED = BooleanProperty.create("lastpowered");

	private static final Set<Location> locations = new HashSet<>();
	
	
	public WirelessRedstoneTransmitter() {
		register();
	}

	@Override
	public String getBlockTexture() {
		return HeadTextures.REDSTONE_TRANSMITTER;
	}


	@Override
	public String getRegistryName() {
		return "wireless_redstone_transmitter";
	}


	@Override
	public String getDisplayName() {
		return "§cWireless Redstone Transmitter";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Sends a signal to all Receivers with the same Frequency.");
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.REDSTONE, RecipeType.ENGINEER);
		recipe.setShape("RET", "SSS");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('S', Material.STONE);
		return recipe;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		FREQUENCY.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState skullBlock, BlockPlaceEvent event) {
		FREQUENCY.transfer(usedItem.getItemMeta(), skullBlock);
		LAST_POWERED.store(skullBlock, false);
		super.onBlockPlace(player, usedItem, skullBlock, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState skullBlock, BlockBreakEvent event) {
		ItemStack stack = super.onBlockBreak(player, skullBlock, event).clone();
		ItemMeta meta = stack.getItemMeta();
		FREQUENCY.transfer(skullBlock, meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void tick(TileState block) {
		int frequency = FREQUENCY.fetch(block) & 0xFF;
		boolean powered = this.isBlockPowered(block);
		boolean lastpowered = LAST_POWERED.fetch(block);
		if(powered != lastpowered) {
			WirelessRedstoneReceiver.setBlockPower(frequency, powered || this.isOtherPowered(frequency));
			LAST_POWERED.store(block, powered);
			block.update();
		}
	}


	private boolean isOtherPowered(int checkFrequency) {
		for(Location location : locations) {
			if(location.getBlock().getState() instanceof Skull) {
				Skull skull = (Skull) location.getBlock().getState();
				if(this.isInstanceOf(skull)) {
					int frequency = FREQUENCY.fetch(skull) & 0xFF;
					if(frequency == checkFrequency) {
						if(this.isBlockPowered(skull)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
