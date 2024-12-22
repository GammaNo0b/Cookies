
package me.gamma.cookies.object.multiblock;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;



public abstract class MultiBlock {

	private final String name;

	public MultiBlock(String name) {
		this.name = name;
	}


	/**
	 * Returns the name of this multi block.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Returns a three domensional array of materials to represent this multi block.
	 * 
	 * @return the structure
	 */
	public abstract Material[][][] getStructure();

	/**
	 * Returns the trhee dimensional vector that points to the block that executes the {@link MultiBlock#onClick(Player, Location, PlayerInteractEvent)}
	 * method when clicked.
	 * 
	 * @return the trigger position
	 */
	public abstract Vector getTrigger();

	/**
	 * Get's executed when the block positioned at the return value of {@link MultiBlock#getTrigger()} is clicked on.
	 * 
	 * @param player   the player who clicked
	 * @param location the location of the block
	 * @param event    the {@link PlayerInteractEvent}
	 */
	public abstract void onClick(Player player, Location location, PlayerInteractEvent event);


	/**
	 * Checks if the given block is valid for the given relative position.
	 * 
	 * @param block the block
	 * @param pos   the position
	 * @return if the block is valid for the position
	 */
	protected boolean checkBlock(Block block, Vector pos) {
		Material material = this.getMaterial(pos);
		return material == null || material == block.getType();
	}


	/**
	 * Returns the material that has to be clicked for this multi block to get triggered.
	 * 
	 * @return the material
	 */
	public Material getTriggerMaterial() {
		return this.getMaterial(this.getTrigger());
	}


	/**
	 * Returns the material at the given relative position.
	 * 
	 * @param pos the position
	 * @return the material
	 */
	public Material getMaterial(Vector pos) {
		return this.getStructure()[pos.getBlockY()][pos.getBlockZ()][pos.getBlockX()];
	}


	/**
	 * Returns the width of this multi block.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return this.getLength() > 0 ? this.getStructure()[0][0].length : 0;
	}


	/**
	 * Returns the length of this mutli block.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return this.getHeight() > 0 ? this.getStructure()[0].length : 0;
	}


	/**
	 * Returns the height of the multiblock.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return this.getStructure().length;
	}


	/**
	 * Checks if at the given block is a valid structure for this multiblock present.
	 * 
	 * @param trigger the trigger block
	 * @return if the structure is valid
	 */
	public boolean isMultiBlock(Block trigger) {
		for(int d = 0; d < 8; d++) {
			int dirx = (d % 2) * 2 - 1;
			int dirz = (d % 4) / 2 * 2 - 1;
			boolean swap = d >= 4;
			Location start = this.getStartRelative(trigger.getLocation(), dirx, dirz, swap);
			boolean valid = true;
			for(int y = 0; valid && y < this.getHeight(); y++) {
				for(int z = 0; valid && z < this.getLength(); z++) {
					for(int x = 0; valid && x < this.getWidth(); x++) {
						int cx = x * dirx;
						int cz = z * dirz;
						Location current = start.clone().add(swap ? cz : cx, y, swap ? cx : cz);
						if(!this.checkBlock(current.getBlock(), new Vector(x, y, z))) {
							valid = false;
						}
					}
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
