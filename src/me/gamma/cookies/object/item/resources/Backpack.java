
package me.gamma.cookies.object.item.resources;


import static me.gamma.cookies.object.block.Backpack.BACKPACKS;
import static me.gamma.cookies.object.block.Backpack.uuid;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class Backpack extends AbstractBlockItem<me.gamma.cookies.object.block.Backpack> {

	public Backpack(me.gamma.cookies.object.block.Backpack backpack) {
		super(backpack);
	}


	@Override
	public String getTitle() {
		return this.block.getTitle();
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		uuid.store(meta, BACKPACKS.createNewInventory(this.getTitle(), this.block.getSize()));
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection("§8UUID: §7" + uuid.fetch(holder).toString(), false);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		player.openInventory(BACKPACKS.getInventory(uuid.fetch(stack.getItemMeta())));
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
			player.openInventory(BACKPACKS.getInventory(uuid.fetch(stack.getItemMeta())));
			return true;
		} else {
			return super.onBlockRightClick(player, stack, block, event);
		}
	}


	@Override
	public boolean hasListener() {
		return true;
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onInventoryInteract(InventoryClickEvent event) {
				if(event.isCancelled())
					return;

				if(this.checkItem(event.getCurrentItem(), event.getInventory())) {
					event.setCancelled(true);
					return;
				}

				event.setCancelled(event.getClick() == ClickType.NUMBER_KEY && this.checkItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()), event.getInventory()));
			}


			private boolean checkItem(ItemStack stack, Inventory mainInventory) {
				AbstractCustomItem item = Items.getCustomItemFromStack(stack);
				if(!(item instanceof Backpack))
					return false;

				Inventory inventory = BACKPACKS.getInventory(uuid.fetch(stack.getItemMeta()));
				return inventory == mainInventory;
			}

		};
	}

}
