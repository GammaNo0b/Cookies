
package me.gamma.cookies.objects.block.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.objects.block.ItemConsumer;
import me.gamma.cookies.objects.block.ItemProvider;
import me.gamma.cookies.objects.block.ItemSupplier;
import me.gamma.cookies.objects.block.Ownable;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.machine.RedstoneMode;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.property.EnumProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.property.StringProperty;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;
import net.minecraft.nbt.NBTTagList;



public interface Machine extends BlockTicker, GuiProvider, Ownable, Switchable, ItemConsumer, ItemSupplier {

	public static final ByteProperty MACHINE_IDENTIFIER = new ByteProperty("machine");
	public static final EnumProperty<RedstoneMode> REDSTONE_MODE = new EnumProperty<>("redstonemode", RedstoneMode.class);
	public static final IntegerProperty PROGRESS = new IntegerProperty("progress");
	public static final StringProperty CURRENT_RECIPE = new StringProperty("recipe");

	public static boolean isMachine(PersistentDataHolder holder) {
		return MACHINE_IDENTIFIER.isPropertyOf(holder);
	}


	boolean isInstanceOf(PersistentDataHolder holder);

	boolean isInstanceOf(TileState block);

	Map<Location, Inventory> getMachineMap();

	MachineTier getTier();

	String getMachineIdentifier();


	@Override
	default String getRegistryName() {
		return this.getMachineIdentifier() + "_tier_" + this.getTier().getTier();
	}


	default List<String> getMachineDescription() {
		return Arrays.asList(this.getTier().getDescripition(), String.format("§bSpeed§3: §e%d", this.getSpeed()), String.format("§aOutput Multiplier§2: §e%.1f", this.getOutputMultiplicator()));
	}


	default void fillMachineMap() {
		Map<Location, Inventory> map = this.getMachineMap();
		for(Location location : this.getLocations()) {
			if(location.getBlock().getState() instanceof TileState) {
				TileState block = (TileState) location.getBlock().getState();
				Inventory inventory = this.createInventory(block);
				ItemStackProperty[] inputProperties = this.createInputProperties();
				List<Integer> inputSlots = this.getInputSlots();
				for(int i = 0; i < inputProperties.length; i++)
					inventory.setItem(inputSlots.get(i), inputProperties[i].fetch(block));
				List<Integer> outputSlots = this.getOutputSlots();
				ItemStackProperty[] outputProperties = this.createOutputProperties();
				for(int i = 0; i < outputProperties.length; i++) {
					ItemStack stack = outputProperties[i].fetch(block);
					if(stack != null && stack.getType() != Material.AIR && stack.getType() != Material.BARRIER) {
						inventory.setItem(outputSlots.get(i), outputProperties[i].fetch(block));
					}
				}
				map.put(location, inventory);
			}
		}
	}


	@Override
	default void load(NBTTagList list) {
		BlockTicker.super.load(list);
		this.fillMachineMap();
	}


