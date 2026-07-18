
package me.gamma.cookies.listener;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TutorialListener implements Listener {

	@EventHandler
	public void onCookieCraft(CraftItemEvent event) {
		if(!(event.getWhoClicked() instanceof Player player))
			return;

		ItemStack result = event.getRecipe().getResult();
		if(result.getType() != Material.COOKIE)
			return;

		if(ItemUtils.isCustomItem(result))
			return;

		PersistentDataObject customData = new PersistentDataObject(player.getPersistentDataContainer());
		if(customData.getBoolean("tutorial", false))
			return;

		customData.setBoolean("tutorial", true);

		player.sendMessage("§6Cook the delicious cookie cook book by surrounding a book with a bottle of honey, a cocoa bean, a cookie and some sugar!");
		player.sendMessage("§6This blueprint may help!");
		ItemUtils.giveItemToPlayer(player, Items.CUSTOM_CRAFTING_TABLE_BLUEPRINT.get());
	}

}
