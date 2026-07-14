
package me.gamma.cookies.object.block.network.item;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.UpdatingGuiProvider;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.object.tile.network.item.ItemStorageCrate;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemStorageCrateBlock extends AbstractCustomTileBlock<ItemStorageCrateBlock, ItemStorageCrate> implements UpdatingGuiProvider {

	private final String identifier;
	private final String texture;
	private final int capacity;
	private final int maxStackSize;

	public ItemStorageCrateBlock(String identifier, String texture, int capacity, int maxStackSize) {
		this.identifier = identifier;
		this.texture = texture;
		this.capacity = capacity;
		this.maxStackSize = maxStackSize;
	}


	public int getCapacity() {
		return this.capacity;
	}


	public int getMaxStackSize() {
		return this.maxStackSize;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		itemData.setObjectList(ItemStorageCrate.KEY_CONTENTS, tileData.getObjectList(ItemStorageCrate.KEY_CONTENTS));
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		this.openGui(player, block);

		return true;
	}


	@Override
	public String getTitle(Block data) {
		return "Storage Crate Block";
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Inventory createGui(Block data) {
		Inventory gui = UpdatingGuiProvider.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 45, filler);
		}
		for(int i = 1; i <= 4; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		for(int i = 1; i <= 4; i++)
			for(int j = 1; j <= 7; j++)
				gui.setItem(i * 9 + j, ItemStorageCrate.EMPTY_SLOT);
		gui.setItem(47, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8<---").build());
		gui.setItem(51, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8--->").build());
		gui.setItem(4, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8Close").build());
		return gui;
	}


	@Override
	public ItemStorageCrateBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemStorageCrate createNewTileEntity(Block block) {
		return new ItemStorageCrate(this, block);
	}


	@Override
	public ContainerTile getContainer(Block block) {
		return this.getTileEntity(block);
	}

}
