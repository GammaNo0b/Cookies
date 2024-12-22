
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;

import me.gamma.cookies.object.item.AbstractCustomItem;



public class MobGuide extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "mob_guide";
	}


	@Override
	public String getTitle() {
		return "§2Mob Guide";
	}


	@Override
	public Material getMaterial() {
		return Material.BOOK;
	}

}
