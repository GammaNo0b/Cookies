
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.machine.BedrockBreakerBlock;
import me.gamma.cookies.util.ItemBuilder;



public class BedrockBreaker extends AbstractItemGenerationMachine<BedrockBreaker, BedrockBreakerBlock> {

	private final Random random = new Random();

	private int processingDusts = 0;

	public BedrockBreaker(BedrockBreakerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.NETHERITE_PICKAXE;
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 24 };
	}


	@Override
	protected int createNextProcess() {
		Block down = this.block.getRelative(BlockFace.DOWN);
		if(down.getType() != Material.BEDROCK)
			return 0;

		down.setType(Material.AIR);
		this.processingDusts = this.random.nextInt(this.customBlock.getMinBedrockDusts(), this.customBlock.getMaxBedrockDusts() + 1);
		return this.customBlock.getMiningDuration();
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		ItemStack dust = new ItemBuilder(Items.BEDROCK_DUST).setAmount(this.processingDusts).build();
		ArrayList<ItemStack> items = new ArrayList<>();
		items.add(dust);
		boolean stored = this.storeOutputs(items);
		this.processingDusts = stored || items.isEmpty() ? 0 : items.get(0).getAmount();

		while(this.tryPushItems());

		return stored;
	}


	@Override
	public BedrockBreaker castTileEntity() {
		return this;
	}

}
