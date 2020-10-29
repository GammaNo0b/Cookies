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
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.BlockRegister;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.property.Properties;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;


public class WirelessRedstoneReceiver extends AbstractSkullBlock implements BlockRegister {
	
	private static final ByteProperty FREQUENCY = Properties.REDSTONE_FREQUENCY;
	
	private static final Set<Location> locations = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.REDSTONE_RECEIVER;
	}


	@Override
	public String getIdentifier() {
		return "wireless_redstone_receiver";
	}


	@Override
	public String getDisplayName() {
		return "§cWireless Redstone Receiver";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Receivers Redstone Signals sent from Transmitter with the same Frequency", "§7and toggles powerable blocks like levers or pressure plates.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.REDSTONE, RecipeType.ENGINEER);
		recipe.setShape("TCR", "SSS");
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('C', Material.COMPARATOR);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('S', Material.STONE);
		return recipe;
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
	
	
	public static void setBlockPower(int frequency, boolean powered) {
		for(Location location : locations) {
			Block block = location.getBlock();
			if(block.getState() instanceof Skull) {
				Skull skull = (Skull) block.getState();
				if(Properties.IDENTIFIER.fetch(skull).equals("wireless_redstone_receiver")) {
					if((FREQUENCY.fetch(skull) & 0xFF) == frequency) {
						for(BlockFace face : Utilities.faces) {
							Block relative = block.getRelative(face);
							if(relative.getBlockData() instanceof Powerable) {
								Powerable powerable = (Powerable) relative.getBlockData();
								powerable.setPowered(powered);
								relative.setBlockData(powerable);
							}
						}
					}
				}
			}
		}
	}

}
