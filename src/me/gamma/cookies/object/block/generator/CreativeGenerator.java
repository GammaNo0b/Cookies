
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;

import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.list.HeadTextures;



public class CreativeGenerator extends AbstractGenerator {

	public CreativeGenerator() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {}


	@Override
	public String getGeneratorRegistryName() {
		return "creative_generator";
	}


	@Override
	public String getTitle() {
		return "§dCreative Generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CREATIVE_GENERATOR;
	}


	@Override
	public int getUpgradeSlots(TileState block) {
		return 0;
	}


	@Override
	public int getMaximumEnergyGeneration() {
		return Integer.MAX_VALUE;
	}


	@Override
	public int getInternalCapacity() {
		return Integer.MAX_VALUE;
	}


	@Override
	protected boolean fullfillsGeneratingConditions(TileState block) {
		return true;
	}


	@Override
	public EnergyProvider getInternalStorage(TileState block) {
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

}
