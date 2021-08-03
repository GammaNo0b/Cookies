
package me.gamma.cookies.objects.item.tools;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class LumberAxe extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "lumber_axe";
	}


	@Override
	public String getDisplayName() {
		return "§9Lumberaxe";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Fells a whole tree.");
	}


	@Override
	public Material getMaterial() {
		return Material.DIAMOND_AXE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.TOOLS, RecipeType.CUSTOM);
		recipe.setShape("DD", "DW", " W");
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('W', Material.SPRUCE_LOG);
		return recipe;
	}


	@Override
	public void onBlockBreak(Player player, ItemStack stack, BlockBreakEvent event) {
		if(event.getBlock().getType().toString().contains("_WOOD") || event.getBlock().getType().toString().contains("_LOG")) {
			Map<Location, Block> nextGeneration = new HashMap<>();
			nextGeneration.put(event.getBlock().getLocation(), event.getBlock());
			Map<ItemStack, Integer> woodBlocks = new HashMap<>();
			int brokenBlocks = breakWood(nextGeneration, woodBlocks);
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof Damageable) {
				Damageable damageable = (Damageable) meta;
				damageable.setDamage(Math.min(stack.getType().getMaxDurability(), damageable.getDamage() + (int) (((double) brokenBlocks) / (meta.getEnchantLevel(Enchantment.DURABILITY) + 1.0D))));
				if(damageable.getDamage() == stack.getType().getMaxDurability()) {
					stack.setType(Material.AIR);
					player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 10.0F, 1.0F);
				}
				stack.setItemMeta((ItemMeta) damageable);
			}
			for(ItemStack wood : woodBlocks.keySet()) {
				dropItemStock(event.getBlock().getLocation(), wood, woodBlocks.get(wood));
			}
		}
	}


	private int breakWood(Map<Location, Block> currentGeneration, Map<ItemStack, Integer> woodBlocks) {
		int amount = 0;
		if(!currentGeneration.isEmpty()) {
			Map<Location, Block> nextGeneration = new HashMap<>();
			for(Location location : currentGeneration.keySet()) {
				Block block = currentGeneration.get(location);
				if(block.getType().toString().contains("_WOOD") || block.getType().toString().contains("_LOG")) {
					amount++;
					ItemStack item = new ItemStack(block.getType());
					block.setType(Material.AIR);
					if(!woodBlocks.containsKey(item)) {
						woodBlocks.put(item, 1);
					} else {
						woodBlocks.put(item, woodBlocks.get(item) + 1);
					}
					for(int y = 0; y <= 1; y++) {
						for(int z = -1; z <= 1; z++) {
							for(int x = -1; x <= 1; x++) {
								Block relative = block.getRelative(x, y, z);
								if(!nextGeneration.containsKey(relative.getLocation())) {
									nextGeneration.put(relative.getLocation(), relative);
								}
							}
						}
					}
				}
			}
			amount += breakWood(nextGeneration, woodBlocks);
		}
		return amount;
	}


	private void dropItemStock(Location location, ItemStack item, int totalAmount) {
		List<ItemStack> items = new ArrayList<>();
		int stackSize = item.getType().getMaxStackSize();
		while(stackSize <= totalAmount && totalAmount > 0) {
			totalAmount -= stackSize;
			items.add(new ItemStack(item.getType(), stackSize));
		}
		if(totalAmount > 0) {
			items.add(new ItemStack(item.getType(), totalAmount));
		}
		for(ItemStack itemStack : items) {
			location.getWorld().dropItemNaturally(location, itemStack);
		}
	}

}
