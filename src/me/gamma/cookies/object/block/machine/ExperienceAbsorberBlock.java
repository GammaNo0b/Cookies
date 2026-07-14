
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.ExperienceAbsorber;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class ExperienceAbsorberBlock extends AbstractGuiMachineBlock<ExperienceAbsorberBlock, ExperienceAbsorber> {

	private int range;

	public ExperienceAbsorberBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.range = config.getInt("range", 4);
	}


	public int getRange() {
		return this.range;
	}


	@Override
	public String getTitle() {
		return "§bExperience Absorber";
	}


	@Override
	public String getMachineRegistryName() {
		return "experience_absorber";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.EXPERIENCE_ABSORBER;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 28;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 19;
	}


	@Override
	public int getUpgradeSlot() {
		return 10;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 1;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(9, 11, 18, 20, 27, 29))
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(12, 17, 21, 26, 30, 35))
			gui.setItem(i, filler);

		gui.setItem(13, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 1 Level").build());
		gui.setItem(14, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 10 Levels").build());
		gui.setItem(15, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 100 Levels").build());
		gui.setItem(16, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore all Levels").build());
		gui.setItem(22, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 1 Level").build());
		gui.setItem(23, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 10 Levels").build());
		gui.setItem(24, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 100 Levels").build());
		gui.setItem(25, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop all Levels").build());
		gui.setItem(31, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 1 Level").build());
		gui.setItem(32, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 10 Levels").build());
		gui.setItem(33, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 100 Levels").build());
		gui.setItem(34, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve all Levels").build());

		return gui;
	}


	@Override
	public ExperienceAbsorberBlock castCustomBlock() {
		return this;
	}


	@Override
	public ExperienceAbsorber createNewTileEntity(Block block) {
		return new ExperienceAbsorber(this, block);
	}

}
