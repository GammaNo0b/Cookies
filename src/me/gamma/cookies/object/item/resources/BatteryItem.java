
package me.gamma.cookies.object.item.resources;


import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.energy.BatteryBlock;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.tile.network.energy.Battery;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class BatteryItem extends AbstractBlockItem<BatteryBlock> {

	private final String name;

	public BatteryItem(BatteryBlock block, String name) {
		super(block);
		this.name = name;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		Section section = builder.createSection("§3Battery Stats", false);
		section.add("  §7Connecting Wires: §9" + this.block.getMaximumWireCount());
		section.add("  §7Capacity: §b" + this.block.getCapacity() + " §cCC");
		Integer stored = data.getInteger(Battery.KEY_ENERGY);
		if(stored != null)
			section.add("  §7Stored: §b" + stored + " §cCC");
	}

}
