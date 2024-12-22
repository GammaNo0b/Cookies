
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;



public class BerryPants extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "berry_pants";
	}


	@Override
	public String getTitle() {
		return "§cBerry Pants";
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
		((LeatherArmorMeta) meta).setColor(Color.fromRGB(128, 8, 8));
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onBerryDamage(EntityDamageByBlockEvent event) {
				if(event.getCause() == DamageCause.CONTACT && event.getDamager().getType() == Material.SWEET_BERRY_BUSH && event.getEntity() instanceof Player player && BerryPants.this.isEquipped(player))
					event.setCancelled(true);
			}

		};
	}

}
