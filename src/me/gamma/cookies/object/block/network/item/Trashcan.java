
package me.gamma.cookies.object.block.network.item;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.list.HeadTextures;



public class Trashcan extends AbstractCustomBlock implements ItemConsumer {

	@Override
	public String getIdentifier() {
		return "trashcan";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.TRASHCAN;
	}


	@Override
	public byte getItemInputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs(PersistentDataHolder holder) {
		return List.of(new ItemProvider() {

			@Override
			public void setType(ItemStack type) {}


			@Override
			public void remove(int amount) {}


			@Override
			public ItemStack getType() {
				return null;
			}


			@Override
			public int capacity() {
				return Integer.MAX_VALUE;
			}


			@Override
			public boolean canChangeType(ItemStack type) {
				return true;
			}


			@Override
			public int amount() {
				return 0;
			}


			@Override
			public void add(ItemStack type, int amount) {}

		});
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		player.openInventory(Bukkit.createInventory(null, 36, "§8Trashcan"));
		return true;
	}

}
