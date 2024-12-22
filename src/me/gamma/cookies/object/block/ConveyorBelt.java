
package me.gamma.cookies.object.block;


import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.UUIDProperty;
import me.gamma.cookies.util.ItemUtils;



public class ConveyorBelt extends AbstractWorkBlock implements Cartesian {

	public static final IntegerProperty PROGRESS = Properties.PROGRESS;
	public static final UUIDProperty ITEM_ENTITY_ID = new UUIDProperty("entityid");

	private final ConveyorBelt.Type type;

	private int capacity;
	private int conveyorLength;

	public ConveyorBelt(ConveyorBelt.Type type) {
		this.type = type;
	}


	@Override
	public ConfigurationSection getConfig() {
		return Config.BLOCKS.getConfig().getConfigurationSection("conveyor_belt").getConfigurationSection(this.type.name().toLowerCase());
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
		this.conveyorLength = config.getInt("conveyorLength", 20);
	}


	@Override
	public String getIdentifier() {
		return this.type.name().toLowerCase() + "_conveyor_belt";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MISSING_TEXTURE;
	}


	@Override
	public PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(PROGRESS).add(ITEM_ENTITY_ID);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		if(Bukkit.getEntity(ITEM_ENTITY_ID.fetch(block)) instanceof Item item)
			item.remove();

		return false;
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		if(Bukkit.getEntity(ITEM_ENTITY_ID.fetch(block)) instanceof Item item)
			drops.add(item.getItemStack());

		return drops;
	}


	/**
	 * Tries to pull out an item from the given block. Returns the item stack pulled out.
	 * 
	 * @param block  the supplier block
	 * @param facing the conveyor belt direction
	 * @return the pulled item stack
	 */
	private ItemStack pullItem(Block block, BlockFace facing) {
		if(!(block.getState() instanceof TileState state))
			return null;

		ItemSupplier supplier = ItemSupplier.getItemSupplier(state);
		if(supplier == null)
			return null;

		if(!supplier.canAccessItemOutputs(state, facing.getOppositeFace()))
			return null;

		ItemStack[] ref = { null };
		supplier.removeItem(state, (type, amount) -> Math.min(amount, this.capacity), stack -> {
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

		if(block.getState() instanceof TileState state) {
			if(Blocks.getCustomBlockFromBlock(state) instanceof ConveyorBelt conveyorBelt) {
				if(this.hasItemOnConveyorBelt(state))
					return false;

				return conveyorBelt.putItemOnConveyorBelt(state, item);
			}

			ItemConsumer consumer = ItemConsumer.getItemConsumer(state);
			if(consumer == null)
				return false;

			if(!consumer.canAccessItemInputs(state, facing))
				return false;

			stack = consumer.addStack(state, stack);
			if(ItemUtils.isEmpty(stack)) {
				item.remove();
				return true;
			}

			item.setItemStack(stack);
		}
		return false;
	}


	/**
	 * Checks if the conveyor belt has an item currently transporting.
	 * 
	 * @param block the conveyor belt block
	 * @return if there are items present
	 */
	private boolean hasItemOnConveyorBelt(TileState block) {
		return PROGRESS.fetch(block) > 0 || Bukkit.getEntity(ITEM_ENTITY_ID.fetch(block)) != null;
	}


	/**
	 * Tries to put the given item on the conveyor belt.
	 * 
	 * @param block the conveyor belt block
	 * @param item  the item enttiy to put on
	 * @return if the entire item was transferred
	 */
	private boolean putItemOnConveyorBelt(TileState block, Item item) {
		ItemStack stack = item.getItemStack();
		boolean overflow = stack.getAmount() > this.capacity;
		if(overflow) {
			ItemUtils.increaseItem(stack, -this.capacity);
			item.setItemStack(stack);

			item = item.getWorld().spawn(item.getLocation(), Item.class);
			stack = stack.clone();
			stack.setAmount(this.capacity);
			item.setItemStack(stack);
		}

		item.setGravity(false);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setUnlimitedLifetime(true);
		item.setGravity(false);
		item.setInvulnerable(true);
		item.setPortalCooldown(Integer.MAX_VALUE);

		PROGRESS.store(block, this.conveyorLength);
		ITEM_ENTITY_ID.store(block, item.getUniqueId());
		block.update();

		return !overflow;
	}


	/**
	 * Calculates the front location of the conveyor belt where items move towards.
	 * 
	 * @param block the conveyor belt block
	 * @return the front location
	 */
	private Location calculateConveyorBeltFront(TileState block) {
		Vector direction = this.getFacing(block).getDirection();
		return block.getLocation().add(0.5D, 0.5D, 0.5D).add(direction.multiply(-0.5D));
	}


	/**
	 * Sets the items velocity so that it will move slowly to the end of the conveyor belt.
	 * 
	 * @param block the conveyor belt block
	 */
	private void updateItemVelocity(TileState block) {
		int progress = PROGRESS.fetch(block);
		UUID itemEntityId = ITEM_ENTITY_ID.fetch(block);
		Entity entity = Bukkit.getEntity(itemEntityId);
		if(!(entity instanceof Item item))
			return;

		Location goal = this.calculateConveyorBeltFront(block);
		Vector diff = goal.subtract(item.getLocation()).toVector();
		item.setVelocity(diff.multiply(1.0D / progress));
	}


	@Override
	public void tick(TileState block) {
		int progress = PROGRESS.fetch(block);
		if(progress == 0) {
			BlockFace facing = this.getFacing(block);

			// push items from the conveyor belt
			UUID itemEntityId = ITEM_ENTITY_ID.fetch(block);
			Entity entity = Bukkit.getEntity(itemEntityId);
			if(entity instanceof Item item) {
				if(!this.pushItem(item, block.getBlock().getRelative(facing.getOppositeFace()), facing))
					return;
			}

			Item item = null;

			// search for already existing item entity
			for(Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.75D, 0.5D), 0.25D + 0.25D * Math.abs(facing.getModX()), 0.25D, 0.25D + 0.25D * Math.abs(facing.getModZ()))) {
				if(e instanceof Item i && i.getPickupDelay() <= 0) {
					item = i;
					break;
				}
			}

			// pull item from block
			if(item == null) {
				ItemStack stack = this.pullItem(block.getBlock().getRelative(facing), facing);
				if(ItemUtils.isEmpty(stack)) {
					ITEM_ENTITY_ID.storeEmpty(block);
					block.update();
					return;
				}

				Location pos = block.getLocation().add(0.5D, 0.5D, 0.5D).add(facing.getDirection().multiply(0.5));
				item = pos.getWorld().spawn(pos, Item.class);
				item.setItemStack(stack);
			}

			this.putItemOnConveyorBelt(block, item);
			this.updateItemVelocity(block);
		} else {
			this.updateItemVelocity(block);
			PROGRESS.store(block, progress - 1);
			block.update();
		}
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(Bukkit.getEntity(ITEM_ENTITY_ID.fetch(block)) instanceof Item item) {
			ItemUtils.dropItem(item.getItemStack(), block);
			item.remove();
			ITEM_ENTITY_ID.storeEmpty(block);
			block.update();
		}

		return true;
	}


	@Override
	protected int getWorkPeriod() {
		return 1;
	}

	public static enum Type {
		LIGHT,
		MEDIUM,
		HEAVY;
	}

}
