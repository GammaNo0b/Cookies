
package me.gamma.cookies.util;


import org.bukkit.entity.Player;



public class ExperienceUtils {

	public static final int TOTAL_EXP_LEVEL_15 = getTotalExperience(15);
	public static final int TOTAL_EXP_LEVEL_30 = getTotalExperience(30);

	/**
	 * Returns the amount of experience the player has in their current level. Does not return the total experience of the player.
	 * 
	 * @param player the player
	 * @return the amount of experience of the player of their current level
	 */
	public static int getPlayerLevelExp(Player player) {
		return Math.round(player.getExp() * getExperienceForNextLevel(player.getLevel()));
	}


	/**
	 * Returns the amount of experience required to get from the given level to the enxt one.
	 * 
	 * @param level the current experience level
	 * @return the amount of experience needed for the next level
	 */
	public static int getExperienceForNextLevel(int level) {
		if(level >= 30) {
			return level * 9 - 158;
		} else if(level >= 15) {
			return level * 5 - 38;
		} else {
			return level * 2 + 7;
		}
	}


	/**
	 * Returns the amount of experience required to get from level 0 to the given one.
	 * 
	 * @param level the level
	 * @return the amount of experience worth the given level
	 */
	public static int getTotalExperience(int level) {
		if(level >= 30) {
			return (9 * level * level - 325 * level) / 2 + 2220;
		} else if(level >= 15) {
			return (5 * level * level - 81 * level) / 2 + 360;
		} else {
			return level * level + 6 * level;
		}
	}


	/**
	 * Returns the amount of experience required to get from <code>currentLevel</code> to <code>requiredLevel</code>.
	 * 
	 * @param currentLevel the current level
	 * @param targetLevel  the target level
	 * @return the required experience
	 */
	public static int getExperienceForLevel(int currentLevel, int targetLevel) {
		return getTotalExperience(targetLevel) - getTotalExperience(currentLevel);
	}


	/**
	 * Returns the level the given amount of experience corresponds to.
	 * 
	 * @param experience the experience
	 * @return the corresponding level
	 */
	public static int getLevelFromExperience(int experience) {
		if(experience >= TOTAL_EXP_LEVEL_30) {
			return (int) Math.floor(325.0D / 18.0D + Math.sqrt(2.0D / 9.0D * (experience - 54215.0D / 72.0D)));
		} else if(experience >= TOTAL_EXP_LEVEL_15) {
			return (int) Math.floor(81.0D / 10.0D + Math.sqrt(2.0D / 5.0D * (experience - 7893.0D / 40.0D)));
		} else {
			return (int) Math.floor(Math.sqrt(experience + 9.0D) - 3.0D);
		}
	}

}
