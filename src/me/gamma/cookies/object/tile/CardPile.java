
package me.gamma.cookies.object.tile;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.CardPileBlock;
import me.gamma.cookies.object.block.CardPileBlock.Card;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CardPile extends AbstractCustomTileEntity<CardPile, CardPileBlock> implements ContainerTile {

	public static final String KEY_CARDS = "cards";
	public static final String KEY_FACE_UP = "faceup";

	private final List<ItemStack> cards = new ArrayList<>();
	private boolean faceUp = false;

	public CardPile(CardPileBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		if(PersistentDataUtils.getList(data, KEY_CARDS, this.cards, PersistentDataUtils::loadItemStack) == null)
			return false;

		this.faceUp = data.getBoolean(KEY_FACE_UP, true);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setList(data, KEY_CARDS, this.cards, PersistentDataUtils::saveItemStack);
		data.setBoolean(KEY_FACE_UP, this.faceUp);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		this.updateCards();
	}


	/**
	 * Adds the given card to the card pile at the given block. If the item is a card that can be added, true is returned, otherwise false.
	 * 
	 * @param stack the card
	 * @return if the card could be added
	 */
	public boolean addCard(ItemStack stack) {
		if(!Items.isInstanceOf(stack, Card.class))
			return false;

		ItemStack clone = stack.clone();
		clone.setAmount(1);
		this.cards.add(clone);
		this.updateCards();
		stack.setAmount(stack.getAmount() - 1);
		return true;
	}


	/**
	 * Removes the topmost card and returns it or null if the pile is empty.
	 * 
	 * @return the card
	 */
	public ItemStack removeCard() {
		if(this.cards.isEmpty())
			return null;

		ItemStack card = this.cards.remove(this.cards.size() - 1);
		this.updateCards();
		return card;
	}


	/**
	 * Updates the cards of the pile of the given block.
	 */
	private void updateCards() {
		Inventory gui = this.getInventory();
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		int size = this.cards.size();
		int start = Math.max(0, size - 10);
		for(int i = 0; i < 10; i++) {
			int row = (i & 1) + 1;
			int col = (i >> 1) + 2;
			int slot = row * 9 + col;
			int index = start + i;
			ItemStack stack = filler;
			if(index < size) {
				stack = this.cards.get(index);
				if(this.faceUp)
					stack = this.generateFlippedCard(stack);
			}
			gui.setItem(slot, stack);
		}
	}

	/**
	 * All colorcodes except for white (f), gray (7), dark gray (8) and black (0).
	 */
	private static final char[] colorcodes = "1234569abcde".toCharArray();

	/**
	 * Number of questionmarks a flipped card displays.
	 */
	private static final int questionmarks = 3;

	/**
	 * Returns a new card icon with pseudo-randomly generated name.
	 * 
	 * @param card the original card
	 * @return the new card icon
	 */
	private ItemStack generateFlippedCard(ItemStack card) {
		long hashcode = card.hashCode() & Long.MAX_VALUE;
		long mod = colorcodes.length;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < questionmarks; i++) {
			builder.append('§');
			builder.append(colorcodes[(int) (hashcode % mod)]);
			builder.append('?');
			hashcode /= mod;
		}
		return new ItemBuilder(Material.PAPER).setName(builder.toString()).build();
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 4) {
			this.faceUp = !this.faceUp;
			this.updateCards();
		} else {
			int row = slot / 9;
			int column = slot - row * 9;
			if(1 <= row && row <= 2 && 1 <= column && column <= 7)
				ItemUtils.giveItemToPlayer(player, this.removeCard());
		}

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		this.addCard(event.getCurrentItem());
		return true;
	}


	@Override
	public CardPile castTileEntity() {
		return this;
	}


	@Override
	public BlockInventoryProvider getInventoryProvider() {
		return this.customBlock;
	}

}
