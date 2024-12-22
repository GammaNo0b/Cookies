
package me.gamma.cookies.util;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.object.property.VectorProperty;



public class InventoryUtils {

	private static final String FILLER_STRING = "___filler___";
	private static final StringProperty FILLER_PROPERTY = new StringProperty(FILLER_STRING);

	/**
	 * Creates and returns a copy of the given inventory.
	 * 
	 * @param inventory the inventory
	 * @return the copy
	 */
	public static Inventory copyInventory(Inventory inventory) {
		Inventory copy = Bukkit.createInventory(null, inventory.getType());
		for(int i = 0; i < inventory.getSize(); i++)
			copy.setItem(i, inventory.getItem(i).clone());
		return copy;
	}


	/**
	 * Fills the entire inventory with the given item.
	 * 
	 * @param inventory the inventory
	 * @param item      the item
	 * @return the same inventory instance
	 */
	public static Inventory fillInventory(Inventory inventory, ItemStack item) {
		int size = inventory.getSize();
		for(int i = 0; i < size; i++)
			inventory.setItem(i, item);

		return inventory;
	}


	/**
	 * Fills the border of the given inventory with the given item.
	 * 
	 * @param inventory the inventory
	 * @param border    the border item
	 * @return the same inventory instance
	 */
	public static Inventory fillBorder(Inventory inventory, ItemStack border) {
		if(inventory.getType() != InventoryType.CHEST)
			return inventory;

		int size = inventory.getSize();
		int rows = size / 9;
		for(int i = 1; i < 8; i++) {
			inventory.setItem(i, border);
			inventory.setItem(size - 1 - i, border);
		}
		for(int i = 0; i < rows; i++) {
			inventory.setItem(9 * i, border);
			inventory.setItem(9 * i + 8, border);
		}

		return inventory;
	}


	/**
	 * Fills the top and bottom row of the given inventory with the given item.
	 * 
	 * @param inventory the inventory
	 * @param border    the border item
	 * @return the same inventory
	 */
	public static Inventory fillTopBottom(Inventory inventory, ItemStack border) {
		if(inventory.getType() != InventoryType.CHEST)
			return inventory;

		int size = inventory.getSize();
		for(int i = 0; i < 9; i++) {
			inventory.setItem(i, border);
			inventory.setItem(size - 1 - i, border);
		}

		return inventory;
	}


	/**
	 * Fills the left and right row of the given inventory with the given item.
	 * 
	 * @param inventory the inventory
	 * @param border    the border item
	 * @return the same inventory
	 */
	public static Inventory fillLeftRight(Inventory inventory, ItemStack border) {
		if(inventory.getType() != InventoryType.CHEST)
			return inventory;

		int size = inventory.getSize();
		int rows = size / 9;
		for(int i = 0; i < rows; i++) {
			inventory.setItem(9 * i, border);
			inventory.setItem(9 * i + 8, border);
		}

		return inventory;
	}


	/**
	 * Creates an item with an empty display name to fill an inventory.
	 * 
	 * @param material the material of the item
	 * @return the filler item
	 */
	public static ItemStack filler(Material material) {
		return markFiller(new ItemBuilder(material).setName(" ").build());
	}


	/**
	 * Marks the given item stack as a filler.
	 * 
	 * @param stack the stack
	 * @return the modified filler stack
	 */
	public static ItemStack markFiller(ItemStack stack) {
		if(ItemUtils.isEmpty(stack))
			return null;

		ItemMeta meta = stack.getItemMeta();
		FILLER_PROPERTY.store(meta, FILLER_STRING);
		stack.setItemMeta(meta);
		return stack;
	}


	/**
	 * Checks if an item is a filler item.
	 * 
	 * @param stack the item to be checked
	 * @return if the item is a filler item
	 */
	public static boolean isFiller(ItemStack stack) {
		if(ItemUtils.isEmpty(stack))
			return false;

		return FILLER_STRING.equals(FILLER_PROPERTY.fetch(stack.getItemMeta()));
	}


