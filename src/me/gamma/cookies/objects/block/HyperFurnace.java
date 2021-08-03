
package me.gamma.cookies.objects.block;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.property.StringProperty;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.MathHelper;
import me.gamma.cookies.util.Utilities;
import net.minecraft.nbt.NBTTagList;



public class HyperFurnace extends AbstractTileStateBlock implements BlockTicker, GuiProvider, Ownable, Switchable, ItemConsumer, ItemSupplier {

	private final HashSet<Location> locations = new HashSet<>();
	private final HashMap<Location, List<Player>> viewers = new HashMap<>();
	private final HashMap<Location, Inventory> inventories = new HashMap<>();

	private final int level;
	private final int speed;
	private final double fortune;
	private final int efficiency;
	private final int slots;
	private final ItemStack previousFurnace;

	public HyperFurnace(int level, int speed, double fortune, int efficiency, int slots, ItemStack previousFurnace) {
		this.level = level;
		this.speed = speed;
		this.fortune = fortune;
		this.efficiency = efficiency;
		this.slots = slots;
		this.previousFurnace = previousFurnace;

		register();
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public void load(NBTTagList list) {
		BlockTicker.super.load(list);
		this.setupFurnaces();
	}


	private void setupFurnaces() {
		for(Location location : this.getLocations()) {
			if(location.getBlock().getState() instanceof TileState) {
				TileState furnace = (TileState) location.getBlock().getState();
				this.createMainGui(null, furnace);
			}
		}
	}


	private List<Integer> getInputSlots() {
		List<Integer> slots = new ArrayList<>(this.slots);
		for(int i = 0; i < this.slots; i++)
			slots.add((i + 1) * 9 + 2);
		return slots;
	}


	private List<Integer> getOutputSlots() {
		List<Integer> slots = new ArrayList<>(this.slots);
		for(int i = 0; i < this.slots; i++)
			slots.add((i + 1) * 9 + 6);
		return slots;
	}


	private int getFuelSlot() {
		return this.getRows() * 9 - 16;
	}


	@Override
	public List<ItemProvider> getInputStackHolders(TileState block) {
		return this.getInputSlots().stream().map(slot -> ItemProvider.fromInventory(this.getInventory(block.getLocation()), slot)).collect(Collectors.toList());
	}


	@Override
	public List<ItemProvider> getOutputStackHolders(TileState block) {
		return this.getOutputSlots().stream().map(slot -> ItemProvider.fromInventory(this.getInventory(block.getLocation()), slot)).collect(Collectors.toList());
	}


	@Override
	public int getRows() {
		return this.slots + 3;
	}


	@Override
	public Sound getSound() {
		return null;
	}


	@Override
	public List<Player> getViewers(TileState block) {
		List<Player> viewers = this.viewers.get(block.getLocation());
		if(viewers == null) {
			viewers = new ArrayList<>();
			this.viewers.put(block.getLocation(), viewers);
		}
		return viewers;
	}


	@Override
	public void tick(TileState block) {
		boolean shouldBurn = false;
		for(int i = 0; i < this.slots; i++) {
			if(this.getProgress(block, i) > 0) {
				shouldBurn = true;
				break;
			}
			if(this.getRecipe(block, i) == null && getRecipeWith(this.getInput(block, i)) != null) {
				shouldBurn = true;
				break;
			}
		}
		boolean canProgress = !shouldBurn;
		if(shouldBurn)
			canProgress = this.burnFuel(block);

		for(int i = 0; i < this.slots; i++) {
			int progress = this.getProgress(block, i);
			CookingRecipe<?> recipe = this.getRecipe(block, i);
			if(canProgress && progress > 0)
				progress -= this.speed;
			if(progress <= 0) {
				if(recipe != null) {
					boolean newRecipe = false;
					ItemStack result = recipe.getResult();
					ItemStack output = this.getOutput(block, i);
					boolean empty = Utilities.isEmpty(output);
					int total = result.getAmount() + (empty ? 0 : output.getAmount());
					if(empty || CookieRecipe.sameIngredient(result, output) && total <= result.getType().getMaxStackSize()) {
						result.setAmount(total);
						this.setOutput(block, i, result);
						newRecipe = true;
					}
					if(newRecipe) {
						recipe = null;
						this.setProgress(block, recipe, i, 0);
						this.setRecipe(block, i, null);
						this.setProcessing(block, i, null);
					} else {
						this.setProgress(block, recipe, i, -1);
					}
				}
			} else {
				this.setProgress(block, recipe, i, progress);
			}

			if(recipe == null && shouldBurn && canProgress) {
				ItemStack input = this.getInput(block, i);
				if(!Utilities.isEmpty(input)) {
					CookingRecipe<?> cooking = getRecipeWith(input);
					if(cooking != null) {
						ItemStack clone = input.clone();
						clone.setAmount(1);
						this.setProcessing(block, i, clone);
						this.setRecipe(block, i, cooking);
						this.setProgress(block, cooking, i, cooking.getCookingTime());
						input.setAmount(input.getAmount() - 1);
						this.setInput(block, i, input);
					}
				}
			}
		}

		this.storeItemProperties(block);
		block.update();
	}


	private void storeItemProperties(TileState block) {
		Inventory inventory = this.getInventory(block.getLocation());
		this.createSlotFuelProperty().store(block, inventory.getItem(this.getFuelSlot()));
		ItemStackProperty[] slotInputProperties = this.createSlotInputProperties();
		ItemStackProperty[] slotOutputProperties = this.createSlotOutputProperties();
		for(int i = 0; i < this.slots; i++) {
			slotInputProperties[i].store(block, inventory.getItem((i + 1) * 9 + 2));
			slotOutputProperties[i].store(block, inventory.getItem((i + 1) * 9 + 6));
		}
	}


	private boolean burnFuel(TileState block) {
		final IntegerProperty fuelCounterProperty = this.createFuelCounterProperty();
		int counter = fuelCounterProperty.fetch(block);
		if(counter > 0) {
			counter--;
			fuelCounterProperty.store(block, counter);
		} else {
			final Inventory inventory = this.getInventory(block.getLocation());
			final IntegerProperty fuelProperty = this.createFuelProperty();
			int fuel = fuelProperty.fetch(block);
			if(fuel > 0) {
				fuel--;
				fuelProperty.store(block, fuel);
			} else {
				final ItemStackProperty fuelSlotProperty = this.createSlotFuelProperty();
				ItemStack stack = fuelSlotProperty.fetch(block);
				int burntime;
				if(!Utilities.isEmpty(stack) && (burntime = Utilities.getFuel(stack.getType())) > 0) {
					fuelProperty.store(block, burntime);
					stack.setAmount(stack.getAmount() - 1);
					fuelSlotProperty.store(block, stack);
					inventory.setItem(inventory.getSize() - 16, stack);
				} else {
					return false;
				}
			}
			fuelCounterProperty.store(block, this.efficiency - 1);
			ItemStack stack = inventory.getItem(inventory.getSize() - 14);
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName("§8Fuel: §6" + fuelProperty.fetch(block));
			stack.setItemMeta(meta);
		}
		block.update();
		return true;
	}


	private static CookingRecipe<?> getRecipeWith(ItemStack ingredient) {
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		while(iterator.hasNext()) {
			Recipe recipe = iterator.next();
			if(recipe instanceof CookingRecipe<?>) {
				CookingRecipe<?> cooking = (CookingRecipe<?>) recipe;
				if(cooking.getInputChoice().test(ingredient)) {
					return cooking;
				}
			}
		}
		return null;
	}


	private void setInput(TileState block, int slot, ItemStack input) {
		if(Utilities.isEmpty(input))
			input = null;
		this.createSlotInputProperty(slot).store(block, input);
		this.getInventory(block.getLocation()).setItem((slot + 1) * 9 + 2, input);
	}


	private ItemStack getInput(TileState block, int slot) {
		return this.createSlotInputProperty(slot).fetch(block);
	}


	private void setOutput(TileState block, int slot, ItemStack output) {
		if(Utilities.isEmpty(output))
			output = null;
		this.createSlotOutputProperty(slot).store(block, output);
		this.getInventory(block.getLocation()).setItem((slot + 1) * 9 + 6, output);
	}


	private ItemStack getOutput(TileState block, int slot) {
		return this.createSlotOutputProperty(slot).fetch(block);
	}


	private void setProcessing(TileState block, int slot, ItemStack processing) {
		if(Utilities.isEmpty(processing))
			processing = null;
		this.createSlotProcessingProperty(slot).store(block, processing);
	}


	private ItemStack getProcessing(TileState block, int slot) {
		return this.createSlotProcessingProperty(slot).fetch(block);
	}


	private void setProgress(TileState block, CookingRecipe<?> recipe, int slot, int progress) {
		this.createSlotProgressProperty(slot).store(block, progress);
		StringBuilder builder = new StringBuilder();
		if(progress == -1) {
			builder.append("§c------->");
		} else if(progress == 0) {
			builder.append("§7------->");
		} else {
			int length = recipe.getCookingTime();
			int intprogress = (int) Math.round(MathHelper.map(progress, length, 0, 0, 8));
			if(intprogress == 0) {
				builder.append("§7------->");
			} else if(intprogress == 8) {
				builder.append("§c------->");
			} else {
				builder.append("§c");
				for(int i = 0; i < intprogress; i++)
					builder.append('-');
				builder.append("§7");
				for(int i = intprogress; i < 7; i++)
					builder.append('-');
				builder.append('>');
			}
		}
		this.getInventory(block.getLocation()).setItem((slot + 1) * 9 + 4, new ItemBuilder(Material.FLINT_AND_STEEL).setName(builder.toString()).build());
	}


	private int getProgress(TileState block, int slot) {
		return this.createSlotProgressProperty(slot).fetch(block);
	}


	private void setRecipe(TileState block, int slot, CookingRecipe<?> recipe) {
		this.createSlotRecipeProperty(slot).store(block, recipe == null ? "" : recipe.getKey().getKey());
	}


	private CookingRecipe<?> getRecipe(TileState block, int slot) {
		String key = this.createSlotRecipeProperty(slot).fetch(block);
		if(key == null)
			return null;
		Recipe recipe;
		try {
			recipe = Bukkit.getRecipe(NamespacedKey.minecraft(key));
		} catch(IllegalArgumentException e) {
			return null;
		}
		if(recipe == null)
			return null;
		if(recipe instanceof CookingRecipe)
			return (CookingRecipe<?>) recipe;
		return null;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return true;
	}


	@Override
	public String getRegistryName() {
		return "hyper_furnace_" + this.level;
	}


	@Override
	public String getDisplayName() {
		return "§8Hyper Furnace §cLevel §4" + Utilities.romanNumber(this.level);
	}


	@Override
	protected Material getMaterial() {
		return Material.FURNACE;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore == null)
			lore = new ArrayList<>();
		lore.add("§dFortune: §5" + this.fortune);
		lore.add("§eSpeed: §6" + this.speed);
		lore.add("§aEfficiency: §2" + this.efficiency);
		lore.add("§3Slots: §9" + this.slots);
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" I ", "HFH", "EME");
		recipe.setIngredient('I', Material.COPPER_INGOT);
		recipe.setIngredient('H', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		recipe.setIngredient('E', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.ELECTROMAGNET.createDefaultItemStack());
		recipe.setIngredient('F', this.previousFurnace);
		return recipe;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		this.createFuelProperty().storeEmpty(block);
		this.createFuelCounterProperty().storeEmpty(block);
		this.createSlotFuelProperty().storeEmpty(block);
		for(ItemStackProperty property : this.createSlotInputProperties())
			property.storeEmpty(block);
		for(ItemStackProperty property : this.createSlotOutputProperties())
			property.storeEmpty(block);
		for(IntegerProperty property : this.createSlotProgressProperties())
			property.storeEmpty(block);
		for(StringProperty property : this.createSlotRecipeProperties())
			property.storeEmpty(block);
		this.inventories.put(block.getLocation(), this.createMainGui(null, block));
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		Utilities.dropItem(this.createSlotFuelProperty().fetch(block), block.getLocation());
		for(int i = 0; i < this.slots; i++) {
			Utilities.dropItem(this.getInput(block, i), block.getLocation());
			Utilities.dropItem(this.getOutput(block, i), block.getLocation());
			Utilities.dropItem(this.getProcessing(block, i), block.getLocation());
		}
		this.inventories.remove(block.getLocation());
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(player.isSneaking())
			return false;
		GuiProvider.super.openGui(player, block);
		return true;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory inventory = this.getInventory(block.getLocation());
		if(inventory == null) {
			inventory = GuiProvider.super.createMainGui(player, block);
			int size = inventory.getSize();

			inventory.setItem(size - 5, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§cLevel: §4" + Utilities.romanNumber(this.level)).addLore("§dFortune: §5" + this.fortune).addLore("§eSpeed: §6" + this.speed).addLore("§aEfficiency: §2" + this.efficiency).addLore("§3Slots: §9" + this.slots).build());

			for(int i = 0; i < 9; i++) {
				if(i != this.getIdentifierSlot())
					inventory.setItem(i, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
				inventory.setItem(size - 1 - i, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
			}

			for(int i = 0; i < this.slots; i++) {
				int firstSlot = (i + 1) * 9;
				inventory.setItem(firstSlot, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
				inventory.setItem(firstSlot + 8, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
				inventory.setItem(firstSlot + 1, InventoryManager.filler(Material.GREEN_STAINED_GLASS_PANE));
				inventory.setItem(firstSlot + 3, InventoryManager.filler(Material.GREEN_STAINED_GLASS_PANE));
				inventory.setItem(firstSlot + 4, new ItemBuilder(Material.FLINT_AND_STEEL).setName("§7------->").build());
				inventory.setItem(firstSlot + 5, InventoryManager.filler(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(firstSlot + 7, InventoryManager.filler(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(firstSlot + 2, this.getInput(block, i));
				inventory.setItem(firstSlot + 6, this.getOutput(block, i));
			}

			inventory.setItem(size - 10, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
			inventory.setItem(size - 18, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
			inventory.setItem(size - 17, InventoryManager.filler(Material.BLACK_STAINED_GLASS_PANE));
			inventory.setItem(size - 15, InventoryManager.filler(Material.BLACK_STAINED_GLASS_PANE));
			inventory.setItem(size - 14, new ItemBuilder(Material.COAL).setName("§8Fuel: §6" + this.createFuelProperty().fetch(block)).build());
			inventory.setItem(this.getFuelSlot(), this.createSlotFuelProperty().fetch(block));
			for(int i = 11; i < 14; i++)
				inventory.setItem(size - i, InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));

			this.inventories.put(block.getLocation(), inventory);
		}
		return inventory;
	}


	@Override
	public void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		GuiProvider.super.onInventoryClose(player, block, gui, event);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		final int slot = event.getSlot();
		if(this.getInputSlots().contains(slot)) {
			return false;
		} else if(this.getOutputSlots().contains(slot)) {
			return event.getCurrentItem() == null;
		} else if(slot == this.getFuelSlot()) {
			return false;
		} else {
			return true;
		}
	}


	private Inventory getInventory(Location location) {
		return this.inventories.get(location);
	}


	private ItemStackProperty[] createSlotInputProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[this.slots];
		for(int i = 0; i < this.slots; i++)
			properties[i] = this.createSlotInputProperty(i);
		return properties;
	}


	private ItemStackProperty createSlotInputProperty(int slot) {
		return new ItemStackProperty("InputSlot" + slot);
	}


	private ItemStackProperty[] createSlotOutputProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[this.slots];
		for(int i = 0; i < this.slots; i++)
			properties[i] = this.createSlotOutputProperty(i);
		return properties;
	}


	private ItemStackProperty createSlotOutputProperty(int slot) {
		return new ItemStackProperty("OutputSlot" + slot);
	}


	private ItemStackProperty createSlotProcessingProperty(int slot) {
		return new ItemStackProperty("ProcessingSlot" + slot);
	}


	private StringProperty[] createSlotRecipeProperties() {
		StringProperty[] properties = new StringProperty[this.slots];
		for(int i = 0; i < this.slots; i++)
			properties[i] = this.createSlotRecipeProperty(i);
		return properties;
	}


	private StringProperty createSlotRecipeProperty(int slot) {
		return new StringProperty("RecipeSlot" + slot);
	}


	private IntegerProperty[] createSlotProgressProperties() {
		IntegerProperty[] properties = new IntegerProperty[this.slots];
		for(int i = 0; i < this.slots; i++)
			properties[i] = this.createSlotProgressProperty(i);
		return properties;
	}


	private IntegerProperty createSlotProgressProperty(int slot) {
		return new IntegerProperty("ProgressSlot" + slot);
	}


	private ItemStackProperty createSlotFuelProperty() {
		return new ItemStackProperty("FuelSlot");
	}


	private IntegerProperty createFuelProperty() {
		return new IntegerProperty("Fuel");
	}


	private IntegerProperty createFuelCounterProperty() {
		return new IntegerProperty("FuelCounter");
	}

}
