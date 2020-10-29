
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.setup.CustomBlockSetup;



public class StorageSkullBlockTier2 extends AbstractStorageSkullBlock {

	@Override
	public int getTier() {
		return 2;
	}


	@Override
	public ItemStack getMaterialIngredient() {
		return new ItemStack(Material.IRON_INGOT);
	}


	@Override
	public ItemStack getMiddleIngredient() {
		return new ItemStack(Material.IRON_NUGGET);
	}


	@Override
	public ItemStack getCenterIngredient() {
		return new ItemStack(Material.GOLD_INGOT);
	}


	@Override
	public ItemStack getPreviousTierStorageIngredient() {
		return CustomBlockSetup.STORAGE_BLOCK_TIER_1.createDefaultItemStack();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.SILVER_STORAGE_CRATE;
	}


//	@Override
//	public int getRows() {
//		return 1;
//	}
//
//
//	@Override
//	public int getIdentifierSlot() {
//		return 2;
//	}
//
//
//	@Override
//	public Inventory createMainGui(Player player, TileState block) {
//		Inventory gui = Bukkit.createInventory(null, InventoryType.HOPPER, this.getDisplayName());
//		gui.setItem(this.getIdentifierSlot(), super.createMainGui(player, block).getItem(this.getIdentifierSlot()));
//		List<BigItemStack> content = getStorageBlockContents(block);
//		int[] itemSlots = new int[] {
//			0, 1, 3, 4
//		};
//		for(int i = 0; i < AbstractStorageSkullBlock.getCapacity(this.getTier()); i++) {
//			if(content.get(i).isEmpty()) {
//				gui.setItem(itemSlots[i], new ItemBuilder(Material.BARRIER).setName("§4Empty").addLore("§3Amount§8: §9?").build());
//			} else {
//				ItemStack item = content.get(i).getStack().clone();
//				String name = "§cItem§8: " + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "§6" + Utilities.toCapitalWords(item.getType().toString().replace('_', ' ')));
//				gui.setItem(itemSlots[i], new ItemBuilder(item).setName(name).addLore("§3Amount§8: §b" + content.get(i).getAmount()).build());
//			}
//		}
//		return gui;
//	}
//
//
//	@Override
//	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
//		int[] itemSlots = new int[] {
//			0, 1, -1, 2, 3
//		};
//		if(event.getSlot() != 2) {
//			if(event.getCurrentItem() == null ? false : event.getCurrentItem().getType() != Material.BARRIER) {
//				Utilities.giveItemToPlayer(clicker, this.requestItemStack(block, itemSlots[event.getSlot()]));
//				clicker.openInventory(this.createMainGui(clicker, block));
//			}
//		}
//		return true;
//	}
//
//
//	@Override
//	public boolean onPlayerInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
//		ItemStack item = event.getCurrentItem();
//		if(item != null) {
//			if(storeItemStack(block, new BigItemStack(item, item.getAmount()))) {
//				clicker.getInventory().remove(item);
//				clicker.openInventory(this.createMainGui(clicker, block));
//			}
//		}
//		return true;
//	}

}
