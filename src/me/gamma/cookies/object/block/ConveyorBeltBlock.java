
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.ConveyorBelt;



public class ConveyorBeltBlock extends AbstractCustomTileBlock<ConveyorBeltBlock, ConveyorBelt> implements Cartesian {

	private final ConveyorBeltBlock.Type type;

	private int capacity;
	private int conveyorLength;

	public ConveyorBeltBlock(ConveyorBeltBlock.Type type) {
		this.type = type;
	}


	@Override
	public ConfigurationSection getConfig() {
		return Config.BLOCKS.getConfig().getConfigurationSection("conveyor_belt").getConfigurationSection(this.type.name().toLowerCase());
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
		this.conveyorLength = config.getInt("conveyorLength", 20);
	}


	public int getCapacity() {
		return this.capacity;
	}


	public int getConveyorLength() {
		return this.conveyorLength;
	}


	@Override
	public String getIdentifier() {
		return this.type.name().toLowerCase() + "_conveyor_belt";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MISSING_TEXTURE;
	}


	@Override
	public boolean onBlockBreak(Player player, Block block, BlockBreakEvent event) {
		if(!super.onBlockBreak(player, block, event))
			return false;

		ConveyorBelt belt = this.getTileEntity(block);
		if(belt != null)
			belt.freeItem();

		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ConveyorBelt belt = this.getTileEntity(block);
		if(belt != null)
			belt.freeItem();

		return true;
	}

	public static enum Type {
		LIGHT,
		MEDIUM,
		HEAVY;
	}

	@Override
	public ConveyorBeltBlock castCustomBlock() {
		return this;
	}


	@Override
	public ConveyorBelt createNewTileEntity(Block block) {
		return new ConveyorBelt(this, block);
	}

}
