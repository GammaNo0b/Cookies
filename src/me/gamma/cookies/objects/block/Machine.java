
package me.gamma.cookies.objects.block;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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

import me.gamma.cookies.objects.block.skull.AbstractMachine;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.property.StringProperty;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;
import net.minecraft.server.v1_16_R2.NBTTagList;



public interface Machine extends BlockTicker, GuiProvider, Ownable, Switchable, ItemConsumer, ItemSupplier {

	public static final ByteProperty MACHINE_IDENTIFIER = ByteProperty.create("machine");
	public static final IntegerProperty PROGRESS = IntegerProperty.create("progress");
	public static final StringProperty CURRENT_RECIPE = StringProperty.create("currentrecipe");

	boolean isInstanceOf(PersistentDataHolder holder);

	boolean isInstanceOf(TileState block);

	Map<Location, Inventory> getMachineMap();


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
	Inventory createInventory(TileState block);


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


	default Map<String, MachineRecipe> createRecipeMap() {
		Map<String, MachineRecipe> map = new HashMap<>();
		this.getMachineRecipes().forEach(recipe -> map.put(recipe.getIdentifier(), recipe));
		return map;
	}


	Map<String, MachineRecipe> getRecipeMap();

	int getProgressSlot();

	ItemStack getProgressIcon();

	List<Integer> getInputSlots();

	List<Integer> getOutputSlots();


	@Override
	default List<Supplier<ItemStack>> getInputStackHolders(TileState block) {
		List<Supplier<ItemStack>> suppliers = new ArrayList<>();
		final Inventory inventory = this.getInventory(block);
		for(int slot : this.getInputSlots()) {
			final ItemStack stack = inventory.getItem(slot);
			suppliers.add(() -> stack);
		}
		return suppliers;
	}


	@Override
	default List<Supplier<ItemStack>> getOutputStackHolders(TileState block) {
		List<Supplier<ItemStack>> suppliers = new ArrayList<>();
		final Inventory inventory = this.getInventory(block);
		for(int slot : this.getOutputSlots()) {
			final ItemStack stack = inventory.getItem(slot);
			suppliers.add(() -> stack);
		}
		return suppliers;
	}


	default int getSpeed() {
		return 1;
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
		return AbstractMachine.isMachine(block);
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
				if(progress >= current.getDuration()) {
					List<ItemStack> results = new ArrayList<>();
					results.add(current.getResult());
					ItemStack[] extra = current.getExtraResults();
					for(int i = 0; i < extra.length; i++)
						results.add(extra[i]);
					for(int i = 0; i < results.size(); i++) {
						ItemStack result = results.get(i).clone();
						List<Integer> outputslots = this.getOutputSlots();
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
					inventory.setItem(this.getProgressSlot(), new ItemBuilder(this.getProgressIcon()).setName("§8Empty").build());
					PROGRESS.store(block, -1);
					current = null;
				} else {
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
				List<Integer> inputslots = this.getInputSlots();
				ItemStack[] input = new ItemStack[inputslots.size()];
				for(int i = 0; i < inputslots.size(); i++)
					input[i] = inventory.getItem(inputslots.get(i));
				for(MachineRecipe recipe : this.getMachineRecipes()) {
					if(recipe.matches(input)) {
						List<Integer> outputslots = this.getOutputSlots();
						ItemStack result = recipe.getResult().clone();
						int store = result.getAmount();
						for(int i = 0; i < outputslots.size(); i++) {
							ItemStack stack = inventory.getItem(outputslots.get(i));
							if(stack == null || stack.getType() == Material.AIR || stack.getType() == Material.BARRIER || stack.getAmount() == 0) {
								result = null;
								break;
							} else if(CookieRecipe.sameIngredient(stack, result)) {
								store -= stack.getType().getMaxStackSize() - stack.getAmount();
							}
							if(store <= 0) {
								result = null;
								break;
							}
						}
						if(result == null) {
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
