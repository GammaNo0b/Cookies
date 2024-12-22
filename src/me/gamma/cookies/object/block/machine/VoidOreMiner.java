
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;



public class VoidOreMiner extends AbstractItemGenerationMachine {

	private static final ItemStackProperty RESULT = new ItemStackProperty("result");

	private final Random random = new Random();

	private int miningDuration;
	private int mineablesTotalWeight = 0;
	private final HashMap<Material, Integer> mineables = new HashMap<>();

	public VoidOreMiner(MachineTier tier) {
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


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.BEDROCK;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(RESULT);
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34 };
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 37;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 28;
	}


	@Override
	protected int getUpgradeSlot() {
		return 10;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 1;
	}


	@Override
	protected int getProgressSlot() {
		return 19;
	}


	@Override
	public Inventory createGui(TileState block) {
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
	protected int createNextProcess(TileState block) {
		if(!this.checkForVoid(block))
			return 0;

		RESULT.store(block, new ItemStack(this.getRandomWeightedMaterial()));
		return this.miningDuration;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		List<ItemStack> list = new ArrayList<>();
		list.add(RESULT.fetch(block));
		while(!this.storeOutputs(block, list)) {
			if(!this.tryPushItems(block)) {
				RESULT.store(block, list.get(0));
				return false;
			}
		}

		RESULT.storeEmpty(block);
		while(this.tryPushItems(block));

		return true;
	}


	private boolean checkForVoid(TileState block) {
		Block b = block.getBlock();
		while(b.getY() > block.getWorld().getMinHeight()) {
			b = b.getRelative(0, -1, 0);
			if(!b.getType().isAir())
				return b.getType() == Material.BEDROCK;
		}

		return true;
	}


	private Material getRandomWeightedMaterial() {
		int n = this.random.nextInt(this.mineablesTotalWeight);
		int i = 0;
		for(Map.Entry<Material, Integer> entry : this.mineables.entrySet())
			if(n < (i += entry.getValue()))
				return entry.getKey();

		return null;
	}

}
