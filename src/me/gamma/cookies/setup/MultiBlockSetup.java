package me.gamma.cookies.setup;

import java.util.ArrayList;
import java.util.List;

import me.gamma.cookies.objects.block.multi.CustomCraftingTable;
import me.gamma.cookies.objects.block.multi.EngineeringStation;
import me.gamma.cookies.objects.block.multi.Kitchen;
import me.gamma.cookies.objects.block.multi.MultiBlock;

public class MultiBlockSetup {
	
	public static final List<MultiBlock> multiBlocks = new ArrayList<>();
	
	public static final CustomCraftingTable CUSTOM_CRAFTING_TABLE = registerMultiBlock(new CustomCraftingTable());
	public static final EngineeringStation ENGINEERING_STATION = registerMultiBlock(new EngineeringStation());
	public static final Kitchen KITCHEN = registerMultiBlock(new Kitchen());
	
	public static <S extends MultiBlock> S registerMultiBlock(S multiBlock) {
		multiBlocks.add(multiBlock);
		return multiBlock;
	}

}
