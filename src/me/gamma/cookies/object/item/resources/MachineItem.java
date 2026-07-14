
package me.gamma.cookies.object.item.resources;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.machine.AbstractMachineBlock;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.tile.generator.AbstractGenerator;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class MachineItem extends AbstractBlockItem<AbstractMachineBlock<?, ?>> {

	public static final String MACHINE_STATS_TITLE = "§l§2Machine Stats";

	private String[] description = null;

	public MachineItem(AbstractMachineBlock<?, ?> block) {
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
		Section section = builder.createSection(MACHINE_STATS_TITLE, true);
		MachineTier tier = this.block.getTier();
		if(tier != null)
			section.add(tier.getDescription());
		section.add("  §7Energy Consumption: §b" + this.block.getEnergyConsumption() + " §cCC/t");
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


	public MachineItem setDescription(String... description) {
		this.description = description;
		return this;
	}


	public static void upgradeMachine(ItemStack result, ItemStack... ingredients) {
		CustomItemData resultData = getCustomData(result);
		if(resultData == null)
			return;

		int energy = 0;
		for(ItemStack ingredient : ingredients) {
			CustomItemData ingredientData = getCustomData(ingredient);
			if(ingredientData == null)
				continue;

			energy += ingredientData.getData().getInteger(AbstractGenerator.KEY_ENERGY, 0);
		}

		resultData.getData().setInteger(AbstractGenerator.KEY_ENERGY, energy);
		resultData.save();
	}

}
