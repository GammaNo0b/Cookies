
package me.gamma.cookies.object.recipe;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public enum RecipeType {

	CUSTOM("§2Custom §aCrafting", Material.CRAFTING_TABLE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, 1, 1, 3, 3),
	ENGINEER("§4Engineering", Material.SMITHING_TABLE, Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, 1, 1, 3, 3),
	KITCHEN("§eKitchen Cooking", Material.SMOKER, Material.GREEN_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, 1, 1, 3, 3),
	ALTAR("§5Magic Altar", Material.ENCHANTING_TABLE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, 1, 1, 3, 3),
	MACHINE("§8Machine", null, null, null, -1, -1, 0, 0);

	private String name;
	private Material icon;
	private Material border;
	private Material background;
	private int startX;
	private int startY;
	private int width;
	private int height;

	private RecipeType(String name, Material icon, Material border, Material background, int startX, int startY, int width, int height) {
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


	public Material getIcon() {
		return icon;
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


	public int getCraftingSlot() {
		int h = (this.height + 1) / 2;
		int even = this.height + 1 - h * 2;
		int slot = h * 9 + this.width + 1 + even;
		return slot;
	}


	public int getResultSlot() {
		int h = (this.height + 1) / 2;
		int even = this.height + 1 - h * 2;
		int slot = h * 9 + this.width + 1 + even;
		return slot + 2 + even * 7;
	}


	/**
	 * Opens the crafting inventory for the given player
	 * 
	 * @param player the player
	 */
	public void open(HumanEntity player) {
		if(this == RecipeType.MACHINE)
			return;

		int size = (2 + this.height) * 9;
		Inventory craftingGui = Bukkit.createInventory(null, size, this.name);
		ItemStack border = InventoryUtils.filler(this.border);
		for(int i = 0; i < 9; i++) {
			craftingGui.setItem(i, border);
			craftingGui.setItem(size - i - 1, border);
		}
		for(int i = 1; i <= this.height; i++) {
			craftingGui.setItem(9 * i, border);
			craftingGui.setItem(9 * i + 8, border);
		}

		ItemStack background = InventoryUtils.filler(this.background);
		for(int i = 1; i <= this.height; i++)
			for(int j = 1 + this.width; j < 8; j++)
				craftingGui.setItem(i * 9 + j, background);

		int h = (this.height + 1) / 2;
		int even = this.height + 1 - h * 2;
		int slot = h * 9 + this.width + 1 + even;
		 craftingGui.setItem(slot, new ItemBuilder(this.icon).setName("§9Craft").build());
		craftingGui.setItem(slot + 2 + even * 7, null);

		player.openInventory(craftingGui);
	}


	public static RecipeType byName(String name) {
		for(RecipeType type : values())
			if(name.equals(type.name))
				return type;
		return null;
	}
	
	public static RecipeType byId(String id) {
		for(RecipeType type : values())
			if(id.equals(type.name.replaceAll("§.", "").replace(' ', '_').toLowerCase()))
				return type;
		return null;
	}

}
