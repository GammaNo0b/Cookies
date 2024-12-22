
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.util.Utils;



public class HasteArmorPiece extends AbstractCustomArmorItem {

	private ArmorType type;
	private String identifier;
	private String name;

	public HasteArmorPiece(ArmorType type) {
		this.type = type;
		this.identifier = "haste_" + type.name().toLowerCase();
		this.name = "§cHaste " + Utils.toCapitalWords(type);
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
	public ItemStack get() {
		ItemStack stack = super.get();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(255, 181, 0));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Listener getListener() {
		return this.type != ArmorType.HELMET ? null : new Listener() {

			@EventHandler
			public void onArmorEquip(PlayerArmorEquipEvent event) {
				final Player player = event.getPlayer();
				player.removePotionEffect(PotionEffectType.HASTE);
				int count = HasteArmorPiece.this.countArmorPieces(player);
				if(count > 0)
					player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, count));
			}

		};
	}


	private int countArmorPieces(Player player) {
		int count = 0;
		for(ArmorType type : ArmorType.values()) {
			ItemStack armor = type.getArmor(player.getInventory());
			if(armor != null && armor.getType() != Material.AIR && ("haste_" + type.name().toLowerCase()).equals(IDENTIFIER.fetch(armor.getItemMeta())))
				count++;
		}
		return count;
	}

}
