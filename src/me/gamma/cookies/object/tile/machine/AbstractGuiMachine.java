
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.machine.AbstractGuiMachineBlock;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.util.MachineUpgradeGui;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractGuiMachine<T extends AbstractGuiMachine<T, B>, B extends AbstractGuiMachineBlock<B, T>> extends AbstractMachine<T, B> implements ContainerTile, BlockFaceConfigurable {

	public AbstractGuiMachine(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		return super.load(chunk, data) && this.loadInventory();
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		return super.save(chunk, data) && this.saveInventory();
	}


	@Override
	public void setupInventory(Inventory inventory) {
		this.updateEnergyLevel();
		this.updateRedstoneMode();
		inventory.setItem(this.customBlock.getUpgradeSlot(), new ItemBuilder(Material.DIAMOND).setName("§bMachine Upgrades").build());
		inventory.setItem(this.customBlock.getBlockFaceConfigSlot(), BLOCK_FACE_CONFIG_ICON);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == this.customBlock.getUpgradeSlot() && this.getUpgradeSlots() > 0) {
			History.add(player, new StaticInventoryTask(gui));
			MachineUpgradeGui.open(player, this);
		} else if(slot == this.customBlock.getRedstoneModeSlot()) {
			ClickType type = event.getClick();
			this.toggleRedstoneMode((type.isLeftClick() ? 1 : 0) - (type.isRightClick() ? 1 : 0));
		} else if(slot == this.customBlock.getBlockFaceConfigSlot()) {
			History.add(player, new StaticInventoryTask(gui));
			this.openBlockFaceConfig(player);
		}
		return true;
	}


	@Override
	public void toggleRedstoneMode(int amount) {
		super.toggleRedstoneMode(amount);
		this.updateRedstoneMode();
	}


	@Override
	public void tick() {
		this.updateEnergyLevel();
		super.tick();
	}


	/**
	 * Updates the energy level icon in the inventory of the given block.
	 */
	protected void updateEnergyLevel() {
		int capacity = this.customBlock.getInternalCapacity();
		double percent = 1.0D * this.energy / capacity;
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
		ItemStack icon = new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(texture).setName("§9Energy: " + ColorUtils.colorProgress(this.energy, 0, capacity, ColorUtils.STOPLIGHT_PROGRESS) + " §3/ §b" + capacity).addLore(ColorUtils.colorProgress(energy * 100.0D / capacity, 0, 100, ColorUtils.STOPLIGHT_PROGRESS) + "%").addLore("  §8Efficiency: §7" + Math.round(this.getEfficiency() * 100.0D) + '%').build();
		this.getInventory().setItem(this.customBlock.getEnergyLevelSlot(), icon);
	}


	/**
	 * Updates the redstone mode icon in the inventory of the given block.
	 * 
	 * @param gui the inventory
	 */
	protected void updateRedstoneMode() {
		this.getInventory().setItem(this.customBlock.getRedstoneModeSlot(), this.mode.createMenu());
	}


	@Override
	public BlockInventoryProvider getInventoryProvider() {
		return this.customBlock;
	}

}
