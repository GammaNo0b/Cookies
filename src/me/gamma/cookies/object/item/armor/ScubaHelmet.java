
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class ScubaHelmet extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "scuba_helmet";
	}


	@Override
	public String getTitle() {
		return "§6Scuba Helmet";
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
		((LeatherArmorMeta) meta).setColor(Color.ORANGE);
	}


	@Override
	protected void onEquip(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, false, false));
	}


	@Override
	protected void onUnequip(Player player) {
		player.removePotionEffect(PotionEffectType.WATER_BREATHING);
	}

}
