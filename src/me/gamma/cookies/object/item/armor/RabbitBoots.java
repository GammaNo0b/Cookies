
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class RabbitBoots extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "rabbit_boots";
	}


	@Override
	public String getTitle() {
		return "§6Rabbit Boots";
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.BOOTS;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(145, 118, 77));
	}


	@Override
	protected void onEquip(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2, false, false));
	}


	@Override
	protected void onUnequip(Player player) {
		player.removePotionEffect(PotionEffectType.JUMP_BOOST);
	}

}
