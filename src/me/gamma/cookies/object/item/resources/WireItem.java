
package me.gamma.cookies.object.item.resources;


import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.block.network.WireHolder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class WireItem extends AbstractCustomItem {

	public static final String KEY_HOOK_POS = "hookpos";

	private final String name;
	private final String identifier;
	private final String texture;

	private final int transfer;

	public WireItem(String name, String texture, int transfer) {
		this.name = name;
		this.identifier = Utils.getIdentifierFromName(name);
		this.texture = texture;
		this.transfer = transfer;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	protected String getBlockTexture() {
		return this.texture;
	}


	public int getTransfer() {
		return this.transfer;
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection("", false).add("§7Transfers §b" + this.transfer + " §cCC/t").build().createSection("", false).add("§7Shift-Right click to set first position.").add("§7Right click to connect wire.");
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return true;

		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		if(tileEntity == null || !(tileEntity instanceof WireHolder holder))
			return true;

		if(player.isSneaking()) {
			PersistentDataUtils.setVector(data.getData(), KEY_HOOK_POS, block.getLocation().toVector());
			data.save();
		} else {
			Vector pos = PersistentDataUtils.getVector(data.getData(), KEY_HOOK_POS);
			if(pos != null && !block.getLocation().toVector().equals(pos) && holder.createWire(pos.toLocation(block.getWorld()), this))
				if(player.getGameMode() == GameMode.SURVIVAL)
					ItemUtils.increaseItem(stack, -1);
		}

		return super.onBlockRightClick(player, stack, block, event);
	}


	@Override
	public boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return true;

		data.getData().remove(KEY_HOOK_POS);
		data.save();

		return true;
	}


	@Override
	public boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		if(tileEntity == null || !(tileEntity instanceof WireHolder holder))
			return true;

		Wire<?> wire = holder.removeWire();
		if(wire != null)
			ItemUtils.giveItemToPlayer(player, wire.getWireItem().get());

		return true;
	}

}
