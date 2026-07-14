
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class DispenserBucketCauldronFeature extends SimpleCookieListener {

	static void insertOrDispense(BlockDispenseEvent event, ItemStack replacement) {
		event.setItem(event.getBlock().getState() instanceof BlockInventoryHolder holder && holder.getInventory().addItem(replacement).isEmpty() ? null : replacement);
	}


	@EventHandler
	public void onDispense(BlockDispenseEvent event) {
		if(!this.isEnabled())
			return;

		Block block = event.getBlock();
		if(block.getType() != Material.DISPENSER)
			return;

		ItemStack stack = event.getItem();
		if(ItemUtils.isCustomItem(stack) || stack.getAmount() != 1)
			return;

		Directional dispenser = (Directional) block.getBlockData();
		BlockFace facing = dispenser.getFacing();
		Block target = block.getRelative(facing);

		if(ItemUtils.isType(stack, Material.GLASS_BOTTLE)) {
			// bottle water from cauldron
			if(target.getType() != Material.WATER_CAULDRON)
				return;

			if(!(target.getBlockData() instanceof Levelled cauldron))
				return;

			int level = cauldron.getLevel();
			if(level == 1) {
				target.setType(Material.CAULDRON);
			} else {
				cauldron.setLevel(level - 1);
				target.setBlockData(cauldron);
			}

			insertOrDispense(event, new ItemBuilder(Material.POTION).setBasePotionType(PotionType.WATER).build());
		} else if(ItemUtils.isType(stack, Material.POTION)) {
			// bottle water into cauldron
			if(!(stack.getItemMeta() instanceof PotionMeta meta))
				return;

			if(meta.getBasePotionType() != PotionType.WATER)
				return;

			if(target.getType() == Material.CAULDRON) {
				target.setType(Material.WATER_CAULDRON);
				if(target.getBlockData() instanceof Levelled cauldron) {
					cauldron.setLevel(1);
					target.setBlockData(cauldron);
				}
			} else if(target.getType() == Material.WATER_CAULDRON) {
				if(!(target.getBlockData() instanceof Levelled cauldron))
					return;

				int level = cauldron.getLevel();
				if(level == 3) {
					return;
				} else {
					cauldron.setLevel(level + 1);
					target.setBlockData(cauldron);
				}
			}

			insertOrDispense(event, new ItemStack(Material.GLASS_BOTTLE));
		} else if(ItemUtils.isType(stack, Material.BUCKET)) {
			// empty cauldron
			Material bucket = null;
			if(target.getType() == Material.WATER_CAULDRON) {
				bucket = Material.WATER_BUCKET;
			} else if(target.getType() == Material.LAVA_CAULDRON) {
				bucket = Material.LAVA_BUCKET;
			} else if(target.getType() == Material.POWDER_SNOW_CAULDRON) {
				bucket = Material.POWDER_SNOW_BUCKET;
			}

			if(bucket == null)
				return;

			if(!(target.getBlockData() instanceof Levelled cauldron))
				return;

			if(cauldron.getLevel() < cauldron.getMaximumLevel())
				return;

			target.setType(Material.CAULDRON);
			insertOrDispense(event, new ItemStack(bucket));
		} else if(target.getType() == Material.CAULDRON) {
			// fill cauldron
			Material cauldron = null;
			if(ItemUtils.isType(stack, Material.WATER_BUCKET)) {
				cauldron = Material.WATER_CAULDRON;
			} else if(ItemUtils.isType(stack, Material.WATER_BUCKET)) {
				cauldron = Material.LAVA_CAULDRON;
			} else if(ItemUtils.isType(stack, Material.WATER_BUCKET)) {
				cauldron = Material.POWDER_SNOW_CAULDRON;
			}

			if(cauldron == null)
				return;

			target.setType(cauldron);
			insertOrDispense(event, new ItemStack(Material.BUCKET));
		}
	}

}
