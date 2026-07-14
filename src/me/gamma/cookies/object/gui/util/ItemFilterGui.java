
package me.gamma.cookies.object.gui.util;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.object.block.AdvancedFilterBlock;
import me.gamma.cookies.object.block.FilterBlock;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.GuiUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.math.MathHelper;



public class ItemFilterGui implements InventoryProvider<ItemFilterGui.FilterData> {

	public static final String KEY_LOCATION = "location";
	public static final String KEY_WORLD = "world";
	public static final String KEY_COMPARATOR = "comparator";
	public static final String KEY_PRIORITY = "priority";
	public static final String KEY_CHANNEL = "channel";
	public static final String KEY_SIDE_FLAGS_PROPERTY = "sideflags";

	public static final int CLOSE_SLOT = 4;
	public static final ItemStack CLOSE_ICON = new ItemBuilder(Material.BARRIER).setName("§cClose").build();

	public static final int CHANNEL_SLOT = 15;
	public static final int PRIORITY_SLOT = 34;
	public static final int COMPARE_COUNT_SLOT = 35;
	public static final int LIST_SLOT = 43;
	public static final int NBT_SLOT = 44;
	public static final int BLOCK_SIDE_CONFIG_SLOT = 7;

	private static final Material[] channelMaterials = { Material.WHITE_WOOL, Material.LIGHT_GRAY_WOOL, Material.GRAY_WOOL, Material.BLACK_WOOL, Material.BROWN_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.GREEN_WOOL, Material.CYAN_WOOL, Material.LIGHT_BLUE_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.MAGENTA_WOOL, Material.PINK_WOOL };
	public static final ItemStack[] CHANNELS = new ItemStack[16];
	static {
		for(int i = 0; i < 16; i++)
			CHANNELS[i] = new ItemBuilder(channelMaterials[i]).setName("§8" + Utils.HEX_DIGITS[i]).build();
	}

	public static final ItemStack COMPARE_COUNT = new ItemBuilder(Material.PRISMARINE_CRYSTALS).setName("§3Keep Count").addEnchantment(Enchantment.PROTECTION, 1).setItemFlag(ItemFlag.HIDE_ENCHANTS).build();
	public static final ItemStack DISCARD_COUNT = new ItemBuilder(Material.PRISMARINE_CRYSTALS).setName("§8Ignore Count").build();
	public static final ItemStack WHITELIST = new ItemBuilder(Material.WHITE_CONCRETE).setName("§fWhitelist").build();
	public static final ItemStack BLACKLIST = new ItemBuilder(Material.BLACK_CONCRETE).setName("§fBlacklist").build();
	public static final ItemStack IGNORE_NBT = new ItemBuilder(Material.STONE_SWORD).setName("§cIgnore NBT").build();
	public static final ItemStack RESPECT_NBT = new ItemBuilder(Material.STONE_SWORD).setName("§aCompare NBT").addEnchantment(Enchantment.PROTECTION, 1).setItemFlag(ItemFlag.HIDE_ENCHANTS).build();
	public static final ItemStack FILLER = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

