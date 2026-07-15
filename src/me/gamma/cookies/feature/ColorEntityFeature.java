
package me.gamma.cookies.feature;


import org.bukkit.GameMode;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.core.MinecraftItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;



public class ColorEntityFeature extends SimpleCookieListener {

	public ColorEntityFeature() {
		super("color_entity_name");
	}

	@EventHandler
	public void onEntityRightClick(PlayerInteractAtEntityEvent event) {
		if(!this.isEnabled())
			return;

		Player player = event.getPlayer();
		ItemStack stack = player.getInventory().getItem(event.getHand());
		if(ItemUtils.isEmpty(stack))
			return;

		int color = MinecraftItemHelper.getDyeColor(stack);

		Entity entity = ((CraftEntity) event.getRightClicked()).getHandle();
		// getCustomName
		Component component = entity.getCustomName();
		if(component == null)
			return;

		event.setCancelled(true);

		if(player.getGameMode() == GameMode.SURVIVAL)
			ItemUtils.increaseItem(stack, -1);

		Style style = component.getStyle().withColor(color);
		entity.setCustomName(component.copy().setStyle(style));
	}

}
