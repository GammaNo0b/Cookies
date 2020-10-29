
package me.gamma.cookies.objects.recipe;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.setup.CustomBlockSetup;



public enum RecipeType {

	CUSTOM("§2Custom §aCrafting", new ItemStack(Material.CRAFTING_TABLE), Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE),
	ENGINEER("§4Engineering", new ItemStack(Material.SMITHING_TABLE), Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE),
	KITCHEN("§cKitchen", new ItemStack(Material.SMOKER), Material.GREEN_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE),
	MACHINE("§8Machine", CustomBlockSetup.MOTOR.createDefaultItemStack(), null, null);

	private String name;
	private ItemStack icon;
	private Material border;
	private Material background;

	private RecipeType(String name, ItemStack icon, Material border, Material background) {
		this.name = name;
		this.icon = icon;
		this.border = border;
		this.background = background;
	}


	public String getName() {
		return name;
	}


	public ItemStack getIcon() {
		return icon.clone();
	}


	public Material getBorder() {
		return this.border;
	}


	public Material getBackground() {
		return this.background;
	}


	public static RecipeType byName(String name) {
		for(RecipeType type : values()) {
			if(name.equals(type.name)) {
				return type;
			}
		}
		return null;
	}

}
