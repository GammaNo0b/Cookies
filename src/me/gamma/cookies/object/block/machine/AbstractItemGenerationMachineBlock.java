
package me.gamma.cookies.object.block.machine;


import static me.gamma.cookies.object.tile.machine.AbstractItemGenerationMachine.KEY_ITEM_OUTPUT_ACCESS_FLAGS;

import me.gamma.cookies.object.tile.machine.AbstractItemGenerationMachine;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractItemGenerationMachineBlock<B extends AbstractItemGenerationMachineBlock<B, T>, T extends AbstractItemGenerationMachine<T, B>> extends AbstractProcessingMachineBlock<B, T> {

	public AbstractItemGenerationMachineBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		itemData.setByte(KEY_ITEM_OUTPUT_ACCESS_FLAGS, tileData.getByte(KEY_ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F));
	}

}
