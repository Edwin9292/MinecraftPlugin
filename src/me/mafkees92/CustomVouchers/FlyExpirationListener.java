package me.mafkees92.CustomVouchers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;

public class FlyExpirationListener {

	Main main;
	LuckPerms api;

	public FlyExpirationListener(Main main, LuckPerms api) {
		this.api = api;
		this.main = main;

		EventBus eventBus = api.getEventBus();
		eventBus.subscribe(NodeRemoveEvent.class, this::onNodeRemoval);

	}

	private void onNodeRemoval(NodeRemoveEvent event) {
		// as we want to access the Bukkit API, we need to use the scheduler to jump
		// back onto the main thread.

		if (event.getTarget() instanceof User) {
			User user = (User) event.getTarget();
			if (event.getNode().getKey().equalsIgnoreCase("essentials.fly")) {

				Bukkit.getScheduler().runTask(main, () -> {
					Player p = Bukkit.getPlayer(user.getUniqueId());
					p.setAllowFlight(false);
				});
			}
		}

	}
}
