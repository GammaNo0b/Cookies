
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;



public class FlameArrowIngniteFeature extends SimpleCookieListener {

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(!this.enabled)
			return;
		
		Projectile entity = event.getEntity();
		if(entity instanceof Arrow arrow) {
			if(arrow.getFireTicks() > 0) {
				if(event.getHitBlock() != null) {
					BlockFace face = event.getHitBlockFace();
					Block block = event.getHitBlock().getRelative(face);
					if(block.getType() == Material.AIR) {
						block.setType(Material.FIRE);
						if(face != BlockFace.UP) {
							BlockData data = block.getBlockData();
							if(data instanceof Fire fire) {
								fire.setFace(face.getOppositeFace(), true);
								block.setBlockData(fire);
							}
						}
					}
				}
			}
		}
	}

}
