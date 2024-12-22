
package me.gamma.cookies.object.network;


public interface TransferRate<T> {

	/**
	 * Returns the amount of resources of the specified type a network can transfer in one tick.
	 * 
	 * @param type the resource type
	 * @return the transfer rate
	 */
	int getTransferRate(T type);

}
