
package me.gamma.cookies.object.item.resources;


import static me.gamma.cookies.object.block.network.fluid.Tank.FLUID;

import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.fluid.Tank;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.item.AbstractBlockItem;



public class TankItem extends AbstractBlockItem<Tank> {

	public TankItem(Tank block) {
		super(block);
	}


	@Override
	public String getTitle() {
		return this.block.getTitle(null);
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		Fluid fluid = FLUID.fetch(holder);
		FluidType type = fluid.getType();
		Section section = builder.createSection("§5Tank Stats", true);
		section.add("  §7Capacity: §d" + this.block.getCapacity());
		section.add("  §7Stored: " + (type == null ? "§3None" : type.getName()));
		section.add("  §7Amount: §8(§b" + fluid.getMillibuckets() + " §3mb§8)");
	}

}
