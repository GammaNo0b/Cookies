
package me.gamma.cookies.object.fluid;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public enum FluidType {

	EMPTY(Material.GRAY_STAINED_GLASS_PANE, 'f'),
	WATER(Material.BLUE_STAINED_GLASS_PANE, '9'),
	LAVA(Material.ORANGE_STAINED_GLASS_PANE, '6'),
	MILK(Material.WHITE_STAINED_GLASS_PANE, 'f'),
	SLIME(Material.LIME_STAINED_GLASS_PANE, 'a'),
	HONEY(Material.ORANGE_STAINED_GLASS_PANE, '6'),
	EXPERIENCE(Material.LIME_STAINED_GLASS_PANE, 'e'),
	RED(Material.RED_STAINED_GLASS_PANE, 'c'),
	GREEN(Material.GREEN_STAINED_GLASS_PANE, '2'),
	BLUE(Material.BLUE_STAINED_GLASS_PANE, '9'),
	BLACK(Material.BLACK_STAINED_GLASS_PANE, 'f'),
	BIO_MASS(Material.LIME_STAINED_GLASS_PANE, 'a');

	private final Material icon;
	private final char color;

	private FluidType(Material icon, char color) {
		this.icon = icon;
		this.color = color;
	}


	public String getName() {
		return "§" + this.color + Utils.toCapitalWords(this);
	}


	public Material getIcon() {
		return this.icon;
	}


	public ItemStack createIcon() {
		return new ItemBuilder(this.icon).setName(this.getName()).build();
	}

}
