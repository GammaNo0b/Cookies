
package me.gamma.cookies.objects.block.multi;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;



public abstract class MultiBlock {

	public abstract Material[][][] getStructure();

	public abstract Vector getTrigger();

	public abstract void onClick(Player player, Location location);


	protected boolean checkBlock(Block block, Vector pos) {
		Material material = this.getMaterial(pos);
		return material == null || material == block.getType();
	}


	public Material getTriggerMaterial() {
		return this.getMaterial(this.getTrigger());
	}


	public Material getMaterial(Vector pos) {
		return this.getStructure()[pos.getBlockY()][pos.getBlockZ()][pos.getBlockX()];
	}


	public int getWidth() {
		return this.getLength() > 0 ? this.getStructure()[0][0].length : 0;
	}


	public int getLength() {
		return this.getHeight() > 0 ? this.getStructure()[0].length : 0;
	}


	public int getHeight() {
		return this.getStructure().length;
	}


	public boolean isMultiBlock(Block trigger) {
		for(int d = 0; d < 8; d++) {
			int dirx = (d % 2) * 2 - 1;
			int dirz = (d % 4) / 2 * 2 - 1;
			boolean swap = d >= 4;
			Location start = this.getStartRelative(trigger.getLocation(), dirx, dirz, swap);
			boolean valid = true;
			for(int y = 0; y < this.getHeight(); y++) {
				for(int z = 0; z < this.getLength(); z++) {
					for(int x = 0; x < this.getWidth(); x++) {
						int cx = x * dirx;
						int cz = z * dirz;
						Location current = start.clone().add(swap ? cz : cx, y, swap ? cx : cz);
						if(!this.checkBlock(current.getBlock(), new Vector(x, y, z))) {
							valid = false;
							break;
						}
					}
					if(!valid) {
						break;
					}
				}
				if(!valid) {
					break;
				}
			}
			if(valid)
				return true;
		}
		return false;
	}


	private Location getStartRelative(Location trigger, int dirx, int dirz, boolean swap) {
		int x = this.getTrigger().getBlockX();
		int y = this.getTrigger().getBlockY();
		int z = this.getTrigger().getBlockZ();
		Vector v = new Vector((swap ? z : x) * dirx, y, (swap ? x : z) * dirz);
		return trigger.clone().subtract(v);
	}

}
