
package me.gamma.cookies.object.item.resources;


import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Lootbox extends AbstractCustomItem implements InventoryProvider<Lootbox.LootData> {

	private static final String KEY_LOOT_TABLE = "loot_table";

	@Override
	public String getIdentifier() {
		return "lootbox";
	}


	@Override
	public String getTitle() {
		return "§6Lootbox";
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.LOOTBOX;
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		customData.setString(KEY_LOOT_TABLE, "");
	}


	public void setLootTable(ItemStack stack, NamespacedKey lootTable) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return;

		data.getData().setString(KEY_LOOT_TABLE, lootTable.toString());
		data.save();
	}


	private LootTable getLootTable(ItemStack stack) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return null;

		String string = data.getData().getString(KEY_LOOT_TABLE);
		if(string == null)
			return null;

		NamespacedKey key = NamespacedKey.fromString(string);
		if(key == null)
			return null;

		return Bukkit.getLootTable(key);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.open(player, stack);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, stack, block, event))
			return false;

		this.open(player, stack);
		return true;
	}


	@Override
	public boolean onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
		this.open(player, stack);
		return true;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public String getTitle(LootData data) {
		return this.getTitle();
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_CHEST_OPEN;
	}


	@Override
	public Inventory createGui(LootData data) {
		Inventory gui = InventoryProvider.super.createGui(data);
		final ItemStack filler = InventoryUtils.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 45, filler);
		}

		Inventory tmp = Bukkit.createInventory(null, 36);
		data.table.fillInventory(tmp, new Random(), data.context);
		for(int i = 0; i < tmp.getSize(); i++)
			gui.setItem(9 + i, tmp.getItem(i));

		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, LootData data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		return slot < 9 || slot >= 45;
	}


	@Override
	public boolean onInventoryClose(Player player, LootData data, Inventory gui, InventoryCloseEvent event) {
		for(int i = 9; i < 45; i++)
			ItemUtils.giveItemToPlayer(player, gui.getItem(i));

		return false;
	}


	private void open(HumanEntity player, ItemStack stack) {
		LootTable table = this.getLootTable(stack);
		if(table == null) {
			player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0.0D, 1.0D, 0.0D), 20, new DustOptions(Color.GRAY, 1.0F));
		} else {
			LootContext context = new LootContext.Builder(player.getLocation()).luck(Optional.of(player.getPotionEffect(PotionEffectType.LUCK)).map(PotionEffect::getAmplifier).orElse(0)).build();
			this.openGui(player, new LootData(table, context), false, true);
		}
		ItemUtils.increaseItem(stack, -1);
	}


	public static ItemStack generateLootbox(String location) {
		return generateLootbox(new NamespacedKey(Cookies.INSTANCE, location));
	}


	public static ItemStack generateLootbox(NamespacedKey key) {
		ItemStack stack = Items.LOOTBOX.get();
		Items.LOOTBOX.setLootTable(stack, key);
		return stack;
	}


	public static ItemStack generateLootbox(LootTables lootTable) {
		ItemStack stack = Items.LOOTBOX.get();
		Items.LOOTBOX.setLootTable(stack, lootTable.getKey());
		return stack;
	}

	static record LootData(LootTable table, LootContext context) {}

}
