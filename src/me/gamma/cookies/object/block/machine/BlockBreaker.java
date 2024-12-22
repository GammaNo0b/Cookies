
package me.gamma.cookies.object.block.machine;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;



public class BlockBreaker extends AbstractMachine {

	public static final Set<Material> UNBREAKABLE_BLOCKS = new HashSet<>(ArrayUtils.asList(Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.WATER, Material.LAVA, Material.AIR, Material.SPAWNER, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID));

	private int breakFrequency;

	public BlockBreaker() {
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
	public int getWorkPeriod() {
		return this.breakFrequency;
	}


	@Override
	protected boolean run(TileState block) {
		BlockFace rotation = ((Rotatable) block.getBlockData()).getRotation();
		Block front = block.getLocation().add(rotation.getOppositeFace().getDirection()).getBlock();
		if(!UNBREAKABLE_BLOCKS.contains(front.getType())) {
			for(ItemStack item : front.getDrops())
				this.ejectCollectedItems(block, rotation, item);
			front.setType(Material.AIR);
			return true;
		}
		return false;
	}


	/**
	 * Transfers the given item broken from the given block in the given direction or drops it to the ground.
	 * 
	 * @param block             the block
	 * @param oppositeDirection the direction
	 * @param item              the broken item
	 */
	private void ejectCollectedItems(TileState block, BlockFace oppositeDirection, ItemStack item) {
		if(!ItemUtils.isEmpty(item = ItemUtils.transferItem(item, block.getBlock(), BlockUtils.cartesian))) {
			Item entityItem = block.getWorld().dropItem(block.getBlock().getRelative(oppositeDirection).getLocation().add(0.5, 0.25, 0.5), item);
			entityItem.setVelocity(oppositeDirection.getDirection());
		}
	}


	@Override
	public String getMachineRegistryName() {
		return "block_breaker";
	}

}
