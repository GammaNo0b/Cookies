
package me.gamma.cookies.object.item.resources;


import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.generator.AbstractGeneratorBlock;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.tile.generator.AbstractGenerator;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class GeneratorItem extends AbstractBlockItem<AbstractGeneratorBlock<?, ?>> {

	public static final String GENERATOR_STATS_TITLE = "§l§2Generator Stats";

	private String[] description = null;

	public GeneratorItem(AbstractGeneratorBlock<?, ?> block) {
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
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		Section section = builder.createSection(GENERATOR_STATS_TITLE, true);
		MachineTier tier = this.block.getTier();
		if(tier != null)
			section.add(tier.getDescription());
		section.add("  §7Max. Energy Generation: §b" + this.block.getMaximumEnergyGeneration() + " §cCC/t");
		section.add("  §7Capacity: §b" + this.block.getInternalCapacity() + " §cCC");
		section.add("  §7Speed: §b" + this.block.getBaseSpeed());
		Integer energy = data.getInteger(AbstractGenerator.KEY_ENERGY);
		if(energy != null)
			section.add("  §7Stored Energy: §b" + energy + " §cCC");
		RedstoneMode mode = PersistentDataUtils.getEnum(data, AbstractGenerator.KEY_REDSTONE_MODE, RedstoneMode.class);
		if(mode != null)
			section.add("  §7Redstone Mode: " + mode.getTitle());

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
