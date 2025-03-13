
package me.gamma.cookies.object.fluid;


import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

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
	 * Creates a {@link FluidProvider} from the given {@link PersistentDataHolder} using the {@link FluidProperty}.
	 * 
	 * @param property the property
	 * @param holder   the data holder
	 * @return the created fluid provider
	 */
	static FluidProvider fromProperty(final FluidProperty property, final PersistentDataHolder holder) {
		return fromProperty(property, holder, Integer.MAX_VALUE);
	}


	/**
	 * Creates a {@link FluidProvider} from the given {@link PersistentDataHolder} using the {@link FluidProperty}.
	 * 
	 * @param property the property
	 * @param holder   the data holder
	 * @param capacity the maximum amount of fluid that can be stored
	 * @return the created fluid provider
	 */
	static FluidProvider fromProperty(final FluidProperty property, final PersistentDataHolder holder, final int capacity) {
		return new FluidProvider() {

			@Override
			public int amount() {
				return property.fetch(holder).getMillibuckets();
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(FluidType type, int amount) {
				Fluid fluid = property.fetch(holder);
				fluid.grow(amount);
				property.store(holder, fluid);
				if(holder instanceof TileState block)
					block.update();
			}


			@Override
			public void remove(int amount) {
				Fluid fluid = property.fetch(holder);
				fluid.shrink(amount);
				property.store(holder, fluid);
				if(holder instanceof TileState block)
					block.update();
			}


			@Override
			public boolean isEmpty() {
				return property.fetch(holder).isEmpty();
			}


			@Override
			public FluidType getType() {
				return property.fetch(holder).getType();
			}


			@Override
			public void setType(FluidType type) {
				Fluid fluid = property.fetch(holder);
				fluid.setType(type);
				property.store(holder, fluid);
				if(holder instanceof TileState block)
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


	static FluidProvider fromProperty(final FluidType type, final IntegerProperty property, final PersistentDataHolder holder, final int capacity) {
		return new FluidProvider() {

			@Override
			public int amount() {
				return property.fetch(holder);
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(FluidType type, int amount) {
				property.increase(holder, amount);
				if(holder instanceof TileState block)
					block.update();
			}


			@Override
			public void remove(int amount) {
				property.decrease(holder, amount);
				if(holder instanceof TileState block)
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
