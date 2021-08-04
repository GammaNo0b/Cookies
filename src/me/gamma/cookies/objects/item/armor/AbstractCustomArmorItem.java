
package me.gamma.cookies.objects.item.armor;


import org.bukkit.Material;

import me.gamma.cookies.objects.item.AbstractCustomItem;



public abstract class AbstractCustomArmorItem extends AbstractCustomItem {

	@Override
	public Material getMaterial() {
		return this.getArmorType().getMaterial(this.getArmorMaterial());
	}


	public abstract ArmorType getArmorType();

	public abstract ArmorMaterial getArmorMaterial();

}
