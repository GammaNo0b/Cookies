
package me.gamma.cookies.object.item;


import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.util.GameProfileHelper;



public abstract class AbstractCustomItem implements IItemSupplier, CustomItemHandler, Configurable {

	public static final StringProperty IDENTIFIER = Properties.IDENTIFIER;

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
	 * @param holder  the {@link PersistentDataHolder}
	 */
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {}


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
	public ItemStack get() {
		return this.get(null);
	}


	@Override
	public ItemStack get(PersistentDataHolder holder) {
		ItemStack stack = new ItemStack(this.getMaterial());
		ItemMeta meta = stack.getItemMeta();

		meta.setDisplayName(this.getTitle());
		meta.setUnbreakable(this.isUnbreakable());

		String texture = this.getBlockTexture();
		if(texture != null && meta instanceof SkullMeta skull)
			GameProfileHelper.setSkullTexture(skull, texture);

		PropertyBuilder builder = this.buildItemProperties(new PropertyBuilder());
		builder.buildAndStore(meta);
		builder.buildAndTransfer(holder, meta);

		this.editItemMeta(meta);
		this.updateDescription(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	/**
	 * Updates the description of the given item meta.
	 * 
	 * @param meta the item meta
	 */
	protected void updateDescription(ItemMeta meta) {
		LoreBuilder builder = new LoreBuilder();
		this.getDescription(builder, meta);
		meta.setLore(builder.build());
	}


	/**
	 * Convenience method to edit the item meta of a new created item of this type. This method get's called directly before the meta will be applied to
	 * the item, meaning it may override values that were set before.
	 * 
	 * @param meta the item meta
	 */
	protected void editItemMeta(ItemMeta meta) {}


	/**
	 * Builds a set containing all properties that are stored inside the item when created.
	 * 
	 * @param builder the property builder
	 * @return the same property builder
	 */
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return builder.add(IDENTIFIER, this.getIdentifier());
	}


	/**
	 * Checks whether the given item stack is an instance of this item.
	 * 
	 * @param stack the item stack
	 * @return if the stack is an instance
	 */
	public boolean isInstanceOf(ItemStack stack) {
		return stack != null && stack.getItemMeta() != null && this.getIdentifier().equals(IDENTIFIER.fetch(stack.getItemMeta()));
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
