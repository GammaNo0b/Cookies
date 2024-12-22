
package me.gamma.cookies.object.item;


import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.block.machine.MachineTier;



public class MachineItem extends AbstractBlockItem<AbstractMachine> {

	public static final String MACHINE_STATS_TITLE = "§l§2Machine Stats";

	private String[] description = null;

	public MachineItem(AbstractMachine block) {
		super(block);
	}


	@Override
	public String getTitle() {
		return this.block.getTitle();
	}


	@Override
	public Material getMaterial() {
		return this.block.getMaterial();
	}


	@Override
	protected String getBlockTexture() {
		return this.block.getBlockTexture();
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		Section section = builder.createSection(MACHINE_STATS_TITLE, true);
		MachineTier tier = this.block.getTier();
		if(tier != null)
			section.add(tier.getDescription());
		section.add("  §7Energy Consumption: §b" + this.block.getEnergyConsumption() + " §cCC/t");
		section.add("  §7Capacity: §b" + this.block.getInternalCapacity() + " §cCC");
		section.add("  §7Speed: §b" + this.block.getBaseSpeed());
		section.add("  §7Stored Energy: §b" + AbstractMachine.INTERNAL_STORAGE.fetchEmpty(holder) + " §cCC");
		section.add("  §7Redstone Mode: " + AbstractMachine.REDSTONE_MODE.fetchEmpty(holder).getTitle());

		if(this.description != null) {
			section = builder.createSection(null, true);
			for(String line : this.description)
				section.add(line);
		}
	}


	public MachineItem setDescription(String... description) {
		this.description = description;
		return this;
	}

}
