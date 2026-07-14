
package me.gamma.cookies.object.tile.network.pipes.item;


import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.tile.network.pipes.PipePacket;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemPacket extends PipePacket {

	private static final String KEY_ITEM_ID = "itemid";

	private ItemStack stack;
	private ItemDisplay item;

	public ItemPacket() {}


	public ItemPacket(ItemStack stack) {
		this.stack = stack;
	}


	public ItemStack getStack() {
		return this.stack;
	}


	public void setStack(ItemStack stack) {
		this.stack = stack;
		if(this.item != null)
			this.item.setItemStack(stack);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		UUID uuid = PersistentDataUtils.getUUID(data, KEY_ITEM_ID);
		if(uuid == null)
			return false;

		for(Entity entity : chunk.getEntities()) {
			if(entity.getUniqueId().equals(uuid)) {
				if(!(entity instanceof ItemDisplay item))
					return false;

				this.item = item;
				this.stack = item.getItemStack();
				return true;
			}
		}

		return false;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setUUID(data, KEY_ITEM_ID, this.item.getUniqueId());

		return true;
	}


	@Override
	protected void setPosition(Location position) {
		super.setPosition(position);

		position.subtract(0.0D, 0.25D, 0.0D);

		if(this.item == null) {
			this.item = position.getWorld().spawn(position, ItemDisplay.class);
			this.item.setItemStack(this.stack);
			this.item.setGravity(false);
			this.item.setInvulnerable(true);
			this.item.setPortalCooldown(Integer.MAX_VALUE);
			this.item.setItemDisplayTransform(ItemDisplayTransform.GROUND);
			this.item.setBrightness(new Brightness(15, 15));
			this.item.setShadowRadius(0.0F);
			this.item.setShadowStrength(0.0F);
		} else {
			this.item.setTeleportDuration(0);
			this.item.teleport(position);
		}
	}


	@Override
	protected void setTarget(Location target) {
		this.item.setTeleportDuration((int) this.ticksLeft);
		this.item.teleport(target.subtract(0.0D, 0.25D, 0.0D));
	}


	@Override
	protected void remove() {
		super.remove();

		this.item.remove();
	}


	@Override
	protected void drop() {
		super.drop();

		this.item.getWorld().dropItemNaturally(this.item.getLocation(), this.item.getItemStack());
		this.item.remove();
	}

}
