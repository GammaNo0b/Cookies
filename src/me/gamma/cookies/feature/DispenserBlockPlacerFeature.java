
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.Utilities;



public class DispenserBlockPlacerFeature implements CookieListener {

	@EventHandler
	public void onDispense(BlockDispenseEvent event) {
		ItemStack stack = event.getItem();
		if(Utilities.isEmpty(stack) || !stack.getType().isBlock() || CustomBlockSetup.getCustomBlockFromStack(stack) != null)
			return;

		event.setCancelled(true);

		Block block = event.getBlock();
		Dispenser dispenser = (Dispenser) block.getBlockData();
		BlockFace facing = dispenser.getFacing();
		Block target = block.getRelative(facing);

		if(target.getType() != Material.AIR)
			return;

		target.setType(stack.getType());
		BlockData data = target.getBlockData();
		if(data instanceof Directional)
			((Directional) data).setFacing(facing);

		BlockState state = block.getState();
		((BlockInventoryHolder) state).getInventory().remove(stack);
		state.update();
	}

}
