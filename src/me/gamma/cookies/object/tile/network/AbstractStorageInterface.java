
package me.gamma.cookies.object.tile.network;


import org.bukkit.Chunk;
import org.bukkit.block.Block;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.block.AdvancedFilterBlock;
import me.gamma.cookies.object.block.network.AbstractStorageInterfaceBlock;
import me.gamma.cookies.object.block.network.NetworkInterfaceBlock;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractStorageInterface<R, F extends Filter<R>, T extends AbstractStorageInterface<R, F, T, B>, B extends AbstractStorageInterfaceBlock<R, B, T>> extends AbstractStorageComponent<R, T, B> implements NetworkInterface<R>, AdvancedFilterBlock<R, F> {

	private static final String KEY_PRIORITY = "priority";
	private static final String KEY_CHANNEL = "channel";
	private static final String KEY_FILTER = "filter";

	private int priority;
	private int channel;
	private F filter;

	public AbstractStorageInterface(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.priority = data.getInteger(KEY_PRIORITY, 0);
		this.channel = data.getInteger(KEY_CHANNEL, 0);
		PersistentDataObject o = data.getObject(KEY_FILTER);
		if(o != null)
			this.filter = this.loadFilter(o);

		if(this.filter == null)
			this.filter = this.emptyFilter();

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_PRIORITY, this.priority);
		data.setInteger(KEY_CHANNEL, this.channel);

		PersistentDataObject o = new PersistentDataObject(data.getAdapterContext());
		this.saveFilter(o, this.filter);
		data.setObject(KEY_FILTER, o);

		return true;
	}


	/**
	 * Creates a new empty filter in case an actual filter could not be loaded.
	 * 
	 * @return the empty filter
	 */
	protected abstract F emptyFilter();

	/**
	 * Loads a filter from the given data.
	 * 
	 * @param data the data
	 * @return the loaded filter
	 */
	protected abstract F loadFilter(PersistentDataObject data);

	/**
	 * Saves the given filter to the given data.
	 * 
	 * @param data   the data
	 * @param filter the filter
	 */
	protected abstract void saveFilter(PersistentDataObject data, F filter);


	@Override
	public NetworkInterfaceBlock<R> getComponentBlock() {
		return this.customBlock;
	}


	@Override
	public int getPriority() {
		return this.priority;
	}


	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}


	@Override
	public int getChannel() {
		return this.channel;
	}


	@Override
	public void setChannel(int channel) {
		this.channel = channel;
	}


	@Override
	public F getFilter() {
		return this.filter;
	}


	@Override
	public void setFilter(F filter) {
		this.filter = filter;
	}


	@Override
	public String getFilterTitle() {
		return this.customBlock.getFilterTitle();
	}

}
