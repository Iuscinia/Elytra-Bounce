package com.luscinia.elytraBounce;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
public List<Player> dead = new ArrayList<>();
public List<Player> sneaking = new ArrayList<>();
public List<Player> toggled = new ArrayList<>();
	
	@Override 
	public void onEnable() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (cmd.getName().equals("bounce")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 0) {
						if (player.hasPermission("elytrabounce.bounce")) {
							if (toggled.contains(player) == false) {
								toggled.add(player);
								player.sendMessage("Elytra bouncing has been enabled!");
							} else if (toggled.contains(player) == true) {
								toggled.remove(player);
								player.sendMessage("Elytra bouncing has been disabled!");
							}
						} else {
							player.sendMessage("You do not have permission to use this command!");
						}
					} else {
						if (player.hasPermission("elytrabounce.bounceothers")) {
							Player target = Bukkit.getPlayerExact(args[0]);
							if (target instanceof Player) {
								if (toggled.contains(target) == false) {
									toggled.add(target);
									player.sendMessage("Elytra bouncing has been enabled for " + target.getDisplayName() + "!");
									target.sendMessage("Your elytra bouncing has been manually enabled!");
								} else if (toggled.contains(target) == true) {
									toggled.remove(target);
									player.sendMessage("Elytra bouncing has been enabled for " + target.getDisplayName() + "!");
									target.sendMessage("Your elytra bouncing has been manually disabled!");
								}
							} else {
								player.sendMessage("That player does not exist or is not currently online.");
							}
						} else {
							player.sendMessage("You do not have permission to use this command!");
						}
					}
				}
			}
		return false;
	}
	
	@EventHandler
	public void onGlide(EntityToggleGlideEvent e) {
		if (e.isGliding() == false && e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (toggled.contains(player)) { 
				player.setGliding(true);
				e.setCancelled(true);
			} 
			if (toggled.contains(player) == false || player.getInventory().getChestplate() == null || sneaking.contains(player) || dead.contains(player)) {
				e.setCancelled(false);
				sneaking.remove(player);
				dead.remove(player);
			}
		}
	}
	
	@EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(player.isSneaking() == true) {
        	sneaking.add(player); 
        }
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			player.setGliding(false);
			dead.add(player);
		}
	}

}
/* TODO
 * Make players become arguments for the command
 * @a, @e, @p, @r
 * Make SHIFT apply locally
 */