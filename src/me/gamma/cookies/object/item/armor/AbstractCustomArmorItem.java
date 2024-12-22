
package me.gamma.cookies.object.item.armor;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.object.item.AbstractCustomItem;



public abstract class AbstractCustomArmorItem extends AbstractCustomItem {

	@Override
	public Material getMaterial() {
		return this.getArmorType().getMaterial(this.getArmorMaterial());
	}


	public abstract ArmorType getArmorType();

	public abstract ArmorMaterial getArmorMaterial();


	public ItemStack getEquippedItem(Player player) {
		return this.getArmorType().getArmor(player.getInventory());
	}


	public boolean isEquipped(Player player) {
		return this.isInstanceOf(this.getEquippedItem(player));
	}


	@Deprecated
	protected void onEquip(Player player) {}


	@Deprecated
	protected void onUnequip(Player player) {}


	@Deprecated
	@Override
	public boolean onPlayerArmorEquipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		if(this.isEquipped(player))
			this.onEquip(player);
		return super.onPlayerArmorEquipItem(player, stack, event);
	}


	@Deprecated
	@Override
	public boolean onPlayerArmorUnequipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		if(!this.isEquipped(player))
			this.onUnequip(player);
		return super.onPlayerArmorUnequipItem(player, stack, event);
	}

}
