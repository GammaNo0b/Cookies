
package me.gamma.cookies.object.block.organic;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Leaves;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.WorldUtils;



public class PlantBushBlock extends AbstractCustomBlock {

	private final String identifier;
	private final Material type;
	private final AbstractCustomBlock fruitBlock;

	public PlantBushBlock(String identifier, Material type, AbstractCustomBlock fruitBlock) {
		this.identifier = identifier;
		this.type = type;
		this.fruitBlock = fruitBlock;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public Material getMaterial() {
		return this.type;
	}


	@Override
	public boolean placeBlock(Block block) {
		if(!super.placeBlock(block))
			return false;

		if(block.getBlockData() instanceof Leaves leaves) {
			leaves.setPersistent(true);
			block.setBlockData(leaves);
		}

		return true;
	}


	@Override
	public void blockBroken(Block block) {
		super.blockBroken(block);

		Block top = block.getRelative(BlockFace.UP);
		if(!top.isEmpty() && CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(top) == this.fruitBlock)
			this.fruitBlock.breakBlock(top);
	}


	@Override
	public void onRandomTick(Block block) {
		if(!WorldUtils.randomChance(block.getWorld(), 1.0D / 17.0D))
			return;

		Block top = block.getRelative(BlockFace.UP);
		if(top.isEmpty()) {
			if(this.fruitBlock.place(top, null)) {
				if(top.getBlockData() instanceof Rotatable rotatable) {
					rotatable.setRotation(CollectionUtils.randomElement(BlockUtils.horizontal_rotation));
					top.setBlockData(rotatable);
				}
			}
		}
	}

}
