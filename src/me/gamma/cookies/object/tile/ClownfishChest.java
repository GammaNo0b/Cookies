
package me.gamma.cookies.object.tile;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.ClownfishChestBlock;
import me.gamma.cookies.object.gui.ItemInventoryHolder;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ClownfishChest extends AbstractCustomTileEntity<ClownfishChest, ClownfishChestBlock> implements BlockInventoryHolder, ItemStorage, ItemInventoryHolder {

	public static final String KEY_INVENTORY = "inventory";

	public static final String TITLE = ColorUtils.color("Clownfish Chest", new char[] { '6', 'f' }, 1);

	private Inventory inventory;

	public ClownfishChest(ClownfishChestBlock customBlock, Block block) {
		super(customBlock, block);

		this.inventory = Bukkit.createInventory(this, 54, TITLE);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		Pair<Inventory, String> pair = PersistentDataUtils.getInventory(data, KEY_INVENTORY);
		if(pair != null && pair.left != null) {
			this.inventory = pair.left;
		} else {
			this.inventory = Bukkit.createInventory(this, 54, TITLE);
		}

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setInventory(data, KEY_INVENTORY, this.inventory, TITLE);

		return true;
	}


	@Override
	public byte getItemInputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemInputAccessFlags(byte flags) {}


	@Override
	public byte getItemOutputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {}


	@Override
	public List<Provider<ItemStack>> getItemProviders() {
		return ItemProvider.fromInventory(this.inventory);
	}


	@Override
	public Inventory getInventory() {
		return this.inventory;
	}


	@Override
	public int[] getInputSlots() {
		return null;
	}


	@Override
	public int[] getOutputSlots() {
		return null;
	}


	@Override
	public ClownfishChest castTileEntity() {
		return this;
	}

}
