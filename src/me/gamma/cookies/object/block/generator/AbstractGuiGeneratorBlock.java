
package me.gamma.cookies.object.block.generator;


import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.UpdatingGuiProvider;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.object.tile.generator.AbstractGuiGenerator;



public abstract class AbstractGuiGeneratorBlock<B extends AbstractGuiGeneratorBlock<B, T>, T extends AbstractGuiGenerator<T, B>> extends AbstractGeneratorBlock<B, T> implements UpdatingGuiProvider {

	public AbstractGuiGeneratorBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public String getTitle(Block data) {
		return this.getTitle();
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
	public int getEnergyLevelSlot() {
		return MachineConstants.ENERGY_LEVEL_SLOT;
	}


	/**
	 * Returns the slot to store the redstone mode icon.
	 * 
	 * @return the slot
	 */
	public int getRedstoneModeSlot() {
		return MachineConstants.REDSTONE_MODE_SLOT;
	}


	/**
	 * Returns the slot to store the upgrade button.
	 * 
	 * @return the slot
	 */
	public int getUpgradeSlot() {
		return MachineConstants.UPGRADE_SLOT;
	}


	/**
	 * Returns the slot to store the block face config button.
	 * 
	 * @return the slot
	 */
	public int getBlockFaceConfigSlot() {
		return MachineConstants.BLOCK_FACE_CONFIG_SLOT;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public void blockBroken(Block block) {
		super.blockBroken(block);

		this.unregisterInventory(block);
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		this.openGui(player, block);

		return true;
	}


	@Override
	public ContainerTile getContainer(Block block) {
		return this.getTileEntity(block);
	}

}
