
package me.gamma.cookies.object.item;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.MultiBlockInit;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class CustomCraftingTableBlueprint extends AbstractCustomItem implements InventoryProvider<Void> {

	@Override
	public String getIdentifier() {
		return "custom_crafting_table_blueprint";
	}


	@Override
	public String getTitle() {
		return "§fCustom Crafting Table Blueprint";
	}


	@Override
	public String getTitle(Void data) {
		return this.getTitle();
	}


	@Override
	public Material getMaterial() {
		return Material.PAPER;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public Sound getSound() {
		return Sound.ITEM_BOOK_PAGE_TURN;
	}


	@Override
	public Inventory createGui(Void data) {
		Inventory gui = InventoryProvider.super.createGui(data);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, InventoryUtils.filler(Material.BLUE_STAINED_GLASS_PANE));
			gui.setItem(i + 36, InventoryUtils.filler(Material.BLUE_STAINED_GLASS_PANE));
		}
		final ItemStack description = new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("§bBuild the Multiblock §3-->").build();
		for(int i = 1; i < 4; i++) {
			gui.setItem(9 * i, InventoryUtils.filler(Material.BLUE_STAINED_GLASS_PANE));
			gui.setItem(9 * i + 8, InventoryUtils.filler(Material.BLUE_STAINED_GLASS_PANE));
			gui.setItem(9 * i + 1, description);
			gui.setItem(9 * i + 2, InventoryUtils.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
			gui.setItem(9 * i + 7, InventoryUtils.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
		}
		final ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		Material[][][] multiblock = MultiBlockInit.CUSTOM_CRAFTING_TABLE.getStructure();
		for(int i = 0; i < multiblock.length; i++) {
			Material[] row = multiblock[i][0];
			for(int j = 0; j < row.length; j++) {
				Material material = row[j];
				int index = (3 - i) * 9 + j + 3;
				if(material == null) {
					gui.setItem(index, filler);
				} else {
					if(material.isItem()) {
						gui.setItem(index, new ItemBuilder(material).setName("§6Position: §4X: §c" + j + " §7| §2Y: §a" + i + " §7| §1Z: §90").build());
					} else {
						gui.setItem(index, new ItemBuilder(Material.BARRIER).setName("§8Material: §7" + Utils.toCapitalWords(material) + "§8, §6Position: §4X: §c" + j + " §7| §2Y: §a" + i + " §7| §1Z: §90").build());
					}
				}
			}
		}
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Void data, Inventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, Void data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.openGui(player, null);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, stack, block, event))
			return false;

		this.openGui(player, null);
		return true;
	}

}
