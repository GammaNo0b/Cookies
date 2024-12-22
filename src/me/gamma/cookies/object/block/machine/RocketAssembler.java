
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class RocketAssembler extends AbstractItemProcessingMachine {

	private static final int PAPER_SLOT = 19;
	private static final int GUNPOWDER_SLOT = 20;
	private static final int FIREWORK_STAR_SLOT = 21;

	private static final ItemStack useFireworkStarIcon = new ItemBuilder(Material.FIREWORK_STAR).setName("§aUse Firework Star").setColor(Color.GREEN).setItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP).build();
	private static final ItemStack craftFlightRocketIcon = new ItemBuilder(Material.FIREWORK_STAR).setName("§8Craft Flight Rocket").setColor(Color.GRAY).setItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP).build();

	public static final IntegerProperty FLIGHT_DURATION = new IntegerProperty("flightduration");
	public static final BooleanProperty USE_FIREWORK_STAR = new BooleanProperty("fireworkstar");
	public static final ItemStackProperty PROCESSING = Properties.PROCESSING;

	public RocketAssembler() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§eRocket Assembler";
	}


	@Override
	public String getMachineRegistryName() {
		return "rocket_assembler";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ROCKET_ASSEMBLER;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FIREWORK_ROCKET;
	}


	@Override
	protected ItemBuilder createProgressIcon(double progress) {
		return super.createProgressIcon(progress).setItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(PROCESSING);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(FLIGHT_DURATION).add(USE_FIREWORK_STAR);
	}


	@Override
	protected int getUpgradeSlot() {
		return 14;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 41;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 32;
	}


	@Override
	protected int getProgressSlot() {
		return 23;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 5;
	}


	@Override
	protected int getInputModeSlot() {
		return -1;
	}


	@Override
	protected int getOutputModeSlot() {
		return -1;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		for(int i : ArrayUtils.array(9, 13, 18, 22, 27, 28, 29, 30, 31))
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : ArrayUtils.array(15, 16, 17, 24, 26, 33, 34, 35))
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		inventory.setItem(PAPER_SLOT - 9, new ItemBuilder(Material.PAPER).setName("§fPaper Slot").build());
		this.updateFlightDuration(block);
		inventory.setItem(FIREWORK_STAR_SLOT - 9, USE_FIREWORK_STAR.fetch(block) ? useFireworkStarIcon : craftFlightRocketIcon);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == GUNPOWDER_SLOT - 9) {
			if(event.getClick().isRightClick()) {
				FLIGHT_DURATION.cycle(block, -1, 8);
			} else if(event.getClick().isLeftClick()) {
				FLIGHT_DURATION.cycle(block, 1, 8);
			} else {
				return true;
			}
			block.update();

			this.updateFlightDuration(block);

			return true;
		} else if(slot == FIREWORK_STAR_SLOT - 9) {
			USE_FIREWORK_STAR.toggle(block);
			block.update();

			gui.setItem(FIREWORK_STAR_SLOT - 9, USE_FIREWORK_STAR.fetch(block) ? useFireworkStarIcon : craftFlightRocketIcon);

			return true;
		}

		return super.onMainInventoryInteract(player, block, gui, event);
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { PAPER_SLOT, GUNPOWDER_SLOT, FIREWORK_STAR_SLOT };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 25 };
	}


	private ItemProvider getPaperInput(Inventory gui) {
		return ItemProvider.fromInventory(gui, PAPER_SLOT, Material.PAPER);
	}


	private ItemProvider getGunpowderInput(Inventory gui) {
		return ItemProvider.fromInventory(gui, GUNPOWDER_SLOT, Material.GUNPOWDER);
	}


	private ItemProvider getFireworkStarInput(Inventory gui) {
		return ItemProvider.fromInventory(gui, FIREWORK_STAR_SLOT, Material.FIREWORK_STAR);
	}


	private ItemProvider getFireworkOutput(Inventory gui) {
		return ItemProvider.fromInventory(gui, 25);
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs(TileState block) {
		Inventory gui = this.getGui(block);

		List<Provider<ItemStack>> inputs = new ArrayList<>();

		inputs.add(this.getPaperInput(gui));
		inputs.add(this.getGunpowderInput(gui));
		inputs.add(this.getFireworkStarInput(gui));

		return inputs;
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(TileState block) {
		return List.of(this.getFireworkOutput(this.getGui(block)));
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);

		Inventory gui = this.getGui(block);

		boolean useFireworkStar = USE_FIREWORK_STAR.fetch(block);
		ItemProvider fireworkStarInput = this.getFireworkStarInput(gui);

		ItemProvider fireworkOutput = this.getFireworkOutput(gui);
		ItemStack firework = ItemProvider.getStack(fireworkOutput);
		if(ItemUtils.isEmpty(firework)) {
			// assemble new rocket
			ItemProvider paperInput = this.getPaperInput(gui);

			if(!paperInput.check(1))
				return 0;

			ItemProvider gunpowderInput = this.getGunpowderInput(gui);
			int gunpowderNeeded = FLIGHT_DURATION.fetch(block) + 1;

			if(!gunpowderInput.check(gunpowderNeeded))
				return 0;

			firework = new ItemStack(Material.FIREWORK_ROCKET, 4);
			FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
			meta.setPower(gunpowderNeeded);

			if(useFireworkStar) {
				ItemStack fireworkStar = ItemProvider.get(fireworkStarInput, 1);
				if(!ItemUtils.isType(fireworkStar, Material.FIREWORK_STAR))
					return 0;

				FireworkEffectMeta emeta = (FireworkEffectMeta) fireworkStar.getItemMeta();
				meta.addEffect(emeta.getEffect());
			}

			firework.setItemMeta(meta);

			paperInput.get(1);
			gunpowderInput.get(gunpowderNeeded);
		} else if(useFireworkStar) {
			// upgrade rocket
			if(!ItemUtils.isType(firework, Material.FIREWORK_ROCKET))
				return 0;

			ItemStack fireworkStar = ItemProvider.get(fireworkStarInput, 1);
			if(ItemUtils.isEmpty(fireworkStar))
				return 0;

			if(!ItemUtils.isType(fireworkStar, Material.FIREWORK_STAR)) {
				fireworkStarInput.set(1);
				return 0;
			}

			firework.setAmount(fireworkOutput.get(4));

			FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
			FireworkEffectMeta emeta = (FireworkEffectMeta) fireworkStar.getItemMeta();
			if(emeta.getEffect() != null)
				meta.addEffect(emeta.getEffect());
			firework.setItemMeta(meta);
		} else {
			// assemble new flying rocket
			ItemProvider paperInput = this.getPaperInput(gui);

			if(!paperInput.check(1))
				return 0;

			ItemProvider gunpowderInput = this.getGunpowderInput(gui);
			int gunpowderNeeded = FLIGHT_DURATION.fetch(block) + 1;

			if(!gunpowderInput.check(gunpowderNeeded))
				return 0;

			firework = new ItemStack(Material.FIREWORK_ROCKET, 4);
			FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
			meta.setPower(gunpowderNeeded);
			firework.setItemMeta(meta);

			paperInput.get(1);
			gunpowderInput.get(gunpowderNeeded);
		}

		PROCESSING.store(block, firework);
		block.update();
		block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.0F);

		return 200;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		List<ItemStack> results = new ArrayList<>();
		results.add(PROCESSING.fetch(block));
		while(!this.storeOutputs(block, results)) {
			if(!this.tryPushItems(block)) {
				PROCESSING.store(block, results.get(0));
				return false;
			}
		}

		block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.BLOCKS, 1.0F, 1.0F);
		PROCESSING.storeEmpty(block);
		while(this.tryPushItems(block));

		return true;
	}


	private void updateFlightDuration(TileState block) {
		int flightDuration = FLIGHT_DURATION.fetch(block);
		this.getGui(block).setItem(GUNPOWDER_SLOT - 9, new ItemBuilder(Material.GUNPOWDER).setName("§fGunpowder Slot").setAmount(flightDuration + 1).addLore("  §8Flight Duration: §7" + (flightDuration + 1) * 0.5D + "s").build());
	}

}
