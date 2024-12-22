
package me.gamma.cookies.listener;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;



public class MachineRecipeListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getView().getTitle().equals(MachineRecipe.MACHINE_RECIPE_TITLE)) {
			event.setCancelled(true);
			if(event.getClickedInventory() instanceof PlayerInventory)
				return;

			if(event.getSlot() == 4) {
				History.travelBack(event.getWhoClicked());
			}
			/*
			 * else if(History.getPresent(event.getWhoClicked()) instanceof MachineRecipeInventoryTask task) { ClickType click = event.getClick(); int shift = 0;
			 * if(click.isLeftClick()) { shift = 1; } else if(click.isRightClick()) { shift = -1; } task.shiftCycle(shift); }
			 */
		}
	}

}
