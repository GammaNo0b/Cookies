
package me.gamma.cookies.object.block.machine;


import me.gamma.cookies.object.tile.machine.EnchantmentMachine;



public abstract class EnchantmentMachineBlock<B extends EnchantmentMachineBlock<B, T>, T extends EnchantmentMachine<?, T, B>> extends AbstractItemProcessingMachineBlock<B, T> {

	public EnchantmentMachineBlock(MachineTier tier) {
		super(tier);
	}

}
