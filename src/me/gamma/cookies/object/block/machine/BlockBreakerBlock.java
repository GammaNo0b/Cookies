
package me.gamma.cookies.object.block.machine;


import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.BlockBreaker;



public class BlockBreakerBlock extends AbstractMachineBlock<BlockBreakerBlock, BlockBreaker> {

	private int breakFrequency;

	public BlockBreakerBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.breakFrequency = config.getInt("breakFrequency", 20);
	}


	@Override
	public String getTitle() {
		return "§eBlock Breaker";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.BLOCK_BREAKER;
	}


	@Override
	public String getMachineRegistryName() {
		return "block_breaker";
	}


	@Override
	public BlockBreakerBlock castCustomBlock() {
		return this;
	}


	@Override
	public BlockBreaker createNewTileEntity(Block block) {
		return new BlockBreaker(this, block);
	}


	public int getBreakFrequency() {
		return this.breakFrequency;
	}

}
