
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Goat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import me.gamma.cookies.util.ItemUtils;



public class DispenserMilkFeature extends SimpleCookieListener {

	private static final Class<?>[] milkable = { Cow.class, Goat.class };

	public DispenserMilkFeature() {
		super("dispenser_milk");
	}


	@EventHandler
	public void onDispense(BlockDispenseEvent event) {
		if(!this.isEnabled())
			return;

		Block block = event.getBlock();
		if(block.getType() != Material.DISPENSER)
			return;

		ItemStack stack = event.getItem();
		if(!ItemUtils.isType(stack, Material.BUCKET) || stack.getAmount() != 1)
			return;

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

		DispenserBucketCauldronFeature.insertOrDispense(event, new ItemStack(Material.MILK_BUCKET));
	}

}
