
package me.gamma.cookies.object.gui;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.Holder;



public class BlockFaceConfig implements BlockFaceGui<BlockFaceConfig.Config> {

	private static final int autoslot = 2;

	private static final ItemStack AUTOMATED = new ItemBuilder(Material.REDSTONE).setName("§cAutomated").build();
	private static final ItemStack MANUAL = new ItemBuilder(Material.GUNPOWDER).setName("§7Manual").build();

	public String getIdentifier() {
		return "blockfaceconfig";
	}


	@Override
	public Inventory createGui(Config data) {
		Inventory gui = BlockFaceGui.super.createGui(data);
		if(data.automated)
			gui.setItem(autoslot, data.getAutomationIcon());
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Config data, Inventory gui, InventoryClickEvent event) {
		BlockFaceGui.super.onMainInventoryInteract(player, data, gui, event);

		if(data.automated && event.getSlot() == autoslot) {
			data.toggleAutomation();
			gui.setItem(autoslot, data.getAutomationIcon());
		}

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, Config data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	public static void openBlockFaceConfig(HumanEntity player, Config config) {
		Inventories.BLOCK_FACE_CONFIG.openGui(player, config);
	}

	public static class Config extends BlockFaceGui.BlockFaceData {

		private final String title;
		private final Holder<Byte> flags;
		private boolean automated;
		private final Material icon;

		public Config(String title, Holder<Byte> flags, boolean automated, Material icon) {
			this.title = title;
			this.flags = flags;
			this.automated = automated;
			this.icon = icon;
		}


		protected ItemStack createIcon() {
			return new ItemBuilder(this.icon).setName(this.title).build();
		}


		protected ItemStack getAutomationIcon() {
			return ((this.flags.get() >>> 6) & 1) == 0 ? MANUAL : AUTOMATED;
		}


		protected void toggleAutomation() {
			byte bits = this.flags.get();
			bits ^= 0x40;
			this.flags.set(bits);
		}


		@Override
		protected String getTitle() {
			return this.title;
		}


		@Override
		protected ItemStack getIcon(BlockUtils.BlockFaceDirection face) {
			int i = face.ordinal();
			int bit = this.flags.get() >>> i & 1;
			return new ItemBuilder(bit == 0 ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE).setName("§6" + Utils.toCapitalWords(face)).build();
		}


		@Override
		protected boolean onBlockFaceClicked(BlockUtils.BlockFaceDirection face, ItemStack stack, Player player) {
			int i = face.ordinal();
			byte bits = this.flags.get();
			bits ^= (1 << i);
			this.flags.set(bits);

			return true;
		}


		@Override
		protected void onClose() {
			super.onClose();
		}

	}

}
