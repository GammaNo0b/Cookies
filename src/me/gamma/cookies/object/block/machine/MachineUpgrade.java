
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.IntToDoubleFunction;

import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.math.MathHelper;



public class MachineUpgrade {

	private static final HashSet<MachineUpgrade> instances = new HashSet<>();

	public static final MachineUpgrade SPEED = new MachineUpgrade(Items.UPGRADE_SPEED, 8, x -> x * 0.5D);
	public static final MachineUpgrade EFFICIENCY = new MachineUpgrade(Items.UPGRADE_EFFICIENCY, 8, x -> 1.0 - MathHelper.intpow(0.75D, x));
	public static final MachineUpgrade LUCK = new MachineUpgrade(Items.UPGRADE_LUCK, 8, x -> 1 + x * 0.25D);
	public static final MachineUpgrade ENERGY_STORAGE = new MachineUpgrade(Items.UPGRADE_ENERGY_STORAGE, 4, x -> x);
	public static final MachineUpgrade RANGE = new MachineUpgrade(Items.UPGRADE_RANGE, 4, x -> x);

	private final AbstractCustomItem item;
	private final int maxStackSize;
	private final IntToDoubleFunction function;

	public MachineUpgrade(AbstractCustomItem item, int maxStackSize, IntToDoubleFunction function) {
		this.item = item;
		this.maxStackSize = maxStackSize;
		this.function = function;

		instances.add(this);
	}


	public String getRegistryName() {
		return this.item.getIdentifier();
	}


	public String getDisplayName() {
		return this.item.getTitle();
	}


	public AbstractCustomItem getItem() {
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


	public double getValue(int level) {
		return this.function.applyAsDouble(level);
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


	public static int getNumberOfMachineUpgrades() {
		return instances.size();
	}

}