	@Override
	public String getIdentifier() {
		return "item_filter";
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public String getTitle(FilterData data) {
		return data.block().getFilterTitle();
	}


	@Override
	public int rows() {
		return 6;
	}


	private void updateCountComparator(Inventory inventory, ItemFilter.CountComparison comparator) {
		ItemStack compareCount = GuiUtils.createEnumMenu(comparator).createMenu();
		InventoryUtils.storeIntInStack(compareCount, KEY_COMPARATOR, comparator.ordinal());
		inventory.setItem(COMPARE_COUNT_SLOT, compareCount);
	}


	private void updateChannel(Inventory inventory, int channel) {
		int c1 = (channel >> 4) & 0xF;
		int c2 = channel & 0xF;
		ItemStack stack = new ItemBuilder(Material.PAPER).setName("§2Channel: §a0x" + Utils.HEX_DIGITS[c1] + Utils.HEX_DIGITS[c2]).build();
		InventoryUtils.storeIntInStack(stack, KEY_CHANNEL, channel & 0xFF);
		inventory.setItem(CHANNEL_SLOT, stack);
		inventory.setItem(CHANNEL_SLOT + 1, CHANNELS[c1]);
		inventory.setItem(CHANNEL_SLOT + 2, CHANNELS[c2]);
	}


	private void updatePriority(Inventory inventory, int priority) {
		ItemStack stack = new ItemBuilder(Material.PAPER).setName("§3Priority: §b" + priority).addLore("  §a+ §7Left-Click §8to increase").addLore("  §c- §7Right-Click §8to decrease").build();
		InventoryUtils.storeIntInStack(stack, KEY_PRIORITY, priority);
		inventory.setItem(PRIORITY_SLOT, stack);
	}


	@Override
	public Sound getSound() {
		return Sound.ITEM_BOOK_PAGE_TURN;
	}


	@Override
	public Inventory createGui(FilterData data) {
		Inventory gui = InventoryProvider.super.createGui(data);

		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}

		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : new int[] { 24, 25, 26, 33, 42 })
			gui.setItem(i, filler);

		filler = InventoryUtils.filler(data.color());
		for(int i = 9; i < 45; i += 9) {
			gui.setItem(i, filler);
			gui.setItem(i + 5, filler);
		}

		ItemFilter filter = data.getFilter();
		for(int i = 0; i < ItemFilter.SIZE; i++) {
			int r = (i >> 2) + 1;
			int c = (i & 0x3) + 1;
			ItemStack current = filter.getFilterItem(i);
			gui.setItem(r * 9 + c, ItemUtils.isEmpty(current) ? FILLER : current);
		}

		gui.setItem(CLOSE_SLOT, CLOSE_ICON);

		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

		if(data.hasChannel()) {
			this.updateChannel(gui, data.getChannel());
		} else {
			gui.setItem(CHANNEL_SLOT, filler);
			gui.setItem(CHANNEL_SLOT + 1, filler);
			gui.setItem(CHANNEL_SLOT + 2, filler);
		}

		if(data.hasPriority()) {
			this.updatePriority(gui, data.getPriority());
		} else {
			gui.setItem(PRIORITY_SLOT, filler);
		}

		this.updateCountComparator(gui, filter.getCountComparator());
		gui.setItem(LIST_SLOT, filter.isWhitelisted() ? WHITELIST : BLACKLIST);
		gui.setItem(NBT_SLOT, filter.isIgnoreNBT() ? IGNORE_NBT : RESPECT_NBT);

