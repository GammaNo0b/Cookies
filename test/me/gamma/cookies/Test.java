
package me.gamma.cookies;


import org.bukkit.block.BlockFace;

import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ExperienceUtils;



public class Test {

	public static void main(String[] args) {}


	public static void testBlockFaceRot() {
		for(BlockFace face : BlockFace.values()) {
			System.out.println(face + " | " + BlockUtils.rotateYClockwise(face) + " | " + BlockUtils.rotateYCounterClockwise(face) + " | " + BlockUtils.getClosestCartesianFacing(face));
		}
	}


	public static void testExp() {
		int totalExp = 0;
		for(int level = 0; level < 100; level++) {
			int diff = ExperienceUtils.getExperienceForNextLevel(level);
			int total = ExperienceUtils.getTotalExperience(level);
			int lvlBack = ExperienceUtils.getLevelFromExperience(total);
			System.out.printf("% 3d | % 6d | % 6d | % 6d | % 3d", level, diff, total, totalExp, lvlBack);
			if(total != totalExp || level != lvlBack)
				System.out.print(" - ! -");
			System.out.println();
			totalExp += diff;
		}
	}

}
