
package me.gamma.cookies.object.gui;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class BlockFaceConfigs implements InventoryProvider<BlockFaceConfigurable> {

	private static final int[] border = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
	private static final int backslot = 4;

	@Override
	public String getIdentifier() {
		return "blockfaceconfigs";
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public String getTitle(BlockFaceConfigurable data) {
		return "§8Block Face Configuration";
	}


	@Override
	public int rows() {
		return 3;
	}


	@Override
	public Inventory createGui(BlockFaceConfigurable data) {
		List<BlockFaceConfig.Config> configs = new ArrayList<>();
		data.listBlockFaceProperties(configs);
		Inventory gui = InventoryProvider.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);

		for(int i : border)
			gui.setItem(i, filler);

		for(int i = 0; i < configs.size(); i++) {
			Config config = configs.get(i);
			gui.setItem(10 + i, config.createIcon());
		}

		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 10 + configs.size(); i < 17; i++)
			gui.setItem(i, filler);

		gui.setItem(backslot, new ItemBuilder(Material.BARRIER).setName("§cClose").build());

		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, BlockFaceConfigurable data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == backslot) {
			History.travelBack(event.getWhoClicked());
		} else {
			int index = slot - 10;
			if(index < 0)
				return true;

			List<BlockFaceConfig.Config> configs = new ArrayList<>();
			data.listBlockFaceProperties(configs);
			if(index >= configs.size())
				return true;

			BlockFaceConfig.openBlockFaceConfig(player, configs.get(index));
		}

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, BlockFaceConfigurable data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	public static void openBlockFaceConfigs(HumanEntity player, BlockFaceConfigurable configurable) {
		Inventories.BLOCK_FACE_CONFIGS.openGui(player, configurable);
	}

}
