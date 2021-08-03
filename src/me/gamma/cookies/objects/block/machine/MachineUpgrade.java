
package me.gamma.cookies.objects.block.machine;


import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.item.tools.MachineUpgradeItem;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.setup.CustomItemSetup;



public class MachineUpgrade {

	private static final HashSet<MachineUpgrade> instances = new HashSet<>();

	public static final MachineUpgrade SPEED = new MachineUpgrade(CustomItemSetup.UPGRADE_SPEED, 8);
	public static final MachineUpgrade EFFICIENCY = new MachineUpgrade(CustomItemSetup.UPGRADE_EFFICIENCY, 8);
	public static final MachineUpgrade FORTUNE = new MachineUpgrade(CustomItemSetup.UPGRADE_FORTUNE, 8);

	private final ByteProperty property;
	private final MachineUpgradeItem item;
	private final int maxStackSize;

	public MachineUpgrade(MachineUpgradeItem item, int maxStackSize) {
		this.item = item;
		this.maxStackSize = maxStackSize;
		this.property = new ByteProperty("upgrade_" + this.getRegistryName());

		instances.add(this);
	}


	public String getRegistryName() {
		return this.item.getRegistryName();
	}


	public String getDisplayName() {
		return this.item.getDisplayName();
	}


	public MachineUpgradeItem getItem() {
		return this.item;
	}


	public int getMaxStackSize() {
		return this.maxStackSize;
	}


	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MachineUpgrade))
			return false;
		return this.getRegistryName().equals(((MachineUpgrade) obj).getRegistryName());
	}


	@Override
	public int hashCode() {
		return this.getRegistryName().hashCode();
	}


	public void storeEmpty(TileState block) {
		this.store(block, 0);
	}


	public void store(TileState block, int amount) {
		property.store(block, (byte) amount);
		block.update();
	}


	public int fetch(TileState block) {
		return property.fetch(block);
	}


	public static MachineUpgrade fromStack(ItemStack stack) {
		for(MachineUpgrade upgrade : instances)
			if(upgrade.getItem().isInstanceOf(stack))
				return upgrade;
		return null;
	}


	public static MachineUpgrade fromString(String string) {
		for(MachineUpgrade upgrade : instances)
			if(upgrade.getRegistryName().equals(string))
				return upgrade;
		return null;
	}


	public static MachineUpgrade[] upgrades(String... strings) {
		ArrayList<MachineUpgrade> list = new ArrayList<>();
		for(String string : strings) {
			string = "upgrade_".concat(string);
			MachineUpgrade upgrade = fromString(string);
			if(upgrade != null)
				list.add(upgrade);
		}
		return list.toArray(new MachineUpgrade[list.size()]);
	}

}
