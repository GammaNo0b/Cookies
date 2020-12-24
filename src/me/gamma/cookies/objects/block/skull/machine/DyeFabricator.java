
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ColorProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;



public class DyeFabricator extends AbstractGuiProvidingSkullBlock {

	public static final IntegerProperty RED = IntegerProperty.create("red");
	public static final IntegerProperty GREEN = IntegerProperty.create("green");
	public static final IntegerProperty BLUE = IntegerProperty.create("blue");
	public static final ColorProperty COLOR = ColorProperty.create("color");

	@Override
	public String getBlockTexture() {
		return HeadTextures.DYE_FABRICATOR;
	}


	@Override
	public String getDisplayName() {
		return "§4Dye §2Fabri§1cator";
	}


	@Override
	public String getRegistryName() {
		return "dye_fabricator";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Mix Dyes and color Leather Armor!");
	}


	@Override
	public int getRows() {
		return 5;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_BARREL_OPEN;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack item = super.createDefaultItemStack();
		ItemMeta meta = item.getItemMeta();
		RED.storeEmpty(meta);
		GREEN.storeEmpty(meta);
		BLUE.storeEmpty(meta);
		COLOR.storeEmpty(meta);
		List<String> lore = meta.getLore();
		lore.addAll(Arrays.asList("", "§4Red: §c0", "§2Green: §a0", "§1Blue: §90"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		for(int i = 0; i < 9; i++) {
			if(i != 4)
				gui.setItem(i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(i + 36, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 4; j++) {
				gui.setItem((i + 1) * 9 + j, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
			}
		}
		gui.setItem(10, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§4Red: §c" + RED.fetch(block)).build());
		gui.setItem(13, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§c  - 16").build());
		gui.setItem(14, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§c  -  1").build());
		gui.setItem(16, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§c  +  1").build());
		gui.setItem(17, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§c  + 16").build());
		gui.setItem(19, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§2Green: §a" + GREEN.fetch(block)).build());
		gui.setItem(22, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§a  - 16").build());
		gui.setItem(23, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§a  -  1").build());
		gui.setItem(25, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§a  +  1").build());
		gui.setItem(26, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§a  + 16").build());
		gui.setItem(28, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§1Blue: §9" + BLUE.fetch(block)).build());
		gui.setItem(31, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9  - 16").build());
		gui.setItem(32, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9  -  1").build());
		gui.setItem(34, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9  +  1").build());
		gui.setItem(35, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9  + 16").build());
		gui.setItem(15, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§eRandom Color").build());
		gui.setItem(24, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§8Current Color").setColor(COLOR.fetch(block)).build());
		gui.setItem(33, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Reset Color").build());
		return gui;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("IGI", "RMB", "ITI");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);
		recipe.setIngredient('T', Material.GLASS);
		recipe.setIngredient('M', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		RED.transfer(usedItem.getItemMeta(), block);
		GREEN.transfer(usedItem.getItemMeta(), block);
		BLUE.transfer(usedItem.getItemMeta(), block);
		COLOR.transfer(usedItem.getItemMeta(), block);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack item = super.onBlockBreak(player, block, event);
		ItemMeta meta = item.getItemMeta();
		RED.transfer(block, meta);
		GREEN.transfer(block, meta);
		BLUE.transfer(block, meta);
		COLOR.transfer(block, meta);
		meta.setLore(Arrays.asList("", "§4Red: §c" + RED.fetch(meta), "§2Green: §a" + GREEN.fetch(meta), "§1Blue: §9" + BLUE.fetch(meta)));
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		ItemStack cursor = event.getCursor();
		if(event.getSlot() == 10) {
			// red
			if(cursor != null && cursor.getType() == Material.RED_DYE) {
				this.addRed(block, cursor.getAmount() * 256);
				cursor.setAmount(0);
				player.openInventory(this.createMainGui(player, block));
			}
		} else if(event.getSlot() == 19) {
			// green
			if(cursor != null && cursor.getType() == Material.GREEN_DYE) {
				this.addGreen(block, cursor.getAmount() * 256);
				cursor.setAmount(0);
				player.openInventory(this.createMainGui(player, block));
			}
		} else if(event.getSlot() == 28) {
			// blue
			if(cursor != null && cursor.getType() == Material.BLUE_DYE) {
				this.addBlue(block, cursor.getAmount() * 256);
				cursor.setAmount(0);
				player.openInventory(this.createMainGui(player, block));
			}
		} else if(event.getSlot() == 24) {
			// current color
			if(cursor != null && cursor.getItemMeta() instanceof LeatherArmorMeta) {
				LeatherArmorMeta meta = (LeatherArmorMeta) cursor.getItemMeta();
				Color color = COLOR.fetch(block);
				if(!color.equals(meta.getColor())) {
					if(this.removeColor(block, color)) {
						meta.setColor(color);
						cursor.setItemMeta(meta);
						this.handleCursor(player, cursor, block);
					}
				}
			}
		} else if(event.getSlot() == 15) {
			// random Color
			COLOR.store(block, Color.fromRGB(new Random().nextInt(256 * 256 * 256)));
			block.update();
			this.handleCursor(player, cursor, block);
		} else if(event.getSlot() == 33) {
			// reset Color
			COLOR.storeEmpty(block);
			block.update();
			this.handleCursor(player, cursor, block);
		} else if(Arrays.asList(13, 14, 16, 17, 22, 23, 25, 26, 31, 32, 34, 35).contains(event.getSlot())) {
			// increase / decrease color
			int amount = (event.getSlot() - 13) % 9;
			switch (amount) {
				case 0:
					amount = -16;
					break;
				case 1:
					amount = -1;
					break;
				case 3:
					amount = 1;
					break;
				case 4:
					amount = 16;
					break;
			}
			switch ((event.getSlot() - 13) / 9) {
				case 0:
					if(this.changeLocalRed(block, amount)) {
						this.handleCursor(player, cursor, block);
					}
					break;
				case 1:
					if(this.changeLocalGreen(block, amount)) {
						this.handleCursor(player, cursor, block);
					}
					break;
				case 2:
					if(this.changeLocalBlue(block, amount)) {
						this.handleCursor(player, cursor, block);
					}
					break;
			}
		}
		return true;
	}


	private void handleCursor(Player player, ItemStack cursor, TileState block) {
		ItemStack clone = cursor.clone();
		cursor.setAmount(0);
		player.openInventory(this.createMainGui(player, block));
		player.getOpenInventory().setCursor(clone);
	}


	private void addRed(TileState block, int red) {
		RED.increase(block, red);
		block.update();
	}


	private void addGreen(TileState block, int blue) {
		GREEN.increase(block, blue);
		block.update();
	}


	private void addBlue(TileState block, int green) {
		BLUE.increase(block, green);
		block.update();
	}


	private boolean removeColor(TileState block, Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		if(r > RED.fetch(block) || g > GREEN.fetch(block) || b > BLUE.fetch(block)) {
			return false;
		} else {
			RED.increase(block, -r);
			GREEN.increase(block, -g);
			BLUE.increase(block, -b);
			block.update();
			return true;
		}
	}


	private boolean changeLocalRed(TileState block, int red) {
		Color color = COLOR.fetch(block);
		red += color.getRed();
		if(red > 255) {
			red = 255;
		} else if(red < 0) {
			red = 0;
		}
		color = color.setRed(red);
		COLOR.store(block, color);
		block.update();
		return true;
	}


	private boolean changeLocalGreen(TileState block, int green) {
		Color color = COLOR.fetch(block);
		green += color.getGreen();
		if(green > 255) {
			green = 255;
		} else if(green < 0) {
			green = 0;
		}
		color = color.setGreen(green);
		COLOR.store(block, color);
		block.update();
		return true;
	}


	private boolean changeLocalBlue(TileState block, int blue) {
		Color color = COLOR.fetch(block);
		blue += color.getBlue();
		if(blue > 255) {
			blue = 255;
		} else if(blue < 0) {
			blue = 0;
		}
		color = color.setBlue(blue);
		COLOR.store(block, color);
		block.update();
		return true;
	}

}
