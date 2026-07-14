
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.machine.HyperFurnaceBlock;
import me.gamma.cookies.util.ItemUtils;



public class HyperFurnace extends AbstractCraftingMachine<HyperFurnace, HyperFurnaceBlock> {

	public HyperFurnace(HyperFurnaceBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FLINT_AND_STEEL;
	}


	@Override
	protected int createNextProcess() {
		int i = super.createNextProcess();
		Furnace furnace = (Furnace) this.block.getBlockData();
		furnace.setLit(i > 0);
		this.block.setBlockData(furnace);
		return i;
	}


	@Override
	public void tick() {
		org.bukkit.block.Furnace furnace = (org.bukkit.block.Furnace) this.block.getState();
		FurnaceInventory inventory = furnace.getSnapshotInventory();
		ItemStack stack = inventory.getSmelting();
		if(!ItemUtils.isEmpty(stack)) {
			inventory.setSmelting(this.addStack(stack));
			furnace.update();
		}

		super.tick();
	}


	@Override
	public double getHalfWidth() {
		// negative to put the wire on the opposite of the block
		return -0.5D;
	}


	@Override
	public Vector getCenter() {
		return new Vector(0.5D, 0.5D, 0.5D);
	}


	@Override
	public HyperFurnace castTileEntity() {
		return this;
	}

}
