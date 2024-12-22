
package me.gamma.cookies.feature;


import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.core.MinecraftItemHelper;
import net.minecraft.network.chat.ChatHexColor;
import net.minecraft.network.chat.ChatModifier;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.ItemDye;



public class ColorEntityFeature extends SimpleCookieListener {

	@EventHandler
	public void onEntityRightClick(PlayerInteractAtEntityEvent event) {
		if(!this.enabled)
			return;

		Player player = event.getPlayer();
		ItemStack stack = player.getInventory().getItem(event.getHand());
		if(ItemUtils.isEmpty(stack))
			return;

		ItemDye dye = MinecraftItemHelper.getItem(stack.getType(), ItemDye.class);
		if(dye == null)
			return;

		EnumColor nmscolor = dye.b();

		Entity entity = ((CraftEntity) event.getRightClicked()).getHandle();
		IChatBaseComponent component = entity.al();
		if(component == null)
			return;

		event.setCancelled(true);

		if(player.getGameMode() == GameMode.SURVIVAL)
			stack.setAmount(stack.getAmount() - 1);

		ChatModifier style = component.a().a(ChatHexColor.a(nmscolor.d()));
		component.forEach(comp -> ((IChatMutableComponent) comp).a(style));
	}

}
