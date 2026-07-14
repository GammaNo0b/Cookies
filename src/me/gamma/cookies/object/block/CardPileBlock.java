
package me.gamma.cookies.object.block;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.CardPile;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CardPileBlock extends AbstractCustomTileBlock<CardPileBlock, CardPile> implements UpdatingGuiProvider {

	@Override
	public String getIdentifier() {
		return "card_pile";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.PLAYING_CARD_PILE;
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		itemData.setBoolean(CardPile.KEY_FACE_UP, tileData.getBoolean(CardPile.KEY_FACE_UP, true));
		itemData.setObjectList(CardPile.KEY_CARDS, tileData.getObjectList(CardPile.KEY_CARDS));
	}


	@Override
	public boolean onBlockBreak(Player player, Block block, BlockBreakEvent event) {
		if(!super.onBlockBreak(player, block, event))
			return false;

		this.unregisterInventory(block);

		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		CardPile pile = this.getTileEntity(block);
		if(pile == null)
			return true;

		if(player.isSneaking()) {
			ItemUtils.giveItemToPlayer(player, pile.removeCard());
		} else if(!pile.addCard(stack)) {
			this.openGui(player, block, true, false);
		}

		return true;
	}


	@Override
	public String getTitle(Block data) {
		return "§fPlaying Card Pile";
	}


	@Override
	public int rows() {
		return 4;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public Inventory createGui(Block data) {
		Inventory gui = UpdatingGuiProvider.super.createGui(data);
		InventoryUtils.fillBorder(gui, InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		gui.setItem(10, filler);
		gui.setItem(25, filler);
		filler = InventoryUtils.filler(Material.RED_STAINED_GLASS_PANE);
		gui.setItem(19, filler);
		gui.setItem(16, filler);
		gui.setItem(4, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aFlip Card Pile").build());
		return gui;
	}

	/**
	 * Represents a card that can be stored in a card pile.
	 * 
	 * @author gamma
	 *
	 */
	public static interface Card {}

	@Override
	public CardPileBlock castCustomBlock() {
		return this;
	}


	@Override
	public CardPile createNewTileEntity(Block block) {
		return new CardPile(this, block);
	}


	@Override
	public ContainerTile getContainer(Block block) {
		return this.getTileEntity(block);
	}

}
