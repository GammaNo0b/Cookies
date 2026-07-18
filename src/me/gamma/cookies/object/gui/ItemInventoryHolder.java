
package me.gamma.cookies.object.gui;


import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;



/**
 * Represents an inventory holder, that allows for a certain part of it's inventory to be seen as a sub-inventory.
 */
public interface ItemInventoryHolder {

	/**
	 * The actual inventory of this holder.
	 * 
	 * @return the actual inventory
	 */
	Inventory getInventory();

	/**
	 * The slots of the base inventory of which items can be inserted in. Returns null if the entire inventory can be used for inputs.
	 * 
	 * @return the input slots
	 */
	int[] getInputSlots();

	/**
	 * The slots of the base inventory of which items can be taken out. Returns null if the entire inventory can be used for outputs.
	 * 
	 * @return the output slots
	 */
	int[] getOutputSlots();


	/**
	 * Returns an inventory for storing items inside this inventory holder.
	 * 
	 * @return the input inventory
	 */
	default Inventory createInputInventory() {
		int[] inputSlots = this.getInputSlots();
		if(inputSlots == null)
			return this.getInventory();

		return createSubInventory(this.getInventory(), inputSlots);
	}


	/**
	 * Returns an inventory for fetching items from this inventory holder.
	 * 
	 * @return the output inventory
	 */
	default Inventory createOutputInventory() {
		int[] outputSlots = this.getOutputSlots();
		if(outputSlots == null)
			return this.getInventory();

		return createSubInventory(this.getInventory(), outputSlots);
	}


	/**
	 * Returns an inventory for both input and output slots of this this inventory holder.
	 * 
	 * @return the inventory
	 */
	default Inventory createInputOutputInventory() {
		int[] slots1 = this.getInputSlots();
		int[] slots2 = this.getOutputSlots();

		if(slots1 == null || slots2 == null)
			return this.getInventory();

		int[] slots = new int[slots1.length + slots2.length];
		if(slots1[slots1.length - 1] > slots2[slots2.length - 1]) {
			int[] tmp = slots1;
			slots1 = slots2;
			slots2 = tmp;
		}

		int i1 = 0, i2 = 0;
		int dups = 0;
		while(i1 < slots1.length) {
			int s1 = slots1[i1];
			int s2 = slots2[i2];
			int s;
			if(s1 <= s2) {
				s = s1;
				++i1;
				if(s1 == s2) {
					++i2;
					++dups;
				}
			} else {
				s = s2;
				++i2;
			}
			slots[i1 + i2 - 1] = s;
		}

		if(dups > 0) {
			int[] nondupslots = new int[slots.length - dups];
			for(int i = 0; i < nondupslots.length; ++i)
				nondupslots[i] = slots[i];
			slots = nondupslots;
		}

		return createSubInventory(this.getInventory(), slots);
	}


	static Inventory createSubInventory(Inventory inventory, int[] slots) {
		if(slots == null)
			return inventory;

		if(slots.length == 0 || !(inventory instanceof CraftInventory craftInventory))
			return null;

		return new CraftInventory(new SubContainer(craftInventory.getInventory(), slots));
	}

}
