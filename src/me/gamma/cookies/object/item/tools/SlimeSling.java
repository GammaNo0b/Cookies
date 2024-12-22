
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class SlimeSling extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "slime_sling";
	}


	@Override
	public String getTitle() {
		return "§aSlime Sling";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Yeets you into the air!");
	}


	@Override
	public Material getMaterial() {
		return Material.TRIDENT;
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onTridentThrow(ProjectileLaunchEvent event) {
				if(event.getEntity() instanceof Trident trident && trident.getShooter() instanceof Player player && SlimeSling.this.isInstanceOf(player.getInventory().getItemInMainHand())) {
					event.setCancelled(true);
					if(((Entity) player).isOnGround()) {
						RayTraceResult result = player.rayTraceBlocks(5.0D);
						if(result != null && result.getHitBlock() != null && result.getHitBlock().getType().isSolid()) {
							player.setVelocity(player.getEyeLocation().toVector().subtract(result.getHitPosition()).normalize().multiply(10.0D).multiply(new Vector(1.0D, 0.3D, 1.0D)));
						}
					}
				}
			}

		};
	}

}
