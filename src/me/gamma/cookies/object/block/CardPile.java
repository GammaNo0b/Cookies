
package me.gamma.cookies.object.block;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class CardPile extends AbstractCustomBlock implements UpdatingGuiProvider {

	public static final ListProperty<ItemStack, ItemStackProperty> CARDS = new ListProperty<>("cards", ItemStackProperty::new);
	public static final BooleanProperty FACE_UP = new BooleanProperty("faceup");

	private final Set<Location> locations = new HashSet<>();
	private final Map<Location, Inventory> inventorymap = new HashMap<>();

	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public Map<Location, Inventory> getInventoryMap() {
		return this.inventorymap;
	}


	@Override
	public String getIdentifier() {
		return "card_pile";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.PLAYING_CARD_PILE;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(FACE_UP);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(CARDS);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		this.unregisterInventory(block);
		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(player.isSneaking()) {
			ItemUtils.giveItemToPlayer(player, this.removeCard(block));
		} else if(!this.addCard(block, stack)) {
			this.openGui(player, block, true, false);
		}

		return true;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public String getTitle(TileState data) {
		return "§fPlaying Card Pile";
	}


	@Override
	public int rows() {
		return 4;
	}


	@Override
	public Inventory createGui(TileState data) {
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


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		this.updateCards(block);
	}


	/**
	 * Adds the given card to the card pile at the given block. If the item is a card that can be added, true is returned, otherwise false.
	 * 
	 * @param block the block
	 * @param stack the card
	 * @return if the card could be added
	 */
	private boolean addCard(TileState block, ItemStack stack) {
		if(!Items.isInstanceOf(stack, Card.class))
			return false;

		List<ItemStack> cards = CARDS.fetchEmpty(block);
		ItemStack clone = stack.clone();
		clone.setAmount(1);
		cards.add(clone);
		CARDS.store(block, cards);
		block.update();
		this.updateCards(block);
		stack.setAmount(stack.getAmount() - 1);
		return true;
	}


	/**
	 * Removes the topmost card and returns it or null if the pile is empty.
	 * 
	 * @param block the pile block
	 * @return the card
	 */
	private ItemStack removeCard(TileState block) {
		List<ItemStack> cards = CARDS.fetchEmpty(block);
		if(cards.isEmpty())
			return null;

		ItemStack card = cards.remove(cards.size() - 1);
		CARDS.store(block, cards);
		block.update();
		this.updateCards(block);
		return card;
	}


	/**
	 * Updates the cards of the pile of the given block.
	 * 
	 * @param block the block
	 */
	private void updateCards(TileState block) {
		Inventory gui = this.getGui(block);
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		List<ItemStack> cards = CARDS.fetch(block);
		int size = cards.size();
		int start = Math.max(0, size - 10);
		boolean faceup = FACE_UP.fetch(block);
		for(int i = 0; i < 10; i++) {
			int row = (i & 1) + 1;
			int col = (i >> 1) + 2;
			int slot = row * 9 + col;
			int index = start + i;
			ItemStack stack = filler;
			if(index < size) {
				stack = cards.get(index);
				if(faceup)
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
	public boolean onMainInventoryInteract(Player player, TileState data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 4) {
			FACE_UP.toggle(data);
			data.update();
			this.updateCards(data);
		} else {
			int row = slot / 9;
			int column = slot - row * 9;
			if(1 <= row && row <= 2 && 1 <= column && column <= 7)
				ItemUtils.giveItemToPlayer(player, this.removeCard(data));
		}

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState data, PlayerInventory gui, InventoryClickEvent event) {
		this.addCard(data, event.getCurrentItem());
		return true;
	}

	/**
	 * Represents a card that can be stored in a card pile.
	 * 
	 * @author gamma
	 *
	 */
	public static interface Card {}

}
