
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.manager.Wire;
import me.gamma.cookies.object.Cable.TransferMode;
import me.gamma.cookies.object.block.AbstractWorkBlock;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.block.Upgradeable;
import me.gamma.cookies.object.block.network.WireRelay;
import me.gamma.cookies.object.block.network.energy.EnergyCable;
import me.gamma.cookies.object.energy.EnergyConsumer;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.item.MachineItem;
import me.gamma.cookies.object.property.DoubleProperty;
import me.gamma.cookies.object.property.EnergyProperty;
import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractMachine extends AbstractWorkBlock implements Upgradeable, Ownable, Switchable, EnergyConsumer, EnergyCable, WireRelay {

	public static final VectorProperty WIRE_POS = Properties.WIRE_POS;

	public static final EnergyProperty INTERNAL_STORAGE = Properties.INTERNAL_STORAGE;
	public static final EnumProperty<RedstoneMode> REDSTONE_MODE = Properties.REDSTONE_MODE;
	public static final DoubleProperty REST_SPEED = Properties.REST_SPEED;

	protected final Set<Location> locations = new HashSet<>();
	protected final HashMap<Location, Wire> wires = new HashMap<>();
	protected final MachineTier tier;

	protected int energyConsumption;
	protected int internalCapacity;
	protected int upgradeSlots;
	protected double baseSpeed;

	public AbstractMachine(MachineTier tier) {
		this.tier = tier;
	}


	public double getBaseSpeed() {
		return this.baseSpeed;
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


	@Override
	public boolean load(TileState block, PersistentDataObject data) {
		if(data.has(WIRE_POS)) {
			Location pos = WIRE_POS.fetch(data.getContainer()).toLocation(block.getWorld());
			this.createWire(block, pos);
		}

		return true;
	}


	@Override
	public boolean save(TileState block, PersistentDataObject data) {
		Location pos = block.getLocation();
		Wire wire = this.wires.get(pos);
		if(wire != null) {
			Location opposite = wire.getOpposite(pos);
			if(opposite != null)
				WIRE_POS.store(data.getContainer(), opposite.toVector());
			wire.destroy();
		}

		return true;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public Wire getConnectedWire(TileState block) {
		return this.wires.get(block.getLocation());
	}


	@Override
	public void setConnectedWire(TileState block, Wire wire) {
		if(wire == null) {
			this.wires.remove(block.getLocation());
		} else {
			this.wires.put(block.getLocation(), wire);
		}
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
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(INTERNAL_STORAGE).add(REDSTONE_MODE).add(ENERGY_INPUT_ACCESS_FLAGS, (byte) 0x3F);
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

		for(int i = 0; i < this.removeAllWires(block); i++)
			ItemUtils.dropItem(Items.INSULATED_COPPER_WIRE.get(), block);

		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(this.canAccess(block, player) && !player.isSneaking()) {
			if(ItemUtils.isType(stack, Material.REDSTONE_TORCH)) {
				this.toggleRedstoneMode(block, 1);
				player.sendMessage("§cRedstone mode set to " + REDSTONE_MODE.fetch(block).getTitle());
				return true;
			} else if(ItemUtils.isType(stack, Material.DIAMOND) && this.getUpgradeSlots(block) > 0) {
				MachineUpgradeGui.open(player, block, this);
				return true;
			}
		}
		return false;
	}


	/**
	 * Returns the {@link EnergyProvider} of this machine to act as an energy buffer for later consumption.
	 * 
	 * @param block the block
	 * @return the energy storage
	 */
	protected EnergyProvider getInternalStorage(TileState block) {
		return EnergyProvider.fromProperty(INTERNAL_STORAGE, block, this.getInternalCapacity());
	}


	@Override
	public EnergyProvider getEnergyInput(TileState block) {
		return this.getInternalStorage(block);
	}


	@Override
	public EnergyProvider getBuffer(TileState block) {
		return this.getInternalStorage(block);
	}


	@Override
	public Stream<TileState> getNeighbors(TileState block) {
		Wire wire = this.getConnectedWire(block);
		if(wire == null)
			return Stream.empty();

		Location opposite = wire.getOpposite(block.getLocation());
		TileState neighbor = BlockUtils.getTileState(opposite);
		return neighbor == null ? Stream.empty() : Stream.of(neighbor);
	}


	@Override
	public TransferMode getTransferMode(TileState block) {
		return TransferMode.ROUND_ROBIN;
	}


	@Override
	public int getTransferRate(TileState block) {
		return 10;
	}


	/**
	 * Represents one work operation by the given block and returns if the operation was successfully performed.
	 * 
	 * @param block the block
	 * @return if the operation was performed
	 */
	protected abstract boolean run(TileState block);


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
			if(this.canConsumeEnergy(block)) {
				double d = REST_SPEED.fetch(block) + this.getSpeed(block);
				boolean worked = false;
				for(; d >= 1.0D; d--)
					if(this.run(block))
						worked = true;

				if(worked)
					this.consumeEnergy(block);

				REST_SPEED.store(block, d);
			}
		}

		this.collectEnergy(block);
	}


	/**
	 * Checks if the machine of the given block can consume the required energy to work.
	 * 
	 * @param block the block
	 * @return if the machine has enough energy
	 */
	protected boolean canConsumeEnergy(TileState block) {
		return this.getInternalStorage(block).check(this.getEnergyConsumption());
	}


	/**
	 * Consumes energy from the storage of the given block.
	 * 
	 * @param block the block
	 */
	protected void consumeEnergy(TileState block) {
		if(Math.random() < this.getEfficiency(block))
			this.getInternalStorage(block).get(this.getEnergyConsumption());
	}


	/**
	 * Returns the number of times the {@link AbstractMachine#run(TileState)} method get's executed in one tick for the given block.
	 * 
	 * @param block the block
	 * @return the speed
	 */
	protected double getSpeed(TileState block) {
		return this.baseSpeed + this.getUpgradeValue(block, MachineUpgrade.SPEED);
	}


	/**
	 * Returns the efficiency of the given block. The efficiency is the chance that for the current tick no energy get's consumed.
	 * 
	 * @param block the block
	 * @return the efficiency
	 */
	protected double getEfficiency(TileState block) {
		return this.getUpgradeValue(block, MachineUpgrade.EFFICIENCY);
	}


	@Override
	public int getUpgradeSlots(TileState block) {
		return this.upgradeSlots;
	}


	@Override
	public String getDisplayName(TileState block) {
		return this.getTitle();
	}


	@Override
	public ItemStack getIcon(TileState block) {
		return new MachineItem(this).get();
	}


	@Override
	public void getAllowedUpgrades(ArrayList<MachineUpgrade> upgrades) {
		upgrades.add(MachineUpgrade.SPEED);
		upgrades.add(MachineUpgrade.EFFICIENCY);
		upgrades.add(MachineUpgrade.ENERGY_STORAGE);
	}


	public static void upgradeMachine(ItemStack result, ItemStack... ingredients) {
		ItemMeta meta = result.getItemMeta();
		for(ItemStack ingredient : ingredients) {
			INTERNAL_STORAGE.transfer(ingredient.getItemMeta(), meta);
		}
		result.setItemMeta(meta);
	}

}
