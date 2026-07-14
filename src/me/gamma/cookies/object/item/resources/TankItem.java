
package me.gamma.cookies.object.item.resources;


import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.fluid.TankBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.tile.network.fluid.Tank;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TankItem extends AbstractBlockItem<TankBlock> {

	public TankItem(TankBlock block) {
		super(block);
	}


	@Override
	public String getTitle() {
		return this.block.getTitle();
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		Fluid fluid = PersistentDataUtils.getFluid(data, Tank.KEY_FLUID);
		Section section = builder.createSection("§5Tank Stats", true);
		section.add("  §7Capacity: §d" + this.block.getCapacity());
		if(fluid != null) {
			section.add("  §7Stored: " + fluid.getType().getName());
			section.add("  §7Amount: §8(§b" + fluid.getMillibuckets() + " §3mb§8)");
		}
	}

}
