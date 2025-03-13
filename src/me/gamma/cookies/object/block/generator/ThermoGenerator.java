
package me.gamma.cookies.object.block.generator;


import java.util.Arrays;
import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;



public class ThermoGenerator extends AbstractGenerator implements FluidConsumer {

	public static final IntegerProperty LAVA = Properties.LAVA;

	public ThermoGenerator() {
		super(null);
	}


	@Override
	public String getGeneratorRegistryName() {
		return "thermo_generator";
	}


	@Override
	public String getTitle() {
		return "§eThermo Generator";
	}


	@Override
	public String getBlockTexture() {
		// TODO find unique texture
		return HeadTextures.OBSIDIAN_GENERATOR;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(LAVA);
	}


	@Override
	public List<FluidProvider> getFluidInputs(PersistentDataHolder holder) {
		return Arrays.asList(FluidProvider.fromProperty(FluidType.LAVA, LAVA, holder, 4000));
	}


	@Override
	protected boolean fullfillsGeneratingConditions(TileState block) {
		return LAVA.decrease(block, 1) == 0;
	}


	@Override
	protected void generateEnergy(TileState block) {
		this.tryPullFluid(block);
		super.generateEnergy(block);
	}

}
