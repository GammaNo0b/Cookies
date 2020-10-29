package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.setup.CustomBlockSetup;


public class StorageSkullBlockTier4 extends AbstractStorageSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.LIGHT_BLUE_STORAGE_CRATE;
	}


	@Override
	public int getTier() {
		return 4;
	}


	@Override
	public ItemStack getMaterialIngredient() {
		return new ItemStack(Material.DIAMOND);
	}


	@Override
	public ItemStack getMiddleIngredient() {
		return new ItemStack(Material.EMERALD);
	}


	@Override
	public ItemStack getCenterIngredient() {
		return new ItemStack(Material.NETHER_STAR);
	}


	@Override
	public ItemStack getPreviousTierStorageIngredient() {
		return CustomBlockSetup.STORAGE_BLOCK_TIER_3.createDefaultItemStack();
	}


	@Override
	public int getRows() {
		return 4;
	}
	
//	@Override
//	public Inventory createMainGui(Player player, TileState block) {
//		Inventory gui = super.createMainGui(player, block);
//		gui.setItem(31, new ItemBuilder(Material.PAPER).setName("§6Page§8: §e0").build());
//		return gui;
//	}
//
//
//	@Override
//	public void onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
//		if(!player.isSneaking()) {
//			player.playSound(player.getLocation(), this.getSound(), 0.2F, 1);
//			player.openInventory(this.constructPage(block, this.createMainGui(player, block), 0));
//		}
//	}
//
//
//	@Override
//	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
//		int[] itemSlots = new int[] {0, 1, 2, 3, -1, 4, 5, 6, 7, 8, 9, 10, 11, -1, 12, 13, 14, 15, 16, 17, 18, 19, -1, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31};
//		int index = itemSlots[event.getSlot()];
//		if(index >= 0) {
//			if(event.getCurrentItem() == null ? false : event.getCurrentItem().getType() != Material.BARRIER) {
//				Utilities.giveItemToPlayer(clicker, StorageProvider.requestItem(block, index));
//				int page = Integer.parseInt(gui.getItem(31).getItemMeta().getDisplayName().substring(12));
//				clicker.openInventory(this.constructPage(block, this.createMainGui(clicker, block), page));
//			}
//		} else if(event.getSlot() == 31) {
//			int page = Integer.parseInt(gui.getItem(31).getItemMeta().getDisplayName().substring(12));
//			gui = this.createMainGui(clicker, block);
//			clicker.openInventory(this.constructPage(block, this.createMainGui(clicker, block), ++page));
//		}
//		return true;
//	}
//
//
//	@Override
//	public boolean onPlayerInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
//		ItemStack item = event.getCurrentItem();
//		if(item == null ? false : (item.getType() != Material.AIR && item.getType() != Material.BARRIER)) {
//			ItemStack rest = StorageProvider.storeItem(block, item);
//			if(rest == null) {
//				clicker.getInventory().removeItem(item);
//				gui = this.createMainGui(clicker, block);
//				int page = Integer.parseInt(gui.getItem(31).getItemMeta().getDisplayName().substring(12));
//				clicker.openInventory(this.constructPage(block, this.createMainGui(clicker, block), page));
//			}
//		}
//		return true;
//	}
//
//
//	private Inventory constructPage(TileState block, Inventory gui, int page) {
//		List<List<BigItemStack>> pages = this.getPages(StorageProvider.getContent(block), 32);
//		page = page % pages.size();
//		gui.setItem(31, new ItemBuilder(Material.PAPER).setName("§6Page§8: §e" + page).build());
//		List<BigItemStack> content = pages.get(page);
//		int[] itemSlots = new int[] {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35};
//		for(int i = 0; i < Math.min(getCapacity(), 32); i++) {
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
//	private List<List<BigItemStack>> getPages(List<BigItemStack> content, int size) {
//		List<List<BigItemStack>> pages = new ArrayList<>();
//		List<BigItemStack> page = new ArrayList<>();
//		int index = 0;
//		for(BigItemStack type : content) {
//			if(index++ < size) {
//				page.add(type);
//			} else {
//				index = 0;
//				pages.add(new ArrayList<>(page));
//				page = new ArrayList<>();
//				page.add(type);
//			}
//		}
//		pages.add(new ArrayList<>(page));
//		return pages;
//	}

}
