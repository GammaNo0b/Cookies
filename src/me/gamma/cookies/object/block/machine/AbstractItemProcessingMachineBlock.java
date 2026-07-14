
package me.gamma.cookies.object.block.machine;


import static me.gamma.cookies.object.item.ItemConsumer.KEY_ITEM_INPUT_ACCESS_FLAGS;
import static me.gamma.cookies.object.tile.machine.AbstractItemProcessingMachine.KEY_LOCK_INPUT;
import static me.gamma.cookies.object.tile.machine.AbstractItemProcessingMachine.KEY_USE_OUTPUT;

import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.object.tile.machine.AbstractItemProcessingMachine;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractItemProcessingMachineBlock<B extends AbstractItemProcessingMachineBlock<B, T>, T extends AbstractItemProcessingMachine<T, B>> extends AbstractItemGenerationMachineBlock<B, T> {

	public AbstractItemProcessingMachineBlock(MachineTier tier) {
		super(tier);
	}


	/**
	 * Returns the slot to store the input mode button.
	 * 
	 * @return the slot
	 */
	public int getInputModeSlot() {
		return MachineConstants.INPUT_MODE_SLOT;
	}


	/**
	 * Returns the slot to store the output mode button.
	 * 
	 * @return the slot
	 */
	public int getOutputModeSlot() {
		return MachineConstants.OUTPUT_MODE_SLOT;
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		itemData.setByte(KEY_ITEM_INPUT_ACCESS_FLAGS, tileData.getByte(KEY_ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3F));
		if(tileData.has(KEY_LOCK_INPUT, PersistentDataType.BOOLEAN))
			itemData.setBoolean(KEY_LOCK_INPUT, tileData.getBoolean(KEY_LOCK_INPUT, false));
		if(tileData.has(KEY_USE_OUTPUT, PersistentDataType.BOOLEAN))
			itemData.setBoolean(KEY_USE_OUTPUT, tileData.getBoolean(KEY_USE_OUTPUT, false));
	}

}
