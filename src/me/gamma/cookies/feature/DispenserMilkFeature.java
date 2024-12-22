
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Goat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class DispenserMilkFeature extends SimpleCookieListener {

	private static final Class<?>[] milkable = { Cow.class, Goat.class };

	@EventHandler
	public void onDispense(BlockDispenseEvent event) {
		if(!this.enabled)
			return;

		ItemStack stack = event.getItem();
		if(!ItemUtils.isType(stack, Material.BUCKET))
			return;

		Block block = event.getBlock();
		World world = block.getWorld();
		Directional dispenser = (Directional) block.getBlockData();
		BlockFace facing = dispenser.getFacing();
		Block target = block.getRelative(facing);
		BoundingBox box = new BoundingBox(target.getX(), target.getY(), target.getZ(), target.getX() + 1, target.getY() + 1, target.getZ() + 1);

		boolean milked = false;
		for(Entity entity : world.getEntitiesByClasses(milkable)) {
			if(box.overlaps(entity.getBoundingBox())) {
				milked = true;
				break;
			}
		}

		if(!milked)
			return;

		event.setCancelled(true);

		Vector velocity = event.getVelocity();
		Utils.runLater(() -> {
			Container state = (Container) block.getState();
			Inventory inventory = state.getSnapshotInventory();
			inventory.removeItem(new ItemStack(Material.BUCKET));
			if(!inventory.addItem(new ItemStack(Material.MILK_BUCKET)).isEmpty())
				world.dropItem(block.getLocation().add(facing.getDirection()).add(0.5D, 0.3D, 0.5D), new ItemStack(Material.MILK_BUCKET)).setVelocity(velocity);
			state.update();
		});
	}

}
