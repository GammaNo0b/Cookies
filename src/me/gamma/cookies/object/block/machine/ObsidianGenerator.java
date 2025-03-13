
package me.gamma.cookies.object.block.machine;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.math.MathHelper;



public class ObsidianGenerator extends AbstractProcessingMachine implements ItemSupplier, FluidConsumer {

	private static final int[] output_slots = { 24, 25, 33, 34 };
	private static final int[] output_border_slots = { 14, 15, 16, 17, 23, 26, 32, 35, 41, 42, 43, 44 };

	private static final Material[] progress_materials = { Material.LIGHT_BLUE_STAINED_GLASS, Material.ICE, Material.PACKED_ICE, Material.BLUE_ICE };
	private static final Material[] lava_materials = { Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE };

	private int capacity;
	private int obsidianCoolingTime;

	private IntegerProperty lava;

	public ObsidianGenerator() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("lavaCapacity", 4000);
		this.obsidianCoolingTime = config.getInt("obsidianCoolingTime", 1200);

		this.lava = new IntegerProperty("lava", this.capacity);
	}


	@Override
	public String getMachineRegistryName() {
		return "obsidian_generator";
	}


	@Override
	public String getTitle() {
		return "§eObsidian Generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.OBSIDIAN_GENERATOR;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return progress_materials[MathHelper.clamp(0, 3, (int) (progress * 4))];
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F).add(FLUID_INPUT_ACCESS_FLAGS, (byte) 0x3F).add(this.lava);
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> config) {
		super.listBlockFaceProperties(config);
		config.add(this.createItemOutputBlockFaceConfig());
		config.add(this.createFluidInputBlockFaceConfig());
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		ItemStackProperty[] outputProperties = getItemOutputProperties();
		for(int i = 0; i < output_slots.length; i++)
			inventory.setItem(output_slots[i], outputProperties[i].fetch(block));
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		ItemStackProperty[] outputProperties = getItemOutputProperties();
		for(int i = 0; i < output_slots.length; i++)
			outputProperties[i].store(block, inventory.getItem(output_slots[i]));

		block.update();
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = Bukkit.createInventory(null, 54, this.getTitle());

		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = MachineConstants.INPUT_BORDER_MATERIAL;
		ItemStack filler2 = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 1; i <= 4; i++) {
			int j = 9 * i;
			gui.setItem(j, filler);
			gui.setItem(j + 1, filler2);
			gui.setItem(j + 2, filler2);
			gui.setItem(j + 3, filler);
		}

		filler = MachineConstants.OUTPUT_BORDER_MATERIAL;
		for(int i : output_border_slots)
			gui.setItem(i, filler);

		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		int slot = event.getSlot();
		for(int i : output_slots)
			if(i == slot)
				return false;

		return true;
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(PersistentDataHolder holder) {
		return holder instanceof TileState block ? ItemProvider.fromInventory(this.getGui(block), output_slots) : List.of();
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		for(Provider<ItemStack> provider : this.getItemOutputs(block))
			drops.add(ItemProvider.getStack(provider));

		return drops;
	}


	@Override
	public List<FluidProvider> getFluidInputs(PersistentDataHolder holder) {
		return ArrayUtils.asList(FluidProvider.fromProperty(FluidType.LAVA, this.lava, holder, this.capacity));
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.updateLavaTank(block);
	}


	@Override
	protected int createNextProcess(TileState block) {
		this.tryPullFluid(block);

		int lava = this.lava.fetch(block);
		if(lava < 1000)
			return 0;

		this.lava.store(block, lava - 1000);
		block.update();
		return this.obsidianCoolingTime;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {
		this.tryPullFluid(block);
	}


	@Override
	protected boolean finishProcess(TileState block) {
		while(Consumer.consume(new ItemStack(Material.OBSIDIAN), 1, this.getItemOutputs(block)) > 0)
			if(!this.tryPushItems(block))
				return false;

		this.tryPushItems(block);

		return true;
	}


	/**
	 * Updates the lava level in the inventory of the given block.
	 * 
	 * @param block the block
	 */
	protected void updateLavaTank(TileState block) {
		int lava = this.lava.fetch(block);
		int fill = (int) Math.round(lava * 4.0D / this.capacity);
		Inventory gui = this.getGui(block);
		int i = 0;
		for(; i < fill; i++) {
			ItemStack stack = new ItemBuilder(lava_materials[i]).setName("§6Lava: §e" + lava + " mb").build();
			gui.setItem(37 - i * 9, stack);
			gui.setItem(38 - i * 9, stack);
		}
		for(; i < 4; i++) {
			ItemStack stack = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("§6Lava: §e" + lava + " mb").build();
			gui.setItem(37 - i * 9, stack);
			gui.setItem(38 - i * 9, stack);
		}
	}


	/**
	 * Returns an array of item stack properties to store the outputs of the obsidian generator.
	 * 
	 * @return the array
	 */
	private static ItemStackProperty[] getItemOutputProperties() {
		return Arrays.stream(output_slots).mapToObj(slot -> new ItemStackProperty("out" + slot)).toArray(ItemStackProperty[]::new);
	}

}