	/**
	 * Marks the given item stack with the given key name.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 */
	public static void markItem(ItemStack stack, String name) {
		ItemMeta meta = stack.getItemMeta();
		new ByteProperty(name).storeEmpty(meta);
		stack.setItemMeta(meta);
	}


	/**
	 * Unmarks the given item stack with the given key name.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 */
	public static void unmarkItem(ItemStack stack, String name) {
		ItemMeta meta = stack.getItemMeta();
		new ByteProperty(name).remove(meta);
		stack.setItemMeta(meta);
	}


	/**
	 * Checks if the given item stack it marked with the given key name.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 * @return if the stack is marked
	 */
	public static boolean isMarked(ItemStack stack, String name) {
		if(ItemUtils.isEmpty(stack))
			return false;

		return new ByteProperty(name).isPropertyOf(stack.getItemMeta());
	}


	/**
	 * Stores the given integer with the given name as key in the item stack.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 * @param value the value to be stored
	 */
	public static void storeIntInStack(ItemStack stack, String name, int value) {
		ItemMeta meta = stack.getItemMeta();
		new IntegerProperty(name).store(meta, value);
		stack.setItemMeta(meta);
	}


	/**
	 * Reads and returns the stored integer in the given item stack at the given key name.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 * @return the read integer or 0
	 */
	public static int getIntFromStack(ItemStack stack, String name) {
		return getIntFromStack(stack, name, 0);
	}


	/**
	 * Reads and returns the stored integer in the given item stack at the given key name.
	 * 
	 * @param stack  the item stack
	 * @param name   the key name
	 * @param orElse the integer to be returned if no integer was stored
	 * @return the read integer or <code>orElse</code>
	 */
	public static int getIntFromStack(ItemStack stack, String name, int orElse) {
		if(ItemUtils.isEmpty(stack))
			return orElse;

		Integer i = getRawIntFromStack(stack, name);
		return i == null ? orElse : i;
	}


	/**
	 * Reads and returns the stored integer in the given item stack at the given key name or null, if no such key exists.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 * @return the read integer or <code>null</code>
	 */
	public static Integer getRawIntFromStack(ItemStack stack, String name) {
		return new IntegerProperty(name).fetch(stack.getItemMeta());
	}


	/**
	 * Stores the given string with the given name as key in the item stack.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 * @param value the value to be stored
	 */
	public static void storeStringInStack(ItemStack stack, String name, String value) {
		ItemMeta meta = stack.getItemMeta();
		new StringProperty(name).store(meta, value);
		stack.setItemMeta(meta);
	}


	/**
	 * Reads and returns the stored string in the given item stack at the given key name.
	 * 
	 * @param stack the item stack
	 * @param name  the key name
	 * @return the read string or null
	 */
	public static String getStringFromStack(ItemStack stack, String name) {
		if(ItemUtils.isEmpty(stack))
			return null;

		return new StringProperty(name).fetch(stack.getItemMeta());
	}


	/**
	 * Stores the given element with the given name as key in the item stack.
	 * 
	 * @param <E>     the type of the enum element
	 * @param stack   the item stack
	 * @param name    the key name
	 * @param element the enum element to be stored
	 */
	public static <E extends Enum<E>> void storeEnumInStack(ItemStack stack, String name, E element) {
		storeIntInStack(stack, name, element.ordinal());
	}


	/**
	 * Reads and returns the stored enum in the given item stack at the given key name.
	 * 
	 * @param <E>   the type of the enum
	 * @param stack the item stack
	 * @param name  the key name
	 * @param clazz the enum clazz object
	 * @return the read enum or null
	 */
	public static <E extends Enum<E>> E getEnumFromStack(ItemStack stack, String name, Class<E> clazz) {
		Integer i = getRawIntFromStack(stack, name);
		return i == null ? null : EnumUtils.byIndex(clazz, i);
	}


