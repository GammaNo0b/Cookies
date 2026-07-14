
package me.gamma.cookies.object.item.resources;


import static me.gamma.cookies.object.tile.Backpack.BACKPACKS;
import static me.gamma.cookies.object.tile.Backpack.KEY_UUID;

import java.util.UUID;

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

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.block.BackpackBlock;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class BackpackItem extends AbstractBlockItem<BackpackBlock> {

	public BackpackItem(BackpackBlock backpack) {
		super(backpack);
	}


	@Override
	public String getTitle() {
		return this.block.getTitle();
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		PersistentDataUtils.setUUID(customData, KEY_UUID, BACKPACKS.createNewInventory(this.getTitle(), this.block.getSize()));
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection("§9Size: §3" + this.block.getRows() + " §b* §39", false);
		builder.createSection("§8UUID: §7" + PersistentDataUtils.getUUID(data, KEY_UUID).toString(), true);
	}


	private Inventory getInventory(ItemStack stack) {
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return null;

		UUID uuid = PersistentDataUtils.getUUID(data.getData(), KEY_UUID);
		if(uuid == null)
			return null;

		return BACKPACKS.getInventory(uuid);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		player.openInventory(this.getInventory(stack));
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
			player.openInventory(this.getInventory(stack));
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
				if(!(item instanceof BackpackItem backpack))
					return false;

				Inventory inventory = backpack.getInventory(stack);
				return inventory == mainInventory;
			}

		};
	}


	public static void upgradeBackpack(ItemStack oldBackpack, ItemStack newBackpack) {
		CustomItemData oldData = getCustomData(oldBackpack);
		if(oldData == null)
			return;

		CustomItemData newData = getCustomData(newBackpack);
		if(newData == null)
			return;

		BACKPACKS.updateInventory(PersistentDataUtils.getUUID(oldData.getData(), KEY_UUID), PersistentDataUtils.getUUID(newData.getData(), KEY_UUID));
	}

}
