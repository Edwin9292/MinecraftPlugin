package me.mafkees92.SeasonHologram;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import me.mafkees92.Holograms;
import me.mafkees92.Main;
import me.mafkees92.Files.Messages;

public class SeasonHologram {
	
	Hologram hologram = null;
	int timer = 0; 
	
	public SeasonHologram(Main plugin) {
		this.hologram = Holograms.AddHologram(Messages.seasonHologramLocation, getHologramText());
		
		Bukkit.getScheduler().runTaskTimer(plugin, () ->{
			if(timer == 0) {
				if(hologram != null) {
					updateHologram(hologram);
				}
				
				//if the there are more than 24 hours remaining, update the timer
				//else we don't have to reset the timer, because it will keep running every second.
				long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), Messages.seasonEnding);
				if(hours > 24) {
					timer = 60;
				}
			}
			else {
				timer--;
			}
		}, 0, 20);
	}
	
	private void updateHologram(Hologram hologram) {
		Holograms.RemoveHologram(hologram.getLocation());
		hologram = Holograms.AddHologram(Messages.seasonHologramLocation, getHologramText());
	}
	
	private List<String> getHologramText() {
		List<String> textLines = Messages.seasonHologramText;
		Iterator<String> it = textLines.iterator();
		List<String> updatedText = new ArrayList<String>();
		while(it.hasNext()) {
			String line = it.next();
			line = line.replace("%time_till_end%", getTimeTillEnd().trim());
			updatedText.add(line);
		}			
		return updatedText;
	}
	//&cEnding in: &e&l 90 &eDays, &e&l10 &eHours & &e&l50 &eMinutes
	private String getTimeTillEnd() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime end = Messages.seasonEnding;
		long totalSeconds = ChronoUnit.SECONDS.between(now, end);
		int days = (int)(totalSeconds/86400);
		int hours = (int)((totalSeconds % 86400) / 3600);
		int minutes = (int)((totalSeconds % 3600) / 60);
		int seconds = (int)(totalSeconds % 60);
		
		String timeString = "";
		if(days > 0) {
			timeString += "&e&l" + days + " &eDays ";
			timeString += "&e&l" + hours + " &eHours ";
			timeString += "&e&l" + minutes + " &eMinutes";
		}
		else if (hours > 0) {
			timeString += "&e&l" + hours + " &eHours ";
			timeString += "&e&l" + minutes + " &eMinutes ";
			timeString += "&e&l" + seconds + " &eSeconds";
		}
		else if (minutes > 0) {
			timeString += "&e&l" + minutes + " &eMinutes ";
			timeString += "&e&l" + seconds + " &eSeconds";
		}
		else{
			timeString += "&e&l" + seconds + " &eSeconds";
		}
		
		return timeString;
//		return String.format("%02d:%02d:%02d:%02d", 
//				);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
