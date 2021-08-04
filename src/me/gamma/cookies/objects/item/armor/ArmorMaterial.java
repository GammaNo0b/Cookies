
package me.gamma.cookies.objects.item.armor;


import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;



public enum ArmorMaterial {

	LEATHER(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS),
	CHAINMAIL(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS),
	IRON(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS),
	GOLD(Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS),
	DIAMOND(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS),
	NETHERITE(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS),
	ELYTRA(null, Material.ELYTRA, null, null),
	TURTLE_SHELL(Material.TURTLE_HELMET, null, null, null);

	private final Material[] materials = new Material[4];

	private ArmorMaterial(Material head, Material chestplate, Material leggings, Material boots) {
		this.materials[0] = head;
		this.materials[1] = chestplate;
		this.materials[2] = leggings;
		this.materials[3] = boots;
	}


	public Material getMaterial(EquipmentSlot slot) {
		int i = 5 - slot.ordinal();
		return i < 0 || i > 4 ? null : this.materials[i];
	}

}
