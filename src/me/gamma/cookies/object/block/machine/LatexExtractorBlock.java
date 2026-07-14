
package me.gamma.cookies.object.block.machine;


import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.LatexExtractor;
import me.gamma.cookies.util.InventoryUtils;



public class LatexExtractorBlock extends AbstractItemGenerationMachineBlock<LatexExtractorBlock, LatexExtractor> {

	private int frequency;
	private int capacity;
	private int extractAmount;
	private int latexRubberCost;
	private int latexRubberDuration;

	public LatexExtractorBlock() {
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
	}


	public int getFrequency() {
		return this.frequency;
	}


	public int getCapacity() {
		return this.capacity;
	}


	public int getExtractAmount() {
		return this.extractAmount;
	}


	public int getLatexRubberCost() {
		return this.latexRubberCost;
	}


	public int getLatexRubberDuration() {
		return this.latexRubberDuration;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LATEX_EXTRACTOR;
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
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		MachineConstants.setupInventory(gui, MachineTier.ADVANCED);
		return gui;
	}


	@Override
	public LatexExtractorBlock castCustomBlock() {
		return this;
	}


	@Override
	public LatexExtractor createNewTileEntity(Block block) {
		return new LatexExtractor(this, block);
	}

}
