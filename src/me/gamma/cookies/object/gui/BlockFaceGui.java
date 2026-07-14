
package me.gamma.cookies.object.gui;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public interface BlockFaceGui<D extends BlockFaceGui.BlockFaceData> extends InventoryProvider<D> {

	Material[] ICONS_ACTIVE = { Material.WHITE_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE };
	Material[] ICONS_INACTIVE = { Material.GRAY_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE };
	char[] COLORS_ACTIVE = "fdadeb".toCharArray();
	char[] COLORS_INACTIVE = "8c2569".toCharArray();
	int[] slots = { 8, 5, 4, 3, 1, 7 };
	int[] stols = { -1, 4, -1, 3, 2, 1, -1, 5, 0 };
	int backslot = 0;

	@Override
	default int getIdentifierSlot() {
		return 0;
	}


	@Override
	default String getTitle(D data) {
		return data.getTitle();
	}


	@Override
	default int rows() {
		return 0;
	}


	@Override
	default Inventory createGui(D data) {
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		Inventory gui = Bukkit.createInventory(null, InventoryType.DROPPER, this.getTitle(data));
		gui.setItem(backslot, new ItemBuilder(Material.BARRIER).setName("§c<-- Back").build());
		gui.setItem(2, filler);
		gui.setItem(6, filler);
		for(int i = 0; i < 6; i++)
			gui.setItem(slots[i], data.getIcon(BlockUtils.BlockFaceDirection.values()[i]));
		return gui;
	}


	@Override
	default boolean onMainInventoryInteract(Player player, D data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();

		if(slot == backslot) {
			History.travelBack(player);
		} else if(0 <= slot && slot < 9) {
			int i = stols[slot];
			if(i >= 0) {
				BlockUtils.BlockFaceDirection face = BlockUtils.BlockFaceDirection.values()[i];
				if(data.onBlockFaceClicked(face, event.getCurrentItem(), player))
					gui.setItem(slot, data.getIcon(face));
			}
		}

		return true;
	}


	@Override
	default boolean onPlayerInventoryInteract(Player player, D data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	default boolean onInventoryClose(Player player, D data, Inventory gui, InventoryCloseEvent event) {
		data.onClose();

		return false;
	}

	public abstract static class BlockFaceData {

		/**
		 * Returns the title of the gui.
		 * 
		 * @return the title
		 */
		protected abstract String getTitle();


		/**
		 * Returns the icon of the block face for the gui.
		 * 
		 * @param face the block face
		 * @return the item
		 */
		protected ItemStack getIcon(BlockUtils.BlockFaceDirection face) {
			int i = face.ordinal();
			return new ItemBuilder(BlockFaceGui.ICONS_ACTIVE[i]).setName("§" + COLORS_ACTIVE[i] + Utils.toCapitalWords(face)).build();
		}


		/**
		 * Is called when the item of the corresponding block face in the gui was clicked.
		 * 
		 * @param face   the clicked face
		 * @param stack  the clicked item
		 * @param player the player
		 * @return if the gui item should be updated
		 */
		protected boolean onBlockFaceClicked(BlockUtils.BlockFaceDirection face, ItemStack stack, Player player) {
			return false;
		}


		/**
		 * Is called when the gui is closed.
		 */
		protected void onClose() {}

	}

}
