
package me.gamma.cookies.objects.item.tools;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;



public class RedstoneFrequencyGadget extends AbstractCustomItem {

	private static final ByteProperty FREQUENCY = new ByteProperty("frequency");

	@Override
	public String getRegistryName() {
		return "redstone_frequency_gadget";
	}


	@Override
	public String getDisplayName() {
		return "§eRedstone Frequency Gadget";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Displays or changes the frequency", "§7of the clicked Wireless Redstone Component.");
	}


	@Override
	public Material getMaterial() {
		return Material.CLOCK;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.REDSTONE, RecipeType.ENGINEER);
		recipe.setShape(" T ", "GEG", " R ");
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}


	@Override
	public void onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleClick(player, block, player.isSneaking(), false);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleClick(player, block, player.isSneaking(), true);
	}


	private void handleClick(Player player, Block block, boolean display, boolean increase) {
		if(block.getState() instanceof Skull) {
			Skull skull = (Skull) block.getState();
			if(CustomBlockSetup.WIRELESS_REDSTONE_TRANSMITTER.isInstanceOf(skull) || CustomBlockSetup.WIRELESS_REDSTONE_RECEIVER.isInstanceOf(skull)) {
				if(display) {
					player.sendMessage("§aFrequency: §2" + FREQUENCY.fetch(skull));
				} else {
					int frequency = FREQUENCY.fetch(skull) & 0xFF;
					frequency = (frequency + (increase ? 1 : -1) + 16) % 16;
					FREQUENCY.store(skull, (byte) frequency);
					skull.update();
					player.sendMessage("§aNew Frequency: §2" + frequency);
				}
			}
		}
	}

}
