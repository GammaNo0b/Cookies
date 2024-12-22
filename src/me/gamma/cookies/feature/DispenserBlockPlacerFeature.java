
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;



public class DispenserBlockPlacerFeature extends SimpleCookieListener {

	@EventHandler
	public void onDispense(BlockDispenseEvent event) {
		if(!this.enabled)
			return;

		ItemStack stack = event.getItem();
		if(ItemUtils.isEmpty(stack) || !stack.getType().isBlock() || Items.getCustomItemFromStack(stack) instanceof AbstractBlockItem)
			return;

		Block block = event.getBlock();
		Directional dispenser = (Directional) block.getBlockData();
		BlockFace facing = dispenser.getFacing();
		Block target = block.getRelative(facing);

		if(target.getType() != Material.AIR)
			return;

		event.setCancelled(true);

		target.setType(stack.getType());
		BlockData data = target.getBlockData();
		if(data instanceof Directional)
			((Directional) data).setFacing(facing);

		InventoryUtils.removeItemFromDispenser(block, stack);
	}

}
