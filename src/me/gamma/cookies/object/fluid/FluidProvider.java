
package me.gamma.cookies.object.fluid;


import org.bukkit.block.TileState;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.property.FluidProperty;
import me.gamma.cookies.object.property.IntegerProperty;



public interface FluidProvider extends Provider<FluidType> {

	/**
	 * Creates an {@link FluidProvider} using the given {@link Fluid}.
	 * 
	 * @param fluid the fluid
	 * @return the created fluid provider
	 */
	static FluidProvider fromFluid(final Fluid fluid) {
		return fromFluid(fluid, Integer.MAX_VALUE);
	}


	/**
	 * Creates an {@link FluidProvider} using the given {@link Fluid}.
	 * 
	 * @param fluid    the fluid
	 * @param capacity the capacity
	 * @return the created fluid provider
	 */
	static FluidProvider fromFluid(final Fluid fluid, final int capacity) {
		return new FluidProvider() {

			@Override
			public int amount() {
				return fluid.getMillibuckets();
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(FluidType type, int amount) {
				fluid.grow(amount);
			}


			@Override
			public void remove(int amount) {
				fluid.shrink(amount);
			}


			@Override
			public boolean isEmpty() {
				return fluid.isEmpty();
			}


			@Override
			public FluidType getType() {
				return fluid.isEmpty() ? null : fluid.getType();
			}


			@Override
			public void setType(FluidType type) {
				fluid.setType(type);
			}


			@Override
			public boolean canChangeType(FluidType type) {
				return true;
			}


			@Override
			public boolean match(FluidType type) {
				return fluid.getType() == type;
			}

		};
	}


	/**
	 * Creates a {@link FluidProvider} from the given {@link TileState} using the {@link FluidProperty}.
	 * 
	 * @param property the property
	 * @param block    the block
	 * @return the created fluid provider
	 */
	static FluidProvider fromProperty(final FluidProperty property, final TileState block) {
		return fromProperty(property, block, Integer.MAX_VALUE);
	}


	/**
	 * Creates a {@link FluidProvider} from the given {@link TileState} using the {@link FluidProperty}.
	 * 
	 * @param property the property
	 * @param block    the block
	 * @param capacity the maximum amount of fluid that can be stored
	 * @return the created fluid provider
	 */
	static FluidProvider fromProperty(final FluidProperty property, final TileState block, final int capacity) {
		return new FluidProvider() {

			@Override
			public int amount() {
				return property.fetch(block).getMillibuckets();
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(FluidType type, int amount) {
				Fluid fluid = property.fetch(block);
				fluid.grow(amount);
				property.store(block, fluid);
				block.update();
			}


			@Override
			public void remove(int amount) {
				Fluid fluid = property.fetch(block);
				fluid.shrink(amount);
				property.store(block, fluid);
				block.update();
			}


			@Override
			public boolean isEmpty() {
				return property.fetch(block).isEmpty();
			}


			@Override
			public FluidType getType() {
				return property.fetch(block).getType();
			}


			@Override
			public void setType(FluidType type) {
				Fluid fluid = property.fetch(block);
				fluid.setType(type);
				property.store(block, fluid);
				block.update();
			}


			@Override
			public boolean canChangeType(FluidType type) {
				return true;
			}


			@Override
			public boolean match(FluidType type) {
				return this.getType() == type;
			}

		};
	}


	static FluidProvider fromProperty(final FluidType type, final IntegerProperty property, final TileState block, final int capacity) {
		return new FluidProvider() {

			@Override
			public int amount() {
				return property.fetch(block);
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(FluidType type, int amount) {
				property.increase(block, amount);
				block.update();
			}


			@Override
			public void remove(int amount) {
				property.decrease(block, amount);
				block.update();
			}


			@Override
			public FluidType getType() {
				return type;
			}


			@Override
			public void setType(FluidType type) {}


			@Override
			public boolean canChangeType(FluidType type) {
				return false;
			}


			@Override
			public boolean match(FluidType type) {
				return this.getType() == type;
			}

		};
	}

}
