
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;



public class CactusShirt extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "cactus_shirt";
	}


	@Override
	public String getTitle() {
		return "§2Cactus Shirt";
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
	protected void editItemMeta(ItemMeta meta) {
		meta.addEnchant(Enchantment.THORNS, 5, true);
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(0, 140, 20));
	}

	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onDamage(EntityDamageByBlockEvent event) {
				if(event.getCause() == DamageCause.CONTACT && event.getDamager().getType() == Material.CACTUS && event.getEntity() instanceof Player player && CactusShirt.this.isEquipped(player))
					event.setCancelled(true);
			}

		};
	}

}
