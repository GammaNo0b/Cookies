
package me.gamma.cookies.object.block.machine;


import java.util.Iterator;
import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemUtils;



public abstract class AbstractItemGenerationMachine extends AbstractProcessingMachine implements ItemSupplier {

	public AbstractItemGenerationMachine(MachineTier tier) {
		super(tier);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		ItemStackProperty[] outputProperties = this.getItemOutputProperties();
		int[] outputSlots = this.getOutputSlots();
		for(int i = 0; i < outputProperties.length; i++)
			inventory.setItem(outputSlots[i], outputProperties[i].fetch(block));
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		ItemStackProperty[] outputProperties = this.getItemOutputProperties();
		int[] outputSlots = this.getOutputSlots();
		for(int i = 0; i < outputProperties.length; i++)
			outputProperties[i].store(block, inventory.getItem(outputSlots[i]));

		block.update();
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createItemOutputBlockFaceConfig());
	}


	/**
	 * Returns the item output slots.
	 * 
	 * @return the output slots
	 */
	protected int[] getOutputSlots() {
		return MachineConstants.getOutputSlots(this.tier);
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(PersistentDataHolder holder) {
		return holder instanceof TileState block ? ItemProvider.fromInventory(this.getGui(block), this.getOutputSlots()) : List.of();
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.tryPushItems(block);
	}


	/**
	 * Tries to store all items from the given list in the output slot of the given block. Items that could not be stored remain in the list. Returns if
	 * all items were stored.
	 * 
	 * @param block the block
	 * @param items the items to be stored
	 * @return if the outputs could be stored
	 */
	protected boolean storeOutputs(TileState block, List<ItemStack> items) {
		List<Provider<ItemStack>> outputs = this.getItemOutputs(block);
		Iterator<ItemStack> iterator = items.iterator();
		while(iterator.hasNext()) {
			ItemStack stack = iterator.next();
			if(ItemUtils.isEmpty(stack)) {
				iterator.remove();
				continue;
			}

			stack.setAmount(ItemConsumer.addStack(stack, stack.getAmount(), outputs).getAmount());
			if(ItemUtils.isEmpty(stack))
				iterator.remove();
		}

		return items.isEmpty();
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, block, gui, event);

		return !ArrayUtils.contains(this.getOutputSlots(), event.getSlot());
	}


	/**
	 * Returns an array of item stack properties to store the outputs of a machine.
	 * 
	 * @return the array
	 */
	private ItemStackProperty[] getItemOutputProperties() {
		int[] outputSlots = this.getOutputSlots();
		return ArrayUtils.generate(outputSlots.length, i -> new ItemStackProperty("out" + outputSlots[i]), ItemStackProperty[]::new);
	}

}
