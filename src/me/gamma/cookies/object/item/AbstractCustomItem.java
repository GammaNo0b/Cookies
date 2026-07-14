
package me.gamma.cookies.object.item;


import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.util.GameProfileHelper;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractCustomItem implements IItemSupplier, CustomItemHandler, Configurable {

	public static final String KEY_IDENTIFIER = "itemid";

	/**
	 * Returns the custom data of the given item stack.
	 * 
	 * @param stack the item stack
	 * @return the custom data
	 */
	public static CustomItemData getCustomData(ItemStack stack) {
		return CustomItemData.getCustomItemData(stack);
	}


	/**
	 * Returns the identifier of the given custom data.
	 * 
	 * @param data the custom data
	 * @return the idenfitier
	 */
	public static String getIdentifier(CustomItemData data) {
		return data.getData().getString(KEY_IDENTIFIER);
	}


	/**
	 * Returns the identifier of the given item stack.
	 * 
	 * @param stack the item stack
	 * @return the identifier
	 */
	public static String getIdentifier(ItemStack stack) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return null;

		return getIdentifier(data);
	}


	@Override
	public ConfigurationSection getConfig() {
		return Config.ITEMS.getConfig().getConfigurationSection(this.getIdentifier());
	}


	/**
	 * Returns the unique identifier for this custom block.
	 * 
	 * @return the unique identifier
	 */
	public abstract String getIdentifier();

	/**
	 * Returns the display name for this item.
	 * 
	 * @return the display name
	 */
	public abstract String getTitle();


	/**
	 * Builds the lore of this item.
	 * 
	 * @param builder the lore builder
	 * @param meta    the item meta
	 * @param data    the custom data
	 */
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {}


	/**
	 * Initializes custom data for an item.
	 * 
	 * @param customData the custom data
	 */
	protected void createData(PersistentDataObject customData) {
		customData.setString(KEY_IDENTIFIER, this.getIdentifier());
	}


	/**
	 * Returns the material of this item. The default is {@link Material#PLAYER_HEAD}.
	 * 
	 * @return the material
	 */
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	/**
	 * Returns the block texture of this item or null, if the item should not have a texture.
	 * 
	 * @return the texture
	 */
	protected String getBlockTexture() {
		return null;
	}


	/**
	 * Returns true if this item should be unbreakable.
	 * 
	 * @return if this item should be unbreakable
	 */
	public boolean isUnbreakable() {
		return false;
	}


	@Override
	public ItemStack get(Consumer<PersistentDataObject> dataConsumer) {
		ItemStack stack = new ItemStack(this.getMaterial());
		ItemMeta meta = stack.getItemMeta();

		meta.setDisplayName(this.getTitle());
		meta.setUnbreakable(this.isUnbreakable());

		String texture = this.getBlockTexture();
		if(texture != null && meta instanceof SkullMeta skull)
			GameProfileHelper.setSkullTexture(skull, texture);

		PersistentDataObject data = new PersistentDataObject(meta.getPersistentDataContainer().getAdapterContext());
		this.createData(data);
		dataConsumer.accept(data);

		this.editItemMeta(meta, data);
		this.updateDescription(meta, data);

		CustomItemData.initCustomData(meta, data);

		stack.setItemMeta(meta);
		return stack;
	}


	/**
	 * Updates the description of an item with the given item meta and custom data.
	 * 
	 * @param meta the item meta
	 * @param data the custom data
	 */
	protected void updateDescription(ItemMeta meta, PersistentDataObject data) {
		LoreBuilder builder = new LoreBuilder();
		this.buildDescription(builder, meta, data);
		meta.setLore(builder.build());
	}


	/**
	 * Updates the description of the given item.
	 * 
	 * @param stack the item
	 */
	public void updateDescription(ItemStack stack) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return;

		ItemMeta meta = stack.getItemMeta();
		this.updateDescription(meta, data.getData());
		stack.setItemMeta(meta);
	}


	/**
	 * Convenience method to edit the item meta of a new created item of this type. This method get's called directly before the meta will be applied to
	 * the item, meaning it may override values that were set before.
	 * 
	 * @param meta the item meta
	 * @param data the custom data
	 */
	protected void editItemMeta(ItemMeta meta, PersistentDataObject data) {}


	/**
	 * Checks whether the given item stack is an instance of this item.
	 * 
	 * @param stack the item stack
	 * @return if the stack is an instance
	 */
	public boolean isInstanceOf(ItemStack stack) {
		return this.getIdentifier().equals(getIdentifier(stack));
	}


	/**
	 * Returns a custom event listener for this item or null.
	 * 
	 * @return the custom listener or null
	 */
	public Listener getListener() {
		return null;
	}


	/**
	 * Checks if this item provides a custom listener by checking if the returned value of {@link AbstractCustomBlock#getListener()} is non-null.
	 * 
	 * @return if this item has a custom listener
	 */
	public boolean hasListener() {
		return this.getListener() != null;
	}

}
