
package me.gamma.cookies.object.block.network.energy;


import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.energy.EnergyProvider;



public class Battery extends EnergyWireConnector {

	private final String identifier;
	private final String texture;

	public Battery(String identifier, String texture, int maximumWireCount, int capacity) {
		super(maximumWireCount, capacity);
		this.identifier = identifier;
		this.texture = texture;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	public EnergyProvider getEnergyProviderFromHolder(PersistentDataHolder block) {
		return EnergyProvider.fromProperty(INTERNAL_STORAGE, block, this.getEnergyCapacity());
	}

}
