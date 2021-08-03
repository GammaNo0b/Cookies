
package me.gamma.cookies.objects.item.tools;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
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
import me.gamma.cookies.util.Utilities;



public class VeinMinerPickaxe extends AbstractCustomItem {

	public static final Material[] validBlocks = new Material[] {Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS};

	@Override
	public String getRegistryName() {
		return "vein_miner_pickaxe";
	}


	@Override
	public String getDisplayName() {
		return "§bVein Miner Pickaxe";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Mines a whole vine of the same ore!");
	}


	@Override
	public Material getMaterial() {
		return Material.DIAMOND_PICKAXE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.TOOLS, RecipeType.CUSTOM);
		recipe.setShape("RCR", " P ", " S ");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('C', Material.COMPASS);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}


	@Override
	public void onBlockBreak(Player player, ItemStack stack, BlockBreakEvent event) {
		for(Material material : validBlocks) {
			if(event.getBlock().getType() == material) {
				List<ItemStack> drops = new ArrayList<>();
				int blocksBroken = this.breakBlocksRekursive(material, Arrays.asList(event.getBlock()), event.getPlayer().getInventory().getItemInMainHand(), drops);
				for(ItemStack drop : drops) {
					Map<Integer, ItemStack> rest = event.getPlayer().getInventory().addItem(drop);
					if(!rest.isEmpty()) {
						for(ItemStack restStack : rest.values()) {
							event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), restStack);
						}
					}
				}
				((ExperienceOrb) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(event.getExpToDrop() * blocksBroken);
				ItemMeta meta = stack.getItemMeta();
				if(meta instanceof Damageable) {
					Damageable damageable = (Damageable) meta;
					damageable.setDamage(Math.min(stack.getType().getMaxDurability(), damageable.getDamage() + (int) (((double) blocksBroken) / (meta.getEnchantLevel(Enchantment.DURABILITY) + 1.0D))));
					stack.setItemMeta((ItemMeta) damageable);
				}
				event.setCancelled(true);
				return;
			}
		}
	}


	private int breakBlocksRekursive(Material type, List<Block> generation, ItemStack tool, List<ItemStack> drops) {
		int blocksBroken = 0;
		List<Block> nextGeneration = new ArrayList<>();
		for(Block block : generation) {
			if(block.getType() == type) {
				blocksBroken++;
				drops.addAll(block.getDrops(tool));
				block.setType(Material.AIR);
				for(BlockFace face : Utilities.faces) {
					if(block.getRelative(face).getType() == type) {
						nextGeneration.add(block.getRelative(face));
					}
				}
			}
		}
		if(nextGeneration.size() > 0) {
			blocksBroken += breakBlocksRekursive(type, nextGeneration, tool, drops);
		}
		return blocksBroken;
	}

}
