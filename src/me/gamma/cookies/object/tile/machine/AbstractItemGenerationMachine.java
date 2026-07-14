
package me.gamma.cookies.object.tile.machine;


import java.util.Iterator;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.AbstractItemGenerationMachineBlock;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractItemGenerationMachine<T extends AbstractItemGenerationMachine<T, B>, B extends AbstractItemGenerationMachineBlock<B, T>> extends AbstractProcessingMachine<T, B> implements ItemSupplier {

	private byte itemOutputAccessFlags = 0x3F;

	public AbstractItemGenerationMachine(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		ItemSupplier.super.load(chunk, data);

		Inventory inventory = this.getInventory();
		int[] outputSlots = this.getOutputSlots();
		for(int i = 0; i < outputSlots.length; i++)
			inventory.setItem(outputSlots[i], PersistentDataUtils.getItemStack(data, "itemoutput" + i));

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		ItemSupplier.super.save(chunk, data);

		Inventory inventory = this.getInventory();
		int[] outputSlots = this.getOutputSlots();
		for(int i = 0; i < outputSlots.length; i++)
			PersistentDataUtils.setItemStack(data, "itemoutput" + i, inventory.getItem(outputSlots[i]));

		return true;
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {
		configs.add(this.createItemOutputBlockFaceConfig());
		super.listBlockFaceProperties(configs);
	}


	@Override
	public byte getItemOutputAccessFlags() {
		return this.itemOutputAccessFlags;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {
		this.itemOutputAccessFlags = flags;
	}


	/**
	 * Returns the item output slots.
	 * 
	 * @return the output slots
	 */
	protected int[] getOutputSlots() {
		return MachineConstants.getOutputSlots(this.customBlock.getTier());
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs() {
		return ItemProvider.fromInventory(this.getInventory(), this.getOutputSlots());
	}


	@Override
	public void destroy() {
		super.destroy();

		for(Provider<ItemStack> provider : this.getItemOutputs())
			ItemUtils.dropItem(ItemProvider.get(provider), this.block);
	}


	@Override
	public void tick() {
		this.tryPushItems();
		super.tick();
	}


	/**
	 * Tries to store the given item in the output slot.
	 * 
	 * @param item the item to be stored
	 * @return the remaining item
	 */
	protected ItemStack storeOutput(ItemStack item) {
		if(ItemUtils.isEmpty(item))
			return null;

		return ItemConsumer.addStack(item, item.getAmount(), this.getItemOutputs());
	}


	/**
	 * Tries to store all items from the given list in the output slot. Items that could not be stored remain in the list. Returns if all items were
	 * stored.
	 * 
	 * @param items the items to be stored
	 * @return if the outputs could be stored
	 */
	protected boolean storeOutputs(List<ItemStack> items) {
		Iterator<ItemStack> iterator = items.iterator();
		while(iterator.hasNext()) {
			ItemStack stack = iterator.next();
			if(ItemUtils.isEmpty(stack)) {
				iterator.remove();
				continue;
			}

			ItemStack rest = this.storeOutput(stack);
			if(ItemUtils.isEmpty(rest))
				iterator.remove();
			else
				stack.setAmount(rest.getAmount());
		}

		return items.isEmpty();
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, gui, event);

		return !ArrayUtils.contains(this.getOutputSlots(), event.getSlot());
	}

}
