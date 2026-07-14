package me.gamma.cookies.object.tile.machine;

import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.PackingMachineBlock;

public class PackingMachine extends AbstractCraftingMachine<PackingMachine, PackingMachineBlock> {

	public PackingMachine(PackingMachineBlock customBlock, Block block) {
		super(customBlock, block);
	}

	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PISTON;
	}

	@Override
	public PackingMachine castTileEntity() {
		return this;
	}

}
