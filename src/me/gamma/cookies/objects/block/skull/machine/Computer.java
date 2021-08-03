
package me.gamma.cookies.objects.block.skull.machine;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptException;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MinecraftFont;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class Computer extends AbstractGuiProvidingSkullBlock {

	@Override
	public int getRows() {
		return 3;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CONSOLE_SCREEN;
	}


	@Override
	public String getRegistryName() {
		return "computer";
	}


	@Override
	public String getDisplayName() {
		return "§7Computer";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("SWS", "RGR", "ICI");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.ELECTRICAL_CIRCUIT.createDefaultItemStack());
		recipe.setIngredient('G', Material.GLASS_PANE);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('I', Material.IRON_INGOT);
		return recipe;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		ItemStack filler = InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			if(i != 4)
				gui.setItem(i, filler);
			gui.setItem(i + 18, filler);
		}
		gui.setItem(9, filler);
		gui.setItem(17, filler);
		filler = InventoryManager.filler(Material.GREEN_STAINED_GLASS_PANE);
		gui.setItem(10, filler);
		gui.setItem(12, filler);
		gui.setItem(13, new ItemBuilder(Material.MAP).setName("§6Run >").addLore("§cError: §4-").build());
		filler = InventoryManager.filler(Material.RED_STAINED_GLASS_PANE);
		gui.setItem(14, filler);
		gui.setItem(16, filler);
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 13) {
			String error = "-";
			ItemStack script = gui.getItem(11);
			ItemStack output = gui.getItem(15);
			if(Utilities.isEmpty(script) || script.getType() != Material.WRITABLE_BOOK) {
				error = "Non valid script item!";
			} else if(Utilities.isEmpty(output) || output.getType() != Material.WRITABLE_BOOK) {
				error = "Non valid output item!";
			} else {
				try {
					this.setBookContent(output, this.run(this.getBookContent(script)));
				} catch(ScriptException e) {
					error = "Exception running script: " + e.getMessage();
				}
			}
			ItemStack button = gui.getItem(13);
			ItemMeta meta = button.getItemMeta();
			meta.setLore(Arrays.asList("§cError: §4" + error));
			button.setItemMeta(meta);
		}
		return slot != 11 && slot != 15;
	}


	@Override
	public void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		Utilities.giveItemToPlayer(player, gui.getItem(11));
		Utilities.giveItemToPlayer(player, gui.getItem(15));
		super.onInventoryClose(player, block, gui, event);
	}


	private String getBookContent(ItemStack book) {
		BookMeta meta = (BookMeta) book.getItemMeta();
		StringBuffer buffer = new StringBuffer();
		meta.getPages().forEach(line -> buffer.append(line).append('\n'));
		return buffer.toString();
	}


	private void setBookContent(ItemStack book, List<String> lines) {
		BookMeta meta = (BookMeta) book.getItemMeta();
		StringBuffer buffer = new StringBuffer();
		int p = 0;
		int n = 0;
		int width = 0;
		for(String line : lines) {
			if(++n >= 14) {
				n = 0;
				meta.addPage(buffer.toString());
				buffer.delete(0, buffer.length() - 1);
				if(++p > 50) {
					break;
				}
			}
			for(char c : line.toCharArray()) {
				int w = MinecraftFont.Font.getChar(c).getWidth();
				if(width + w > 114) {
					width = 0;
					buffer.append('\n');
					if(++n >= 14) {
						n = 0;
						meta.addPage(buffer.toString());
						buffer.delete(0, buffer.length() - 1);
						if(++p > 50) {
							break;
						}
					}
				}
				buffer.append(c);
			}
			if(p >= 50) {
				break;
			}
		}
		if(p <= 50) {
			meta.addPage(buffer.toString());
		}
		book.setItemMeta(meta);
	}


	private List<String> run(String script) throws ScriptException {
		StringWriter output = new StringWriter();

		// javascript.getContext().setWriter(output);
		// javascript.eval(script);

		List<String> lines = new ArrayList<>();
		StringBuffer buffer = new StringBuffer();
		for(int c : output.getBuffer().chars().toArray()) {
			if(c == '\n') {
				lines.add(buffer.toString());
				buffer.delete(0, buffer.length() - 1);
			} else {
				buffer.append(c);
			}
		}
		lines.add(buffer.toString());
		return lines;
	}

}
