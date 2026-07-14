
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.VoidOreMinerBlock;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class VoidOreMiner extends AbstractItemGenerationMachine<VoidOreMiner, VoidOreMinerBlock> {

	private static final String KEY_PROCESSING = "processing";

	private ItemStack processing = null;

	public VoidOreMiner(VoidOreMinerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.processing = PersistentDataUtils.getItemStack(data, KEY_PROCESSING);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setItemStack(data, KEY_PROCESSING, this.processing);

		return true;
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34 };
	}


	@Override
	protected int createNextProcess() {
		if(!this.checkForVoid())
			return 0;

		this.processing = new ItemStack(this.customBlock.getRandomWeightedMaterial());
		return this.customBlock.getMiningDuration();
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		List<ItemStack> list = new ArrayList<>();
		list.add(this.processing);
		while(!this.storeOutputs(list)) {
			if(!this.tryPushItems()) {
				return false;
			}
		}

		this.processing = null;
		while(this.tryPushItems());

		return true;
	}


	private boolean checkForVoid() {
		Block b = this.block;
		while(b.getY() > block.getWorld().getMinHeight()) {
			b = b.getRelative(0, -1, 0);
			if(!b.getType().isAir())
				return false;
		}

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.BEDROCK;
	}


	@Override
	public VoidOreMiner castTileEntity() {
		return this;
	}

}
