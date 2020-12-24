
package me.gamma.cookies.objects.recipe;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.setup.CustomBlockSetup;



public enum RecipeType {

	CUSTOM("§2Custom §aCrafting", new ItemStack(Material.CRAFTING_TABLE), Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, 1, 1, 3, 3),
	ENGINEER("§4Engineering", new ItemStack(Material.SMITHING_TABLE), Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, 1, 1, 3, 3),
	KITCHEN("§cKitchen", new ItemStack(Material.SMOKER), Material.GREEN_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, 1, 1, 3, 3),
	MACHINE("§8Machine", CustomBlockSetup.MOTOR.createDefaultItemStack(), null, null, -1, -1, 0, 0);

	private String name;
	private ItemStack icon;
	private Material border;
	private Material background;
	private int startX;
	private int startY;
	private int width;
	private int height;

	private RecipeType(String name, ItemStack icon, Material border, Material background, int startX, int startY, int width, int height) {
		this.name = name;
		this.icon = icon;
		this.border = border;
		this.background = background;
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;
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


	public int getStartX() {
		return startX;
	}


	public int getStartY() {
		return startY;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
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
