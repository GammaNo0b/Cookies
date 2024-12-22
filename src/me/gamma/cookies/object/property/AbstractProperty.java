
package me.gamma.cookies.object.property;


import java.util.Objects;

import org.bukkit.block.TileState;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;



/**
 * Class to store data in {@link PersistentDataHolder} like {@link TileState} for blocks or {@link ItemMeta} for items.
 * 
 * @author gamma
 *
 * @param <C> the type of the data
 */
public abstract class AbstractProperty<C> {

	/**
	 * Returns the value of the complex type that should be interpreted as empty.
	 * 
	 * @param container the container from which no value could be fetched
	 * @return the empty value
	 */
	public abstract C emptyValue(PersistentDataContainer container);

	/**
	 * Stores the given data in the given {@link PersistentDataContainer}.
	 * 
	 * @param container the {@link PersistentDataContainer}
	 * @param value     the data to be stored
	 */
	public abstract void store(PersistentDataContainer container, C value);


	/**
	 * Stores the given data in the given data holder.
	 * 
	 * @param holder the {@link PersistentDataHolder}
	 * @param value  the data to be stored
	 */
	public final void store(PersistentDataHolder holder, C value) {
		if(holder != null)
			this.store(holder.getPersistentDataContainer(), value);
	}


	/**
	 * Stores the value returned by {@link AbstractProperty#emptyValue(PersistentDataContainer)} in the given data container.
	 * 
	 * @param container the {@link PersistentDataContainer}
	 */
	public abstract void storeEmpty(PersistentDataContainer container);


	/**
	 * Stores the value returned by {@link AbstractProperty#emptyValue(PersistentDataContainer)} in the given data holder.
	 * 
	 * @param holder the {@link PersistentDataHolder}
	 */
	public final void storeEmpty(PersistentDataHolder holder) {
		if(holder != null)
			this.storeEmpty(holder.getPersistentDataContainer());
	}


	/**
	 * Clears the data from the given data container.
	 * 
	 * @param container the {@link PersistentDataContainer}
	 */
	public abstract void remove(PersistentDataContainer container);


	/**
	 * Clears the data from the given data holder.
	 * 
	 * @param holder the {@link PersistentDataHolder}
	 */
	public final void remove(PersistentDataHolder holder) {
		if(holder != null)
			this.remove(holder.getPersistentDataContainer());
	}


	/**
	 * Returns the data from the given data container.
	 * 
	 * @param holder the {@link PersistentDataContainer}
	 * @return the stored data
	 */
	public abstract C fetch(PersistentDataContainer container);


	/**
	 * Returns the data from the given data holder.
	 * 
	 * @param holder the {@link PersistentDataHolder}
	 * @return the stored data
	 */
	public final C fetch(PersistentDataHolder holder) {
		return holder == null ? null : this.fetch(holder.getPersistentDataContainer());
	}


	/**
	 * Returns the data from the given data container or the empty value.
	 * 
	 * @param holder the {@link PersistentDataContainer}
	 * @return the stored data or the {@link AbstractProperty#emptyValue(PersistentDataContainer)}
	 */
	public abstract C fetchEmpty(PersistentDataContainer container);


	/**
	 * Returns the data from the given data holder or the empty value.
	 * 
	 * @param holder the {@link PersistentDataHolder}
	 * @return the stored data or the {@link AbstractProperty#emptyValue(PersistentDataContainer)}
	 */
	public final C fetchEmpty(PersistentDataHolder holder) {
		return holder == null ? null : this.fetchEmpty(holder.getPersistentDataContainer());
	}


	/**
	 * Transfers the data from the first data container to the second data container.
	 * 
	 * @param from the first {@link PersistentDataContainer}
	 * @param to   the second {@link PersistentDataContainer}
	 */
	public final void transfer(PersistentDataContainer from, PersistentDataContainer to) {
		this.store(to, this.fetch(from));
	}


	/**
	 * Transfers the data from the first data holder to the second data holder.
	 * 
	 * @param from the first {@link PersistentDataHolder}
	 * @param to   the second {@link PersistentDataHolder}
	 */
	public final void transfer(PersistentDataHolder from, PersistentDataHolder to) {
		this.transfer(from.getPersistentDataContainer(), to.getPersistentDataContainer());
	}


	/**
	 * Checks if both data holders have the same data stored.
	 * 
	 * @param holder1 the first {@link PersistentDataHolder}
	 * @param holder2 the second {@link PersistentDataHolder}
	 * @return if the data stored is the same for both holders
	 */
	public final boolean isSame(PersistentDataHolder holder1, PersistentDataHolder holder2) {
		return Objects.equals(this.fetch(holder1), this.fetch(holder2));
	}


	/**
	 * Checks if the data container has some data of this property stored.
	 * 
	 * @param container the {@link PersistentDataContainer}
	 * @return if some data is stored
	 */
	public abstract boolean isPropertyOf(PersistentDataContainer container);


	/**
	 * Checks if the data holder has some data of this property stored.
	 * 
	 * @param holder the {@link PersistentDataHolder}
	 * @return if some data is stored
	 */
	public final boolean isPropertyOf(PersistentDataHolder holder) {
		return holder != null && this.isPropertyOf(holder.getPersistentDataContainer());
	}

}
