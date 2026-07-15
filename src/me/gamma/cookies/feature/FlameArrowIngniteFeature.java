
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

	public FlameArrowIngniteFeature() {
		super("flaming_arrow_ingite");
	}


	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(!this.isEnabled())
			return;

		Projectile entity = event.getEntity();
		if(!(entity instanceof Arrow arrow))
			return;

		if(arrow.getFireTicks() <= 0)
			return;

		if(event.getHitBlock() == null)
			return;

		BlockFace face = event.getHitBlockFace();
		Block block = event.getHitBlock().getRelative(face);
		if(block.getType() != Material.AIR)
			return;

		block.setType(Material.FIRE);
		if(face == BlockFace.UP)
			return;

		BlockData data = block.getBlockData();
		if(data instanceof Fire fire) {
			fire.setFace(face.getOppositeFace(), true);
			block.setBlockData(fire);
		}
	}

}
