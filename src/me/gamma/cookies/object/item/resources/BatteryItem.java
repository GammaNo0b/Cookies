
package me.gamma.cookies.object.item.resources;


import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.energy.Battery;
import me.gamma.cookies.object.item.AbstractBlockItem;



public class BatteryItem extends AbstractBlockItem<Battery> {

	private final String name;

	public BatteryItem(Battery block, String name) {
		super(block);
		this.name = name;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		Section section = builder.createSection("§3Battery Stats", false);
		section.add("  §7Connecting Wires: §9" + this.block.getMaximumWireCount(null));
		section.add("  §7Capacity: §b" + this.block.getEnergyCapacity() + " §cCC");
		int stored = this.block.getEnergyProviderFromHolder(holder).amount();
		if(stored > 0)
			section.add("  §7Stored: §b" + stored + " §cCC");
	}

}
