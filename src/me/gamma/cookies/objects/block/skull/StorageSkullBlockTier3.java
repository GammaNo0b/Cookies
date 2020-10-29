
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.setup.CustomBlockSetup;



public class StorageSkullBlockTier3 extends AbstractStorageSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.GOLDEN_STORAGE_CRATE;
	}
	
	
	@Override
	public int getTier() {
		return 3;
	}


	@Override
	public ItemStack getMaterialIngredient() {
		return new ItemStack(Material.GOLD_INGOT);
	}
	
	
	@Override
	public ItemStack getMiddleIngredient() {
		return new ItemStack(Material.GOLD_NUGGET);
	}


	@Override
	public ItemStack getCenterIngredient() {
		return new ItemStack(Material.DIAMOND);
	}
	
	
	@Override
	public ItemStack getPreviousTierStorageIngredient() {
		return CustomBlockSetup.STORAGE_BLOCK_TIER_2.createDefaultItemStack();
	}


	@Override
	public int getRows() {
		return 2;
	}


//	@Override
//	public Inventory createMainGui(Player player, TileState block) {
//		Inventory gui = super.createMainGui(player, block);
//		List<BigItemStack> content = getStorageBlockContents(block);
//		int[] itemSlots = new int[] {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17};
//		for(int i = 0; i < getCapacity(this.getTier()); i++) {
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
//		int[] itemSlots = new int[] {0, 1, 2, 3, -1, 4, 5, 6, 7, 8, 9, 10, 11, -1, 13, 14, 15, 16};
//		int index = itemSlots[event.getSlot()];
//		if(index >= 0) {
//			if(event.getCurrentItem() == null ? false : event.getCurrentItem().getType() != Material.BARRIER) {
//				Utilities.giveItemToPlayer(clicker, this.requestItemStack(block, index));
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
//		if(item == null ? false : (item.getType() != Material.AIR && item.getType() != Material.BARRIER)) {
//			if(storeItemStack(block, new BigItemStack(item, item.getAmount()))) {
//				clicker.getInventory().removeItem(item);
//				clicker.openInventory(this.createMainGui(clicker, block));
//			}
//		}
//		return true;
//	}

}
