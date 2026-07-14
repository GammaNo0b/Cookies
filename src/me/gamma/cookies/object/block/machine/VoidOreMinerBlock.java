
package me.gamma.cookies.object.block.machine;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.VoidOreMiner;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;



public class VoidOreMinerBlock extends AbstractItemGenerationMachineBlock<VoidOreMinerBlock, VoidOreMiner> {

	private final Random random = new Random();

	private int miningDuration;
	private int mineablesTotalWeight = 0;
	private final HashMap<Material, Integer> mineables = new HashMap<>();

	public VoidOreMinerBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.miningDuration = config.getInt("miningDuration", 1200);

		ConfigurationSection mineables = config.getParent().getConfigurationSection("mineables");
		Map<String, Object> values = mineables.getValues(false);
		for(Map.Entry<String, Object> entry : values.entrySet()) {
			Material material = Material.matchMaterial(entry.getKey());
			if(material == null)
				continue;

			Object value = entry.getValue();
			if(!(value instanceof Number n))
				continue;

			int i = n.intValue();
			this.mineablesTotalWeight += i;
			this.mineables.put(material, i);
		}
	}


	public int getMiningDuration() {
		return this.miningDuration;
	}


	@Override
	public String getTitle() {
		return "§dVoid Ore Miner";
	}


	@Override
	public String getMachineRegistryName() {
		return "void_ore_miner";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.VOID_ORE_MINER;
	}


	public Material getRandomWeightedMaterial() {
		int n = this.random.nextInt(this.mineablesTotalWeight);
		int i = 0;
		for(Map.Entry<Material, Integer> entry : this.mineables.entrySet())
			if(n < (i += entry.getValue()))
				return entry.getKey();

		return null;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 37;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 28;
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
	public int getProgressSlot() {
		return 19;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(9, 11, 18, 20, 27, 29))
			gui.setItem(i, filler);
		for(int i : ArrayUtils.array(12, 17, 21, 26, 30, 35))
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public VoidOreMinerBlock castCustomBlock() {
		return this;
	}


	@Override
	public VoidOreMiner createNewTileEntity(Block block) {
		return new VoidOreMiner(this, block);
	}

}
