
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class GlowHat extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "glow_hat";
	}


	@Override
	public String getTitle() {
		return "§eGlow Hat";
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
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(232, 255, 0));
	}


	@Override
	protected void onEquip(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
	}


	@Override
	protected void onUnequip(Player player) {
		player.removePotionEffect(PotionEffectType.NIGHT_VISION);
	}

}
