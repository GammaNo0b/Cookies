
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.util.Utils;



public class LuckyLeggings extends AbstractCustomArmorItem {

	private int tier;
	private String name;

	public LuckyLeggings(int tier) {
		this.tier = tier;
		this.name = "§2Lucky Leggings §a" + Utils.romanNumber(this.tier);
	}


	@Override
	public String getIdentifier() {
		return "lucky_leggings_" + this.tier;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.LEGGINGS;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(51, 153, 0));
	}


	@Override
	protected void onEquip(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, this.tier - 1, false, false));
	}


	@Override
	protected void onUnequip(Player player) {
		player.removePotionEffect(PotionEffectType.LUCK);
	}

}
