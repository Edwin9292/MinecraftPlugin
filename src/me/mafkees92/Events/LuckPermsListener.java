package me.mafkees92.Events;

import org.bukkit.Bukkit;

import me.mafkees92.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;

public class LuckPermsListener {

	Main main;
	LuckPerms api;

	public LuckPermsListener(Main main, LuckPerms api) {
		this.api = api;
		this.main = main;
		
		EventBus eventBus = api.getEventBus();
		
		eventBus.subscribe(NodeRemoveEvent.class, this::onNodeRemoval);
		
	}

	private void onNodeRemoval(NodeRemoveEvent event) {
		// as we want to access the Bukkit API, we need to use the scheduler to jump
		// back onto the main thread.
		if(event.getTarget() instanceof User) {
			User user = (User)event.getTarget();

			Bukkit.getScheduler().runTask(main, () -> {
				Bukkit.broadcastMessage(
						user.getUsername() + " got his permission removed " + event.getNode().getKey() + "!");
				
				if(event.getNode().getKey().equalsIgnoreCase("essentials.fly")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fly " + user.getUsername());
				}
//
//				Player player = Bukkit.getPlayer(user.getUniqueId());
//				if (player != null) {
//					player.sendMessage("Congrats!");
//				}
			});
			
			
		}
	}

}
