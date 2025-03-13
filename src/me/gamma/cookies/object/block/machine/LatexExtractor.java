
package me.gamma.cookies.object.block.machine;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class LatexExtractor extends AbstractProcessingMachine implements ItemSupplier {

	public static final IntegerProperty LATEX_TICKS = new IntegerProperty("latexticks");
	public static final ItemStackProperty OUTPUT_1 = new ItemStackProperty("output1");
	public static final ItemStackProperty OUTPUT_2 = new ItemStackProperty("output2");

	private int frequency;
	private int capacity;
	private int extractAmount;
	private int latexRubberCost;
	private int latexRubberDuration;

	private IntegerProperty latex;

	public LatexExtractor() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.frequency = config.getInt("frequency", 20);
		this.capacity = config.getInt("capacity", 1000);
		this.extractAmount = config.getInt("extractAmount", 1);
		this.latexRubberCost = config.getInt("latexRubberCost", 100);
		this.latexRubberDuration = config.getInt("latexRubberDuration", 1200);

		this.latex = new IntegerProperty("latex", 0, this.capacity);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		this.updateLatexLevel(block);

		inventory.setItem(24, OUTPUT_1.fetch(block));
		inventory.setItem(25, OUTPUT_2.fetch(block));

		super.setupInventory(block, inventory);
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		OUTPUT_1.store(block, inventory.getItem(24));
		OUTPUT_2.store(block, inventory.getItem(25));

		block.update();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LATEX_EXTRACTOR;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F).add(LATEX_TICKS).add(this.latex);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(OUTPUT_1).add(OUTPUT_2);
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);
		Inventory gui = this.getGui(block);
		IntStream.of(24, 25).mapToObj(gui::getItem).filter(Predicate.not(ItemUtils::isEmpty)).forEach(drops::add);
		return drops;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		MachineConstants.setupInventory(gui, MachineTier.ADVANCED);
		return gui;
	}


	@Override
	protected boolean run(TileState block) {
		return this.extractLatex(block) | super.run(block);
	}


	@Override
	protected int createNextProcess(TileState block) {
		int latex = this.latex.fetch(block);
		if(latex < this.latexRubberCost)
			return 0;

		this.latex.store(block, latex - this.latexRubberCost);
		block.update();

		return this.latexRubberDuration;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {}


	@Override
	protected boolean finishProcess(TileState block) {
		ItemStack rubber = Items.RUBBER.get();
		Inventory gui = this.getGui(block);
		this.tryPushItems(block);
		return gui.addItem(rubber).isEmpty();
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FLINT;
	}


	@Override
	public String getTitle() {
		return "§fLatex Extractor";
	}


	@Override
	public String getMachineRegistryName() {
		return "latex_extractor";
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(PersistentDataHolder holder) {
		return holder instanceof TileState block ? ItemProvider.fromInventory(this.getGui(block), 24, 25) : List.of();
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		int slot = event.getSlot();
		return slot != 24 && slot != 25;
	}


	/**
	 * Extracts latex from a wood block in front of the machine. Returns true if the extraction was successful.
	 * 
	 * @param block the block
	 * @return if the operation was successful
	 */
	private boolean extractLatex(TileState block) {
		int latexTicks = LATEX_TICKS.fetch(block);
		if(++latexTicks < this.frequency) {
			LATEX_TICKS.store(block, latexTicks);
			block.update();
			return false;
		}
		LATEX_TICKS.store(block, 0);

		BlockFace rotation = this.getFacing(block);
		Block target = block.getBlock().getRelative(rotation);

		if(!hasLatex(target.getType()))
			return false;

		if(this.latex.increase(block, this.extractAmount) > 0)
			return false;

		block.update();
		this.updateLatexLevel(block);
		return true;
	}


	/**
	 * Updates the energy level icon in the inventory of the given block.
	 * 
	 * @param block the block
	 */
	protected void updateLatexLevel(TileState block) {
		final int rows = 3;
		int latex = this.latex.fetch(block);
		int levels = (rows * latex + this.capacity / 2) / this.capacity;
		ItemStack icon = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§8Latex: §7" + latex + "mb").build();
		Inventory gui = this.getGui(block);
		for(int i = 3; i > 3 - levels; i--) {
			gui.setItem(i * 9 + 1, icon);
			gui.setItem(i * 9 + 2, icon);
		}
		icon = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("§8Latex: §7" + latex + "mb").build();
		for(int i = 3 - levels; i >= 1; i--) {
			gui.setItem(i * 9 + 1, icon);
			gui.setItem(i * 9 + 2, icon);
		}
	}


	/**
	 * Checks whether or not the given material can be used to extract latex.
	 * 
	 * @param material the material
	 * @return if it has latex
	 */
	public static boolean hasLatex(Material material) {
		if(!material.isBlock())
			return false;

		return material == Material.JUNGLE_LOG || material == Material.JUNGLE_WOOD;
	}

}
