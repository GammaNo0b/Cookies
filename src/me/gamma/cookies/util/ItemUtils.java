
package me.gamma.cookies.util;


import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Cable;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.property.AbstractProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.core.MinecraftItemHelper;



public class ItemUtils {

	/**
	 * Checks if the given stack is either {@code null}, has {@link Material#AIR} as type has a size of 0.
	 * 
	 * @param stack the stack to be checked
	 * @return if the stack is empty
	 */
	public static boolean isEmpty(ItemStack stack) {
		return stack == null || stack.getType() == Material.AIR || stack.getAmount() <= 0;
	}


	/**
	 * Checks if the given stack is not empty and has the given type.
	 * 
	 * @param stack the stack to be checked
	 * @param type  the type
	 * @return if the stack has the given type
	 */
	public static boolean isType(ItemStack stack, Material type) {
		return stack != null && stack.getAmount() > 0 && stack.getType() == type;
	}


	/**
	 * Checks if the given stack is a custom item from this plugin.
	 * 
	 * @param stack the stack to be checked
	 * @return if the stack is custom
	 */
	public static boolean isCustomItem(ItemStack stack) {
		return !isEmpty(stack) && Properties.IDENTIFIER.isPropertyOf(stack.getItemMeta());
	}


	/**
	 * Returns a copy of the given stack of the given amount.
	 * 
	 * @param stack  the stack
	 * @param amount the amount
	 * @return the cloned stack of the given amount
	 */
	public static ItemStack cloneItem(ItemStack stack, int amount) {
		if(isEmpty(stack))
			return null;

		ItemStack clone = stack.clone();
		clone.setAmount(amount);
		return clone;
	}


	/**
	 * Increases the amount of the given stack by the given amount.
	 * 
	 * @param stack  the stack
	 * @param amount the amount
	 */
	public static void increaseItem(ItemStack stack, int amount) {
		if(isEmpty(stack))
			return;

		stack.setAmount(stack.getAmount() + amount);
	}


	/**
	 * Damages the given item by the given amount.
	 * 
	 * @param stack  the stack
	 * @param damage the damage
	 */
	public static void damageItem(ItemStack stack, int damage) {
		if(isEmpty(stack))
			return;

		if(!(stack.getItemMeta() instanceof Damageable meta))
			return;

		meta.setDamage(meta.getDamage() + damage);
		stack.setItemMeta(meta);
	}


	/**
	 * Creates a map containing the items as key and their amount in the iterator as value.
	 * 
	 * @param iterator the iterator of items
	 * @return map linking the amount of items to their type
	 */
	public static Map<ItemStack, Integer> listItems(Iterator<ItemStack> iterator) {
		Map<ItemStack, Integer> map = new HashMap<>();
		while(iterator.hasNext()) {
			ItemStack stack = iterator.next();
			if(!isEmpty(stack)) {
				ItemStack type = stack.clone();
				type.setAmount(1);
				map.put(type, map.getOrDefault(type, 0) + stack.getAmount());
			}
		}
		return map;
	}


	/**
	 * Removes the items in the given iterator from the given map. If more items were to be removed than available, false is returned, otherwise true.
	 * 
	 * @param items    the items
	 * @param iterator the items to remove
	 * @return whether the items could be removed
	 */
	public static boolean removeItems(Map<ItemStack, Integer> items, Iterator<ItemStack> iterator) {
		while(iterator.hasNext()) {
			ItemStack stack = iterator.next();
			if(!isEmpty(stack)) {
				ItemStack type = stack.clone();
				type.setAmount(1);
				int amount = items.getOrDefault(type, 0);
				amount -= stack.getAmount();
				if(amount < 0)
					return false;

				items.put(type, amount);
			}
		}
		return true;
	}


	/**
	 * Stores multiple stacks of the given type if one single stack could not hold the entire specified amount.
	 * 
	 * @param type       the item
	 * @param amount     the amount
	 * @param collection the collection in which to store the stacks
	 */
	public static void getItems(ItemStack type, int amount, Collection<? super ItemStack> collection) {
		if(isEmpty(type))
			return;

		for(int max = type.getMaxStackSize(); amount >= 0; amount -= max)
			collection.add(cloneItem(type, Math.min(amount, max)));
	}


