
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.CrusherBlock;



public class Crusher extends AbstractCraftingMachine<Crusher, CrusherBlock> {

	public Crusher(CrusherBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public Material getProgressMaterial(double progress) {
		switch (this.customBlock.getTier()) {
			case BASIC:
				return Material.IRON_PICKAXE;
			case ADVANCED:
				return Material.GOLDEN_PICKAXE;
			case IMPROVED:
				return Material.DIAMOND_PICKAXE;
			case PERFECTED:
				return Material.NETHERITE_PICKAXE;
			default:
				return null;
		}
	}


	@Override
	protected void proceed() {
		this.block.getWorld().spawnParticle(Particle.DUST, block.getLocation().add(0.5D, 0.5D, 0.5D), 1, 0.25D, 0.25D, 0.25D, 1, new DustOptions(Color.fromBGR(0x3F3F3F), 1.0F));
	}


	@Override
	public Crusher castTileEntity() {
		return this;
	}

}
