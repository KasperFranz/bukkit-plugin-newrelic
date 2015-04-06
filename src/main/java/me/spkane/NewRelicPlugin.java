package me.spkane;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NewRelicPlugin extends JavaPlugin {

	public Permission nrPermission = new Permission("newrelic.admin");

	@Override
	public void onEnable() {
		new NewRelicListener(this);
		this.getConfig().addDefault("enabled", true);
		this.getConfig().addDefault("updates.apply", true);
		this.getConfig().addDefault("updates.progress", true);
		this.getConfig().addDefault("track.entity.death", true);
		this.getConfig().addDefault("track.creature.spawn", true);
		this.getConfig().addDefault("track.player.death", true);
		this.getConfig().addDefault("track.player.join", true);
		this.getConfig().addDefault("track.player.kick", true);
		this.getConfig().addDefault("track.player.quit", true);
		this.getConfig().addDefault("track.player.respawn", true);
		this.getConfig().addDefault("track.player.teleport", true);
		this.getConfig().addDefault("track.block.place", true);
		this.getConfig().addDefault("track.block.break", true);
		this.getConfig().addDefault("track.server.command", true);
		this.getConfig().addDefault("track.server.remotecommand", true);
                this.getConfig().addDefault("track.server.chat", true);
		this.getConfig().addDefault("track.chunk.load", true);
		this.getConfig().addDefault("track.chunk.unload", true);
		this.getConfig().options().copyDefaults(true);
		saveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(nrPermission);
	}

	@Override
	public void onDisable() {
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("nrdisable") && sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("newrelic.admin")) {
				this.getConfig().set("enabled", false);
				saveConfig();
				player.sendMessage( ChatColor.GREEN + "New Relic plugin reporting has been disabled.");
			}else{
				player.sendMessage( ChatColor.RED + "Sorry, but you don't have permissions for this!");
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("nrenable") && sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("newrelic.admin")) {
				this.getConfig().set("enabled", true);
				saveConfig();
				player.sendMessage( ChatColor.GREEN + "New Relic plugin reporting has been enabled.");
			}else{
				player.sendMessage( ChatColor.RED + "Sorry, but you don't have permissions for this!");
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("nrcheck") && sender instanceof Player) {
			Player player = (Player) sender;
			return true;
		} else if (cmd.getName().equalsIgnoreCase("nrupdate") && sender instanceof Player) {
			Player player = (Player) sender;
			return true;
		}
		return false;
	}
	
}
