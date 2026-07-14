
package me.gamma.cookies.object.block;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.ChunkPersistentDataStorage;
import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.util.GameProfileHelper;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractCustomBlock implements CustomBlockHandler, Configurable {

	protected static final StringProperty IDENTIFIER = Properties.IDENTIFIER;

	private IItemSupplier mainDrop = null;

	public AbstractCustomBlock() {
		if(this instanceof WorldPersistentDataStorage storage)
			storage.register();

		if(this instanceof ChunkPersistentDataStorage storage)
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
	 * Sets the {@link IItemSupplier} that should be dropped when this block is broken. Set to null for no main drop.
	 * 
	 * @param mainDrop the main drop
	 */
	public void setMainDrop(IItemSupplier mainDrop) {
		this.mainDrop = mainDrop;
	}


	/**
	 * Returns the item this block drops when broken.
	 * 
	 * @param block the block brokent
	 * @return the item to drop
	 */
	protected ItemStack getMainDrop(Block block) {
		if(this.mainDrop == null)
			return null;

		return this.mainDrop.get(data -> this.getCustomDropData(block, data));
	}


	/**
	 * Checks if the given block has the material of this custom block.
	 * 
	 * @param block the block
	 * @return if the block has the correct material
	 */
	public boolean checkMaterial(Block block) {
		Material type = this.getMaterial();
		if(type == Material.PLAYER_HEAD || type == Material.PLAYER_WALL_HEAD) {
			return block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD;
		} else {
			return block.getType() == type;
		}
	}


	/**
	 * Stores the custom data to put on the drop of this block.
	 * 
	 * @param block the block param the custom data
	 */
	protected void getCustomDropData(Block block, PersistentDataObject data) {}


	@Override
	public boolean placeBlock(Block block, Material type) {
		// set correct material
		if(!this.checkMaterial(block))
			block.setType(type);

		// set texture
		String texture = this.getBlockTexture();
		if(texture != null && block.getLocation().getBlock().getState() instanceof Skull skull)
			GameProfileHelper.setSkullTexture(skull, texture);

		return true;
	}


	@Override
	public boolean placeBlock(Block block) {
		return this.placeBlock(block, this.getMaterial());
	}


	@Override
	public boolean onBlockPlace(Block block, PersistentDataObject data, Player player, BlockPlaceEvent event) {
		if(!CustomBlockHandler.super.onBlockPlace(block, data, player, event))
			return false;

		if(this instanceof Cartesian cartesian && cartesian.shouldCorrectFacing(block))
			cartesian.correctFacing(block);

		return true;
	}


	@Override
	public void blockPlaced(Block block) {
		CustomBlockHandler.super.blockPlaced(block);

		CustomBlockStorage.BLOCK_STORAGE.addCustomBlock(this, block);
	}


	@Override
	public void blockBroken(Block block) {
		ItemUtils.dropItem(this.getMainDrop(block), block);

		CustomBlockStorage.BLOCK_STORAGE.removeCustomBlock(block);
	}


	@Override
	public boolean onLeavesDecay(Block block, LeavesDecayEvent event) {
		CustomBlockStorage.BLOCK_STORAGE.removeCustomBlock(block);

		return true;
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
