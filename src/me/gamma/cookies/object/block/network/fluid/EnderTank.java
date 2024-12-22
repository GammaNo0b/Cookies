
package me.gamma.cookies.object.block.network.fluid;


import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidStorage;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.FluidProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.FluidUtils;



public class EnderTank extends EnderLinkedBlock<Fluid> implements FluidStorage {

	private static final int CAPACITY = 64000;

	private static final FluidProperty FLUID = Properties.FLUID;

	@Override
	protected Fluid newResource() {
		return new Fluid(FluidType.EMPTY, 0);
	}


	@Override
	protected void loadResource(Fluid resource, PersistentDataContainer container) {
		Fluid fluid = FLUID.fetch(container);
		resource.setType(fluid.getType());
		resource.setMillibuckets(fluid.getMillibuckets());
	}


	@Override
	protected boolean saveResource(Fluid resource, PersistentDataContainer container) {
		if(resource.isEmpty())
			return false;

		FLUID.store(container, resource);
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
	public byte getFluidInputAccessFlags(TileState block) {
		return 0x3F;
	}


	@Override
	public byte getFluidOutputAccessFlags(TileState block) {
		return 0x3F;
	}


	@Override
	public List<FluidProvider> getFluidProviders(TileState block) {
		return List.of(FluidProvider.fromFluid(this.getResource(block), CAPACITY));
	}


	@Override
	protected void displayResources(Player player, TileState block, Fluid resource) {
		if(FluidUtils.isEmpty(resource)) {
			player.sendMessage("§7Stored: §fEmpty");
		} else {
			player.sendMessage("§7Stored: " + resource.getType().getName() + " §7( §f" + resource.getMillibuckets() + " §7mb §8)");
		}
	}

}
