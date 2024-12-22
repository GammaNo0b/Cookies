
package me.gamma.cookies.object.item;


import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.generator.AbstractGenerator;
import me.gamma.cookies.object.block.machine.MachineTier;



public class GeneratorItem extends AbstractBlockItem<AbstractGenerator> {

	public static final String GENERATOR_STATS_TITLE = "§l§2Generator Stats";

	private String[] description = null;

	public GeneratorItem(AbstractGenerator block) {
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
		Section section = builder.createSection(GENERATOR_STATS_TITLE, true);
		MachineTier tier = this.block.getTier();
		if(tier != null)
			section.add(tier.getDescription());
		section.add("  §7Max. Energy Generation: §b" + this.block.getMaximumEnergyGeneration() + " §cCC/t");
		section.add("  §7Capacity: §b" + this.block.getInternalCapacity() + " §cCC");
		section.add("  §7Stored Energy: §b" + AbstractGenerator.INTERNAL_STORAGE.fetchEmpty(holder) + " §cCC");
		section.add("  §7Redstone Mode: " + AbstractGenerator.REDSTONE_MODE.fetchEmpty(holder).getTitle());

		if(this.description != null) {
			section = builder.createSection(null, true);
			for(String line : this.description)
				section.add(line);
		}
	}


	public GeneratorItem setDescription(String... description) {
		this.description = description;
		return this;
	}

}
