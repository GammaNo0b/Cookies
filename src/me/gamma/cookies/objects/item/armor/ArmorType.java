
package me.gamma.cookies.objects.item.armor;


import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;



public enum ArmorType {

	HELMET(EquipmentSlot.HEAD), CHESTPLATE(EquipmentSlot.CHEST), LEGGINGS(EquipmentSlot.LEGS), BOOTS(EquipmentSlot.FEET);

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


	public static ArmorType get(ItemStack stack) {
		if(stack == null) {
			return null;
		}
		String type = stack.getType().toString();
		if(type.endsWith("_HELMET")) {
			return HELMET;
		} else if(type.endsWith("_CHESTPLATE")) {
			return CHESTPLATE;
		} else if(type.endsWith("_LEGGINGS")) {
			return LEGGINGS;
		} else if(type.endsWith("_BOOTS")) {
			return BOOTS;
		} else if(type.equals("ELYTRA")) {
			return CHESTPLATE;
		} else if(type.contains("HEAD") || type.contains("SKELETON_SKULL")) {
			return HELMET;
		} else {
			return null;
		}
	}

}
