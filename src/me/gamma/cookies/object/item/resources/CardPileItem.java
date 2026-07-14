
package me.gamma.cookies.object.item.resources;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.CardPileBlock;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.tile.CardPile;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CardPileItem extends AbstractBlockItem<CardPileBlock> implements Comparator<ItemStack> {

	public CardPileItem() {
		super(Blocks.CARD_PILE);
	}


	@Override
	public String getTitle() {
		return "§fPlaying Card Pile";
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		customData.setObjectList(CardPile.KEY_CARDS, new ArrayList<>());
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		Section section;

		section = builder.createSection("§8Item uses:", true);
		section.add(" §7- Right click to throw the topmost card.");
		section.add(" §7- Shift right click to empty the card pile.");
		section.add(" §7- Left click to shuffle the pile.");
		section.add(" §7- Shift Left click to sort the pile.");

		section = builder.createSection("§8Block uses:", true);
		section.add(" §7- Right click with card to place it on top.");
		section.add(" §7- Shift right click to take topmost card.");

		builder.createSection("§8Stored Cards: §7" + data.getObjectList(CardPile.KEY_CARDS).size(), false);
	}


	public ItemStack get32CardDeck(Consumer<PersistentDataObject> dataConsumer) {
		return this.get(dataConsumer.andThen(data -> {
			List<ItemStack> cards = Items.PLAYING_CARDS.stream().filter(card -> card.getRank().ordinal() > CardItem.Rank.SIX.ordinal()).map(CardItem::get).toList();
			PersistentDataUtils.setList(data, CardPile.KEY_CARDS, cards, PersistentDataUtils::saveItemStack);
		}));
	}


	public ItemStack get52CardDeck(Consumer<PersistentDataObject> dataConsumer) {
		return this.get(dataConsumer.andThen(data -> {
			List<ItemStack> cards = Items.PLAYING_CARDS.stream().map(CardItem::get).toList();
			PersistentDataUtils.setList(data, CardPile.KEY_CARDS, cards, PersistentDataUtils::saveItemStack);
		}));
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return true;

		List<ItemStack> cards = PersistentDataUtils.getList(data.getData(), CardPile.KEY_CARDS, null, PersistentDataUtils::loadItemStack);
		if(player.isSneaking()) {
			for(ItemStack card : cards)
				for(int i = 0; i < stack.getAmount(); i++)
					ItemUtils.dropItem(card, player.getLocation());
			PersistentDataUtils.setList(data.getData(), CardPile.KEY_CARDS, List.of(), PersistentDataUtils::saveItemStack);
			data.save();
			ItemMeta meta = stack.getItemMeta();
			this.updateDescription(meta, data.getData());
			stack.setItemMeta(meta);
		} else {
			int top = cards.size() - 1;
			if(top >= 0) {
				ItemStack card = cards.remove(top);
				for(int i = 0; i < stack.getAmount(); i++) {
					Item item = player.getWorld().dropItem(player.getEyeLocation(), card);
					item.setVelocity(player.getLocation().getDirection().multiply(0.3D));
				}
				PersistentDataUtils.setList(data.getData(), CardPile.KEY_CARDS, cards, PersistentDataUtils::saveItemStack);
				data.save();
				ItemMeta meta = stack.getItemMeta();
				this.updateDescription(meta, data.getData());
				stack.setItemMeta(meta);
			}
		}

		return true;
	}


	@Override
	public boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return true;

		List<ItemStack> cards = PersistentDataUtils.getList(data.getData(), CardPile.KEY_CARDS, null, PersistentDataUtils::loadItemStack);
		int size = cards.size();
		if(size >= 2) {
			if(player.isSneaking()) {
				Collections.sort(cards, this);
			} else {
				Collections.shuffle(cards);
			}
			PersistentDataUtils.setList(data.getData(), CardPile.KEY_CARDS, cards, PersistentDataUtils::saveItemStack);
			data.save();
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
