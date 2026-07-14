
package me.gamma.cookies.object.tile.generator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.manager.WireManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.block.Upgradeable;
import me.gamma.cookies.object.block.generator.AbstractGeneratorBlock;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.block.network.WireComponentType;
import me.gamma.cookies.object.block.network.WireHolder;
import me.gamma.cookies.object.block.network.WireRelay;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergySupplier;
import me.gamma.cookies.object.item.resources.GeneratorItem;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractGenerator<T extends AbstractGenerator<T, B>, B extends AbstractGeneratorBlock<B, T>> extends AbstractCustomTileEntity<T, B> implements Upgradeable, Ownable, Switchable, EnergySupplier, WireRelay<Void> {

	public static final String KEY_OWNER = "owner";
	public static final String KEY_UPGRADES = "upgrades";
	public static final String KEY_REST_SPEED = "restspeed";
	public static final String KEY_REDSTONE_MODE = "redstonemode";
	public static final String KEY_ENERGY = "energy";

	private final Map<MachineUpgrade, Integer> upgradeLevels = new HashMap<>();
	private UUID owner;
	private Wire<Void> wire = null;
	private double restSpeed = 0.0D;

	protected RedstoneMode mode = RedstoneMode.REDSTONE_ON;
	protected int energy = 0;

	public AbstractGenerator(B customBlock, Block block) {
		super(customBlock, block);

		List<MachineUpgrade> upgrades = new ArrayList<>();
		this.getAllowedUpgrades(upgrades);
		for(MachineUpgrade upgrade : upgrades)
			this.upgradeLevels.put(upgrade, 0);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.owner = PersistentDataUtils.getUUID(data, KEY_OWNER);
		PersistentDataUtils.getMap(data, KEY_UPGRADES, this.upgradeLevels, (d, key) -> MachineUpgrade.fromString(d.getString(key)), PersistentDataObject::getInteger);
		this.restSpeed = data.getDouble(KEY_REST_SPEED, 0.0D);
		this.mode = PersistentDataUtils.getEnum(data, KEY_REDSTONE_MODE, RedstoneMode.class);
		if(this.mode == null)
			this.mode = RedstoneMode.REDSTONE_ON;

		this.energy = data.getInteger(KEY_ENERGY, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(this.owner != null)
			PersistentDataUtils.setUUID(data, KEY_OWNER, this.owner);

		PersistentDataUtils.setMap(data, KEY_UPGRADES, this.upgradeLevels, (d, key, upgrade) -> d.setString(key, upgrade.getRegistryName()), PersistentDataObject::setInteger);
		data.setDouble(KEY_REST_SPEED, this.restSpeed);
		PersistentDataUtils.setEnum(data, KEY_REDSTONE_MODE, this.mode);
		data.setInteger(KEY_ENERGY, this.energy);

		return true;
	}


	@Override
	public void destroy() {
		super.destroy();

		if(this.wire != null) {
			ItemUtils.dropItem(this.wire.getWireItem().get(), this.block);
			this.wire.destroy();
		}
	}


	@Override
	public RedstoneMode getRedstoneMode() {
		return this.mode;
	}


	@Override
	public Provider<Void> getWireProvider() {
		return this.getEnergyOutput();
	}


	@Override
	public WireComponentType getWireComponentType() {
		return WireComponentType.SUPPLIER;
	}


	@Override
	public boolean createWire(WireHolder<Void> holder, Block other, WireItem wireItem) {
		return WireManager.WIRE_MANAGER.createWire(this, this.block, holder, other, wireItem) != null;
	}


	@Override
	public Wire<Void> getConnectedWire() {
		return this.wire;
	}


	@Override
	public void setConnectedWire(Wire<Void> wire) {
		this.wire = wire;
	}


	@Override
	public UUID getOwner() {
		return this.owner;
	}


	@Override
	public void setOwner(UUID uuid) {
		this.owner = uuid;
	}


	@Override
	public EnergyProvider getEnergyOutput() {
		return EnergyProvider.fromHolder(Holder.create(() -> this.energy, e -> { this.energy = e; }), this.customBlock.getInternalCapacity());
	}


	/**
	 * Cycles the redstone mode by the given amount.
	 */
	public void toggleRedstoneMode(int amount) {
		this.mode = EnumUtils.cycle(this.mode, amount);
	}


	/**
	 * Checks if this generator fulfills all conditions to generate energy.
	 * 
	 * @return if the block can generate energy
	 */
	protected abstract boolean fullfillsGeneratingConditions();


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		super.tick();

		if(this.isActive()) {
			if(this.energy < this.customBlock.getInternalCapacity()) {
				if(this.fullfillsGeneratingConditions()) {
					this.restSpeed += this.getSpeed();
					for(; this.restSpeed >= 1.0D; this.restSpeed--)
						this.generateEnergy();
				}
			}
		}
	}


	/**
	 * Generates energy for the storage of the given block.
	 * 
	 * @param block the block
	 */
	protected void generateEnergy() {
		int energy = this.getEnergyGeneration();

		double efficiency = this.getEfficiency();
		if(Math.random() < efficiency)
			energy *= 2;

		this.getEnergyOutput().set(energy);
	}


	/**
	 * Amount of energy that get's generated by the given block per operation.
	 * 
	 * @param block the block
	 * @return the amount of energy
	 */
	public int getEnergyGeneration() {
		return this.customBlock.getMaximumEnergyGeneration();
	}


	/**
	 * Returns the number of times the {@link AbstractGeneratorBlock#generateEnergy(TileState)} method get's executed in one tick for the given block.
	 * 
	 * @param block the block
	 * @return the speed
	 */
	protected double getSpeed() {
		return this.customBlock.getBaseSpeed() + this.getUpgradeValue(MachineUpgrade.SPEED);
	}


	/**
	 * Returns the efficiency of the given block. The efficiency is the chance that for the current tick extra energy get's generated.
	 * 
	 * @param block the block
	 * @return the efficiency
	 */
	protected double getEfficiency() {
		return this.getUpgradeValue(MachineUpgrade.EFFICIENCY);
	}


	@Override
	public int getUpgradeSlots() {
		return this.customBlock.getUpgradeSlots();
	}


	@Override
	public boolean isAllowedUpgrade(MachineUpgrade upgrade) {
		return this.upgradeLevels.containsKey(upgrade);
	}


	@Override
	public int getUpgradeLevel(MachineUpgrade upgrade) {
		return this.upgradeLevels.get(upgrade);
	}


	@Override
	public void setUpgradeLevel(MachineUpgrade upgrade, int level) {
		this.upgradeLevels.put(upgrade, level);
	}


	@Override
	public String getDisplayName() {
		return this.customBlock.getTitle();
	}


	@Override
	public ItemStack getIcon() {
		return new GeneratorItem(this.customBlock).get();
	}


	/**
	 * Creates a list of allowed upgrades for this generator.
	 * 
	 * @param the list of upgrades
	 */
	public void getAllowedUpgrades(List<MachineUpgrade> upgrades) {
		upgrades.add(MachineUpgrade.SPEED);
		upgrades.add(MachineUpgrade.EFFICIENCY);
		upgrades.add(MachineUpgrade.ENERGY_STORAGE);
	}

}
