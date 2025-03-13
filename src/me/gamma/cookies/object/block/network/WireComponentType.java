
package me.gamma.cookies.object.block.network;


public enum WireComponentType {

	SUPPLIER,
	STORAGE,
	CONSUMER;

	/**
	 * Checks if this type can transfer energy to the given other type.
	 * 
	 * @param type the other type
	 * @return if energy can be transferred
	 */
	public boolean canTransferTo(WireComponentType type) {
		return this != CONSUMER && type != SUPPLIER;
	}

}
