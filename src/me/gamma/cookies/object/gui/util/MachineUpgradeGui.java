
package me.gamma.cookies.object.gui.util;


import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.object.block.Upgradeable;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class MachineUpgradeGui implements InventoryProvider<MachineUpgradeGui.Data> {

	public static final String KEY_LOCATION = "location";
	public static final String KEY_WORLD = "world";

	@Override
	public String getIdentifier() {
		return "machine_upgrade_gui";
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public String getTitle(Data data) {
		return data.upgradeable.getDisplayName(data.block) + " §9Upgrades";
	}


	@Override
	public int rows() {
		return 4;
	}


	@Override
	public void storeData(Inventory inventory, Data data) {
		ItemStack stack = inventory.getItem(this.getIdentifierSlot());
		InventoryUtils.storeLocationInStack(stack, KEY_LOCATION, KEY_WORLD, data.block.getLocation());
		List<MachineUpgrade> upgrades = data.upgradeable.getAllowedUpgrades();
		int index = 0;
		int row, column;
		for(MachineUpgrade upgrade : upgrades) {
			for(int i = 0; i < data.upgradeable.getUpgradeLevel(data.block, upgrade); i++, index++) {
				row = index / 7;
				column = index % 7;
				inventory.setItem(row * 9 + column + 10, upgrade.getItem().get());
			}
		}
	}


	@Override
	public Data fetchData(Inventory inventory) {
		ItemStack stack = inventory.getItem(this.getIdentifierSlot());
		Location location = InventoryUtils.getLocationFromStack(stack, KEY_LOCATION, KEY_WORLD);
		TileState block = BlockUtils.getTileState(location);
		if(block == null)
			return null;

		if(!(Blocks.getCustomBlockFromBlock(block) instanceof Upgradeable upgradeable))
			return null;

		return new Data(block, upgradeable);
	}


	@Override
	public Sound getSound() {
		return Sound.ITEM_BOOK_PAGE_TURN;
	}


	@Override
	public Inventory createGui(Data data) {
		Inventory gui = InventoryProvider.super.createGui(data);

		ItemStack filler = InventoryUtils.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(27 + i, filler);
		}
		gui.setItem(9, filler);
		gui.setItem(17, filler);
		gui.setItem(18, filler);
		gui.setItem(26, filler);
		gui.setItem(4, new ItemBuilder(InventoryUtils.filler(Material.BLUE_STAINED_GLASS_PANE)).setName("§9<--- Back").build());

		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 10; i < 17; i++)
			gui.setItem(i, filler);
		for(int i = 19; i < 26; i++)
			gui.setItem(i, filler);

		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Data data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 4) {
			History.travelBack(player);
			return true;
		}

		ItemStack stack = event.getCurrentItem();
		if(!ItemUtils.isEmpty(stack) && !InventoryUtils.isFiller(stack)) {
			ItemUtils.giveItemToPlayer(player, stack);
			gui.setItem(slot, InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
			MachineUpgrade upgrade = MachineUpgrade.fromStack(stack);
			if(upgrade != null)
				data.decreaseLevel(upgrade);
		}
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, Data data, PlayerInventory gui, InventoryClickEvent event) {
		ItemStack clicked = event.getCurrentItem();
		if(ItemUtils.isEmpty(clicked))
			return true;

		MachineUpgrade upgrade = MachineUpgrade.fromStack(clicked);
		if(upgrade == null)
			return true;

		int amount;
		if(event.getClick().isRightClick()) {
			amount = 1;
		} else if(event.getClick().isLeftClick()) {
			amount = clicked.getAmount();
		} else {
			return true;
		}

		Inventory inventory = event.getInventory();

		int empty = data.upgradeable.getUpgradeSlots(data.block);
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 7; j++)
				if(!InventoryUtils.isFiller(inventory.getItem(i * 9 + j + 10)))
					empty--;

		int set = data.increaseLevel(upgrade, Math.min(amount, empty));
		clicked.setAmount(clicked.getAmount() - set);
		for(int i = 0; i < 2 && set > 0; i++) {
			for(int j = 0; j < 7 && set > 0; j++) {
				if(InventoryUtils.isFiller(inventory.getItem(i * 9 + j + 10))) {
					inventory.setItem(i * 9 + j + 10, upgrade.getItem().get());
					set--;
				}
			}
		}

		return true;
	}


	public static void open(HumanEntity player, TileState block, Upgradeable upgradeable) {
		Inventories.MACHINE_UPGRADES.openGui(player, new Data(block, upgradeable));
	}

	static record Data(TileState block, Upgradeable upgradeable) {

		public void decreaseLevel(MachineUpgrade upgrade) {
			int level = this.upgradeable.getUpgradeLevel(block, upgrade);
			if(level > 0) {
				this.upgradeable.setUpgradeLevel(this.block, upgrade, level - 1);
				this.block.update();
			}
		}


		public int increaseLevel(MachineUpgrade upgrade, int amount) {
			int level = this.upgradeable.getUpgradeLevel(block, upgrade);
			int space = upgrade.getMaxStackSize() - level;
			int transfer = Math.min(amount, space);
			this.upgradeable.setUpgradeLevel(this.block, upgrade, level + transfer);
			this.block.update();
			return transfer;
		}

	}

}
