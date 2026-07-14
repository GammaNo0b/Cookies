
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.block.machine.BlockGeneratorBlock;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class BlockGenerator extends AbstractCustomTileEntity<BlockGenerator, BlockGeneratorBlock> implements Switchable {

	private static final String KEY_REDSTONE_MODE = "redstonemode";
	private static final String KEY_PHASE = "phase";

	private RedstoneMode mode = RedstoneMode.REDSTONE_ON;
	private int phase = 0;

	public BlockGenerator(BlockGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.mode = PersistentDataUtils.getEnum(data, KEY_REDSTONE_MODE, RedstoneMode.class);
		if(this.mode == null)
			this.mode = RedstoneMode.REDSTONE_ON;

		this.phase = data.getInteger(KEY_PHASE, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setEnum(data, KEY_REDSTONE_MODE, this.mode);
		data.setInteger(KEY_PHASE, this.phase);

		return true;
	}


	@Override
	public RedstoneMode getRedstoneMode() {
		return this.mode;
	}


	@Override
	public BlockGenerator castTileEntity() {
		return this;
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		if(!this.isActive())
			return;

		if(++this.phase < this.customBlock.getFrequency())
			return;

		this.phase = 0;

		ItemStack rest = ItemUtils.transferItem(this.customBlock.getGenerator().get(), this.getBlock());
		ItemUtils.dropItem(rest, this.block.getLocation().add(0.5D, 0.5D, 0.5D));
	}


	/**
	 * Cycles the redstone mode by the given amount.
	 * 
	 * @return the new redstone mode
	 */
	public RedstoneMode toggleRedstoneMode(int amount) {
		return this.mode = EnumUtils.cycle(this.mode, amount);
	}

}
