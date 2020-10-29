
package me.gamma.cookies.objects.item;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ItemBuilder;



public class LettuceSeeds extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "lettuce_seeds";
	}


	@Override
	public String getDisplayName() {
		return "§fLettuce Seeds";
	}


	@Override
	public Material getMaterial() {
		return Material.WHEAT_SEEDS;
	}


	@Override
	public Recipe getRecipe() {
		return null;
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.LETTUCE_SEEDS;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onPlantGrow(BlockGrowEvent event) {
				if(event.getBlock().getType() == Material.WHEAT) {
					Ageable data = (Ageable) event.getNewState().getBlockData();
					if(((Ageable) event.getBlock().getBlockData()).getAge() < 4 && data.getAge() >= 4) {
						data.setAge(3);
						event.getBlock().setBlockData(data);
						event.setCancelled(true);
					}
				}
			}


			@EventHandler
			public void onBlockPlace(BlockPlaceEvent event) {
				if(event.getBlockPlaced().getType() == Material.WHEAT) {
					if(!isInstanceOf(event.getItemInHand())) {
						Ageable data = (Ageable) event.getBlockPlaced().getBlockData();
						data.setAge(4);
						event.getBlockPlaced().setBlockData(data);
					}
				}
			}


			@EventHandler
			public void onBlockBreak(BlockBreakEvent event) {
				if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					if(event.getBlock().getType() == Material.WHEAT) {
						Ageable data = (Ageable) event.getBlock().getBlockData();
						int age = data.getAge();
						if(age < 4) {
							List<ItemStack> drops = new ArrayList<>();
							drops.add(createDefaultItemStack());
							if(age == 3) {
								Random r = new Random();
								if(r.nextBoolean())
									drops.add(createDefaultItemStack());
								drops.add(new ItemBuilder(CustomItemSetup.LETTUCE.createDefaultItemStack()).setAmount(new Random().nextInt(3) + 1).build());
							}
							event.setCancelled(true);
							event.getBlock().setType(Material.AIR);
							for(ItemStack drop : drops) {
								event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5D, 0.5D, 0.5D), drop);
							}
						}
					}
				}
			}


			@EventHandler
			public void onBoneMealApply(BlockFertilizeEvent event) {
				if(event.getBlock().getType() == Material.WHEAT) {
					Ageable data = (Ageable) event.getBlock().getBlockData();
					int age = data.getAge();
					if(age < 3) {
						data.setAge(3);
						event.getBlock().setBlockData(data);
						ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
						if(stack.getType() == Material.BONE_MEAL)
							stack.setAmount(stack.getAmount() - 1);
						event.setCancelled(true);
					} else if(age == 3) {
						event.setCancelled(true);
					}
				}
			}

		};
	}

}
