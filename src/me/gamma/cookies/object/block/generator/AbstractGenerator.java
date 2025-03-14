
package me.gamma.cookies.object.block.generator;


import java.util.ArrayList;
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
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.AbstractWorkBlock;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.block.Upgradeable;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.block.network.WireComponentType;
import me.gamma.cookies.object.block.network.WireRelay;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergySupplier;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.GeneratorItem;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.property.DoubleProperty;
import me.gamma.cookies.object.property.EnergyProperty;
import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractGenerator extends AbstractWorkBlock implements Upgradeable, Ownable, Switchable, EnergySupplier, WireRelay<Void> {

	public static final VectorProperty WIRE_POS = Properties.WIRE_POS;
	public static final StringProperty WIRE_ITEM = Properties.WIRE_ITEM;

	public static final EnergyProperty INTERNAL_STORAGE = Properties.INTERNAL_STORAGE;
	public static final EnumProperty<RedstoneMode> REDSTONE_MODE = Properties.REDSTONE_MODE;
	public static final DoubleProperty REST_SPEED = Properties.REST_SPEED;

	protected final Set<Location> locations = new HashSet<>();
	protected final HashMap<Location, Wire<Void>> wires = new HashMap<>();
	protected final MachineTier tier;

	protected int energyGeneration;
	protected int internalCapacity;
	protected int upgradeSlots;
	protected double baseSpeed;

	public AbstractGenerator(MachineTier tier) {
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


	@Override
	public boolean load(TileState block, PersistentDataObject data) {
		if(WIRE_POS.isPropertyOf(data.getContainer())) {
			Location pos = WIRE_POS.fetch(data.getContainer()).toLocation(block.getWorld());
			String identifier = WIRE_ITEM.fetch(data.getContainer());
			AbstractCustomItem item = Items.getCustomItemFromIdentifier(identifier);
			if(item instanceof WireItem wire)
				this.createWire(block, pos, wire);
		}

		return true;
	}


	@Override
	public boolean save(TileState block, PersistentDataObject data) {
		Location pos = block.getLocation();
		Wire<Void> wire = this.wires.get(pos);
		if(wire != null) {
			Location opposite = wire.getOpposite(pos);
			if(opposite != null) {
				WIRE_POS.store(data.getContainer(), opposite.toVector());
				WIRE_ITEM.store(data.getContainer(), wire.getWireItem().getIdentifier());
			}
			wire.destroy();
		}

		return true;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public EnergyProvider getWireProvider(TileState block) {
		return this.getEnergyOutput(block);
	}


	@Override
	public WireComponentType getWireComponentType() {
		return WireComponentType.SUPPLIER;
	}


	@Override
	public Wire<Void> getConnectedWire(TileState block) {
		return this.wires.get(block.getLocation());
	}


	@Override
	public void setConnectedWire(TileState block, Wire<Void> wire) {
		if(wire == null) {
			this.wires.remove(block.getLocation());
		} else {
			this.wires.put(block.getLocation(), wire);
		}
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
	 * Amount of energy that get's generated by the given block per operation.
	 * 
	 * @param block the block
	 * @return the amount of energy
	 */
	public int getEnergyGeneration(TileState block) {
		return this.getMaximumEnergyGeneration();
	}


	/**
	 * Amount of energy that can be stored in the internal storage.
	 * 
	 * @return the capacity
	 */
	public int getInternalCapacity() {
		return this.internalCapacity;
	}


	public int getInteralCapacity(PersistentDataHolder holder) {
		return this.getInternalCapacity() * (int) (1 + this.getUpgradeValue(holder, MachineUpgrade.ENERGY_STORAGE));
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
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(INTERNAL_STORAGE).add(REDSTONE_MODE, RedstoneMode.ALWAYS_ON).add(ENERGY_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(REST_SPEED);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		for(MachineUpgrade upgrade : this.getAllowedUpgrades()) {
			ItemStack item = upgrade.getItem().get();
			item.setAmount(upgrade.fetch(block));
			ItemUtils.dropItem(item, block);
		}

		Wire<Void> wire;
		while((wire = this.removeWire(block)) != null)
			ItemUtils.dropItem(wire.getWireItem().get(), block);

		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(this.canAccess(block, player) && !player.isSneaking()) {
			if(ItemUtils.isType(stack, Material.REDSTONE_TORCH)) {
				this.toggleRedstoneMode(block, 1);
				player.sendMessage("§cRedstone mode set to " + REDSTONE_MODE.fetch(block).getTitle());
				return true;
			} else if(ItemUtils.isType(stack, Material.DIAMOND)) {
				MachineUpgradeGui.open(player, block, this);
				return true;
			}
		}
		return false;
	}


	public EnergyProvider getInternalStorage(PersistentDataHolder holder) {
		return EnergyProvider.fromProperty(INTERNAL_STORAGE, holder, this.getInteralCapacity(holder));
	}


	@Override
	public EnergyProvider getEnergyOutput(PersistentDataHolder holder) {
		return this.getInternalStorage(holder);
	}


	/**
	 * Cycles the redstone mode of the given block by one step.
	 * 
	 * @param block the block
	 */
	public void toggleRedstoneMode(TileState block, int amount) {
		REDSTONE_MODE.cycle(block, amount);
		block.update();
	}


	@Override
	protected int getWorkPeriod() {
		return 1;
	}


	@Override
	public void tick(TileState block) {
		if(REDSTONE_MODE.fetch(block).isActive(this.isBlockPowered(block))) {
			if(INTERNAL_STORAGE.fetch(block) < this.getInteralCapacity(block)) {
				if(this.fullfillsGeneratingConditions(block)) {
					double d = REST_SPEED.fetch(block) + this.getSpeed(block);
					for(; d >= 1.0D; d--)
						this.generateEnergy(block);

					REST_SPEED.store(block, d);
				}
			}
		}
	}


	/**
	 * Checks if the given block fulfills all conditions to generate energy.
	 * 
	 * @param block the block
	 * @return if the block can generate energy
	 */
	protected abstract boolean fullfillsGeneratingConditions(TileState block);


	/**
	 * Generates energy for the storage of the given block.
	 * 
	 * @param block the block
	 */
	protected void generateEnergy(TileState block) {
		int energy = this.getEnergyGeneration(block);

		double efficiency = this.getEfficiency(block);
		if(Math.random() < efficiency)
			energy *= 2;

		this.getInternalStorage(block).set(energy);
	}


	/**
	 * Returns the number of times the {@link AbstractGenerator#generateEnergy(TileState)} method get's executed in one tick for the given block.
	 * 
	 * @param block the block
	 * @return the speed
	 */
	protected double getSpeed(TileState block) {
		return this.baseSpeed + this.getUpgradeValue(block, MachineUpgrade.SPEED);
	}


	/**
	 * Returns the efficiency of the given block. The efficiency is the chance that for the current tick extra energy get's generated.
	 * 
	 * @param block the block
	 * @return the efficiency
	 */
	protected double getEfficiency(TileState block) {
		return this.getUpgradeValue(block, MachineUpgrade.EFFICIENCY);
	}


	@Override
	public int getUpgradeSlots(PersistentDataHolder holder) {
		return this.upgradeSlots;
	}


	@Override
	public String getDisplayName(PersistentDataHolder holder) {
		return this.getTitle();
	}


	@Override
	public ItemStack getIcon(PersistentDataHolder holder) {
		return new GeneratorItem(this).get();
	}


	@Override
	public void getAllowedUpgrades(ArrayList<MachineUpgrade> upgrades) {
		upgrades.add(MachineUpgrade.SPEED);
		upgrades.add(MachineUpgrade.EFFICIENCY);
		upgrades.add(MachineUpgrade.ENERGY_STORAGE);
	}

}
