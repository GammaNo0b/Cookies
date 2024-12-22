
package me.gamma.cookies.object.block;


import me.gamma.cookies.object.block.machine.MachineTier;



public class MachineCasing extends AbstractCustomBlock {

	private final MachineTier tier;
	private final String texture;

	public MachineCasing(MachineTier tier, String texture) {
		this.tier = tier;
		this.texture = texture;
	}


	public MachineTier getTier() {
		return this.tier;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public String getIdentifier() {
		return "machine_casing_" + this.tier.name().toLowerCase();
	}

}
