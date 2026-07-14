
package me.gamma.cookies.object.item.tools;


import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.Cooldownable;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class RocketLauncher extends AbstractCustomItem implements Cooldownable {

	@Override
	public String getIdentifier() {
		return "rocket_launcher";
	}


	@Override
	public String getTitle() {
		return "§eRocket Launcher";
	}


	@Override
	public Material getMaterial() {
		return Material.STICK;
	}


	@Override
	public long getCooldown(ItemStack stack) {
		// TODO change back to 100
		return 0;
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		customData.setLong(KEY_LAST_USED, 0);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.launchRocket(player, stack);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(super.onBlockRightClick(player, stack, block, event)) {
			this.launchRocket(player, stack);
			return true;
		}

		return false;
	}


	private void launchRocket(Player player, ItemStack stack) {
		World world = player.getWorld();
		if(this.isOnCooldown(world, stack))
			return;

		this.setLastUsed(world, stack);

		double speed = 2.0D;
		Vector velocity = player.getLocation().getDirection().multiply(speed);
		Location start = player.getEyeLocation();
		Firework firework = (Firework) world.spawnEntity(start, EntityType.FIREWORK_ROCKET);
		firework.setVelocity(velocity);
		firework.setMaxLife(Integer.MAX_VALUE);
		firework.setShotAtAngle(true);
		FireworkMeta meta = firework.getFireworkMeta();
		for(int i = 0; i < 8; i++)
			meta.addEffect(FireworkEffect.builder().with(Type.BURST).withColor(Color.SILVER, Color.GRAY, Color.BLACK).build());
		meta.setPower(127);
		firework.setFireworkMeta(meta);
	}

}
