
package me.gamma.cookies.object.item.resources;


import static me.gamma.cookies.object.block.CardPile.CARDS;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.craftbukkit.v1_21_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_21_R2.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.ItemUtils;



public class CardPile extends AbstractBlockItem<me.gamma.cookies.object.block.CardPile> implements Comparator<ItemStack> {

	private static final CraftPersistentDataTypeRegistry registry = new CraftPersistentDataTypeRegistry();

	public CardPile() {
		super(Blocks.CARD_PILE);
	}


	@Override
	public String getTitle() {
		return "§fPlaying Card Pile";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		Section section;

		section = builder.createSection("§8Item uses:", true);
		section.add(" §7- Right click to throw the topmost card.");
		section.add(" §7- Shift right click to empty the card pile.");
		section.add(" §7- Left click to shuffle the pile.");
		section.add(" §7- Shift Left click to sort the pile.");

		section = builder.createSection("§8Block uses:", true);
		section.add(" §7- Right click with card to place it on top.");
		section.add(" §7- Shift right click to take topmost card.");

		builder.createSection("§8Stored Cards: §7" + CARDS.fetchEmpty(holder).size(), false);
	}


	public ItemStack get32CardDeck() {
		final CraftPersistentDataContainer container = new CraftPersistentDataContainer(registry);
		CARDS.store(() -> container, Items.PLAYING_CARDS.stream().filter(card -> card.getRank().ordinal() > CardItem.Rank.SIX.ordinal()).map(CardItem::get).toList());
		return this.get(() -> container);
	}


	public ItemStack get52CardDeck() {
		final CraftPersistentDataContainer container = new CraftPersistentDataContainer(registry);
		CARDS.store(() -> container, CollectionUtils.mapList(Items.PLAYING_CARDS, CardItem::get));
		return this.get(() -> container);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		ItemMeta meta = stack.getItemMeta();
		List<ItemStack> cards = CARDS.fetchEmpty(meta);
		if(player.isSneaking()) {
			for(ItemStack card : cards)
				for(int i = 0; i < stack.getAmount(); i++)
					ItemUtils.dropItem(card, player.getLocation());
			CARDS.storeEmpty(meta);
			this.updateDescription(meta);
			stack.setItemMeta(meta);
		} else {
			int top = cards.size() - 1;
			if(top >= 0) {
				ItemStack card = cards.remove(top);
				for(int i = 0; i < stack.getAmount(); i++) {
					Item item = player.getWorld().dropItem(player.getEyeLocation(), card);
					item.setVelocity(player.getLocation().getDirection().multiply(0.3D));
				}
				CARDS.store(meta, cards);
				this.updateDescription(meta);
				stack.setItemMeta(meta);
			}
		}

		return true;
	}


	@Override
	public boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		ItemMeta meta = stack.getItemMeta();
		List<ItemStack> cards = CARDS.fetch(meta);
		int size = cards.size();
		if(size >= 2) {
			if(player.isSneaking()) {
				Collections.sort(cards, this);
			} else {
				Collections.shuffle(cards);
			}
			CARDS.store(meta, cards);
			stack.setItemMeta(meta);
		}
		return true;
	}


	@Override
	public int compare(ItemStack o1, ItemStack o2) {
		AbstractCustomItem item1 = Items.getCustomItemFromStack(o1);
		AbstractCustomItem item2 = Items.getCustomItemFromStack(o2);

		if(item1 == null) {
			if(item2 == null) {
				return o1.getType().compareTo(o2.getType());
			} else {
				return 1;
			}
		}

		if(item2 == null) {
			return -1;
		}

		if(!(item1 instanceof CardItem card1)) {
			if(item2 instanceof CardItem) {
				return 1;
			} else {
				return item1.getIdentifier().compareTo(item2.getIdentifier());
			}
		}

		if(!(item2 instanceof CardItem card2)) {
			return -1;
		}

		return card1.compareTo(card2);
	}

}
