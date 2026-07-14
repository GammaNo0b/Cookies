
package me.gamma.cookies.object.tile.generator;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.generator.CreativeGeneratorBlock;
import me.gamma.cookies.object.energy.EnergyProvider;



public class CreativeGenerator extends AbstractGenerator<CreativeGenerator, CreativeGeneratorBlock> {

	public CreativeGenerator(CreativeGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected boolean fullfillsGeneratingConditions() {
		return true;
	}


	@Override
	public CreativeGenerator castTileEntity() {
		return this;
	}


	@Override
	public EnergyProvider getEnergyOutput() {
		return new EnergyProvider() {

			@Override
			public void remove(int amount) {}


			@Override
			public int capacity() {
				return Integer.MAX_VALUE;
			}


			@Override
			public int amount() {
				return Integer.MAX_VALUE;
			}


			@Override
			public void add(Void type, int amount) {}


			@Override
			public int set(Void type, int amount) {
				return amount;
			}


			@Override
			public int get(int max) {
				return max;
			}


			@Override
			public boolean check(int amount) {
				return true;
			}


			@Override
			public boolean isEmpty() {
				return false;
			}


			@Override
			public boolean isFull() {
				return true;
			}

		};
	}


	@Override
	public int getUpgradeSlots() {
		return 0;
	}

}
