
package me.gamma.cookies.object.tile.network.energy;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergyStorage;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.util.collection.Holder;



public class Tesseract extends EnderLinkedTileEntity<Holder<Integer>> implements EnergyStorage {

	private static final int CAPACITY = 100000;

	public Tesseract(EnderLinkedBlock<Holder<Integer>> customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public EnergyProvider getEnergyProvider() {
		return EnergyProvider.fromHolder(this.getResource(), CAPACITY);
	}

}
