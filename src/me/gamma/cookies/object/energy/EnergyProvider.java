
package me.gamma.cookies.object.energy;


import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.TypelessProvider;
import me.gamma.cookies.object.property.EnergyProperty;
import me.gamma.cookies.util.collection.Holder;



public interface EnergyProvider extends TypelessProvider {

	/**
	 * Creates an {@link EnergyProvider} from the {@link PersistentDataHolder} using the {@link EnergyProperty}.
	 * 
	 * @param property the property
	 * @param holder   the data holder
	 * @return the created energy provider
	 */
	static EnergyProvider fromProperty(final EnergyProperty property, final PersistentDataHolder holder) {
		return new EnergyProvider() {

			@Override
			public int amount() {
				return property.fetch(holder);
			}


			@Override
			public int capacity() {
				return property.getMax();
			}


			@Override
			public void add(Void type, int amount) {
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

		};
	}


	/**
	 * Creates an {@link EnergyProvider} with the given capacity from the {@link PersistentDataHolder} using the {@link EnergyProperty}.
	 * 
	 * @param property the property
	 * @param holder   the data holder
	 * @param capacity the capacity
	 * @return the created energy provider
	 */
	static EnergyProvider fromProperty(final EnergyProperty property, final PersistentDataHolder holder, final int capacity) {
		return new EnergyProvider() {

			@Override
			public int amount() {
				return property.fetch(holder);
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(Void type, int amount) {
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

		};
	}


	/**
	 * Creates an {@link EnergyProvider} with the given capacity from the {@link Holder}.
	 * 
	 * @param holder   the holder
	 * @param capacity the capacity
	 * @return the created energy provider
	 */
	static EnergyProvider fromHolder(final Holder<Integer> holder, final int capacity) {
		return new EnergyProvider() {

			@Override
			public int amount() {
				return holder.value;
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(Void type, int amount) {
				holder.value += amount;
			}


			@Override
			public void remove(int amount) {
				holder.value += amount;
			}

		};
	}

}
