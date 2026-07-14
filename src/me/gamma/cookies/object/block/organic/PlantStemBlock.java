
package me.gamma.cookies.object.block.organic;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomBlock;



public class PlantStemBlock extends AbstractCustomBlock {

	private final String identifier;
	private final Material type;
	private final AbstractCustomBlock bushBlock;

	public PlantStemBlock(String identifier, Material type, AbstractCustomBlock bushBlock) {
		this.identifier = identifier;
		this.type = type;
		this.bushBlock = bushBlock;
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
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	@Override
	public boolean onBlockStructureGrow(Block block, StructureGrowEvent event) {
		if(this.breakBlock(block))
			this.bushBlock.place(block.getLocation().getBlock(), null);

		return false;
	}

}
