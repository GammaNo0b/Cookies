
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.block.machine.TradingMachineBlock;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.tools.TradingCardItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TradingMachine extends AbstractItemProcessingMachine<TradingMachine, TradingMachineBlock> {

	private static final String KEY_TRADING_CARD = "tradingcard";
	private static final String KEY_TRADE = "trade";
	private static final String KEY_RESULT = "result";

	private static final int SLOT_TRADING_CARD = 1;
	private static final int SLOT_TRADING_ICON = 2;
	private static final int SLOT_TRADES_UP = 12;
	private static final int SLOT_TRADES_DOWN = 39;

	private static final ItemStack ICON_TRADING_CARD = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§bLoad Trading Data").build();
	private static final ItemStack ICON_TRADE_DISABLED = new ItemBuilder(Material.BARRIER).setName("§cDisabled").build();
	private static final ItemStack ICON_TRADES_UP = new ItemBuilder(Material.PLAYER_HEAD).setName("§aUp").setTexture(HeadTextures.WOODEN_ARROW_UP).build();
	private static final ItemStack ICON_TRADES_DOWN = new ItemBuilder(Material.PLAYER_HEAD).setName("§aDown").setTexture(HeadTextures.WOODEN_ARROW_DOWN).build();

	private TradingData data = null;
	private int tradingDataOffset = 0;
	private TradingRecipe trade = null;
	private ItemStack result = null;

	public TradingMachine(TradingMachineBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.trade = PersistentDataUtils.get(data, KEY_TRADE, new TradingRecipe());
		this.result = PersistentDataUtils.getItemStack(data, KEY_RESULT);

		Inventory gui = this.getInventory();
		gui.setItem(SLOT_TRADING_CARD, PersistentDataUtils.getItemStack(data, KEY_TRADING_CARD));
		this.loadTrades();
		this.updateTrades();

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		if(this.trade != null)
			PersistentDataUtils.set(data, KEY_TRADE, this.trade);
		if(this.result != null)
			PersistentDataUtils.setItemStack(data, KEY_RESULT, this.result);

		Inventory gui = this.getInventory();
		PersistentDataUtils.setItemStack(data, KEY_TRADING_CARD, gui.getItem(SLOT_TRADING_CARD));

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		inventory.setItem(SLOT_TRADING_CARD, null);
		inventory.setItem(SLOT_TRADING_ICON, ICON_TRADING_CARD);
		inventory.setItem(SLOT_TRADES_UP, ICON_TRADES_UP);
		inventory.setItem(SLOT_TRADES_DOWN, ICON_TRADES_DOWN);
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 15, 16 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 33, 34, 42, 43 };
	}


	@Override
	protected int createNextProcess() {
		Map<ItemStack, Integer> inputs = this.getUseableItems();

		if(this.data == null)
			return 0;

		loop: for(TradingRecipe trade : this.data.trades) {
			if(!trade.enabled)
				continue;

			Map<ItemStack, Integer> copy = new HashMap<>(inputs);

			ArrayList<ItemStack> consume = new ArrayList<>();
			for(ItemStack stack : new ItemStack[] { trade.first, trade.second }) {
				if(ItemUtils.isEmpty(stack))
					continue;

				ItemStack type = stack.clone();
				type.setAmount(1);
				if(copy.merge(type, -stack.getAmount(), (i, j) -> i - j) < 0)
					continue loop;

				consume.add(stack.clone());
			}

			this.consumeInputs(consume);

			this.trade = trade;
			this.result = trade.result.clone();
			return trade.getDuration();
		}

		return 0;
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		this.result = this.storeOutput(this.result);
		return ItemUtils.isEmpty(this.result);
	}


	@Override
	public void destroy() {
		super.destroy();

		if(!ItemUtils.isEmpty(this.trade.first))
			ItemUtils.dropItem(this.trade.first.clone(), this.block);
		if(!ItemUtils.isEmpty(this.trade.second))
			ItemUtils.dropItem(this.trade.second.clone(), this.block);
		ItemUtils.dropItem(this.getInventory().getItem(SLOT_TRADING_CARD), this.block);
	}


	private void loadTrades() {
		Inventory gui = this.getInventory();
		ItemStack tradingCard = gui.getItem(SLOT_TRADING_CARD);
		AbstractCustomItem custom = Items.getCustomItemFromStack(tradingCard);
		this.data = custom instanceof TradingCardItem item ? item.getTradingData(tradingCard) : null;
		this.tradingDataOffset = 0;
	}


	private void updateTrades() {
		Inventory gui = this.getInventory();
		final ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		int r = 1;
		if(this.data != null) {
			for(int i = this.tradingDataOffset; r < 5 && i < this.data.trades.size(); ++r, ++i) {
				TradingRecipe trade = this.data.trades.get(i);
				gui.setItem(r * 9, trade.enabled ? ItemUtils.isEmpty(trade.first) ? filler : new ItemBuilder(trade.first).setLore(List.of("", "§7First ingredient")).build() : ICON_TRADE_DISABLED);
				gui.setItem(r * 9 + 1, trade.enabled ? ItemUtils.isEmpty(trade.second) ? filler : new ItemBuilder(trade.second).setLore(List.of("", "§7Second ingredient")).build() : ICON_TRADE_DISABLED);
				gui.setItem(r * 9 + 2, new ItemBuilder(trade.result).setLore(List.of("", String.format("§7Trading duration: §2%.2f§7s", trade.getDuration() * 0.05D))).build());
			}
		}
		for(; r < 5; ++r)
			for(int c = 0; c < 3; ++c)
				gui.setItem(9 * r + c, filler);
	}


	private void saveTrades() {
		if(this.data == null)
			return;

		Inventory gui = this.getInventory();
		ItemStack tradingCard = gui.getItem(SLOT_TRADING_CARD);
		AbstractCustomItem custom = Items.getCustomItemFromStack(tradingCard);
		if(!(custom instanceof TradingCardItem item))
			return;

		item.setTradingData(tradingCard, this.data);

	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, gui, event))
			return false;

		int slot = event.getSlot();
		if(slot == SLOT_TRADES_UP) {
			if(--this.tradingDataOffset < 0 || event.isShiftClick())
				this.tradingDataOffset = 0;
			this.updateTrades();
		}

		if(slot == SLOT_TRADES_DOWN) {
			if(this.data == null)
				return true;

			int max = this.data.trades.size() - 4;
			if(max < 0)
				max = 0;
			if(++this.tradingDataOffset > max || event.isShiftClick())
				this.tradingDataOffset = max;
			this.updateTrades();
		}

		if(slot == SLOT_TRADING_ICON) {
			this.loadTrades();
			this.updateTrades();
			return true;
		}

		if(slot == SLOT_TRADING_CARD) {
			Utils.runLater(() -> {
				this.loadTrades();
				this.updateTrades();
			});
			return false;
		}

		int c = slot % 9;
		int r = slot / 9;
		if(0 <= c && c < 3 && 1 <= r && r < 5) {
			if(this.data != null) {
				int i = this.tradingDataOffset + r - 1;
				if(i < this.data.trades.size()) {
					TradingRecipe trade = this.data.trades.get(i);
					trade.enabled = !trade.enabled;
					this.updateTrades();
					this.saveTrades();
				}
			}
		}

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.EMERALD;
	}


	@Override
	public TradingMachine castTileEntity() {
		return this;
	}

	public static class TradingRecipe implements DataStorage {

		private static final String KEY_RESULT = "result";
		private static final String KEY_FIRST = "first";
		private static final String KEY_SECOND = "second";
		private static final String KEY_ENABLED = "enabled";

		private ItemStack result;
		private ItemStack first;
		private ItemStack second;
		private boolean enabled = true;

		public TradingRecipe() {}


		public TradingRecipe(ItemStack result, ItemStack first, ItemStack second) {
			this.result = result;
			this.first = first;
			this.second = second;
		}


		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			this.result = PersistentDataUtils.getItemStack(data, KEY_RESULT);
			this.first = PersistentDataUtils.getItemStack(data, KEY_FIRST);
			this.second = PersistentDataUtils.getItemStack(data, KEY_SECOND);
			this.enabled = data.getBoolean(KEY_ENABLED, true);

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			PersistentDataUtils.setItemStack(data, KEY_RESULT, this.result);
			PersistentDataUtils.setItemStack(data, KEY_FIRST, this.first);
			PersistentDataUtils.setItemStack(data, KEY_SECOND, this.second);
			data.setBoolean(KEY_ENABLED, this.enabled);

			return true;
		}


		public int getDuration() {
			int firstItems = ItemUtils.isEmpty(this.first) ? 0 : this.first.getAmount();
			int secondItems = ItemUtils.isEmpty(this.second) ? 0 : this.second.getAmount();
			int resultItems = this.result.getAmount();
			return (firstItems + secondItems) * 100 + resultItems * 200;
		}

	}

	public static class TradingData implements DataStorage {

		private static final String KEY_MERCHANT = "merchant";
		private static final String KEY_TRADES = "trades";

		private String merchant = "-";
		private List<TradingRecipe> trades = new ArrayList<>();

		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			this.merchant = data.getString(KEY_MERCHANT, "Merchant");
			PersistentDataUtils.getList(data, KEY_TRADES, this.trades, o -> {
				TradingRecipe recipe = new TradingRecipe();
				recipe.load(null, o);
				return recipe;
			});

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			data.setString(KEY_MERCHANT, this.merchant);
			PersistentDataUtils.setList(data, KEY_TRADES, this.trades, (o, r) -> r.save(null, o));

			return false;
		}


		public boolean loadTrades(Merchant merchant) {
			this.trades.clear();

			this.merchant = switch (merchant) {
				case Villager villager -> {
					NamespacedKey typek = villager.getVillagerType().getKeyOrNull();
					NamespacedKey professionk = villager.getProfession().getKeyOrNull();
					String type = typek == null ? null : Utils.toCapitalWords(typek.getKey());
					String profession = professionk == null ? null : Utils.toCapitalWords(professionk.getKey());
					yield type + " " + profession;
				}
				case WanderingTrader _ -> "Wandering Trader";
				default -> "Merchant";
			};

			for(MerchantRecipe recipe : merchant.getRecipes()) {
				List<ItemStack> ingredients = recipe.getIngredients();
				this.trades.add(new TradingRecipe(recipe.getResult(), ingredients.isEmpty() ? null : ingredients.getFirst(), ingredients.size() < 2 ? null : ingredients.get(1)));
			}

			return !this.trades.isEmpty();
		}


		public void clear() {
			this.merchant = "-";
			this.trades.clear();
		}


		public String getMerchant() {
			return this.merchant;
		}


		public int getTrades() {
			return this.trades.size();
		}

	}

}
