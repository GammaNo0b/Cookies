
package me.gamma.cookies.object.block.machine;


import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;



public class ItemAbsorber extends AbstractGuiMachine implements ItemSupplier {

	private static final int[] slots = new int[] { 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34 };
	private static final ItemStackProperty[] itemProperties = ArrayUtils.generate(slots.length, i -> new ItemStackProperty("item" + i), ItemStackProperty[]::new);

	private int frequency;
	private int range;

	public ItemAbsorber() {
		super(null);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		for(int i = 0; i < slots.length; i++)
			inventory.setItem(slots[i], itemProperties[i].fetch(block));
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		for(int i = 0; i < slots.length; i++)
			itemProperties[i].store(block, inventory.getItem(slots[i]));

		block.update();
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.frequency = config.getInt("frequency", 20);
		this.range = config.getInt("range", 0);
	}


	@Override
	public String getTitle() {
		return "§bItem Absorber";
	}


	@Override
	public String getMachineRegistryName() {
		return "item_absorber";
	}


	@Override
	public int getWorkPeriod() {
		return this.frequency;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_ABSORBER;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createItemOutputBlockFaceConfig());
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(9, 11, 18, 20, 27, 29))
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(12, 17, 21, 26, 30, 35))
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		for(int i = 0; i < slots.length; i++)
			if(slots[i] == slot)
				return false;

		return super.onMainInventoryInteract(player, block, gui, event);
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		for(Provider<ItemStack> input : this.getItemOutputs(block))
			drops.add(ItemProvider.getStack(input));

		return drops;
	}


	@Override
	protected boolean run(TileState block) {
		this.tryPushItems(block);
		Location front = block.getLocation().add(0.5D, 0.5D, 0.5D).add(((Rotatable) block.getBlockData()).getRotation().getDirection().multiply(0.25D));
		for(Entity entity : block.getLocation().getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D), this.range, this.range, this.range)) {
			if(entity instanceof Item) {
				Item item = (Item) entity;
				ParticleManager.drawAnimatedLine(item.getLocation(), front, 1, 10, pos -> pos.getWorld().spawnParticle(Particle.DUST, pos, 1, 0.1F, 0.1F, 0.1F, new Particle.DustOptions(Color.WHITE, 1.0F)));
				ItemStack stack = item.getItemStack();
				int amount = stack.getAmount();
				stack = ItemConsumer.addStack(stack, amount, this.getItemOutputs(block));
				if(ItemUtils.isEmpty(stack))
					item.remove();

				block.update();
				if(amount - stack.getAmount() > 0)
					return true;
			}
		}
		return false;
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(TileState block) {
		return ItemProvider.fromInventory(this.getGui(block), slots);
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 28;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 19;
	}


	@Override
	protected int getUpgradeSlot() {
		return 10;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 1;
	}

}
