
package me.gamma.cookies.object.block.machine;


public enum MachineTier {

	BASIC("§fBasic", 'f'),
	ADVANCED("§eAdvanced", 'e'),
	IMPROVED("§bImproved", 'b'),
	PERFECTED("§dPerfected", 'd');

	private final String name;
	private final char colorcode;

	private MachineTier(String name, char colorcode) {
		this.name = name;
		this.colorcode = colorcode;
	}


	public String getName() {
		return this.name;
	}
	
	public char getColorcode() {
		return this.colorcode;
	}


	public String getDescription() {
		return String.format("§9Tier §1(§3%d§1):   %s", this.getTier(), this.name);
	}


	public int getTier() {
		return this.ordinal() + 1;
	}


	public MachineTier increase() {
		return byTier(this.getTier() + 1);
	}


	public MachineTier decrease() {
		return byTier(this.getTier() + values().length - 1);
	}


	public static MachineTier byTier(int tier) {
		return values()[(tier - 1) % values().length];
	}

}
