
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.BlockGenerator;
import me.gamma.cookies.util.ItemUtils;



public class BlockGeneratorBlock extends AbstractCustomTileBlock<BlockGeneratorBlock, BlockGenerator> {

	private final String identifier;
	private final IItemSupplier generator;
	private final int frequency;

	public BlockGeneratorBlock(String identifier, IItemSupplier generator, int frequency) {
		this.identifier = identifier;
		this.generator = generator;
		this.frequency = frequency;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COBBLESTONE_GENERATOR;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		if(ItemUtils.isType(stack, Material.REDSTONE_TORCH)) {
			BlockGenerator generator = this.getTileEntity(block);
			if(generator != null)
				player.sendMessage("§cRedstone mode set to " + generator.toggleRedstoneMode(1).getTitle());
		}

		return true;
	}


	@Override
	public BlockGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public BlockGenerator createNewTileEntity(Block block) {
		return new BlockGenerator(this, block);
	}


	public int getFrequency() {
		return this.frequency;
	}


	public IItemSupplier getGenerator() {
		return this.generator;
	}

}