	/**
	 * Returns the display name of the item stack if present, otherwise the material name.
	 * 
	 * @param stack the stack
	 * @return the name
	 */
	public static String getItemName(ItemStack stack) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : Utils.toCapitalWords(stack.getType());
	}


	/**
	 * Returns the material that remains after one crafting operation.
	 * 
	 * @param type the crafting material
	 * @return the remaining material
	 */
	public static Material getCraftingRemainingItem(Material type) {
		switch (type) {
			case WATER_BUCKET:
			case LAVA_BUCKET:
			case MILK_BUCKET:
				return Material.BUCKET;
			case DRAGON_BREATH:
			case HONEY_BOTTLE:
			case POTION:
				return Material.GLASS_BOTTLE;
			default:
				return null;
		}
	}


	/**
	 * Checks if the two stacks are from the same type.
	 * 
	 * @param stack1 first stack
	 * @param stack2 second stack
	 * @return if the stacks are similar
	 */
	public static boolean similar(ItemStack stack1, ItemStack stack2) {
		boolean empty = isEmpty(stack1);
		if(empty ^ isEmpty(stack2))
			return false;

		if(empty)
			return true;

		if(stack1.getType() != stack2.getType())
			return false;

		final boolean custom1 = isCustomItem(stack1);
		final boolean custom2 = isCustomItem(stack2);

		if(custom1 != custom2)
			return false;

		return !custom1 || Properties.IDENTIFIER.isSame(stack1.getItemMeta(), stack2.getItemMeta());
	}


	/**
	 * Checks if the two stacks are from the same type and match on the given properties.
	 * 
	 * @param stack1     first stack
	 * @param stack2     second stack
	 * @param properties the properties
	 * @return if the stack match on the given properties
	 */
	public static boolean match(ItemStack stack1, ItemStack stack2, AbstractProperty<?>... properties) {
		if(!similar(stack1, stack2))
			return false;

		if(stack1 == null || stack2 == null)
			return false;

		ItemMeta meta1 = stack1.getItemMeta();
		ItemMeta meta2 = stack2.getItemMeta();

		for(AbstractProperty<?> property : properties) {
			if(property.isPropertyOf(meta1) != property.isPropertyOf(meta2))
				return false;

			if(!property.isSame(meta1, meta2))
				return false;
		}

		return true;
	}


	/**
	 * Checks if the two stacks are similar and have the same data.
	 * 
	 * @param stack1 first stack
	 * @param stack2 second stack
	 * @return if the stacks are equal
	 */
	public static boolean equals(ItemStack stack1, ItemStack stack2) {
		return stack1 == null ? stack2 == null : stack1.isSimilar(stack2);
	}


	/**
	 * Checks if all the given stacks are equal.
	 * 
	 * @param stacks the stacks to be checked
	 * @return if all stacks are equal
	 */
	public static boolean equals(ItemStack... stacks) {
		if(stacks.length < 2) {
			return true;
		}
		ItemStack stack = stacks[0];
		for(int i = 1; i < stacks.length; i++) {
			ItemStack other = stacks[i];
			if(equals(stack, other)) {
				stack = other;
			} else {
				return false;
			}
		}
		return true;
	}


	/**
	 * Checks if the given stack is enchanted with at least one enchantment.
	 * 
	 * @param stack the stack
	 * @return whether the stack is enchanted or not
	 */
	public static boolean isEnchanted(ItemStack stack) {
		if(isEmpty(stack))
			return false;

		return stack.getEnchantments().size() > 0;
	}


	/**
	 * Checks if the given material is a flower type.
	 * 
	 * @param m the material to be checked
	 * @return if the material is a flower
	 */
	public static boolean isFlower(Material m) {
		return Tag.SMALL_FLOWERS.isTagged(m);
	}


	/**
	 * Checks if the given material is a block on that a flower can be placed.
	 * 
	 * @param m the material to be checked
	 * @return if flowers can be placed on top of this material
	 */
	public static boolean isFlowerSoil(Material m) {
		return Tag.DIRT.isTagged(m) || m == Material.FARMLAND;
	}


	/**
	 * Returns the amount of ticks this stack will burn in a furnace.
	 * 
	 * @param stack the stack to be checked
	 * @return the amount of ticks it will burn
	 */
	public static int getFuel(ItemStack stack) {
		return MinecraftItemHelper.getFuel(stack);
	}


	/**
	 * Returns the chance the given material increases the composter level. Returns -1 if the material cannot be composted.
	 * 
	 * @param material the material
	 * @return the composting chance
	 */
	public static float getCompostChance(Material material) {
		return MinecraftItemHelper.getCompostChance(material);
	}


	/**
	 * Subtracts the amount of the subtrahend from the amount of the minuend if the stacks are similar.
	 * 
	 * @param minuend    the stack from that items should be subtracted
	 * @param subtrahend the stack that should be subtracted
	 * @return the resulting stack
	 */
	public static ItemStack subtractItemStack(ItemStack minuend, ItemStack subtrahend) {
		if(subtrahend == null || !minuend.isSimilar(subtrahend)) {
			return minuend;
		}
		minuend.setAmount(minuend.getAmount() - Math.min(minuend.getAmount(), subtrahend.getAmount()));
		return minuend;
	}


	/**
	 * Inserts the given stack into the given slot stack. The resultung slot stack is returned while the stack is updated in case of items beeing
	 * inserted.
	 * 
	 * @param stack the stack to store
	 * @param slot  the stack to store into
	 * @return the new slot stack with items beeing stored (or not)
	 */
	public static ItemStack insertItemStack(ItemStack stack, ItemStack slot) {
		if(ItemUtils.isEmpty(stack))
			return slot;

		if(ItemUtils.isEmpty(slot)) {
			slot = stack.clone();
			stack.setAmount(0);
			return slot;
		}

		if(!stack.isSimilar(slot))
			return slot;

		int transfer = Math.min(stack.getAmount(), slot.getMaxStackSize() - slot.getAmount());
		increaseItem(stack, -transfer);
		increaseItem(slot, transfer);
		return slot;
	}


	/**
	 * Transfers the given item from the given block in all directions.
	 * 
	 * @param stack the stack to be transfered
	 * @param block the block from which the stack should be transfered
	 * @return the rest of the stack that could not be transfered
	 */
	public static ItemStack transferItem(ItemStack stack, Block block) {
		return transferItem(stack, block, BlockUtils.cartesian);
	}


	/**
	 * Transfers the given item from the given block in every of the given directions.
	 * 
	 * @param stack      the stack to be transfered
	 * @param block      the block from which the stack should be transfered
	 * @param directions the directions in which the stack should be tried to be transfered
	 * @return the rest of the stack that could not be transfered
	 */
	public static ItemStack transferItem(ItemStack stack, Block block, BlockFace[] directions) {
		for(BlockFace face : directions) {
			if(face != null) {
				Block relative = block.getRelative(face);
				BlockState state = relative.getState();
				boolean isCustom = false;
				if(state instanceof TileState) {
					TileState tile = (TileState) state;
					AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(tile);
					if(custom != null) {
						isCustom = true;
						if(custom instanceof ItemConsumer consumer) {
							int amount = stack.getAmount();
							for(Provider<ItemStack> provider : consumer.getItemInputs(tile)) {
								amount = Cable.distribute(Cable.TransferMode.ORDERED, stack, amount, Arrays.asList(provider));
								if(amount <= 0)
									return null;
							}
							stack.setAmount(amount);
						}
					}
				}
				if(!isCustom && relative.getState() instanceof BlockInventoryHolder holder) {
					Map<Integer, ItemStack> rest = holder.getInventory().addItem(stack);
					if(rest.isEmpty())
						return null;
					for(ItemStack restStack : rest.values()) {
						if(restStack != null) {
							stack = restStack;
							continue;
						}
					}
				}
			}
		}
		return stack;
	}


	/**
	 * Fills the empty slots of the given inventory with the given item.
	 * 
	 * @param inventory the inventory
	 * @param filler    the item
	 * @return the filled inventory
	 */
	public static Inventory fillEmptySlotsWith(Inventory inventory, ItemStack filler) {
		for(int i = 0; i < inventory.getSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if(stack == null) {
				inventory.setItem(i, filler);
			}
		}
		return inventory;
	}


	/**
	 * Gives the given item stack to the player and drops what the player could not hold to the ground.
	 * 
	 * @param player the player
	 * @param stack  the item stack to be given to the player
	 */
	public static void giveItemToPlayer(HumanEntity player, ItemStack stack) {
		if(!isEmpty(stack))
			for(ItemStack rest : player.getInventory().addItem(stack).values())
				if(!isEmpty(rest))
					dropItem(rest, player.getLocation());
	}


	/**
	 * Gives the given item stacks to the player and drops what the player could not hold to the ground.
	 * 
	 * @param player the player
	 * @param stacks the item stacks to be given to the player
	 */
	public static void giveItemsToPlayer(HumanEntity player, Collection<ItemStack> stacks) {
		for(ItemStack stack : stacks)
			giveItemToPlayer(player, stack);
	}


	/**
	 * Drops the given stack at the given location to the ground.
	 * 
	 * @param stack    the stack to be dropped
	 * @param location the location at which the stack should be dropped
	 */
	public static void dropItem(ItemStack stack, Location location) {
		if(!isEmpty(stack))
			location.getWorld().dropItemNaturally(location, stack);
	}


	/**
	 * Drops the given stack at the center of the given block to the ground.
	 * 
	 * @param stack the stack to be dropped
	 * @param block the block at which the stack should be dropped
	 */
	public static void dropItem(ItemStack stack, Block block) {
		dropItem(stack, block.getLocation().add(0.5D, 0.5D, 0.5D));
	}


	/**
	 * Drops the given stack at the center of the given block state to the ground.
	 * 
	 * @param stack the stack to be dropped
	 * @param state the block state at which the stack should be dropped
	 */
	public static void dropItem(ItemStack stack, BlockState state) {
		dropItem(stack, state.getLocation().add(0.5D, 0.5D, 0.5D));
	}

}
