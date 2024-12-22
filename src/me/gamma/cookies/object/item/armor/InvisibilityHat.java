
package me.gamma.cookies.object.item.armor;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.object.item.PlayerRegister;



public class InvisibilityHat extends AbstractCustomArmorItem implements PlayerRegister {

	private Set<UUID> players = new HashSet<>();

	@Override
	public String getIdentifier() {
		return "invisibility_hat";
	}


	@Override
	public String getTitle() {
		return "§bInvisibility Hat";
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.HELMET;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(120, 255, 226));
	}


	@Override
	public boolean onPlayerArmorUnequipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		this.unregisterPlayer(player);
		return false;
	}


	@Override
	public boolean onPlayerArmorEquipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		this.registerPlayer(player);
		return false;
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}


	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return this.isEquipped(player) && PlayerRegister.super.shouldRegisterPlayer(player);
	}


	@Override
	public void onUnregisterPlayer(Player player, boolean success) {
		Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(Cookies.INSTANCE, player));
	}


	@Override
	public void onRegisterPlayer(Player player, boolean success) {
		Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(Cookies.INSTANCE, player));
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onPlayerJoin(PlayerJoinEvent event) {
				InvisibilityHat.this.players.stream().map(Bukkit::getPlayer).filter(player -> player != null).forEach(player -> event.getPlayer().hidePlayer(Cookies.INSTANCE, player));
			}

		};
	}

}
