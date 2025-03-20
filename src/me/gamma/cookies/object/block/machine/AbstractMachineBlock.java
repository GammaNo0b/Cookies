
package me.gamma.cookies.object.block.machine;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.property.DoubleProperty;
import me.gamma.cookies.object.property.EnergyProperty;
import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.object.tile.machine.AbstractMachine;
import me.gamma.cookies.util.ItemUtils;



public abstract class AbstractMachineBlock extends AbstractCustomTileBlock {

	protected final MachineTier tier;

	protected int energyConsumption;
	protected int internalCapacity;
	protected int upgradeSlots;
	protected double baseSpeed;

	public AbstractMachineBlock(MachineTier tier) {
		this.tier = tier;
	}


	@Override
	public ConfigurationSection getConfig() {
		ConfigurationSection section = Config.MACHINES.getConfig().getConfigurationSection(this.getMachineRegistryName());
		return this.tier == null ? section : section.getConfigurationSection(this.tier.name().toLowerCase());
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.energyConsumption = config.getInt("energyConsumption", 1);
		this.internalCapacity = config.getInt("internalCapacity", 1000);
		this.upgradeSlots = config.getInt("upgradeSlots", 0);
		this.baseSpeed = config.getDouble("baseSpeed", 1.0D);
	}


	/**
	 * Returns the display name for this machine.
	 * 
	 * @return the display name
	 */
	public abstract String getTitle();


	/**
	 * Returns the tier of the machine or null if it has no tier.
	 * 
	 * @return the tier or null
	 */
	public final MachineTier getTier() {
		return this.tier;
	}


	/**
	 * Amount of energy that get's consumed per operation.
	 * 
	 * @return the energy
	 */
	public int getEnergyConsumption() {
		return this.energyConsumption;
	}


	/**
	 * Amount of energy that can be stored in the internal storage.
	 * 
	 * @return the capacity
	 */
	public int getInternalCapacity() {
		return this.internalCapacity;
	}


	/**
	 * Returns the number of upgrade slots.
	 * 
	 * @return the upgrade slots
	 */
	public int getUpgradeSlots() {
		return this.upgradeSlots;
	}

	/**
	 * Returns the base speed.
	 * 
	 * @return the base speed
	 */
	public double getBaseSpeed() {
		return this.baseSpeed;
	}



	/**
	 * Returns the registry name for this machine.
	 * 
	 * @return the machine registry name
	 */
	public abstract String getMachineRegistryName();


	@Override
	public String getIdentifier() {
		StringBuilder builder = new StringBuilder(this.getMachineRegistryName());
		if(this.getTier() != null)
			builder.append("_tier_").append(this.getTier().name().toLowerCase());
		return builder.toString();
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		AbstractMachine<?> machine = this.getTileEntity(block.getBlock());
		if(machine == null)
			return false;

		for(MachineUpgrade upgrade : machine.getAllowedUpgrades()) {
			ItemStack item = upgrade.getItem().get();
			item.setAmount(upgrade.fetch(block));
			ItemUtils.dropItem(item, block);
		}

		Wire<Void> wire;
		while((wire = machine.removeWire()) != null)
			ItemUtils.dropItem(wire.getWireItem().get(), block);

		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(super.onBlockRightClick(player, block, stack, event))
			return true;

		if(!player.isSneaking())
			return false;
		
		AbstractMachine<?> machine = this.getTileEntity(block.getBlock());
		if(machine == null)
			return false;

		if(machine.canAccess(player)) {
			if(ItemUtils.isType(stack, Material.REDSTONE_TORCH)) {
				machine.toggleRedstoneMode(1);
				player.sendMessage("§cRedstone mode set to " + machine.getRedstoneMode().getTitle());
				return true;
			} else if(ItemUtils.isType(stack, Material.DIAMOND) && machine.getUpgradeSlots() > 0) {
				MachineUpgradeGui.open(player, block, machine);
				return true;
			}
		}
		return false;
	}

}
