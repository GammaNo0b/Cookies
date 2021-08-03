
package me.gamma.cookies.objects.item.food;


import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.item.AbstractSkullItem;



public abstract class EdibleSkullItem extends AbstractSkullItem implements Food {

	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.eat(player, stack);
		super.onAirRightClick(player, stack, event);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.eat(player, stack);
		super.onBlockRightClick(player, stack, block, event);
	}


	private void eat(Player player, ItemStack stack) {
		if(player.getFoodLevel() < 20) {
			stack.setAmount(stack.getAmount() - 1);
			player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10.0F, 1.0F);
			player.setFoodLevel(Math.min(20, player.getFoodLevel() + getHunger()));
			player.setSaturation(Math.min(20F, player.getSaturation() + getSaturation()));
		}
	}

}
