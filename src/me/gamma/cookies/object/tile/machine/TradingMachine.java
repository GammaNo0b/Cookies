
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TradingMachine extends AbstractItemProcessingMachine<TradingMachine, TradingMachineBlock> {

	private static final String KEY_TRADING_CARD = "tradingcard";
	private static final String KEY_INGREDIENTS = "ingredients";
	private static final String KEY_RESULT = "result";

	private static final int SLOT_TRADING_CARD = 1;
	private static final int SLOT_TRADING_ICON = 2;
	private static final int SLOT_TRADES_UP = 12;
	private static final int SLOT_TRADES_DOWN = 39;
	private static final int SLOT_VILLAGER_STATUS = 46;

	private static final ItemStack ICON_TRADING_CARD = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§bLoad Trading Data").build();
	private static final ItemStack ICON_TRADE_DISABLED = new ItemBuilder(Material.BARRIER).setName("§cDisabled").build();
	private static final ItemStack ICON_TRADES_UP = new ItemBuilder(Material.PLAYER_HEAD).setName("§aUp").setTexture(HeadTextures.WOODEN_ARROW_UP).build();
	private static final ItemStack ICON_TRADES_DOWN = new ItemBuilder(Material.PLAYER_HEAD).setName("§aDown").setTexture(HeadTextures.WOODEN_ARROW_DOWN).build();

	private TradingData data = null;
	private int tradingDataOffset = 0;
	private ItemStack[] ingredients = null;
	private ItemStack result = null;

	public TradingMachine(TradingMachineBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.ingredients = PersistentDataUtils.getArray(data, KEY_INGREDIENTS, ItemStack[]::new, PersistentDataUtils::loadItemStack);
		this.result = PersistentDataUtils.getItemStack(data, KEY_RESULT);

		Inventory gui = this.getInventory();
		gui.setItem(SLOT_TRADING_CARD, PersistentDataUtils.getItemStack(data, KEY_TRADING_CARD));
		this.loadTradeData();
		this.updateTrades();

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		if(this.ingredients != null)
			PersistentDataUtils.setArray(data, KEY_INGREDIENTS, this.ingredients, PersistentDataUtils::saveItemStack);
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
		inventory.setItem(SLOT_VILLAGER_STATUS, VillagerStatus.INVALID.icon);
	}


	@Override
	public int[] getInputSlots() {
		return new int[] { 15, 16 };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 33, 34, 42, 43 };
	}


	@Override
	protected int createNextProcess() {
		Map<ItemStack, Integer> inputs = this.getUseableItems();

		if(this.data == null)
			return 0;

		Pair<Villager, VillagerStatus> pair = this.getVillagerAndStatus();
		if(pair.right != VillagerStatus.VALID)
			return 0;

		Villager villager = pair.left;
		int reputation = villager.getReputation(this.getOwner());

		loop: for(int i = 0; i < this.data.trades; ++i) {
			if(!this.data.isTradeEnabled(i))
				continue;

			MerchantRecipe trade = villager.getRecipe(i);
			if(trade.getUses() >= trade.getMaxUses())
				continue;

			List<ItemStack> ingredients = trade.getIngredients();
			if(ingredients.isEmpty())
				continue;

			trade.setSpecialPrice(trade.getSpecialPrice() - (int) Math.floor(reputation * trade.getPriceMultiplier()));

			List<ItemStack> adjustedIngredients = new ArrayList<>(2);
			adjustedIngredients.add(trade.getAdjustedIngredient1());
			if(ingredients.size() > 1)
				adjustedIngredients.add(ingredients.get(1));

			trade.setSpecialPrice(0);

			Map<ItemStack, Integer> copy = new HashMap<>(inputs);
			List<ItemStack> consume = new ArrayList<>();
			for(ItemStack cost : adjustedIngredients) {
				if(ItemUtils.isEmpty(cost))
					continue;

				ItemStack type = cost.clone();
				type.setAmount(1);
				if(copy.merge(type, -cost.getAmount(), Integer::sum) < 0)
					continue loop;

				consume.add(cost);
			}

			this.consumeInputs(consume);

			trade.setUses(trade.getUses() + 1);
			villager.setRecipe(i, trade);
			this.updateTrade(i, trade);

			this.ingredients = adjustedIngredients.toArray(ItemStack[]::new);
			this.result = trade.getResult().clone();

			return 200;
		}

		return 0;
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		this.ingredients = null;
		this.result = this.storeOutput(this.result);
		return ItemUtils.isEmpty(this.result);
	}


	@Override
	public void destroy() {
		super.destroy();

		if(this.ingredients != null)
			for(ItemStack ingredient : this.ingredients)
				if(!ItemUtils.isEmpty(ingredient))
					ItemUtils.dropItem(ingredient, this.block);
		ItemUtils.dropItem(this.getInventory().getItem(SLOT_TRADING_CARD), this.block);
	}


	private void loadTradeData() {
		Inventory gui = this.getInventory();
		ItemStack tradingCard = gui.getItem(SLOT_TRADING_CARD);
		AbstractCustomItem custom = Items.getCustomItemFromStack(tradingCard);
		this.data = custom instanceof TradingCardItem item ? item.getTradingData(tradingCard) : null;
		this.tradingDataOffset = 0;
	}


	private void updateTrade(int index, MerchantRecipe trade) {
		int row = 1 + index - this.tradingDataOffset;
		if(1 <= row && row < 5)
			this.updateTrade(this.getInventory(), row, index, trade);
	}


	private double calculatePriceAdjustment(MerchantRecipe trade) {
		List<ItemStack> ingredients = trade.getIngredients();
		if(ingredients.isEmpty())
			return 1.0D;

		ItemStack base = ingredients.get(0);
		ItemStack adjusted = trade.getAdjustedIngredient1();
		return (double) adjusted.getAmount() / base.getAmount();
	}


	private void updateTrade(Inventory gui, int row, int index, MerchantRecipe trade) {
		final ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		boolean enabled = this.data.isTradeEnabled(index);
		ItemStack first = trade.getAdjustedIngredient1();
		List<ItemStack> ingredients = trade.getIngredients();
		ItemStack second = ingredients.size() > 1 ? ingredients.get(1) : null;
		double priceAdjustment = this.calculatePriceAdjustment(trade);
		int priceAdjustmentPercentage = (int) Math.round(100.0D * priceAdjustment);
		String priceAdjustmentStr;
		if(priceAdjustmentPercentage < 50) {
			priceAdjustmentStr = "§a";
		} else if(priceAdjustmentPercentage < 80) {
			priceAdjustmentStr = "§e";
		} else if(priceAdjustmentPercentage > 250) {
			priceAdjustmentStr = "§4";
		} else if(priceAdjustmentPercentage > 120) {
			priceAdjustmentStr = "§c";
		} else {
			priceAdjustmentStr = "§6";
		}
		priceAdjustmentStr = priceAdjustmentStr + priceAdjustmentPercentage + "%";
		gui.setItem(row * 9, enabled ? ItemUtils.isEmpty(first) ? filler : new ItemBuilder(first).setLore(List.of("", "§7First ingredient")).build() : ICON_TRADE_DISABLED);
		gui.setItem(row * 9 + 1, enabled ? ItemUtils.isEmpty(second) ? filler : new ItemBuilder(second).setLore(List.of("", "§7Second ingredient")).build() : ICON_TRADE_DISABLED);
		gui.setItem(row * 9 + 2, new ItemBuilder(trade.getResult()).setLore(List.of("", String.format("§7Trades left: §3%d §8/ §3%d", trade.getMaxUses() - trade.getUses(), trade.getMaxUses()), "§7Price Adjustment: " + priceAdjustmentStr)).build());
	}


	private Pair<Villager, VillagerStatus> getVillagerAndStatus() {
		if(this.data == null) {
			this.getInventory().setItem(SLOT_VILLAGER_STATUS, VillagerStatus.NO_CARD.icon.clone());
			return new Pair<>(null, VillagerStatus.NO_CARD);
		}

		Pair<Villager, VillagerStatus> pair = this.data.getVillager(this.getBlock().getRelative(BlockFace.DOWN));
		this.getInventory().setItem(SLOT_VILLAGER_STATUS, new ItemBuilder(pair.right.icon).setLore(List.of("", "§8" + this.data.name, "  §7Trades: §3" + this.data.trades)).build());
		return pair;
	}


	private void updateTrades() {
		Pair<Villager, VillagerStatus> pair = this.getVillagerAndStatus();
		Inventory gui = this.getInventory();

		final ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

		Villager villager = pair.left;
		int r = 1;
		if(villager != null)
			for(int i = this.tradingDataOffset; r < 5 && i < this.data.trades; ++r, ++i)
				this.updateTrade(gui, r, i, villager.getRecipe(i));

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
			if(this.data == null)
				return true;

			if(--this.tradingDataOffset < 0 || event.isShiftClick())
				this.tradingDataOffset = 0;

			this.updateTrades();

			return true;
		}

		if(slot == SLOT_TRADES_DOWN) {
			if(this.data == null)
				return true;

			int max = this.data.trades - 4;
			if(max < 0)
				max = 0;

			if(++this.tradingDataOffset > max || event.isShiftClick())
				this.tradingDataOffset = max;

			this.updateTrades();

			return true;
		}

		if(slot == SLOT_TRADING_ICON) {
			this.loadTradeData();
			this.updateTrades();

			return true;
		}

		if(slot == SLOT_TRADING_CARD) {
			Utils.runLater(() -> {
				this.loadTradeData();
				this.updateTrades();
			});

			return false;
		}

		int c = slot % 9;
		int r = slot / 9;
		if(0 <= c && c < 3 && 1 <= r && r < 5) {
			if(this.data != null) {
				int i = this.tradingDataOffset + r - 1;
				if(i < this.data.trades) {
					this.data.setTradeEnabled(i, !this.data.isTradeEnabled(i));
					this.saveTrades();

					Pair<Villager, VillagerStatus> pair = this.getVillagerAndStatus();
					Villager villager = pair.left;
					if(villager != null && i < villager.getRecipeCount())
						this.updateTrade(gui, r, i, villager.getRecipe(i));
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

	public static class TradingData implements DataStorage {

		public static final double MAX_TRADING_DISTANCE = 4.0D;

		private static final String KEY_VILLAGER_ID = "id";
		private static final String KEY_VILLAGER_NAME = "name";
		private static final String KEY_TRADES = "trades";
		private static final String KEY_ENABLED_TRADES = "enabled";

		private UUID villagerID;
		private String name = "-";
		private int trades = 0;
		private int enabledTrades = -1;

		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			this.villagerID = data.getUUID(KEY_VILLAGER_ID);
			this.name = data.getString(KEY_VILLAGER_NAME, "Merchant");
			this.trades = data.getInteger(KEY_TRADES, 0);
			this.enabledTrades = data.getInteger(KEY_ENABLED_TRADES, 0);

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			data.setUUID(KEY_VILLAGER_ID, this.villagerID);
			data.setString(KEY_VILLAGER_NAME, this.name);
			data.setInteger(KEY_TRADES, this.trades);
			data.setInteger(KEY_ENABLED_TRADES, this.enabledTrades);

			return false;
		}


		public String getName() {
			return this.name;
		}


		public int getTrades() {
			return this.trades;
		}


		public boolean isTradeEnabled(int i) {
			return ((this.enabledTrades >>> i) & 1) == 1;
		}


		public void setTradeEnabled(int i, boolean enable) {
			if(enable) {
				this.enabledTrades |= 1 << i;
			} else {
				this.enabledTrades &= ~(1 << i);
			}
		}


		public Pair<Villager, VillagerStatus> getVillager(Block workstation) {
			Entity entity = Bukkit.getEntity(this.villagerID);
			if(entity == null || !(entity instanceof Villager villager))
				return new Pair<>(null, VillagerStatus.INVALID);

			if(!entity.getWorld().equals(workstation.getWorld()))
				return new Pair<>(villager, VillagerStatus.OTHER_DIMENSION);

			double distance = entity.getLocation().distance(workstation.getLocation().add(0.5D, 0.5D, 0.5D));
			if(distance > MAX_TRADING_DISTANCE)
				return new Pair<>(villager, VillagerStatus.TOO_FAR);

			Location location = villager.getMemory(MemoryKey.JOB_SITE);
			if(location == null)
				return new Pair<>(villager, VillagerStatus.JOBLESS);

			if(!location.getBlock().equals(workstation))
				return new Pair<>(villager, VillagerStatus.WRONG_WORKSTATION);

			if(!this.loadData(villager))
				return new Pair<>(villager, VillagerStatus.NO_TRADES);

			if(villager.isTrading())
				return new Pair<>(villager, VillagerStatus.TRADING);

			return new Pair<>(villager, VillagerStatus.VALID);
		}


		public boolean loadData(Villager villager) {
			this.villagerID = villager.getUniqueId();
			NamespacedKey typek = villager.getVillagerType().getKeyOrNull();
			NamespacedKey professionk = villager.getProfession().getKeyOrNull();
			String type = typek == null ? "" : Utils.toCapitalWords(typek.getKey());
			String profession = professionk == null ? "" : Utils.toCapitalWords(professionk.getKey());
			this.name = type + " " + profession;
			this.trades = villager.getRecipeCount();

			return this.trades > 0;
		}


		public void clear() {
			this.villagerID = new UUID(0, 0);
			this.name = "-";
			this.trades = 0;
			this.enabledTrades = 0;
		}

	}

	private static enum VillagerStatus {

		NO_CARD(Items.TRADING_CARD.getMaterial(), "§8No Trading Card inserted."),
		INVALID(Material.BARRIER, "§cInvalid Trading Card!"),
		OTHER_DIMENSION(Material.END_PORTAL_FRAME, "§6Interdimensional trading not possible."),
		TOO_FAR(Material.STICK, "§6The villager's arms are too interlocked to trade over such a large distance."),
		JOBLESS(Material.CRAFTING_TABLE, "§6This villager is currently lazy and has no workstation."),
		WRONG_WORKSTATION(Material.FLETCHING_TABLE, "§eThis villager does not work here."),
		NO_TRADES(Material.GLASS_BOTTLE, "§eThis villager does not want to give you anything, because he has nothing."),
		TRADING(Material.IRON_INGOT, "§eThis villager is already giving his stuff away."),
		VALID(Material.EMERALD, "§aThis villager is willing to give you his zeug.");

		private final ItemStack icon;

		private VillagerStatus(ItemStack icon) {
			this.icon = icon;
		}


		private VillagerStatus(Material icon, String message) {
			this(new ItemBuilder(icon).setName(message).build());
		}

	}

}
