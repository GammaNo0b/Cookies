
package me.gamma.cookies.object.item.resources;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.CardPile.Card;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class CardItem extends AbstractCustomItem implements Comparable<CardItem>, Card {

	private final Color color;
	private final Rank rank;

	public CardItem(Color color, Rank rank) {
		this.color = color;
		this.rank = rank;
	}


	public Color getColor() {
		return this.color;
	}


	public Rank getRank() {
		return this.rank;
	}


	@Override
	public String getIdentifier() {
		return String.format("%s_of_%s", this.rank.identifier(), this.color.identifier());
	}


	@Override
	public String getTitle() {
		return String.format("§%c%s §f%s §%1$c%2$s", this.color.colorcode, this.color.symbol, this.rank.symbol);
	}


	@Override
	public Material getMaterial() {
		return Material.PAPER;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return false;
	}


	@Override
	public int compareTo(CardItem o) {
		int cmp = this.color.compareTo(o.color);
		return cmp == 0 ? this.rank.compareTo(o.rank) : cmp;
	}

	public static enum Color {

		DIAMONDS("♦", 'c'),
		HEARTS("♥", 'c'),
		SPADES("♠", '8'),
		CLUBS("♣", '8');

		private final String symbol;
		private final char colorcode;

		private Color(String symbol, char colorcode) {
			this.symbol = symbol;
			this.colorcode = colorcode;
		}


		private String identifier() {
			return this.name().toLowerCase();
		}

	}

	public static enum Rank {

		TWO("2"),
		THREE("3"),
		FOUR("4"),
		FIVE("5"),
		SIX("6"),
		SEVEN("7"),
		EIGHT("8"),
		NINE("9"),
		TEN("10"),
		JACK("J"),
		QUEEN("Q"),
		KING("K"),
		ACE("A");

		private final String symbol;

		private Rank(String symbol) {
			this.symbol = symbol;
		}


		private String identifier() {
			return this.name().toLowerCase();
		}

	}

}
