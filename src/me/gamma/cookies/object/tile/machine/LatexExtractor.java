
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.machine.LatexExtractorBlock;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class LatexExtractor extends AbstractItemGenerationMachine<LatexExtractor, LatexExtractorBlock> implements ItemSupplier {

	private static final String KEY_LATEX = "latex";
	private static final String KEY_LATEX_TICKS = "latexticks";

	private int latex = 0;
	private int latexTicks = 0;

	public LatexExtractor(LatexExtractorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.latex = data.getInteger(KEY_LATEX, 0);
		this.latexTicks = data.getInteger(KEY_LATEX_TICKS, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_LATEX, this.latex);
		data.setInteger(KEY_LATEX_TICKS, this.latexTicks);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		this.updateLatexLevel();
	}


	@Override
	protected boolean run() {
		return this.extractLatex() | super.run();
	}


	@Override
	protected int createNextProcess() {
		final int latexRubberCost = this.customBlock.getLatexRubberCost();

		if(this.latex < latexRubberCost)
			return 0;

		this.latex -= latexRubberCost;

		return this.customBlock.getLatexRubberDuration();
	}


	@Override
	protected boolean finishProcess() {
		this.tryPushItems();
		return ItemUtils.isEmpty(ItemConsumer.addStack(Items.RUBBER.get(), 1, this.getItemOutputs()));
	}


	/**
	 * Extracts latex from a wood block in front of the machine. Returns true if the extraction was successful.
	 * 
	 * @return if the operation was successful
	 */
	private boolean extractLatex() {
		if(++this.latexTicks < this.customBlock.getFrequency())
			return false;

		this.latexTicks = 0;

		BlockFace rotation = this.customBlock.getFacing(this.block);
		Block target = this.block.getRelative(rotation);

		if(!hasLatex(target.getType()))
			return false;

		final int extractAmount = this.customBlock.getExtractAmount();
		if(this.latex + extractAmount > this.customBlock.getCapacity())
			return false;

		this.latex += extractAmount;
		this.updateLatexLevel();

		return true;
	}


	/**
	 * Updates the energy level icon in the inventory.
	 */
	protected void updateLatexLevel() {
		final int capacity = this.customBlock.getCapacity();
		final int rows = 3;
		int levels = (rows * this.latex + capacity / 2) / capacity;
		ItemStack icon = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§8Latex: §7" + this.latex + "mb").build();
		Inventory gui = this.getInventory();
		for(int i = 3; i > 3 - levels; i--) {
			gui.setItem(i * 9 + 1, icon);
			gui.setItem(i * 9 + 2, icon);
		}
		icon = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("§8Latex: §7" + this.latex + "mb").build();
		for(int i = 3 - levels; i >= 1; i--) {
			gui.setItem(i * 9 + 1, icon);
			gui.setItem(i * 9 + 2, icon);
		}
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FLINT;
	}


	@Override
	public LatexExtractor castTileEntity() {
		return this;
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