	/**
	 * Stores the coordinates from the given location under the given location key name and the world name in the item stack.
	 * 
	 * @param stack       the stack
	 * @param locationKey the location key
	 * @param worldKey    the world key
	 * @param location    the location
	 */
	public static void storeLocationInStack(ItemStack stack, String locationKey, String worldKey, Location location) {
		ItemMeta meta = stack.getItemMeta();
		new StringProperty(worldKey).store(meta, location.getWorld().getName());
		new VectorProperty(locationKey).store(meta, location.toVector());
		stack.setItemMeta(meta);
	}


	/**
	 * Stores the coordinates from the given location under the given name in the stack.
	 * 
	 * @param stack    the stack
	 * @param name     the name
	 * @param location the location
	 */
	public static void storeLocationInStack(ItemStack stack, String name, Location location) {
		ItemMeta meta = stack.getItemMeta();
		new VectorProperty(name).store(meta, location.toVector());
		stack.setItemMeta(meta);
	}


	/**
	 * Stores the coordinates from the given location under the given name from the given world name in the item stack.
	 * 
	 * @param stack       the stack
	 * @param locationKey the location key
	 * @param worldKey    the world key
	 * @return the stored location
	 */
	public static Location getLocationFromStack(ItemStack stack, String locationKey, String worldKey) {
		if(ItemUtils.isEmpty(stack))
			return null;

		ItemMeta meta = stack.getItemMeta();
		StringProperty worldProperty = new StringProperty(worldKey);
		if(!worldProperty.isPropertyOf(meta))
			return null;

		String name = worldProperty.fetch(meta);
		if(name == null)
			return null;

		World world = Bukkit.getWorld(name);
		if(world == null)
			return null;

		return getLocationFromStack(stack, locationKey, world);
	}


	/**
	 * Reads the under the given name stored coordinates and creates the location with the given world from the given stack and returns it or null.
	 * 
	 * @param stack the stack
	 * @param name  the name
	 * @param world the world
	 * @return the stored location
	 */
	public static Location getLocationFromStack(ItemStack stack, String name, World world) {
		ItemMeta meta = stack.getItemMeta();
		VectorProperty locationProperty = new VectorProperty(name);
		if(!locationProperty.isPropertyOf(meta))
			return null;

		return locationProperty.fetch(meta).toLocation(world);
	}


	/**
	 * Creates the basic gui for the given {@link BlockInventoryProvider}.
	 * 
	 * @param provider the gui provider
	 * @return the created inventory
	 */
	public static <D> Inventory createBasicInventoryProviderGui(InventoryProvider<D> provider, D data) {
		return Bukkit.createInventory(null, provider.rows() * 9, provider.getTitle(data));
	}


	/**
	 * Removes the given material from the dispenser.
	 * 
	 * @param block    the dispenser
	 * @param material the item
	 */
	public static void removeMaterialFromDispenser(Block block, Material material) {
		Utils.runLater(() -> {
			Dispenser dispenser = ((Dispenser) block.getState());
			Inventory inventory = dispenser.getSnapshotInventory();
			for(int i = 0; i < inventory.getSize(); i++) {
				ItemStack item = inventory.getItem(i);
				if(ItemUtils.isType(item, material)) {
					item.setAmount(item.getAmount() - 1);
					dispenser.update();
					return;
				}
			}
		});
	}


	/**
	 * Removes the given item from the dispenser.
	 * 
	 * @param block the dispenser
	 * @param stack the item
	 */
	public static void removeItemFromDispenser(Block block, ItemStack stack) {
		Utils.runLater(() -> {
			Dispenser dispenser = ((Dispenser) block.getState());
			Inventory inventory = dispenser.getSnapshotInventory();
			for(int i = 0; i < inventory.getSize(); i++) {
				ItemStack item = inventory.getItem(i);
				if(ItemUtils.equals(item, stack)) {
					item.setAmount(item.getAmount() - 1);
					dispenser.update();
					return;
				}
			}
		});
	}

}
