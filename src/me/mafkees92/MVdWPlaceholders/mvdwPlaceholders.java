package me.mafkees92.MVdWPlaceholders;

import me.mafkees92.Main;

public class mvdwPlaceholders {

	public mvdwPlaceholders(Main plugin){

		//register all custom placeholders here
		new mafkees_island_size(plugin);
		new mafkees_island_chest_size(plugin);
		new mafkees_player_health(plugin);
		new mafkees_remaining_fly_time(plugin);
		new mafkees_average_island_rate(plugin);
		new mafkees_max_team_members(plugin);
		new mafkees_island_locked(plugin);
		new mafkees_has_island(plugin);
		new mafkees_is_combattagged(plugin);
		new mafkees_combattag_time(plugin);
	}
}
