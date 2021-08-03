
package me.gamma.cookies.objects.item.food;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.item.AbstractCustomItem;



public abstract class AbstractFood extends AbstractCustomItem implements Food {

	protected static final Material DEFAULT_FOOD = Material.APPLE;

	@Override
	public Material getMaterial() {
		Material food = this.getFood();
		if(food == null || !food.isEdible()) {
			return DEFAULT_FOOD;
		} else {
			return food;
		}
	}


	protected abstract Material getFood();
	
	
	@Override
	public void onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		int food = player.getFoodLevel();
		food += this.getHunger();
		food = Math.min(20, food);
		player.setFoodLevel(food);
		float saturation = player.getSaturation();
		saturation += this.getSaturation();
		saturation = Math.min(20.0F, saturation);
		player.setSaturation(20.0F);
		stack.setAmount(stack.getAmount() - 1);
	}

}
