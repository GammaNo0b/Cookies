
package me.gamma.cookies.object.gui.task;


import org.bukkit.inventory.Inventory;



public class StaticInventoryTask extends InventoryTask {

	public StaticInventoryTask(Inventory inventory) {
		super(inventory, 0);
	}


	@Override
	public void run() {}


	@Override
	public void start() {}


	@Override
	public void stop() {}


	@Override
	protected void updateInventory() {}

}
