
package me.gamma.cookies.object.tile.machine;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.BlockBreakerBlock;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;



public class BlockBreaker extends AbstractMachine<BlockBreaker, BlockBreakerBlock> {

	public static final Set<Material> UNBREAKABLE_BLOCKS = new HashSet<>(ArrayUtils.asList(Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.WATER, Material.LAVA, Material.AIR, Material.SPAWNER, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID));

	private int ticks = 0;

	public BlockBreaker(BlockBreakerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected boolean run() {
		if(this.ticks > 0) {
			this.ticks--;
			return false;
		}

		this.ticks = this.customBlock.getBreakFrequency();

		BlockFace rotation = ((Rotatable) this.block.getBlockData()).getRotation();
		Block front = block.getLocation().add(rotation.getOppositeFace().getDirection()).getBlock();
		if(UNBREAKABLE_BLOCKS.contains(front.getType()))
			return false;

		for(ItemStack item : front.getDrops())
			this.ejectCollectedItems(rotation, item);
		front.setType(Material.AIR);
		return true;
	}


	/**
	 * Transfers the given item broken from the given block in the given direction or drops it to the ground.
	 * 
	 * @param block             the block
	 * @param oppositeDirection the direction
	 * @param item              the broken item
	 */
	private void ejectCollectedItems(BlockFace oppositeDirection, ItemStack item) {
		item = ItemUtils.transferItem(item, this.getBlock(), BlockUtils.cartesian);
		if(ItemUtils.isEmpty(item))
			return;

		Item entityItem = block.getWorld().dropItem(this.getBlock().getRelative(oppositeDirection).getLocation().add(0.5, 0.25, 0.5), item);
		entityItem.setVelocity(oppositeDirection.getDirection());
	}


	@Override
	public BlockBreaker castTileEntity() {
		return this;
	}

}
