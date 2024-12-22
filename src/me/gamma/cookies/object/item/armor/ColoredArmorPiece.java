
package me.gamma.cookies.object.item.armor;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.object.item.ItemTicker;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.util.Utils;



public class ColoredArmorPiece extends AbstractCustomArmorItem implements ItemTicker {

	private static final IntegerProperty TARGET_RED = new IntegerProperty("targetred");
	private static final IntegerProperty TARGET_GREEN = new IntegerProperty("targetgreen");
	private static final IntegerProperty TARGET_BLUE = new IntegerProperty("targetblue");

	private ArmorType type;
	private String identifier;
	private String name;
	private Set<UUID> players;

	public ColoredArmorPiece(ArmorType type) {
		this.type = type;
		this.identifier = "colored_" + type.name().toLowerCase();
		this.name = "§6Colored " + Utils.toCapitalWords(type);
		this.players = new HashSet<>();
		this.registerItemTicker();
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
	public ArmorType getArmorType() {
		return this.type;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}


	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return this.isEquipped(player);
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		((LeatherArmorMeta) meta).setColor(Color.OLIVE);
		Random r = new Random();
		TARGET_RED.store(meta, r.nextInt(256));
		TARGET_GREEN.store(meta, r.nextInt(256));
		TARGET_BLUE.store(meta, r.nextInt(256));
	}


	@Override
	public void tick(Player player, ItemStack stack) {
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		Random ra = new Random();
		Color color = meta.getColor();
		int cr = color.getRed();
		int cg = color.getGreen();
		int cb = color.getBlue();

		int r = TARGET_RED.fetch(meta);
		while(cr == r)
			r = ra.nextInt() & 0xFF;
		TARGET_RED.store(meta, r);

		int g = TARGET_GREEN.fetch(meta);
		while(cg == g)
			g = ra.nextInt() & 0xFF;
		TARGET_GREEN.store(meta, g);

		int b = TARGET_BLUE.fetch(meta);
		while(cb == b)
			b = ra.nextInt() & 0xFF;
		TARGET_BLUE.store(meta, b);

		meta.setColor(Color.fromRGB(cr + (cr > r ? 1 : -1), cg + (cg > g ? 1 : -1), cb + (cb > b ? 1 : -1)));
		stack.setItemMeta(meta);
	}


	@Override
	public List<ItemStack> getStacksFromPlayer(Player player) {
		ItemStack stack = this.getEquippedItem(player);
		return this.isInstanceOf(stack) ? List.of(stack) : new ArrayList<>();
	}


	@Override
	protected void onEquip(Player player) {
		this.registerPlayer(player);
	}


	@Override
	protected void onUnequip(Player player) {
		this.unregisterPlayer(player);
	}

}
