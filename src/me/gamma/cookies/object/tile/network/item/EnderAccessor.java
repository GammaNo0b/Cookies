
package me.gamma.cookies.object.tile.network.item;


import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.network.item.EnderAccessorBlock;
import me.gamma.cookies.object.gui.ItemInventoryHolder;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class EnderAccessor extends AbstractCustomTileEntity<EnderAccessor, EnderAccessorBlock> implements ItemStorage, Ownable, ItemInventoryHolder {

	private static final String KEY_OWNER = "owner";

	private UUID owner = null;

	public EnderAccessor(EnderAccessorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.owner = PersistentDataUtils.getUUID(data, KEY_OWNER);
		if(this.owner == null)
			return false;

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setUUID(data, KEY_OWNER, this.owner);

		return true;
	}


	@Override
	public boolean canAccessItemInputs(BlockFace face) {
		return true;
	}


	@Override
	public byte getItemInputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemInputAccessFlags(byte flags) {}


	@Override
	public boolean canAccessItemOutputs(BlockFace face) {
		return true;
	}


	@Override
	public byte getItemOutputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {}


	@Override
	public UUID getOwner() {
		return this.owner;
	}


	@Override
	public void setOwner(UUID uuid) {
		this.owner = uuid;
	}


	@Override
	public List<Provider<ItemStack>> getItemProviders() {
		Inventory inventory = this.getInventory();
		return inventory == null ? List.of() : ItemProvider.fromInventory(inventory);
	}


	@Override
	public EnderAccessor castTileEntity() {
		return this;
	}


	@Override
	public Inventory getInventory() {
		Player player = this.getOwningPlayer();
		return player == null ? null : player.getEnderChest();
	}


	@Override
	public int[] getInputSlots() {
		return null;
	}


	@Override
	public int[] getOutputSlots() {
		return null;
	}

}
