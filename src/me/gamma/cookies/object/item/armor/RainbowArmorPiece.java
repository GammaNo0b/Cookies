
package me.gamma.cookies.object.item.armor;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.object.item.ItemTicker;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.Utils;



public class RainbowArmorPiece extends AbstractCustomArmorItem implements ItemTicker {

	private ArmorType type;
	private String identifier;
	private String name;
	private Set<UUID> players;

	public RainbowArmorPiece(ArmorType type) {
		this.type = type;
		this.identifier = "rainbow_" + type.name().toLowerCase();
		this.name = ColorUtils.color("Rainbow " + Utils.toCapitalWords(type), "4c6eab3915".toCharArray(), 1);
		this.players = new HashSet<>();
		this.registerItemTicker();
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	public ArmorType getArmorType() {
		return this.type;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}


	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return this.isInstanceOf(this.type.getArmor(player.getInventory()));
	}


	@Override
	public List<ItemStack> getStacksFromPlayer(Player player) {
		ItemStack stack = this.getEquippedItem(player);
		return this.isInstanceOf(stack) ? List.of(stack) : new ArrayList<>();
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public void tick(Player player, ItemStack stack) {
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		Color color = meta.getColor();
		float[] hsb = java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
		hsb[0] = (hsb[0] + 1.0F / 360.0F) % 1.0F;
		meta.setColor(Color.fromRGB(java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0xFFFFFF));
		stack.setItemMeta(meta);
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		((LeatherArmorMeta) meta).setColor(Color.RED);
	}


	public ArmorType getType() {
		return this.type;
	}


	@Override
	protected void onEquip(Player player) {
		this.registerPlayer(player);
	}


	@Override
	protected void onUnequip(Player player) {
		this.unregisterPlayer(player);
	}

}
