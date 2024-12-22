
package me.gamma.cookies.object.block.generator;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.UpdatingGuiProvider;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemBuilder;



public abstract class AbstractGuiGenerator extends AbstractGenerator implements UpdatingGuiProvider, BlockFaceConfigurable {

	public AbstractGuiGenerator(MachineTier tier) {
		super(tier);
	}

	protected final Map<Location, Inventory> inventories = new HashMap<>();

	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Map<Location, Inventory> getInventoryMap() {
		return this.inventories;
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		this.updateEnergyLevel(block);
		this.updateRedstoneMode(block);
		inventory.setItem(this.getUpgradeSlot(), new ItemBuilder(Material.DIAMOND).setName("§bMachine Upgrades").build());
		inventory.setItem(this.getBlockFaceConfigSlot(), BLOCK_FACE_CONFIG_ICON);
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(super.onBlockRightClick(player, block, stack, event))
			return true;

		if(player.isSneaking())
			return true;

		if(!this.canAccess(block, player)) {
			player.sendMessage("§cYou don't own this generator!");
			return true;
		}

		this.openGui(player, block);
		return true;
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		this.unregisterInventory(block);
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == this.getUpgradeSlot()) {
			MachineUpgradeGui.open(player, block, this);
		} else if(slot == this.getRedstoneModeSlot()) {
			ClickType type = event.getClick();
			this.toggleRedstoneMode(block, (type.isLeftClick() ? 1 : 0) - (type.isRightClick() ? 1 : 0));
		} else if(slot == this.getBlockFaceConfigSlot()) {
			History.add(player, new StaticInventoryTask(gui));
			this.openBlockFaceConfig(player, block);
		}
		return true;
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {}


	@Override
	public void toggleRedstoneMode(TileState block, int amount) {
		super.toggleRedstoneMode(block, amount);
		this.updateRedstoneMode(block);
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.updateEnergyLevel(block);
	}


	@Override
	public int rows() {
		return MachineConstants.GUI_ROWS;
	}


	@Override
	public int getIdentifierSlot() {
		return MachineConstants.IDENTIFIER_SLOT;
	}


	/**
	 * Returns the slot to store the energy level icon.
	 * 
	 * @return the slot
	 */
	protected int getEnergyLevelSlot() {
		return MachineConstants.ENERGY_LEVEL_SLOT;
	}


	/**
	 * Returns the slot to store the redstone mode icon.
	 * 
	 * @return the slot
	 */
	protected int getRedstoneModeSlot() {
		return MachineConstants.REDSTONE_MODE_SLOT;
	}


	/**
	 * Returns the slot to store the upgrade button.
	 * 
	 * @return the slot
	 */
	protected int getUpgradeSlot() {
		return MachineConstants.UPGRADE_SLOT;
	}


	/**
	 * Returns the slot to store the block face config button.
	 * 
	 * @return the slot
	 */
	protected int getBlockFaceConfigSlot() {
		return MachineConstants.BLOCK_FACE_CONFIG_SLOT;
	}


	/**
	 * Updates the energy level icon in the inventory of the given block.
	 * 
	 * @param block the block
	 */
	protected void updateEnergyLevel(TileState block) {
		int energy = INTERNAL_STORAGE.fetch(block);
		int capacity = this.getInternalCapacity();
		double percent = 1.0D * energy / capacity;
		String texture;
		if(percent < 0.25D) {
			texture = HeadTextures.BATTERY_EMPTY;
		} else if(percent < 0.5D) {
			texture = HeadTextures.BATTERY_LOW;
		} else if(percent < 0.75D) {
			texture = HeadTextures.BATTERY_MEDIUM;
		} else {
			texture = HeadTextures.BATTERY_FULL;
		}
		ItemStack icon = new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(texture).setName("§9Energy: " + ColorUtils.colorProgress(energy, 0, capacity, ColorUtils.STOPLIGHT_PROGRESS) + " §3/ §b" + this.getInternalCapacity()).addLore(ColorUtils.colorProgress(energy * 100.0D / capacity, 0, 100, ColorUtils.STOPLIGHT_PROGRESS) + "%").addLore("  §8Efficiency: §7" + Math.round(this.getEfficiency(block) * 100.0D) + '%').build();
		Inventory inventory = this.getGui(block);
		inventory.setItem(this.getEnergyLevelSlot(), icon);
	}


	/**
	 * Updates the redstone mode icon in the inventory of the given block.
	 * 
	 * @param block the block
	 */
	protected void updateRedstoneMode(TileState block) {
		RedstoneMode mode = REDSTONE_MODE.fetch(block);
		Inventory inventory = this.getGui(block);
		inventory.setItem(this.getRedstoneModeSlot(), mode.createMenu());
	}

}
