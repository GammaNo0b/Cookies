
package me.gamma.cookies.object.block.network.fluid;


import java.util.List;

import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.list.HeadTextures;



public class WasteBarrel extends AbstractCustomBlock implements FluidConsumer {

	@Override
	public String getIdentifier() {
		return "waste_barrel";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.WASTE_BARREL;
	}


	@Override
	public byte getFluidInputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public List<FluidProvider> getFluidInputs(PersistentDataHolder holder) {
		return List.of(new FluidProvider() {

			@Override
			public void setType(FluidType type) {}


			@Override
			public void remove(int amount) {}


			@Override
			public boolean match(FluidType type) {
				return true;
			}


			@Override
			public FluidType getType() {
				return FluidType.EMPTY;
			}


			@Override
			public int capacity() {
				return Integer.MAX_VALUE;
			}


			@Override
			public boolean canChangeType(FluidType type) {
				return true;
			}


			@Override
			public int amount() {
				return 0;
			}


			@Override
			public void add(FluidType type, int amount) {}

		});
	}

}
