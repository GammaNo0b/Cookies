
package me.gamma.cookies.object.tile;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.ConveyorBeltBlock;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ConveyorBelt extends AbstractCustomTileEntity<ConveyorBelt, ConveyorBeltBlock> {

	private static final String KEY_PROGRESS = "progress";
	private static final String KEY_ITEM_ID = "itemid";

	private int progress = 0;
	private Item item = null;

	public ConveyorBelt(ConveyorBeltBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.progress = data.getInteger(KEY_PROGRESS, 0);
		UUID uuid = PersistentDataUtils.getUUID(data, KEY_ITEM_ID);
		if(uuid != null)
			if(Bukkit.getEntity(uuid) instanceof Item item)
				this.item = item;

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_PROGRESS, this.progress);
		if(this.item != null)
			PersistentDataUtils.setUUID(data, KEY_ITEM_ID, this.item.getUniqueId());

		return true;
	}


	@Override
	public ConveyorBelt castTileEntity() {
		return this;
	}


	/**
	 * Tries to pull out an item from the given block. Returns the item stack pulled out.
	 * 
	 * @param block  the supplier block
	 * @param facing the conveyor belt direction
	 * @return the pulled item stack
	 */
	private ItemStack pullItem(Block block, BlockFace facing) {
		ItemSupplier supplier = ItemSupplier.getItemSupplier(block);
		if(supplier == null)
			return null;

		if(!supplier.canAccessItemOutputs(facing.getOppositeFace()))
			return null;

		ItemStack[] ref = { null };
		supplier.removeItem((_, amount) -> Math.min(amount, this.customBlock.getCapacity()), stack -> {
			ref[0] = stack;
			return null;
		});
		return ref[0];
	}


	/**
	 * Tries to push the given item content into the block. Returns if the item entity is empty and removed.
	 * 
	 * @param item   the item entity
	 * @param block  the consumer block
	 * @param facing the conveyor belt direction
	 * @return if the item got stored
	 */
	private boolean pushItem(Item item, Block block, BlockFace facing) {
		item.setVelocity(new Vector());

		ItemStack stack = item.getItemStack();
		if(ItemUtils.isEmpty(stack)) {
			item.remove();
			return true;
		}

		ConveyorBelt belt = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		if(belt != null) {
			if(belt.hasItemOnConveyorBelt())
				return false;

			return belt.putItemOnConveyorBelt(item);
		}

		ItemConsumer consumer = ItemConsumer.getItemConsumer(block);
		if(consumer == null)
			return false;

		if(!consumer.canAccessItemInputs(facing))
			return false;

		stack = consumer.addStack(stack);
		if(ItemUtils.isEmpty(stack)) {
			item.remove();
			return true;
		}

		item.setItemStack(stack);
		return false;
	}


	/**
	 * Checks if the conveyor belt has an item currently transporting.
	 * 
	 * @return if there are items present
	 */
	private boolean hasItemOnConveyorBelt() {
		return this.progress > 0 || this.item != null;
	}


	/**
	 * Tries to put the given item on the conveyor belt.
	 * 
	 * @param item the item enttiy to put on
	 * @return if the entire item was transferred
	 */
	private boolean putItemOnConveyorBelt(Item item) {
		final int capacity = this.customBlock.getCapacity();

		ItemStack stack = item.getItemStack();
		boolean overflow = stack.getAmount() > capacity;
		if(overflow) {
			ItemUtils.increaseItem(stack, -capacity);
			item.setItemStack(stack);

			item = item.getWorld().spawn(item.getLocation(), Item.class);
			stack = stack.clone();
			stack.setAmount(capacity);
			item.setItemStack(stack);

			this.item = item;
		}

		item.setGravity(false);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setUnlimitedLifetime(true);
		item.setInvulnerable(true);
		item.setPortalCooldown(Integer.MAX_VALUE);

		this.progress = this.customBlock.getConveyorLength();

		return !overflow;
	}


	/**
	 * Calculates the front location of the conveyor belt where items move towards.
	 * 
	 * @return the front location
	 */
	private Location calculateConveyorBeltFront() {
		Vector direction = this.customBlock.getFacing(this.block).getDirection();
		return this.block.getLocation().add(0.5D, 0.5D, 0.5D).add(direction.multiply(-0.5D));
	}


	/**
	 * Sets the items velocity so that it will move slowly to the end of the conveyor belt.
	 */
	private void updateItemVelocity() {
		if(this.item == null || this.progress <= 0)
			return;

		Location goal = this.calculateConveyorBeltFront();
		Vector diff = goal.subtract(item.getLocation()).toVector();
		this.item.setVelocity(diff.multiply(1.0D / this.progress));
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		if(this.progress == 0) {
			BlockFace facing = this.customBlock.getFacing(this.block);

			// push items from the conveyor belt
			if(this.item != null)
				if(!this.pushItem(this.item, this.block.getRelative(facing.getOppositeFace()), facing))
					return;

			// search for already existing item entity
			for(Entity e : this.block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.75D, 0.5D), 0.25D + 0.25D * Math.abs(facing.getModX()), 0.25D, 0.25D + 0.25D * Math.abs(facing.getModZ()))) {
				if(e instanceof Item i && i.getPickupDelay() <= 0) {
					this.item = i;
					break;
				}
			}

			// pull item from block
			if(this.item == null) {
				ItemStack stack = this.pullItem(this.block.getRelative(facing), facing);
				if(ItemUtils.isEmpty(stack))
					return;

				Location pos = block.getLocation().add(0.5D, 0.5D, 0.5D).add(facing.getDirection().multiply(0.5));
				this.item = pos.getWorld().spawn(pos, Item.class);
				this.item.setItemStack(stack);
			}

			this.putItemOnConveyorBelt(item);
			this.updateItemVelocity();
		} else {
			this.updateItemVelocity();
			this.progress--;
		}
	}


	/**
	 * Releases the item on this conveyor belt.
	 */
	public void freeItem() {
		if(this.item == null)
			return;

		this.item.setGravity(true);
		this.item.setPickupDelay(20);
		this.item.setUnlimitedLifetime(false);
		this.item.setInvulnerable(false);
		this.item.setPortalCooldown(0);

		Vector random = Vector.getRandom().add(new Vector(-0.5D, 0.0D, -0.5D));
		this.item.setVelocity(this.item.getVelocity().add(random));

		this.item = null;
		this.progress = 0;
	}

}
