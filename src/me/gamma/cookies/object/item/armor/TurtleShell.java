
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.util.collection.PersistentDataObject;



public class TurtleShell extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "turtle_shell";
	}


	@Override
	public String getTitle() {
		return "§aTurtle Shell";
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.CHESTPLATE;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	protected void editItemMeta(ItemMeta meta, PersistentDataObject data) {
		meta.addEnchant(Enchantment.UNBREAKING, 5, true);
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(20, 200, 0));
	}


	@Override
	protected void onEquip(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 3, false, false));
	}


	@Override
	protected void onUnequip(Player player) {
		player.removePotionEffect(PotionEffectType.RESISTANCE);
	}

}
