
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.util.GameProfileHelper;
import me.gamma.cookies.util.ItemUtils;



public abstract class AbstractCustomBlock implements CustomBlockHandler, Configurable {

	protected static final StringProperty IDENTIFIER = Properties.IDENTIFIER;

	private IItemSupplier mainDrop = null;

	public AbstractCustomBlock() {
		if(this instanceof WorldPersistentDataStorage storage)
			storage.register();
	}


	@Override
	public ConfigurationSection getConfig() {
		return Config.BLOCKS.getConfig().getConfigurationSection(this.getIdentifier());
	}


	/**
	 * Returns the unique identifier for this custom block.
	 * 
	 * @return the unique identifier
	 */
	public abstract String getIdentifier();


	/**
	 * Returns the material of this item. The default is {@link Material#PLAYER_HEAD}.
	 * 
	 * @return the material
	 */
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	/**
	 * Returns the block texture of this block or null, if the block should not have a texture.
	 * 
	 * @return the texture
	 */
	public String getBlockTexture() {
		return HeadTextures.MISSING_TEXTURE;
	}


	/**
	 * Returns the list of items this block drops when destroyed.
	 * 
	 * @param block the block to be broken
	 * @return the list of drops
	 */
	public List<ItemStack> getDrops(TileState block) {
		return new ArrayList<>();
	}


	/**
	 * Sets the {@link IItemSupplier} that should be dropped when this block is broken. Set to null for no main drop.
	 * 
	 * @param mainDrop the main drop
	 */
	public void setMainDrop(IItemSupplier mainDrop) {
		this.mainDrop = mainDrop;
	}


	/**
	 * Builds a set containing all properties that are stored inside the block when placed but not the original item.
	 * 
	 * @param builder the property builder
	 * @return the same property builder
	 */
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return builder.add(IDENTIFIER, this.getIdentifier());
	}


	/**
	 * Builds a set containing all properties that gets transferred from the original item to the block when placed.
	 * 
	 * @param builder the property builder
	 * @return the same property builder
	 */
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return builder;
	}


	@Override
	public boolean place(Player player, ItemStack stack, Block block) {
		// check for placing permission
		if(!this.canPlace(player, block))
			return false;

		// set block material and texture
		block.setType(this.getMaterial());
		String texture = this.getBlockTexture();
		if(texture != null && block.getState() instanceof Skull skull)
			GameProfileHelper.setSkullTexture(skull, texture);

		// rotate rotational blocks
		if(player != null) {
			BlockData data = block.getBlockData();
			if(data instanceof Rotatable rotatable) {
				int x = (int) Math.floor((player.getLocation().getYaw() + 225.0F) / 90.0F);
				rotatable.setRotation(BlockFace.values()[x & 3]);
				block.setBlockData(data);
			} else if(data instanceof Directional directional) {
				int x = (int) Math.floor((player.getLocation().getYaw() + 45.0F) / 90.0F);
				directional.setFacing(BlockFace.values()[x & 3]);
				block.setBlockData(data);
			}
		}

		// IMPORTANT
		if(this.onBlockPlace(player, stack == null ? null : stack.getItemMeta(), (TileState) block.getState()))
			return false;

		// decrease stack size
		if(player != null && player.getGameMode() == GameMode.SURVIVAL)
			stack.setAmount(stack.getAmount() - 1);

		return true;
	}


	@Override
	public boolean onBlockPlace(Player player, PersistentDataHolder holder, TileState block) {
		this.buildBlockItemProperties(new PropertyBuilder()).buildAndTransfer(holder, block);
		this.buildBlockProperties(new PropertyBuilder()).buildAndStore(block);

		if(this instanceof Ownable ownable) {
			if(player == null)
				return true;

			ownable.setOwner(block, player.getUniqueId());
		}

		block.update();

		if(this instanceof BlockRegister register)
			register.getLocations().add(block.getLocation());

		if(this instanceof Cartesian cartesian && cartesian.shouldCorrectFacing(block))
			cartesian.correctFacing(block);

		return false;
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(this instanceof Ownable ownable && !ownable.canAccess(block, player))
			return true;

		if(this.mainDrop != null)
			ItemUtils.dropItem(this.mainDrop.get(block), block);

		if(this instanceof BlockRegister register)
			register.getLocations().remove(block.getLocation());

		for(ItemStack stack : this.getDrops(block))
			ItemUtils.dropItem(stack, block);

		return false;
	}


	/**
	 * Returns a custom event listener for this block or null.
	 * 
	 * @return the custom listener or null
	 */
	public Listener getListener() {
		return null;
	}


	/**
	 * Checks if this block provides a custom listener by checking if the returned value of {@link AbstractCustomBlock#getListener()} is non-null.
	 * 
	 * @return if this block has a custom listener
	 */
	public boolean hasListener() {
		return this.getListener() != null;
	}


	/**
	 * Checks if the given {@link PersistentDataHolder} is an instance of this custom data holder.
	 * 
	 * @param holder the persistent data holder
	 * @return if the given holder is an instance of this block
	 */
	public final boolean isInstanceOf(PersistentDataHolder holder) {
		return this.getIdentifier().equals(IDENTIFIER.fetch(holder));
	}


	/**
	 * Checks if the given {@link TileState} is an instance of this custom block.
	 * 
	 * @param block the block
	 * @return if the block is an instance of this block
	 */
	public final boolean isInstanceOf(TileState block) {
		return this.isInstanceOf((PersistentDataHolder) block);
	}

}
