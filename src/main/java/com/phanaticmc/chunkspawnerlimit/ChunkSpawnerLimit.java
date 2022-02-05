package com.phanaticmc.chunkspawnerlimit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import com.phanaticmc.chunkspawnerlimit.listeners.*;
import com.phanaticmc.chunkspawnerlimit.commands.*;
import org.bukkit.configuration.file.FileConfiguration;

public class ChunkSpawnerLimit extends JavaPlugin {
	public static ChunkSpawnerLimit instance;
	public static int limit, groupChunksRadius;
	public static boolean cleanOnChunkLoad, notifyOnChunkLoad, groupChunks;
	public static Material spawnermat;
	public static String denyMessage;

	@Override
	public void onEnable() {
		instance = this;

		FileConfiguration config = getConfig();
		if (!config.contains("limit")) {
			config.addDefault("limit", 4);
		}
		if (!config.contains("cleanOnChunkLoad")) {
			config.addDefault("cleanOnChunkLoad", false);
		}
		if (!config.contains("notifyOnChunkLoad")) {
			config.addDefault("notifyOnChunkLoad", false);
		}
		if (!config.contains("groupChunks")) {
			config.addDefault("groupChunks", false);
		}
		if (!config.contains("groupChunksRadius")) {
			config.addDefault("groupChunksRadius", 1);
		}
		if (!config.contains("denyMessage")) {
			config.addDefault("denyMessage", "Too many Spawners in this chunk, 4 is the max!");
		}

		config.options().copyDefaults(true);
		saveConfig();

		limit = config.getInt("limit");
		groupChunksRadius = config.getInt("groupChunksRadius");
		groupChunks = config.getBoolean("groupChunks");
		cleanOnChunkLoad = config.getBoolean("cleanOnChunkLoad");
		notifyOnChunkLoad = config.getBoolean("notifyOnChunkLoad");
		denyMessage = ChatColor.translateAlternateColorCodes('&', config.getString("denyMessage"));

		if (cleanOnChunkLoad || notifyOnChunkLoad) {
			Bukkit.getPluginManager().registerEvents(new ChunkLoad(), this);
		}
		Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
		getServer().getPluginCommand("spawnerlist").setExecutor(new ListCommand());
		getServer().getPluginCommand("spawnerdelete").setExecutor(new DeleteCommand());

		setSpawnermat();
	}

	private void setSpawnermat() {
		for (Material mat : Material.values()) {
			if (mat.toString().endsWith("SPAWNER")) {
				spawnermat = mat;
				break;
			}
		}
	}
}
