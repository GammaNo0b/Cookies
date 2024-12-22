
package me.gamma.cookies.object.item.armor;


import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;

import me.gamma.cookies.command.GodCommand;



public class AngelWings extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "angel_wings";
	}


	@Override
	public String getTitle() {
		return "§bAngel's Wings";
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.CHESTPLATE;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.ELYTRA;
	}


	@Override
	protected void onEquip(Player player) {
		player.setAllowFlight(true);
	}


	@Override
	protected void onUnequip(Player player) {
		player.setAllowFlight(GodCommand.canFlyByDefault(player));
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onPlayerGlide(EntityToggleGlideEvent event) {
				Entity entity = event.getEntity();
				if(!(entity instanceof Player player))
					return;

				if(!AngelWings.this.isEquipped(player))
					return;

				if(event.isGliding())
					event.setCancelled(true);
			}


			@EventHandler
			public void onServerReload(ServerLoadEvent event) {
				for(Player player : Bukkit.getOnlinePlayers())
					this.checkPlayerFlight(player);
			}


			@EventHandler
			public void onServerEnter(PlayerJoinEvent event) {
				this.checkPlayerFlight(event.getPlayer());
			}


			@EventHandler
			public void onWorldEnter(PlayerChangedWorldEvent event) {
				this.checkPlayerFlight(event.getPlayer());
			}


			@EventHandler
			public void onDamage(EntityDamageEvent event) {
				if(event.getCause() == DamageCause.FLY_INTO_WALL && event.getEntity() instanceof Player player && AngelWings.this.isEquipped(player))
					event.setCancelled(true);
			}


			private void checkPlayerFlight(Player player) {
				player.setAllowFlight(AngelWings.this.isEquipped(player) || GodCommand.canFlyByDefault(player));
			}

		};
	}

}
