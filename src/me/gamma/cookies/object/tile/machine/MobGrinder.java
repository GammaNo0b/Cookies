
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.machine.MobGrinderBlock;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftEntityHelper;



public class MobGrinder extends AbstractGuiMachine<MobGrinder, MobGrinderBlock> {

	private static final String KEY_HIT_TICKS = "hitticks";

	private int hitTicks = 0;

	public MobGrinder(MobGrinderBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.hitTicks = data.getInteger(KEY_HIT_TICKS, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_HIT_TICKS, this.hitTicks);

		return true;
	}


	@Override
	protected boolean run() {
		BlockFace facing = ((Rotatable) block.getBlockData()).getRotation();
		Vector direction = facing.getDirection();
		int hit = 0;
		for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D).subtract(direction), 0.5D, 2.5D, 0.5D)) {
			if(entity instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) entity;
				if(living.isDead() || living.isInvulnerable() || living.getNoDamageTicks() > 0)
					continue;

				Player owner = this.getOwningPlayer();
				if(owner != null)
					MinecraftEntityHelper.setLastHurtByPlayer(living, owner);
				living.damage(this.customBlock.getDamage());

				if(hit++ > this.customBlock.getMaxHits())
					break;
			}
		}
		return hit > 0;
	}


	@Override
	public MobGrinder castTileEntity() {
		return this;
	}

}
