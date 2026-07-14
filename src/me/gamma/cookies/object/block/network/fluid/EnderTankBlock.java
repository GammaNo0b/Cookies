
package me.gamma.cookies.object.block.network.fluid;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.object.tile.network.fluid.EnderTank;
import me.gamma.cookies.util.FluidUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class EnderTankBlock extends EnderLinkedBlock<Fluid> {

	@Override
	protected Fluid newResource() {
		return new Fluid(FluidType.EMPTY);
	}


	@Override
	protected boolean loadResource(Fluid resource, PersistentDataObject data) {
		return PersistentDataUtils.loadFluid(data, resource) != null;
	}


	@Override
	protected boolean saveResource(Fluid resource, PersistentDataObject data) {
		PersistentDataUtils.saveFluid(data, resource);
		return true;
	}


	@Override
	public String getIdentifier() {
		return "ender_tank";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ENDER_TANK;
	}


	@Override
	protected void displayResources(Player player, Fluid resource) {
		if(FluidUtils.isEmpty(resource)) {
			player.sendMessage("§7Stored: §fEmpty");
		} else {
			player.sendMessage("§7Stored: " + resource.getType().getName() + " §7( §f" + resource.getMillibuckets() + " §7mb §8)");
		}
	}


	@Override
	public EnderLinkedBlock<Fluid> castCustomBlock() {
		return this;
	}


	@Override
	public EnderLinkedTileEntity<Fluid> createNewTileEntity(Block block) {
		return new EnderTank(this, block);
	}

}
