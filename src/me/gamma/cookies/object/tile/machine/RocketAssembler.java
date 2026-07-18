
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.RocketAssemblerBlock;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class RocketAssembler extends AbstractItemProcessingMachine<RocketAssembler, RocketAssemblerBlock> {

	private static final String KEY_FLIGHT_DURATION = "flightduration";
	private static final String KEY_USE_FIREWORK_STAR = "usefireworkstar";
	private static final String KEY_PROCESSING = "processing";

	private static final int PAPER_SLOT = 19;
	private static final int GUNPOWDER_SLOT = 20;
	private static final int FIREWORK_STAR_SLOT = 21;

	private static final ItemStack useFireworkStarIcon = new ItemBuilder(Material.FIREWORK_STAR).setName("§aUse Firework Star").setColor(Color.GREEN).setItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP).build();
	private static final ItemStack craftFlightRocketIcon = new ItemBuilder(Material.FIREWORK_STAR).setName("§8Craft Flight Rocket").setColor(Color.GRAY).setItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP).build();

	private int flightDuration = 1;
	private boolean useFireworkStar = false;
	private ItemStack processing = null;

	public RocketAssembler(RocketAssemblerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.flightDuration = data.getInteger(KEY_FLIGHT_DURATION, 1);
		this.useFireworkStar = data.getBoolean(KEY_USE_FIREWORK_STAR, false);
		this.processing = PersistentDataUtils.getItemStack(data, KEY_PROCESSING);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_FLIGHT_DURATION, this.flightDuration);
		data.setBoolean(KEY_USE_FIREWORK_STAR, this.useFireworkStar);
		PersistentDataUtils.setItemStack(data, KEY_PROCESSING, this.processing);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		inventory.setItem(PAPER_SLOT - 9, new ItemBuilder(Material.PAPER).setName("§fPaper Slot").build());
		this.updateFlightDuration();
		this.updateUseFireworkStar();
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
	public int[] getInputSlots() {
		return new int[] { PAPER_SLOT, GUNPOWDER_SLOT, FIREWORK_STAR_SLOT };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 25 };
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == GUNPOWDER_SLOT - 9) {
			if(event.getClick().isRightClick()) {
				if(--this.flightDuration < 1)
					this.flightDuration = 8;
			} else if(event.getClick().isLeftClick()) {
				if(++this.flightDuration > 8)
					this.flightDuration = 1;
			} else {
				return true;
			}

			this.updateFlightDuration();

			return true;
		} else if(slot == FIREWORK_STAR_SLOT - 9) {
			this.useFireworkStar = !this.useFireworkStar;
			this.updateUseFireworkStar();

			return true;
		}

		return super.onMainInventoryInteract(player, gui, event);

	}


	private void updateUseFireworkStar() {
		this.getInventory().setItem(FIREWORK_STAR_SLOT - 9, this.useFireworkStar ? useFireworkStarIcon : craftFlightRocketIcon);

	}


	private void updateFlightDuration() {
		this.getInventory().setItem(GUNPOWDER_SLOT - 9, new ItemBuilder(Material.GUNPOWDER).setName("§fGunpowder Slot").setAmount(this.flightDuration).addLore("  §8Flight Duration: §7" + this.flightDuration * 0.5D + "s").build());
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
	public List<Provider<ItemStack>> getItemInputs() {
		Inventory gui = this.getInventory();

		List<Provider<ItemStack>> inputs = new ArrayList<>();

		inputs.add(this.getPaperInput(gui));
		inputs.add(this.getGunpowderInput(gui));
		inputs.add(this.getFireworkStarInput(gui));

		return inputs;
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs() {
		return List.of(this.getFireworkOutput(this.getInventory()));
	}


	@Override
	protected int createNextProcess() {
		super.createNextProcess();

		Inventory gui = this.getInventory();

		ItemProvider fireworkStarInput = this.getFireworkStarInput(gui);

		ItemProvider fireworkOutput = this.getFireworkOutput(gui);
		ItemStack firework = ItemProvider.getStack(fireworkOutput);
		if(ItemUtils.isEmpty(firework)) {
			// assemble new rocket
			ItemProvider paperInput = this.getPaperInput(gui);

			if(!paperInput.check(1))
				return 0;

			ItemProvider gunpowderInput = this.getGunpowderInput(gui);
			if(!gunpowderInput.check(this.flightDuration))
				return 0;

			firework = new ItemStack(Material.FIREWORK_ROCKET, 4);
			FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
			meta.setPower(this.flightDuration);

			if(this.useFireworkStar) {
				ItemStack fireworkStar = ItemProvider.get(fireworkStarInput, 1);
				if(!ItemUtils.isType(fireworkStar, Material.FIREWORK_STAR))
					return 0;

				FireworkEffectMeta emeta = (FireworkEffectMeta) fireworkStar.getItemMeta();
				meta.addEffect(emeta.getEffect());
			}

			firework.setItemMeta(meta);

			paperInput.get(1);
			gunpowderInput.get(this.flightDuration);
		} else if(this.useFireworkStar) {
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
			if(!gunpowderInput.check(this.flightDuration))
				return 0;

			firework = new ItemStack(Material.FIREWORK_ROCKET, 4);
			FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
			meta.setPower(this.flightDuration);
			firework.setItemMeta(meta);

			paperInput.get(1);
			gunpowderInput.get(this.flightDuration);
		}

		this.processing = firework;

		this.block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.0F);

		return 200;
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		this.tryPushItems();
		this.processing = this.storeOutput(this.processing);
		if(ItemUtils.isEmpty(this.processing)) {
			this.block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return true;
		} else {
			return false;
		}
	}


	@Override
	public RocketAssembler castTileEntity() {
		return this;
	}

}
