
package me.gamma.cookies.object.block.network.energy;


public class Battery extends EnergyWireConnector {

	private final String identifier;
	private final String texture;

	public Battery(String identifier, String texture, int maximumWireCount, int capacity, int transferRate) {
		super(maximumWireCount, capacity, transferRate);
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

}
