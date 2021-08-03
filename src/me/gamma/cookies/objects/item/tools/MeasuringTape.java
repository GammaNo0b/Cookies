
package me.gamma.cookies.objects.item.tools;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.gamma.cookies.objects.item.AbstractSkullItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.VectorProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class MeasuringTape extends AbstractSkullItem {

	private static final VectorProperty MEASURE_POSITION = new VectorProperty("measureposition");

	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	@Override
	protected String getBlockTexture() {
		return HeadTextures.MEASURING_TAPE;
	}


	@Override
	public String getRegistryName() {
		return "measuring_tape";
	}


	@Override
	public String getDisplayName() {
		return "§eMeasuring Tape";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Measures the distance between two clicked positions.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("IPN");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('P', CustomItemSetup.GILDED_PAPER.createDefaultItemStack());
		recipe.setIngredient('N', Material.IRON_NUGGET);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		MEASURE_POSITION.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		RayTraceResult result = event.getPlayer().rayTraceBlocks(5.0D);
		if(result != null) {
			this.handleClick(player, stack, result.getHitPosition());
		}
		super.onAirRightClick(player, stack, event);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		RayTraceResult result = event.getPlayer().rayTraceBlocks(5.0D);
		this.handleClick(player, stack, result.getHitPosition());
		super.onBlockRightClick(player, stack, block, event);
	}


	private void handleClick(Player player, ItemStack stack, Vector clickpos) {
		ItemMeta meta = stack.getItemMeta();
		Vector position = MEASURE_POSITION.fetch(meta);
		if(position != null) {
			if(player.isSneaking()) {
				MEASURE_POSITION.storeEmpty(meta);
				List<String> lore = meta.getLore();
				lore.remove(lore.size() - 1);
				lore.remove(lore.size() - 1);
				meta.setLore(lore);
				stack.setItemMeta(meta);
				player.sendMessage("§eReset Measuring Position.");
			} else if(clickpos != null) {
				player.sendMessage("§eMeasured Distance: §6" + FORMAT.format(clickpos.distance(position)) + "m");
			}
		} else if(clickpos != null) {
			MEASURE_POSITION.store(meta, clickpos);
			String vectorstring = "§6X: §b" + FORMAT.format(clickpos.getX()) + " §6Y: §b" + FORMAT.format(clickpos.getY()) + " §6Z: §b" + FORMAT.format(clickpos.getZ());
			List<String> lore = meta.getLore();
			if(lore == null)
				lore = new ArrayList<>();
			lore.add("");
			lore.add("§6Current Measuring Position: " + vectorstring);
			meta.setLore(lore);
			stack.setItemMeta(meta);
			player.sendMessage("§eSet Measuring Position to: " + vectorstring);
		}
	}

}
