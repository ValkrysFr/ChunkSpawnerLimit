package com.phanaticmc.chunkspawnerlimit.listeners;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.phanaticmc.chunkspawnerlimit.ChunkSpawnerLimit.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlockPlace implements Listener {
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlockPlaced().getType() != spawnermat) {
			return;
		}
		Collection<Chunk> chunks = around(event.getBlock().getLocation().getChunk(), groupChunks ? groupChunksRadius : 0);
		int spawnercount = 1;
		for (Chunk chunk : chunks)
			for (BlockState block : chunk.getTileEntities()) {
				if (block.getType() == spawnermat) {
					spawnercount++;
					if (spawnercount > limit) {
						event.getPlayer().sendMessage(denyMessage);
						event.setCancelled(true);
						return;
					}
				}
			}
	}

	public static Collection<Chunk> around(Chunk origin, int radius) {
		World world = origin.getWorld();
	
		int length = (radius * 2) + 1;
		Set<Chunk> chunks = new HashSet<>(length * length);
	
		chunks.add(origin);

		int cX = origin.getX();
		int cZ = origin.getZ();
	
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				chunks.add(world.getChunkAt(cX + x, cZ + z));
			}
		}

		return chunks;
	}
}
