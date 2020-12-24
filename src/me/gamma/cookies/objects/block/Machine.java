
package me.gamma.cookies.objects.block;


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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.objects.IItemSupplier;
import me.gamma.cookies.objects.block.skull.machine.AbstractSkullMachine;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.property.StringProperty;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;
import net.minecraft.server.v1_16_R3.NBTTagList;



public interface Machine extends BlockTicker, GuiProvider, Ownable, Switchable, ItemConsumer, ItemSupplier {

	public static final ByteProperty MACHINE_IDENTIFIER = ByteProperty.create("machine");
	public static final IntegerProperty PROGRESS = IntegerProperty.create("progress");
	public static final StringProperty CURRENT_RECIPE = StringProperty.create("currentrecipe");


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
		for(Location location : this.getLocations())
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


	@Override
	default void load(NBTTagList list) {
		BlockTicker.super.load(list);
		this.fillMachineMap();
	}


	/* Creates the basic Inventory. */
	default Inventory createInventory(TileState block) {
		return this.getTier().createInventory(block.getLocation(), this);
	}


	default Inventory getInventory(TileState block) {
		return this.getMachineMap().get(block.getLocation());
	}


	default void onBlockPlace(TileState block) {
		MACHINE_IDENTIFIER.storeEmpty(block);
		PROGRESS.store(block, -1);
		CURRENT_RECIPE.storeEmpty(block);
		for(ItemStackProperty property : this.createInputProperties())
			property.storeEmpty(block);
		for(ItemStackProperty property : this.createOutputProperties())
			property.storeEmpty(block);
		this.getLocations().add(block.getLocation());
		this.getMachineMap().put(block.getLocation(), this.createInventory(block));
	}


	default void onBlockBreak(TileState block) {
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
			if(stack != null && stack.getType() != Material.BARRIER)
				drops.add(stack);
		}
		String identifier = CURRENT_RECIPE.fetch(block);
		MachineRecipe recipe;
		if(!identifier.isEmpty() && (recipe = this.getRecipeMap().get(identifier)) != null)
			for(ItemStack stack : recipe.getIngredients())
				drops.add(stack);
		drops.forEach(drop -> location.getWorld().dropItem(location, drop));
	}


	List<MachineRecipe> getMachineRecipes();


	default void createRecipeMap() {
		this.getMachineRecipes().forEach(recipe -> getRecipeMap().put(recipe.getIdentifier(), recipe));
	}


	Map<String, MachineRecipe> getRecipeMap();


	int getProgressSlot();


	ItemStack getProgressIcon();


	List<Integer> getInputSlots();


	List<Integer> getOutputSlots();


	@Override
	default List<IItemSupplier> getInputStackHolders(TileState block) {
		return this.getInputSlots().stream().map(slot -> IItemSupplier.of(getInventory(block).getItem(slot))).collect(Collectors.toList());
	}


	@Override
	default List<IItemSupplier> getOutputStackHolders(TileState block) {
		return this.getOutputSlots().stream().map(slot -> IItemSupplier.of(getInventory(block).getItem(slot))).collect(Collectors.toList());
	}


	default int getSpeed() {
		return 1;
	}


	default double getOutputMultiplicator() {
		return 0.0D;
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
		return AbstractSkullMachine.isMachine(block);
	}


	@Override
	default void tick(TileState block) {
		final Inventory inventory = this.getInventory(block);
		if(inventory == null)
			return;
		if(this.isBlockPowered(block)) {
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
							if(stack == null || stack.getType() == Material.AIR || stack.getType() == Material.BARRIER) {
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
						if(result.getAmount() > 0)
							storeable = false;
						if(!storeable)
							break;
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
							if(stack == null || stack.getType() == Material.AIR || stack.getType() == Material.BARRIER) {
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
						/*
						 * // check if output can be stored; ItemStack result = recipe.getResult().clone(); List<Integer> outputslots = this.getOutputSlots(); int store =
						 * result.getAmount(); for(int i = 0; i < outputslots.size(); i++) { ItemStack stack = inventory.getItem(outputslots.get(i)); if(stack == null ||
						 * stack.getType() == Material.AIR || stack.getType() == Material.BARRIER || stack.getAmount() == 0) { result = null; break; } else
						 * if(CookieRecipe.sameIngredient(stack, result)) { store -= stack.getType().getMaxStackSize() - stack.getAmount(); } if(store <= 0) { result = null;
						 * break; } } if(result == null) { // start recipe }
						 */

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
			if(stack != null && stack.getType() != Material.AIR && stack.getType() != Material.BARRIER) {
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
		if(this.getInputSlots().contains(slot)) {
			return false;
		} else if(this.getOutputSlots().contains(slot)) {
			ItemStack stack = event.getInventory().getItem(slot);
			if(stack != null && stack.getType() != Material.AIR && stack.getType() != Material.BARRIER) {
				event.getInventory().setItem(event.getSlot(), new ItemBuilder(Material.BARRIER).setName(" ").build());
				Utilities.giveItemToPlayer(player, stack);
			}
			return true;
		}
		return true;
	}


	default ItemStackProperty[] createInputProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[this.getInputSlots().size()];
		for(int i = 0; i < properties.length; i++)
			properties[i] = ItemStackProperty.create("InputItem" + i);
		return properties;
	}


	default ItemStackProperty[] createOutputProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[this.getOutputSlots().size()];
		for(int i = 0; i < properties.length; i++)
			properties[i] = ItemStackProperty.create("OutputItem" + i);
		return properties;
	}

}
