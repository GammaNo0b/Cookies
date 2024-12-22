
package me.gamma.cookies.object.gui.util;


import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.gui.util.ColorWheel.Data;
import me.gamma.cookies.object.property.ColorProperty;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class ColorWheel implements InventoryProvider<Data> {

	public static final String KEY_COLOR = "color";
	public static final String KEY_SECTOR = "sector";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_WORLD = "world";
	public static final String KEY_PROPERTY = "property";

	private static final String title = ColorUtils.color("Color Wheel", ColorUtils.RAINBOW_COLOR_SEQUENCE, 1);

	@Override
	public String getIdentifier() {
		return "color_wheel";
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public String getTitle(Data data) {
		return title;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Inventory createGui(Data data) {
		Inventory gui = InventoryProvider.super.createGui(data);

		ItemStack filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 45, filler);
		}

		filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 1; i < 5; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 5, filler);
		}
		for(int i = 0; i < 3; i++)
			gui.setItem(24 + i, filler);

		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		gui.setItem(15, filler);
		gui.setItem(17, filler);

		gui.setItem(4, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dSelect Color").build());

		this.update(gui, data);

		return gui;
	}


	private void update(Inventory gui, Data data) {
		int[][] palette = data.getPalette();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				int c = palette[i][j];
				gui.setItem(i * 9 + j + 10, data.createIcon(c, c == data.color));
			}
		}

		for(int i = 0; i < Sector.grid.length; i++) {
			Sector[] row = Sector.grid[i];
			for(int j = 0; j < row.length; j++) {
				Sector sector = row[j];
				gui.setItem(i * 9 + j + 33, sector.createIcon(sector == data.sector));
			}
		}

		gui.setItem(16, data.createIcon(data.color, false));
	}


	@Override
	public void storeData(Inventory inventory, Data data) {
		ItemStack identifier = inventory.getItem(this.getIdentifierSlot());
		InventoryUtils.storeIntInStack(identifier, KEY_COLOR, data.color);
		InventoryUtils.storeEnumInStack(identifier, KEY_SECTOR, data.sector);
		InventoryUtils.storeLocationInStack(identifier, KEY_LOCATION, KEY_WORLD, data.block.getLocation());
		InventoryUtils.storeStringInStack(identifier, KEY_PROPERTY, data.property.getName());
	}


	@Override
	public Data fetchData(Inventory inventory) {
		ItemStack identifier = inventory.getItem(this.getIdentifierSlot());
		int color = InventoryUtils.getIntFromStack(identifier, KEY_COLOR);
		Sector sector = InventoryUtils.getEnumFromStack(identifier, KEY_SECTOR, Sector.class);
		Location location = InventoryUtils.getLocationFromStack(identifier, KEY_LOCATION, KEY_WORLD);
		if(!(location.getBlock().getState() instanceof TileState state))
			return null;

		ColorProperty property = new ColorProperty(InventoryUtils.getStringFromStack(identifier, KEY_PROPERTY));

		return new Data(color, sector, state, property);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Data data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		boolean update = false;

		if(slot == 4) {
			History.travelBack(player);
		} else {
			int r = slot / 9;
			int c = slot % 9;
			if(3 <= r && r < 5 && 6 <= c && c < 9) {
				data = new Data(data.color, Sector.grid[r - 3][c - 6], data.block, data.property);
				ItemStack identifier = gui.getItem(this.getIdentifierSlot());
				InventoryUtils.storeEnumInStack(identifier, KEY_SECTOR, data.sector);
				update = true;
			} else if(1 <= r && r < 5 && 1 <= c && c < 5) {
				data = new Data(data.getPalette()[r - 1][c - 1], data.sector, data.block, data.property);
				ItemStack identifier = gui.getItem(this.getIdentifierSlot());
				InventoryUtils.storeIntInStack(identifier, KEY_COLOR, data.color);
				update = true;
			}
		}

		if(update)
			this.update(gui, data);

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, Data data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public boolean onInventoryClose(Player player, Data data, Inventory gui, InventoryCloseEvent event) {
		data.storeColor();
		return false;
	}


	public static void openColorWheel(HumanEntity player, TileState block, ColorProperty property) {
		Inventories.COLOR_WHEEL.openGui(player, new Data(property.fetch(block).asRGB(), Sector.RED_MAJOR, block, property));
	}

	record Data(int color, Sector sector, TileState block, ColorProperty property) {

		private void storeColor() {
			this.property.store(this.block, Color.fromRGB(this.color));
			this.block.update();
		}


		private int[] separateColor() {
			int[] separated = new int[6];
			for(int i = 0; i < 6; i++)
				separated[5 - i] = (this.color >> (i * 4)) & 0xF;
			return separated;
		}


		private static int combineColors(int[] separated) {
			int color = 0;
			for(int i = 0; i < 6; i++) {
				color <<= 4;
				color |= separated[i];
			}
			return color;
		}


		private int[][] getPalette() {
			int sector = this.sector.ordinal();
			int[] separated = this.separateColor();
			int[][] palette = new int[4][4];
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					separated[sector] = i * 4 + j;
					palette[i][j] = combineColors(separated);
				}
			}
			return palette;
		}


		private ItemStack createIcon(int color, boolean selected) {
			char[] name = new char[21];
			name[0] = '§';
			name[1] = 'f';
			name[2] = '#';
			for(Sector sector : Sector.values()) {
				int i = sector.ordinal();
				int j = 3 * i + 3;
				name[j++] = '§';
				name[j++] = sector.getColor(sector == this.sector);
				name[j] = Utils.HEX_DIGITS[(color >> ((5 - i) * 4)) & 0xF];
			}

			ItemBuilder builder = new ItemBuilder(Material.LEATHER_CHESTPLATE).setName(new String(name)).setColor(Color.fromRGB(color)).setItemFlag(ItemFlag.HIDE_DYE).setItemFlag(ItemFlag.HIDE_ATTRIBUTES);
			if(selected)
				builder.addEnchantment(Enchantment.PROTECTION, 1).setItemFlag(ItemFlag.HIDE_ENCHANTS);
			return builder.build();
		}

	}

	static enum Sector {

		RED_MAJOR('4', '4', 'c', Material.RED_STAINED_GLASS),
		RED_MINOR('c', '4', 'c', Material.RED_STAINED_GLASS_PANE),
		GREEN_MAJOR('2', '2', 'a', Material.LIME_STAINED_GLASS),
		GREEN_MINOR('a', '2', 'a', Material.LIME_STAINED_GLASS_PANE),
		BLUE_MAJOR('9', '9', '3', Material.BLUE_STAINED_GLASS),
		BLUE_MINOR('b', '9', '3', Material.BLUE_STAINED_GLASS_PANE);

		static Sector[][] grid = { { RED_MAJOR, GREEN_MAJOR, BLUE_MAJOR }, { RED_MINOR, GREEN_MINOR, BLUE_MINOR } };

		private final String name;
		private final char primaryColor;
		private final char secondaryColor;
		private final Material icon;

		private Sector(char color, char primaryColor, char secondaryColor, Material icon) {
			this.name = String.format("§%c%s", color, Utils.toCapitalWords(this));
			this.primaryColor = primaryColor;
			this.secondaryColor = secondaryColor;
			this.icon = icon;
		}


		private Material getIcon() {
			return this.icon;
		}


		private ItemStack createIcon(boolean selected) {
			ItemBuilder builder = new ItemBuilder(this.icon).setName(this.name);
			if(selected)
				builder.addEnchantment(Enchantment.UNBREAKING, 1).setItemFlag(ItemFlag.HIDE_ENCHANTS);
			return builder.build();
		}


		private char getColor(boolean selected) {
			return selected ? this.secondaryColor : this.primaryColor;
		}


		public static Sector byIcon(Material icon) {
			return EnumUtils.byMember(Sector.class, Sector::getIcon, icon);
		}

	}

}
