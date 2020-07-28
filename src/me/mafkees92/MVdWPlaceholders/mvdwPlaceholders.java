package me.mafkees92.MVdWPlaceholders;

import me.mafkees92.Main;

public class mvdwPlaceholders {

	public mvdwPlaceholders(Main plugin){

		//register all custom placeholders here
		new mafkees_island_size(plugin);
		new mafkees_average_island_rate(plugin);
		new mafkees_player_health(plugin);
		new mafkees_remaining_fly_time(plugin);
		
	}
	
	
	
	
}
