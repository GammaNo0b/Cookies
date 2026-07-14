
package me.gamma.cookies.object.block;


import static me.gamma.cookies.object.tile.Backpack.KEY_UUID;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.tile.Backpack;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class BackpackBlock extends AbstractCustomTileBlock<BackpackBlock, Backpack> {

	private final String identifier;
	private final String title;
	private final String texture;
	private final int rows;

	public BackpackBlock(String identifier, String title, String texture, int rows) {
		this.identifier = identifier;
		this.title = title;
		this.texture = texture;
		this.rows = rows;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	public String getTitle() {
		return this.title;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	public int getRows() {
		return this.rows;
	}


	public int getSize() {
		return this.rows * 9;
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		PersistentDataUtils.setUUID(itemData, KEY_UUID, PersistentDataUtils.getUUID(itemData, KEY_UUID));
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		Backpack backpack = this.getTileEntity(block);
		if(backpack == null)
			return true;

		if(!backpack.canAccess(player)) {
			player.sendMessage("§cYou do not own this backpack!");
			return true;
		}

		player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		player.openInventory(backpack.getInventory());
		return true;
	}


	@Override
	public BackpackBlock castCustomBlock() {
		return this;
	}


	@Override
	public Backpack createNewTileEntity(Block block) {
		return new Backpack(this, block);
	}

}