		if(data.hasSideFlags()) {
			gui.setItem(BLOCK_SIDE_CONFIG_SLOT, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§eBlock Face Config").build());
		}

		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, FilterData data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == CLOSE_SLOT) {
			History.travelBack(player);
		} else if(slot == COMPARE_COUNT_SLOT) {
			int d;
			ClickType click = event.getClick();
			if(click.isLeftClick()) {
				d = 1;
			} else if(click.isRightClick()) {
				d = -1;
			} else {
				return true;
			}

			this.updateCountComparator(gui, EnumUtils.byIndex(ItemFilter.CountComparison.class, d + InventoryUtils.getIntFromStack(gui.getItem(COMPARE_COUNT_SLOT), KEY_COMPARATOR, 0)));
		} else if(slot == LIST_SLOT) {
			gui.setItem(LIST_SLOT, ItemUtils.equals(gui.getItem(LIST_SLOT), WHITELIST) ? BLACKLIST : WHITELIST);
		} else if(slot == NBT_SLOT) {
			gui.setItem(NBT_SLOT, ItemUtils.equals(gui.getItem(NBT_SLOT), IGNORE_NBT) ? RESPECT_NBT : IGNORE_NBT);
		} else if(data.hasPriority() && slot == PRIORITY_SLOT) {
			int d;
			ClickType click = event.getClick();
			if(click.isLeftClick()) {
				d = 1;
			} else if(click.isRightClick()) {
				d = -1;
			} else {
				return true;
			}

			if(click.isShiftClick())
				d *= 10;

			this.updatePriority(gui, InventoryUtils.getIntFromStack(gui.getItem(PRIORITY_SLOT), KEY_PRIORITY) + d);
		} else if(data.hasChannel() && (slot == CHANNEL_SLOT + 1 || slot == CHANNEL_SLOT + 2)) {
			int d;
			ClickType click = event.getClick();
			if(click.isLeftClick()) {
				d = 1;
			} else if(click.isRightClick()) {
				d = -1;
			} else {
				return true;
			}

			if(click.isShiftClick())
				d *= 4;

			int channel = InventoryUtils.getIntFromStack(gui.getItem(CHANNEL_SLOT), KEY_CHANNEL);
			int c1 = (channel >> 4) & 0xF;
			int c2 = channel & 0xF;
			if(slot == CHANNEL_SLOT + 1) {
				c1 = (c1 + d) & 0xF;
			} else {
				c2 = (c2 + d) & 0xF;
			}

			this.updateChannel(gui, c1 << 4 | c2);
		} else if(data.hasSideFlags() && slot == BLOCK_SIDE_CONFIG_SLOT) {
			BlockFaceConfig.openBlockFaceConfig(player, new Config("§eFilter Block Face Config", data.sideFlags(), false, data.color()));
		} else {
			int r = slot / 9 - 1;
			int c = slot % 9 - 1;
			if(0 <= r && r < 4 && 0 <= c && c < 4) {
				ItemStack stack = gui.getItem(slot);
				if(InventoryUtils.isFiller(stack))
					return true;

				int d;
				ClickType click = event.getClick();
				if(click.isLeftClick()) {
					d = 1;
				} else if(click.isRightClick()) {
					d = -1;
				} else {
					if(click == ClickType.DROP || click == ClickType.CONTROL_DROP)
						gui.setItem(slot, FILLER);
					return true;
				}

				if(click.isShiftClick())
					d *= 10;

				stack.setAmount(MathHelper.clamp(1, stack.getMaxStackSize(), stack.getAmount() + d));
			}
		}
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, FilterData data, PlayerInventory gui, InventoryClickEvent event) {
		ItemStack clicked = event.getCurrentItem();
		if(ItemUtils.isEmpty(clicked))
			return true;

		Inventory inventory = event.getInventory();
		int empty = -1;
		for(int i = 0; i < ItemFilter.SIZE; i++) {
			int r = (i >> 2) + 1;
			int c = (i & 0x3) + 1;
			int s = r * 9 + c;
			ItemStack type = inventory.getItem(s);
			if(InventoryUtils.isFiller(type)) {
				if(empty < 0)
					empty = s;
			} else if(ItemUtils.equals(type, clicked)) {
				return true;
			}
		}

		if(empty >= 0)
			inventory.setItem(empty, clicked.clone());

		return true;
	}


	@Override
	public boolean onInventoryClose(Player player, FilterData data, Inventory gui, InventoryCloseEvent event) {
		data.save(gui);
		return false;
	}


	public static void open(HumanEntity player, FilterBlock<ItemStack, ItemFilter> block, Holder<Byte> sideFlags, Material color) {
		Inventories.ITEM_FILTER.openGui(player, block instanceof AdvancedFilterBlock<ItemStack, ItemFilter> advancedBlock ? new AdvancedData(advancedBlock, sideFlags, color) : new Data(block, sideFlags, color));
	}

	static interface FilterData {

		FilterBlock<ItemStack, ItemFilter> block();

		Material color();

		Holder<Byte> sideFlags();

		boolean hasSideFlags();

		byte getSideFlags();

		void setSideFlags(byte flags);

		boolean hasPriority();

		int getPriority();

		void setPriority(int priority);

		boolean hasChannel();

		int getChannel();

		void setChannel(int channel);

		ItemFilter getFilter();

		void setFilter(ItemFilter filter);

		void save(Inventory gui);

	}

	static record Data(FilterBlock<ItemStack, ItemFilter> block, Holder<Byte> sideFlags, Material color) implements FilterData {

		@Override
		public boolean hasSideFlags() {
			return this.sideFlags != null;
		}


		@Override
		public byte getSideFlags() {
			return this.sideFlags.get();
		}


		@Override
		public void setSideFlags(byte flags) {
			this.sideFlags.set(flags);
		}


		@Override
		public int getPriority() {
			return 0;
		}


		@Override
		public boolean hasPriority() {
			return false;
		}


		@Override
		public void setPriority(int priority) {}


		@Override
		public boolean hasChannel() {
			return false;
		}


		@Override
		public int getChannel() {
			return 0;
		}


		@Override
		public void setChannel(int channel) {}


		@Override
		public ItemFilter getFilter() {
			return this.block.getFilter();
		}


		@Override
		public void setFilter(ItemFilter filter) {
			this.block.setFilter(filter);
		}


		@Override
		public void save(Inventory gui) {
			ItemFilter filter = this.getFilter();
			for(int i = 0; i < ItemFilter.SIZE; i++) {
				int r = (i >> 2) + 1;
				int c = (i & 0x3) + 1;
				ItemStack stack = gui.getItem(r * 9 + c);
				filter.setFilterItem(i, InventoryUtils.isFiller(stack) ? null : stack);
			}

			filter.setCountComparator(EnumUtils.byIndex(ItemFilter.CountComparison.class, InventoryUtils.getIntFromStack(gui.getItem(COMPARE_COUNT_SLOT), KEY_COMPARATOR, 0)));
			filter.setWhitelisted(ItemUtils.equals(gui.getItem(LIST_SLOT), WHITELIST));
			filter.setIgnoreNBT(ItemUtils.equals(gui.getItem(NBT_SLOT), IGNORE_NBT));

			this.setFilter(filter);
		}

	}

	static record AdvancedData(AdvancedFilterBlock<ItemStack, ItemFilter> block, Holder<Byte> sideFlags, Material color) implements FilterData {

		@Override
		public boolean hasSideFlags() {
			return this.sideFlags != null;
		}


		@Override
		public byte getSideFlags() {
			return this.sideFlags.get();
		}


		@Override
		public void setSideFlags(byte flags) {
			this.sideFlags.set(flags);
		}


		public boolean hasPriority() {
			return true;
		}


		@Override
		public int getPriority() {
			return this.block.getPriority();
		}


		@Override
		public void setPriority(int priority) {
			this.block.setPriority(priority);
		}


		@Override
		public boolean hasChannel() {
			return true;
		}


		@Override
		public int getChannel() {
			return this.block.getChannel();
		}


		@Override
		public void setChannel(int channel) {
			this.block.setChannel(channel);
		}


		@Override
		public ItemFilter getFilter() {
			return this.block.getFilter();
		}


		@Override
		public void setFilter(ItemFilter filter) {
			this.block.setFilter(filter);
		}


		@Override
		public void save(Inventory gui) {
			ItemFilter filter = this.getFilter();
			for(int i = 0; i < ItemFilter.SIZE; i++) {
				int r = (i >> 2) + 1;
				int c = (i & 0x3) + 1;
				ItemStack stack = gui.getItem(r * 9 + c);
				filter.setFilterItem(i, InventoryUtils.isFiller(stack) ? null : stack);
			}
			filter.setCountComparator(EnumUtils.byIndex(ItemFilter.CountComparison.class, InventoryUtils.getIntFromStack(gui.getItem(COMPARE_COUNT_SLOT), KEY_COMPARATOR, 0)));
			filter.setWhitelisted(ItemUtils.equals(gui.getItem(LIST_SLOT), WHITELIST));
			filter.setIgnoreNBT(ItemUtils.equals(gui.getItem(NBT_SLOT), IGNORE_NBT));
			this.setFilter(filter);
			this.setPriority(InventoryUtils.getIntFromStack(gui.getItem(PRIORITY_SLOT), KEY_PRIORITY));
			this.setChannel(InventoryUtils.getIntFromStack(gui.getItem(CHANNEL_SLOT), KEY_CHANNEL));
		}

	}

}
