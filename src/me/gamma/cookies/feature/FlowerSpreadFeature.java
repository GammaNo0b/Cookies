
package me.gamma.cookies.feature;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Snow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.core.MinecraftWorldHelper;
import net.minecraft.util.RandomSource;



public class FlowerSpreadFeature extends SimpleCookieListener {

	@EventHandler
	public void onBoneMeal(PlayerInteractEvent event) {
		if(!this.enabled)
			return;

		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		ItemStack stack = event.getItem();
		if(ItemUtils.isEmpty(stack) || stack.getType() != Material.BONE_MEAL)
			return;

		Block block = event.getClickedBlock();
		Material type = block.getType();
		if(!ItemUtils.isFlower(type) || type == Material.WITHER_ROSE)
			return;

		this.spreadFlowers(block);
		stack.setAmount(stack.getAmount() - 1);
		event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_BONE_MEAL_USE, 10.0F, 1.0F);
	}


	@EventHandler
	public void onDispenserFertilize(BlockDispenseEvent event) {
		if(!this.enabled)
			return;

		ItemStack stack = event.getItem();
		if(stack.getType() != Material.BONE_MEAL)
			return;

		Block dispenser = event.getBlock();
		Block block = dispenser.getRelative(((Dispenser) dispenser.getBlockData()).getFacing());
		Material type = block.getType();
		if(!ItemUtils.isFlower(type) || type == Material.WITHER_ROSE)
			return;

		event.setCancelled(true);

		this.spreadFlowers(block);

		InventoryUtils.removeItemFromDispenser(dispenser, stack);
	}


	private void spreadFlowers(Block origin) {
		RandomSource r = MinecraftWorldHelper.getRandom(origin.getWorld());
		int flowers = r.a(4) + 4;
		Location[] locations = new Location[flowers];
		for(int i = 0; i < flowers; i++) {
			int x = r.a(9) - 4 + origin.getX();
			int z = r.a(9) - 4 + origin.getZ();
			int y = origin.getY() - 1;
			for(int j = 0; j < 2; j++, y++) {
				Block block = origin.getWorld().getBlockAt(x, y, z);
				Material type = block.getType();
				if(type != Material.AIR) {
					if(type != Material.SNOW)
						continue;
					Snow snow = (Snow) block.getBlockData();
					if(snow.getLayers() != 1)
						continue;
				}
				Block down = block.getRelative(BlockFace.DOWN);
				if(ItemUtils.isFlowerSoil(down.getType()))
					locations[i] = new Location(origin.getWorld(), x, y, z);
			}
		}

		for(Location location : locations)
			if(location != null)
				location.getBlock().setType(origin.getType());
	}

}
