
package me.gamma.cookies.object.block.organic;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Skull;
import org.bukkit.block.data.type.WallSkull;
import org.bukkit.event.block.LeavesDecayEvent;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.WorldUtils;



public class FruitTreeLeavesBlock extends AbstractCustomBlock {

	private final String identifier;
	private final Material type;
	private final AbstractCustomBlock fruitBlock;
	private final boolean growSideways;

	public FruitTreeLeavesBlock(String identifier, Material type, AbstractCustomBlock fruitBlock, boolean growSideways) {
		this.identifier = identifier;
		this.type = type;
		this.fruitBlock = fruitBlock;
		this.growSideways = growSideways;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public Material getMaterial() {
		return this.type;
	}


	private void breakFruits(Block block) {
		Block down = block.getRelative(BlockFace.DOWN);
		if(!down.isEmpty() && down.getBlockData() instanceof Skull && CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(down) == this.fruitBlock)
			this.fruitBlock.breakBlock(down);

		if(this.growSideways) {
			for(BlockFace side : BlockUtils.horizontal) {
				Block sideways = block.getRelative(side);
				if(sideways.isEmpty())
					continue;

				if(!(sideways.getBlockData() instanceof WallSkull skull))
					continue;

				if(skull.getFacing() != side)
					continue;

				if(CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(sideways) != this.fruitBlock)
					continue;

				this.fruitBlock.breakBlock(sideways);
			}
		}
	}


	@Override
	public void blockBroken(Block block) {
		super.blockBroken(block);

		this.breakFruits(block);
	}


	@Override
	public boolean onLeavesDecay(Block block, LeavesDecayEvent event) {
		this.breakFruits(block);

		return super.onLeavesDecay(block, event);
	}


	private boolean canGrowFruit(Block block) {
		Block down = block.getRelative(BlockFace.DOWN);
		if(CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(down) == this)
			return false;

		down = down.getRelative(BlockFace.DOWN);
		if(CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(down) == this)
			return false;

		// count fruits
		int fruits = 0;
		for(int y = -1; y <= 1; ++y)
			for(int z = -1; z <= 1; ++z)
				for(int x = -1; x <= 1; ++x)
					if(CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block.getRelative(x, y, z)) == this.fruitBlock)
						++fruits;

		return fruits < 4;
	}


	@Override
	public void onRandomTick(Block block) {
		if(!WorldUtils.randomChance(block.getWorld(), 1.0D / 40.0D))
			return;

		if(!this.growSideways || WorldUtils.randomChance(block.getWorld(), 0.2D)) {
			Block down = block.getRelative(BlockFace.DOWN);
			if(!down.isEmpty())
				return;

			if(!this.canGrowFruit(down))
				return;

			if(this.fruitBlock.place(down, null)) {
				if(down.getBlockData() instanceof Rotatable rotatable) {
					rotatable.setRotation(CollectionUtils.randomElement(BlockUtils.horizontal_rotation));
					down.setBlockData(rotatable);
				}
			}
		} else {
			BlockFace face = CollectionUtils.randomElement(BlockUtils.horizontal);
			Block side = block.getRelative(face);
			if(!side.isEmpty())
				return;

			if(!this.canGrowFruit(side))
				return;

			if(this.fruitBlock.place(side, Material.PLAYER_WALL_HEAD, null)) {
				if(side.getBlockData() instanceof Directional directional) {
					directional.setFacing(face);
					side.setBlockData(directional);
				}
			}
		}
	}

}