	/* Creates the basic Inventory. */
	default Inventory createInventory(TileState block) {
		final Location location = block.getLocation();
		Inventory gui = this.getTier().createInventory(block.getLocation(), this);
		gui.setItem(this.getIdentifierSlot(), InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE));
		InventoryManager.setIdentifierStack(location, gui.getItem(this.getIdentifierSlot()));
		gui.setItem(this.getProgressSlot(), this.getProgressIcon());
		gui.setItem(this.getRecipeBookSlot(), new ItemBuilder(Material.BOOK).setName("§6Machine Recipes").build());
		gui.setItem(this.getRedstoneModeSlot(), REDSTONE_MODE.fetch(block).getIcon());
		gui.setItem(this.getMachineUpgradesSlot(), new ItemBuilder(Material.DIAMOND).setName("§bUpgrades").build());
		return gui;
	}


	default Inventory getInventory(TileState block) {
		return this.getMachineMap().get(block.getLocation());
	}


	ItemStack createDefaultItemStack();


	default void createDefaultItemStack(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		REDSTONE_MODE.storeEmpty(meta);
		stack.setItemMeta(meta);
	}


	default void onBlockPlace(TileState block, ItemStack stack) {
		MACHINE_IDENTIFIER.storeEmpty(block);
		REDSTONE_MODE.transfer(stack.getItemMeta(), block);
		PROGRESS.store(block, -1);
		CURRENT_RECIPE.storeEmpty(block);
		for(ItemStackProperty property : this.createInputProperties())
			property.storeEmpty(block);
		for(ItemStackProperty property : this.createOutputProperties())
			property.storeEmpty(block);
		for(MachineUpgrade upgrade : this.getAllowedMachineUpgrades())
			upgrade.storeEmpty(block);
		this.getLocations().add(block.getLocation());
		this.getMachineMap().put(block.getLocation(), this.createInventory(block));
	}


	/**
	 * Causes all stored items and machine upgrades to drop when the block get's broken.
	 */
	default void onBlockBreak(TileState block, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		REDSTONE_MODE.transfer(block, meta);
		item.setItemMeta(meta);
		Location location = block.getLocation();
		this.getLocations().remove(location);
		Inventory gui = this.getMachineMap().remove(location);
		List<ItemStack> drops = new ArrayList<>();
		for(int i : this.getInputSlots()) {
			ItemStack stack = gui.getItem(i);
			if(stack != null)
				drops.add(stack);
		}
		for(int i : this.getOutputSlots()) {
			ItemStack stack = gui.getItem(i);
			if(stack != null)
				drops.add(stack);
		}
		for(MachineUpgrade upgrade : this.getAllowedMachineUpgrades()) {
			int amount = upgrade.fetch(block);
			if(amount > 0)
				drops.add(new ItemBuilder(upgrade.getItem().get()).setAmount(amount).build());
		}
		String identifier = CURRENT_RECIPE.fetch(block);
		MachineRecipe recipe;
		if(!identifier.isEmpty() && (recipe = this.getRecipeMap().get(identifier)) != null)
			for(ItemStack stack : recipe.getIngredients())
				drops.add(stack);
		drops.forEach(drop -> location.getWorld().dropItem(location, drop));
	}


	/**
	 * List of machine recipes for this machine.
	 */
	List<MachineRecipe> getMachineRecipes();


	default void createRecipeMap() {
		this.getMachineRecipes().forEach(recipe -> getRecipeMap().put(recipe.getIdentifier(), recipe));
	}


	Map<String, MachineRecipe> getRecipeMap();

	/**
	 * Slot in the inventory that displays the progress of the machine.
	 */
	int getProgressSlot();

	/**
	 * Slot in the inventory that will open the recipe list.
	 */
	int getRecipeBookSlot();

	/**
	 * Slot in the inventory that will allow to change the redstone mode.
	 */
	int getRedstoneModeSlot();

	/**
	 * Slot in the inventory that will open the machine upgrades inventory.
	 */
	int getMachineUpgradesSlot();

	/**
	 * ItemStack that will be used to show the progress. If the item has durability, the durability bar will be used aswell.
	 */
	ItemStack getProgressIcon();

	/**
	 * Amount of upgrade slots available for this machine.
	 */
	int getUpgradeSlots();

	/**
	 * Creates the icon for this machine.
	 */
	ItemStack createIcon();

	/**
	 * Returns the slot of the inventory if this machine that acts as input slots.
	 */
	List<Integer> getInputSlots();

	/**
	 * Returns the slot of the inventory if this machine that acts as output slots.
	 */
	List<Integer> getOutputSlots();


	@Override
	default List<ItemProvider> getInputStackHolders(TileState block) {
		return this.getInputSlots().stream().map(slot -> ItemProvider.fromInventory(getInventory(block), slot)).collect(Collectors.toList());
	}


	@Override
	default List<ItemProvider> getOutputStackHolders(TileState block) {
		return this.getOutputSlots().stream().map(slot -> ItemProvider.fromInventory(getInventory(block), slot)).collect(Collectors.toList());
	}


	/**
	 * Returns the speed of this machine.
	 */
	default int getSpeed() {
		return 1;
	}


	/**
	 * Returns the output multiplicator of this machine. It will add one to this and multiply it then with the amount of output items.
	 */
	default double getOutputMultiplicator() {
		return 0.0D;
	}


	@Override
	default void update(TileState block) {
		// no extra updating needed
	}


	@Override
	default Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	default Inventory createMainGui(Player player, TileState block) {
		return this.getInventory(block);
	}


	@Override
	default long getDelay() {
		return 1;
	}


	@Override
	default boolean shouldTick(TileState block) {
		return Machine.isMachine(block);
	}


	@Override
	default void tick(TileState block) {
		final Inventory inventory = this.getInventory(block);
		if(inventory == null)
			return;
		if(REDSTONE_MODE.fetch(block).isActive(this.isBlockPowered(block))) {
			final String identifier = CURRENT_RECIPE.fetch(block);
			MachineRecipe current = identifier.isEmpty() ? null : this.getRecipeMap().get(identifier);
			if(current != null) {
				int progress = PROGRESS.fetch(block) + this.getSpeed();
				// progress complete
				if(progress >= current.getDuration()) {
					// store results in a temporary list
					List<ItemStack> tempresults = new ArrayList<>();
					tempresults.add(current.getResult());
					ItemStack[] extra = current.getExtraResults();
					for(int i = 0; i < extra.length; i++)
						tempresults.add(extra[i]);
					// increase amount of items by output multiplicator and store new results in the final result list
					Random r = new Random();
					List<ItemStack> results = new ArrayList<>();
					for(int i = 0; i < tempresults.size(); i++) {
						ItemStack tempresult = tempresults.get(i);
						if(tempresult != null && tempresult.getType() != Material.AIR) {
							double factor = 1.0D + r.nextDouble() * this.getOutputMultiplicator();
							int amount = (int) Math.round(tempresult.getAmount() * factor);
							while(amount > 0) {
								int shrink = Math.min(amount, tempresult.getType().getMaxStackSize());
								amount -= shrink;
								ItemStack result = tempresult.clone();
								result.setAmount(shrink);
								results.add(result);
							}
						}
					}
					// check if results can be stored by storing all results in a copy
					boolean storeable = true;
					Inventory copy = Bukkit.createInventory(null, inventory.getSize());
					List<Integer> outputslots = this.getOutputSlots();
					for(int output : outputslots)
						copy.setItem(output, inventory.getItem(output));
					for(int i = 0; i < results.size(); i++) {
						ItemStack result = results.get(i).clone();
						for(int j = 0; j < outputslots.size(); j++) {
							int slot = outputslots.get(j);
							ItemStack stack = copy.getItem(slot);
							if(stack == null || stack.getType() == Material.AIR) {
								copy.setItem(slot, result);
								result.setAmount(0);
								break;
							} else if(CookieRecipe.sameIngredient(stack, result)) {
								int canstore = stack.getType().getMaxStackSize() - stack.getAmount();
								int store = Math.min(canstore, result.getAmount());
								stack.setAmount(stack.getAmount() + store);
								result.setAmount(result.getAmount() - store);
								if(result.getAmount() == 0)
									break;
							}
						}
						if(result.getAmount() > 0) {
							storeable = false;
							break;
						}
					}
					// return if results can't be stored
					if(!storeable)
						return;
					// store results in the right inventory
					for(int i = 0; i < results.size(); i++) {
						ItemStack result = results.get(i).clone();
						for(int j = 0; j < outputslots.size(); j++) {
							int slot = outputslots.get(j);
							ItemStack stack = inventory.getItem(slot);
							if(stack == null || stack.getType() == Material.AIR) {
								inventory.setItem(slot, result);
								break;
							} else if(CookieRecipe.sameIngredient(stack, result)) {
								int canstore = stack.getType().getMaxStackSize() - stack.getAmount();
								int store = Math.min(canstore, result.getAmount());
								stack.setAmount(stack.getAmount() + store);
								result.setAmount(result.getAmount() - store);
								if(result.getAmount() == 0)
									break;
							}
						}
					}
					// store progress and reset the current recipe
					inventory.setItem(this.getProgressSlot(), new ItemBuilder(this.getProgressIcon()).setName("§8Empty").build());
					PROGRESS.store(block, -1);
					current = null;
				} else {
					// progress foreward
					ItemStack icon = this.getProgressIcon().clone();
					ItemMeta meta = icon.getItemMeta();
					double percentage = (double) progress / (double) current.getDuration();
					meta.setDisplayName(String.format("§7%d §8/ §7%d", progress, current.getDuration()));
					meta.setLore(Arrays.asList(Utilities.colorizeProgress(percentage * 100.0D, 0.0D, 100.0D, "4c6ea2".toCharArray())));
					if(meta instanceof Damageable) {
						Damageable damageable = (Damageable) meta;
						int maxdmg = icon.getType().getMaxDurability();
						damageable.setDamage(maxdmg - (int) Math.round(maxdmg * percentage));
						icon.setItemMeta((ItemMeta) damageable);
					} else {
						icon.setItemMeta(meta);
					}
					inventory.setItem(this.getProgressSlot(), icon);
					PROGRESS.store(block, progress);
				}
			}
			if(current == null) {
				// check if new recipe should start
				List<Integer> inputslots = this.getInputSlots();
				ItemStack[] input = new ItemStack[inputslots.size()];
				for(int i = 0; i < inputslots.size(); i++)
					input[i] = inventory.getItem(inputslots.get(i));
				// iterate through all available recipes for this machine
				for(MachineRecipe recipe : this.getMachineRecipes()) {
					// if recipe has all ingredients
					if(recipe.matches(input)) {
						// start recipe
						current = recipe;
						for(ItemStack remove : current.getIngredients()) {
							remove = remove.clone();
							if(remove != null && remove.getType() != Material.AIR) {
								for(int i : this.getInputSlots()) {
									ItemStack stack = inventory.getItem(i);
									if(stack != null && CookieRecipe.sameIngredient(stack, remove)) {
										int canremove = Math.min(stack.getAmount(), remove.getAmount());
										stack.setAmount(stack.getAmount() - canremove);
										remove.setAmount(remove.getAmount() - canremove);
										if(remove.getAmount() == 0)
											continue;
									}
								}
							}
						}
						PROGRESS.storeEmpty(block);
						break;
					}
				}
			}
			CURRENT_RECIPE.store(block, current == null ? "" : current.getIdentifier());
		}
		ItemStackProperty[] inputProperties = this.createInputProperties();
		List<Integer> inputSlots = this.getInputSlots();
		for(int i = 0; i < inputProperties.length; i++)
			inputProperties[i].store(block, inventory.getItem(inputSlots.get(i)));
		List<Integer> outputSlots = this.getOutputSlots();
		ItemStackProperty[] outputProperties = this.createOutputProperties();
		for(int i = 0; i < outputProperties.length; i++) {
			ItemStack stack = inventory.getItem(outputSlots.get(i));
			if(stack != null && stack.getType() != Material.AIR) {
				outputProperties[i].store(block, stack);
			} else {
				outputProperties[i].storeEmpty(block);
			}
		}
		block.update();
	}


	@Override
	default boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == this.getRecipeBookSlot()) {
			InventoryManager.registerInventory(player, gui);
			InventoryManager.openMachineRecipeList(player, this, 1);
		} else if(slot == this.getRedstoneModeSlot()) {
			RedstoneMode mode = REDSTONE_MODE.cycle(block);
			Inventory inventory = this.getInventory(block);
			if(inventory != null) {
				inventory.setItem(this.getRedstoneModeSlot(), mode.getIcon());
				block.update();
			}
		} else if(slot == this.getMachineUpgradesSlot()) {
			InventoryManager.registerInventory(player, gui);
			InventoryManager.openUpgradeInventory(player, this, block);
		}
		return !this.getInputSlots().contains(slot) && !this.getOutputSlots().contains(slot);
	}


	default void onUpgradeInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {}


	default void onUpgradeInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {}


	default ItemStackProperty[] createInputProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[this.getInputSlots().size()];
		for(int i = 0; i < properties.length; i++)
			properties[i] = new ItemStackProperty("InputItem" + i);
		return properties;
	}


	default ItemStackProperty[] createOutputProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[this.getOutputSlots().size()];
		for(int i = 0; i < properties.length; i++)
			properties[i] = new ItemStackProperty("OutputItem" + i);
		return properties;
	}


	default MachineUpgrade[] getAllowedMachineUpgrades() {
		return MachineUpgrade.upgrades("speed", "efficiency", "fortune");
	}


	default boolean supportMachineUpgrade(MachineUpgrade upgrade) {
		return Utilities.contains(this.getAllowedMachineUpgrades(), upgrade);
	}

}
