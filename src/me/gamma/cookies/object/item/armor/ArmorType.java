
package me.gamma.cookies.object.item.armor;


import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.util.ItemUtils;



public enum ArmorType {

	HELMET(EquipmentSlot.HEAD),
	CHESTPLATE(EquipmentSlot.CHEST),
	LEGGINGS(EquipmentSlot.LEGS),
	BOOTS(EquipmentSlot.FEET);

	private EquipmentSlot slot;

	private ArmorType(EquipmentSlot slot) {
		this.slot = slot;
	}


	public int getRawSlot() {
		return this.ordinal() + 5;
	}


	public ItemStack getArmor(PlayerInventory inventory) {
		return inventory.getItem(this.slot);
	}


	public void setArmor(PlayerInventory inventory, ItemStack armor) {
		inventory.setItem(this.slot, armor);
	}


	public Material getMaterial(ArmorMaterial material) {
		return material.getMaterial(this.slot);
	}


	public static ArmorType get(ItemStack stack) {
		return ItemUtils.isEmpty(stack) ? null : get(stack.getType());
	}


	public static ArmorType get(Material material) {
		String type = material.name();
		if(type.endsWith("_HELMET") || type.contains("HEAD") || type.contains("SKELETON_SKULL")) {
			return HELMET;
		} else if(type.endsWith("_CHESTPLATE") || type.equals("ELYTRA")) {
			return CHESTPLATE;
		} else if(type.endsWith("_LEGGINGS")) {
			return LEGGINGS;
		} else if(type.endsWith("_BOOTS")) {
			return BOOTS;
		} else {
			return null;
		}
	}

}
