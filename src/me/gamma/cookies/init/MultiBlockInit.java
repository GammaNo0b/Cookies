
package me.gamma.cookies.init;


import me.gamma.cookies.object.multiblock.CustomCraftingTable;
import me.gamma.cookies.object.multiblock.EngineeringStation;
import me.gamma.cookies.object.multiblock.Kitchen;
import me.gamma.cookies.object.multiblock.MagicAltar;
import me.gamma.cookies.object.multiblock.MultiBlock;



public class MultiBlockInit {

	public static final Registry<MultiBlock> MULTIBLOCKS = new Registry<>();

	public static CustomCraftingTable CUSTOM_CRAFTING_TABLE;
	public static EngineeringStation ENGINEERING_STATION;
	public static MagicAltar MAGIC_ALTAR;
	public static Kitchen KITCHEN;

	public static void init() {
		CUSTOM_CRAFTING_TABLE = MULTIBLOCKS.register(new CustomCraftingTable());
		ENGINEERING_STATION = MULTIBLOCKS.register(new EngineeringStation());
		MAGIC_ALTAR = MULTIBLOCKS.register(new MagicAltar());
		KITCHEN = MULTIBLOCKS.register(new Kitchen());
	}


	public static MultiBlock getMultiBlockByName(String name) {
		return MULTIBLOCKS.filterFirst(block -> block.getName().equals(name));
	}

}
