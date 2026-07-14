
package me.gamma.cookies.object.tile;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Light;

import me.gamma.cookies.object.block.LEDBlock;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.util.BlockUtils;



public class LED extends AbstractCustomTileEntity<LED, LEDBlock> implements Switchable {

	private boolean powered = false;

	public LED(LEDBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public RedstoneMode getRedstoneMode() {
		return RedstoneMode.REDSTONE_ON;
	}


	public void enableLights() {
		for(BlockFace face : BlockUtils.cartesian) {
			Block relative = this.block.getRelative(face);
			if(relative.getType().isAir()) {
				relative.setType(Material.LIGHT);
				Light light = (Light) relative.getBlockData();
				light.setLevel(15);
				relative.setBlockData(light);
			}
		}
	}


	public void removeLights() {
		for(BlockFace face : BlockUtils.cartesian) {
			Block relative = this.block.getRelative(face);
			if(relative.getType() == Material.LIGHT)
				relative.setType(Material.AIR);
		}
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		boolean power = this.isBlockPowered();
		if(this.powered != power) {
			this.customBlock.updateTexture(this.block, power);
			this.powered = power;

			if(!power)
				this.removeLights();
		}

		if(power)
			this.enableLights();
	}


	@Override
	public LED castTileEntity() {
		return this;
	}

}
