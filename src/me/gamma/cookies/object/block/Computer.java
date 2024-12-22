
package me.gamma.cookies.object.block;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class Computer extends AbstractCustomBlock implements BlockInventoryProvider {

	public static final IntegerProperty TASKID = new IntegerProperty("taskid");
	public static final ItemStackProperty SCRIPT_ITEM = new ItemStackProperty("script");
	public static final ItemStackProperty LOG_ITEM = new ItemStackProperty("log");

	@Override
	public int getIdentifierSlot() {
		return 4;
	}


	@Override
	public int rows() {
		return 3;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COMPUTER;
	}


	@Override
	public String getIdentifier() {
		return "computer";
	}


	@Override
	public String getTitle(TileState block) {
		return "§5Computer";
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = BlockInventoryProvider.super.createGui(block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 18, filler);
		}
		gui.setItem(9, filler);
		gui.setItem(17, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		gui.setItem(10, filler);
		ItemStack script = SCRIPT_ITEM.fetch(block);
		if(script != null)
			gui.setItem(11, script);
		gui.setItem(12, filler);
		gui.setItem(13, new ItemBuilder(Material.MAP).setName("§6Run >").addLore("§cError: §4-").build());
		filler = InventoryUtils.filler(Material.RED_STAINED_GLASS_PANE);
		gui.setItem(14, filler);
		ItemStack log = LOG_ITEM.fetch(block);
		if(log != null)
			gui.setItem(15, log);
		gui.setItem(16, filler);
		return gui;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(TASKID).add(SCRIPT_ITEM).add(LOG_ITEM);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;
		
		int id = TASKID.fetch(block);
		if(id > 0)
			Bukkit.getScheduler().cancelTask(id);
		ItemUtils.dropItem(SCRIPT_ITEM.fetch(block), block);
		ItemUtils.dropItem(LOG_ITEM.fetch(block), block);
		
		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		this.openGui(player, block);
		return true;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 13) {
			int id = TASKID.fetch(block);
			if(id > 0) {
				Bukkit.getScheduler().cancelTask(id);
				return true;
			}
			ItemStack run = gui.getItem(13);
			ItemMeta runmeta = run.getItemMeta();
			runmeta.setDisplayName("§cStop script ■");
			runmeta.setLore(new ArrayList<>());
			run.setItemMeta(runmeta);
			// ItemStack script = gui.getItem(11);
			/*
			 * ScriptPrintStream out = new ScriptPrintStream(ItemProvider.fromInventory(gui, 15), msg -> player.sendMessage("[Computer]: §7§o" + msg)); id =
			 * Lua.executeComputer((Skull) block, script, out, out, e -> { ItemStack button = gui.getItem(13); if(!ItemUtils.isEmpty(button)) { ItemMeta meta =
			 * button.getItemMeta(); if(e == null) { meta.setDisplayName("§6Run >"); meta.setLore(Arrays.asList("§cError: §4-")); } else { ArrayList<String> lore
			 * = new ArrayList<>(); lore.add("§c" + e.getClass().getSimpleName()); StringBuilder builder = new StringBuilder(); for(char c :
			 * e.getMessage().toCharArray()) { if(MinecraftFont.Font.getWidth(builder.toString() + c) > 500) { lore.add("§4" + builder.toString());
			 * builder.delete(0, builder.length()); } builder.append(c); } if(builder.length() > 0) lore.add("§4" + builder.toString()); meta.setLore(lore); }
			 * button.setItemMeta(meta); } }); if(id > 0) TASKID.store(block, id);
			 */
			return true;
		}
		return slot != 11 && slot != 15;
	}


	@Override
	public boolean onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		SCRIPT_ITEM.store(block, gui.getItem(11));
		LOG_ITEM.store(block, gui.getItem(15));
		block.update();
		return false;
	}

}
