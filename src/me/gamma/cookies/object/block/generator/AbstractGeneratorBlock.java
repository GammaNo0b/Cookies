
package me.gamma.cookies.object.block.generator;


import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.tile.generator.AbstractGenerator;
import me.gamma.cookies.util.ItemUtils;



public abstract class AbstractGeneratorBlock extends AbstractCustomTileBlock {

	protected final MachineTier tier;

	protected int energyGeneration;
	protected int internalCapacity;
	protected int upgradeSlots;
	protected double baseSpeed;

	public AbstractGeneratorBlock(MachineTier tier) {
		this.tier = tier;
	}


	@Override
	public ConfigurationSection getConfig() {
		ConfigurationSection section = Config.GENERATORS.getConfig().getConfigurationSection(this.getGeneratorRegistryName());
		return this.tier == null ? section : section.getConfigurationSection(this.tier.name().toLowerCase());
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.energyGeneration = config.getInt("energyGeneration", 1);
		this.internalCapacity = config.getInt("internalCapacity", 1000);
		this.upgradeSlots = config.getInt("upgradeSlots", 0);
		this.baseSpeed = config.getDouble("baseSpeed", 1.0D);
	}


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
	 * Maximum amount of energy this generator can generate without any upgrades.
	 * 
	 * @return the amount of energy
	 */
	public int getMaximumEnergyGeneration() {
		return this.energyGeneration;
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
	 * Returns the registry name for this generator.
	 * 
	 * @return the generator registry name
	 */
	protected abstract String getGeneratorRegistryName();


	@Override
	public String getIdentifier() {
		StringBuilder builder = new StringBuilder(this.getGeneratorRegistryName());
		if(this.getTier() != null)
			builder.append("_tier_").append(this.getTier().name().toLowerCase());
		return builder.toString();
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		AbstractGenerator<?> generator = this.getTileEntity(block.getBlock());
		if(generator == null)
			return false;

		for(MachineUpgrade upgrade : generator.getAllowedUpgrades()) {
			ItemStack item = upgrade.getItem().get();
			item.setAmount(upgrade.fetch(block));
			ItemUtils.dropItem(item, block);
		}

		Wire<Void> wire;
		while((wire = generator.removeWire()) != null)
			ItemUtils.dropItem(wire.getWireItem().get(), block);

		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(super.onBlockRightClick(player, block, stack, event))
			return true;

		if(!player.isSneaking())
			return false;

		AbstractGenerator<?> generator = this.getTileEntity(block.getBlock());
		if(generator == null)
			return false;

		if(generator.canAccess(player)) {
			if(ItemUtils.isType(stack, Material.REDSTONE_TORCH)) {
				generator.toggleRedstoneMode(1);
				player.sendMessage("§cRedstone mode set to " + generator.getRedstoneMode().getTitle());
				return true;
			} else if(ItemUtils.isType(stack, Material.DIAMOND)) {
				MachineUpgradeGui.open(player, block, generator);
				return true;
			}
		}
		return false;
	}

}
