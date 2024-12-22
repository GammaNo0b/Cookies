
package me.gamma.cookies.object.gui;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class BlockFaceConfig implements Listener {

	private static final int[] slots = { 4, 8, 1, 7, 3, 5 };
	private static final int[] border = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
	private static final int autoslot = 2;

	private static final ItemStack AUTOMATED = new ItemBuilder(Material.REDSTONE).setName("§cAutomated").build();
	private static final ItemStack MANUAL = new ItemBuilder(Material.GUNPOWDER).setName("§7Manual").build();

	public static Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onInventoryClick(InventoryClickEvent event) {
				Location location = validateBlockFaceConfigsInventory(event.getView());
				Inventory inventory = event.getInventory();
				if(location != null) {
					event.setCancelled(true);

					if(event.getClickedInventory() instanceof PlayerInventory)
						return;

					if(event.getSlot() == 4) {
						History.travelBack(event.getWhoClicked());
						return;
					}

					ItemStack stack = event.getCurrentItem();
					if(ItemUtils.isEmpty(stack))
						return;
					
					if(InventoryUtils.isFiller(stack))
						return;

					if(!(location.getBlock().getState() instanceof TileState block))
						return;

					openBlockFaceConfig(event.getWhoClicked(), block, stack.getItemMeta().getDisplayName(), new ByteProperty(InventoryUtils.getStringFromStack(stack, "property")), 1 == InventoryUtils.getIntFromStack(stack, "automated"));
					return;
				}

				location = validateBlockFaceConfigInventory(event.getView());
				if(location != null) {
					event.setCancelled(true);

					if(event.getClickedInventory() instanceof PlayerInventory)
						return;

					ItemStack stack = event.getCurrentItem();
					if(ItemUtils.isEmpty(stack))
						return;

					if(event.getSlot() == 0) {
						History.travelBack(event.getWhoClicked());
						return;
					} else if(event.getSlot() == autoslot) {
						if(!InventoryUtils.isFiller(stack)) {
							inventory.setItem(autoslot, stack.getType() == Material.REDSTONE ? MANUAL : AUTOMATED);
							return;
						}
					}

					if(InventoryUtils.isFiller(stack))
						return;

					stack.setType(stack.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE);
					return;
				}
			}


			@EventHandler
			public void onInventoryClose(InventoryCloseEvent event) {
				Location location = validateBlockFaceConfigsInventory(event.getView());
				if(location != null) {
					return;
				}

				location = validateBlockFaceConfigInventory(event.getView());
				if(location != null) {

					if(!(location.getBlock().getState() instanceof TileState block))
						return;

					Inventory inventory = event.getInventory();
					String name = InventoryUtils.getStringFromStack(inventory.getItem(6), "blockfaceconfig");
					ByteProperty property = new ByteProperty(name);

					byte b = 0;
					for(int i = 0; i < 6; i++) {
						Material type = inventory.getItem(slots[i]).getType();
						if(type == Material.LIME_STAINED_GLASS_PANE)
							b |= 1 << i;
					}
					if(inventory.getItem(autoslot).getType() == Material.REDSTONE)
						b |= 0x40;

					property.store(block, b);
					block.update();

					return;
				}
			}

		};
	}


	/**
	 * property.bool == true <=> property is input
	 */
	public static void openBlockFaceConfigs(HumanEntity player, TileState block, String title, ArrayList<Config> configs) {
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.markItem(filler, "blockfaceconfigs");
		InventoryUtils.storeLocationInStack(filler, "pos", "world", block.getLocation());
		Inventory inventory = Bukkit.createInventory(null, 27, title);
		for(int i : border)
			inventory.setItem(i, filler);
		for(int i = 0; i < configs.size(); i++) {
			Config config = configs.get(i);
			inventory.setItem(10 + i, config.createIcon());
		}
		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 10 + configs.size(); i < 17; i++)
			inventory.setItem(i, filler);
		inventory.setItem(4, new ItemBuilder(Material.BARRIER).setName("§cClose").build());

		History.proceed(player, new StaticInventoryTask(inventory));
	}


	public static void openBlockFaceConfig(HumanEntity player, TileState block, String title, ByteProperty property, boolean automated) {
		byte flags = property.fetch(block);

		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		InventoryUtils.storeStringInStack(filler, "blockfaceconfig", property.getName());
		InventoryUtils.storeLocationInStack(filler, "pos", "world", block.getLocation());
		Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, title);
		inventory.setItem(6, filler);
		inventory.setItem(0, new ItemBuilder(Material.BARRIER).setName("§c<-- Back").build());
		for(int i = 0; i < 6; i++) {
			BlockUtils.BlockFaceDirection face = BlockUtils.BlockFaceDirection.values()[i];
			int bit = (flags >> i) & 1;
			inventory.setItem(slots[i], new ItemBuilder(bit == 0 ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE).setName("§6" + Utils.toCapitalWords(face)).build());
		}
		inventory.setItem(autoslot, automated ? ((flags & 0x40) == 0 ? MANUAL : AUTOMATED) : filler);

		History.proceed(player, new StaticInventoryTask(inventory));
	}


	private static Location validateBlockFaceConfigsInventory(InventoryView view) {
		Inventory inventory = view.getTopInventory();
		if(inventory.getSize() != 27)
			return null;

		ItemStack stack = inventory.getItem(0);
		if(ItemUtils.isEmpty(stack))
			return null;

		if(!InventoryUtils.isMarked(stack, "blockfaceconfigs"))
			return null;

		return InventoryUtils.getLocationFromStack(stack, "pos", "world");
	}


	private static Location validateBlockFaceConfigInventory(InventoryView view) {
		if(view.getType() != InventoryType.DROPPER)
			return null;

		Inventory inventory = view.getTopInventory();
		ItemStack stack = inventory.getItem(6);
		if(ItemUtils.isEmpty(stack))
			return null;

		if(InventoryUtils.getStringFromStack(stack, "blockfaceconfig") == null)
			return null;

		return InventoryUtils.getLocationFromStack(stack, "pos", "world");
	}

	public static record Config(String name, ByteProperty property, boolean automated, Material icon) {

		public ItemStack createIcon() {
			ItemStack icon = new ItemBuilder(this.icon).setName(this.name).build();
			InventoryUtils.storeStringInStack(icon, "property", this.property.getName());
			InventoryUtils.storeIntInStack(icon, "automated", automated ? 1 : 0);
			return icon;
		}

	}

}
