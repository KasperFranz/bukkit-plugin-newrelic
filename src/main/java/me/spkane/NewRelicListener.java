package me.spkane;

import com.google.common.base.Joiner;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class NewRelicListener implements Listener {
	
	NewRelicPlugin configGetter;
	
	public NewRelicListener(NewRelicPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		configGetter = plugin;
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onEntityDeath(EntityDeathEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.entity.death") == true ) {
			NewRelic.setTransactionName(null, "EntityDeathEvent");
			Entity entity = e.getEntity();
		    if (e.getEntity().getKiller() != null) {           
		    	Entity killer = e.getEntity().getKiller();           
		    	if (killer instanceof Player ) {
		    		Player player = (Player) killer;
		    		NewRelic.addCustomParameter("killedByPlayer", "true");
		    		NewRelic.addCustomParameter("playerName", player.getName());
		    	} else {
		    		NewRelic.addCustomParameter("killedByPlayer", "false");
		    		NewRelic.addCustomParameter("playerName", "");
		    	}
		    } else {
		    	NewRelic.addCustomParameter("killedByPlayer", "false");
	    		NewRelic.addCustomParameter("playerName", "");
		    }
		    String entityname = entity.getType().toString();
		    NewRelic.addCustomParameter("entityType", entityname);
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.creature.spawn") == true ) {
			NewRelic.setTransactionName(null, "CreatureSpawnEvent");
			Entity entity = e.getEntity();
		    String entityname = entity.getType().toString();
		    NewRelic.addCustomParameter("entityType", entityname);
                    String spawnreason =  (e.getSpawnReason() != null) ? e.getSpawnReason().toString() : "";
                    
                    NewRelic.addCustomParameter("spawnReason", spawnreason);
                    Chunk ChunkLocation = e.getEntity().getLocation().getChunk();
                    NewRelic.addCustomParameter("position_X", e.getEntity().getLocation().getBlockX());
                    NewRelic.addCustomParameter("position_Y", e.getEntity().getLocation().getBlockY());
                    NewRelic.addCustomParameter("position_Z", e.getEntity().getLocation().getBlockZ());
                    NewRelic.addCustomParameter("chunk_X",  ChunkLocation.getX());
                    NewRelic.addCustomParameter("chunk_Z", ChunkLocation.getZ());
                    NewRelic.addCustomParameter("world", ChunkLocation.getWorld().getName());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.player.death") == true ) {
			NewRelic.setTransactionName(null, "PlayerDeathEvent");
			Player player = e.getEntity();
			NewRelic.addCustomParameter("playerName", player.getName());
			NewRelic.addCustomParameter("playerDeathMessage", e.getDeathMessage());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.player.join") == true ) {
			NewRelic.setTransactionName(null, "PlayerJoinEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			if (player.hasPlayedBefore() == false) {
				NewRelic.addCustomParameter("playerNew", "true");
			} else {
				NewRelic.addCustomParameter("playerNew", "false");
			}
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerKick(PlayerKickEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.player.kick") == true ) {
			NewRelic.setTransactionName(null, "PlayerKickEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			if (e.getReason() != null) {
			    NewRelic.addCustomParameter("playerKickReason", e.getReason());
			} else {
			    NewRelic.addCustomParameter("playerKickReason", "");
			}
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.player.quit") == true ) {
			NewRelic.setTransactionName(null, "PlayerQuitEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.player.respawn") == true ) {
			NewRelic.setTransactionName(null, "PlayerRespawnEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			NewRelic.addCustomParameter("playerRespawnLocation", e.getRespawnLocation().toString());
			String bedspawn = e.isBedSpawn() ? "true" : "false";
                        
			NewRelic.addCustomParameter("playerIsBedSpawn", bedspawn);
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.player.teleport") == true ) {
			NewRelic.setTransactionName(null, "PlayerTeleportEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			if (e.getCause() != null) {
			    NewRelic.addCustomParameter("playerTeleportCause", e.getCause().toString());
			} else {
				NewRelic.addCustomParameter("playerTeleportCause", "");
			}
			NewRelic.addCustomParameter("playerTeleportFrom", e.getFrom().toString());
			NewRelic.addCustomParameter("playerTeleportTo", e.getTo().toString());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.block.place") == true ) {
                    NewRelic.setTransactionName(null, "BlockPlaceEvent");
                    Player player = e.getPlayer();
                    Block block = e.getBlock();
                    NewRelic.addCustomParameter("playerName", player.getName());
                    NewRelic.addCustomParameter("blockType", block.getType().toString());
                    NewRelic.addCustomParameter("position_X", block.getX());
                    NewRelic.addCustomParameter("position_Y", block.getY());
                    NewRelic.addCustomParameter("position_Z", block.getZ());
                    NewRelic.addCustomParameter("chunk_X", block.getChunk().getX());
                    NewRelic.addCustomParameter("chunk_Z", block.getChunk().getZ());
                    NewRelic.addCustomParameter("world", block.getWorld().getName());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onBlockBreak(BlockBreakEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.block.break") == true ) {
                    NewRelic.setTransactionName(null, "BlockBreakEvent");
                    Player player = e.getPlayer();
                    Block block = e.getBlock();
                    NewRelic.addCustomParameter("playerName", player.getName());
                    NewRelic.addCustomParameter("blockType", block.getType().toString());
                    NewRelic.addCustomParameter("position_X", block.getX());
                    NewRelic.addCustomParameter("position_Y", block.getY());
                    NewRelic.addCustomParameter("position_Z", block.getZ());
                    NewRelic.addCustomParameter("chunk_X", block.getChunk().getX());
                    NewRelic.addCustomParameter("chunk_Z", block.getChunk().getZ());
                    NewRelic.addCustomParameter("world", block.getWorld().getName());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onRemoteServerCommand(RemoteServerCommandEvent  e) {
            if (configGetter.getConfig().getBoolean("enabled") == true && 
                configGetter.getConfig().getBoolean("track.server.remotecommand") == true ) {
                NewRelic.setTransactionName(null, "RemoteCommandEvent");
                NewRelic.addCustomParameter("playerName", e.getSender().toString());
                NewRelic.addCustomParameter("command", e.getCommand());
            } else {
                    NewRelic.ignoreTransaction();
            }
	}
        
        @EventHandler
	@Trace (dispatcher=true)
	public void onPlayerPreprocessEvent(PlayerCommandPreprocessEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
                configGetter.getConfig().getBoolean("track.server.command") == true ) {
                    String[] myList = e.getMessage().split(" ");
                    String command = (String) myList[0].substring(1);
                    int n=myList.length-1;
                    String[] myList1=new String[n];
                    System.arraycopy(myList,1,myList1,0,n);
                    String params = Joiner.on(" ").join(myList1);
                    NewRelic.setTransactionName(null, "CommandEvent");
                    Chunk ChunkLocation = e.getPlayer().getWorld().getChunkAt(e.getPlayer().getLocation());
                    NewRelic.addCustomParameter("playerName", e.getPlayer().getName());
                    NewRelic.addCustomParameter("command", command);
                    NewRelic.addCustomParameter("params", params);
                    NewRelic.addCustomParameter("position_X", e.getPlayer().getLocation().getBlockX());
                    NewRelic.addCustomParameter("position_Y", e.getPlayer().getLocation().getBlockY());
                    NewRelic.addCustomParameter("position_Z", e.getPlayer().getLocation().getBlockZ());
                    NewRelic.addCustomParameter("chunk_X",  ChunkLocation.getX());
                    NewRelic.addCustomParameter("chunk_Z", ChunkLocation.getZ());
                    NewRelic.addCustomParameter("world", ChunkLocation.getWorld().getName());
		} else {
			NewRelic.ignoreTransaction();
		}
	}


	@EventHandler
	@Trace (dispatcher=true)
	public void onServerCommand(ServerCommandEvent  e) {
            if (configGetter.getConfig().getBoolean("enabled") == true && 
                            configGetter.getConfig().getBoolean("track.server.command") == true ) {
                     String[] myList = e.getCommand().split(" ");
                String command = (String) myList[0];
                int n=myList.length-1;
                String[] myList1=new String[n];
                System.arraycopy(myList,1,myList1,0,n);
                String params = Joiner.on(" ").join(myList1);
                NewRelic.setTransactionName(null, "CommandEvent");
                NewRelic.addCustomParameter("playerName", "console");
                NewRelic.addCustomParameter("command", command);
                NewRelic.addCustomParameter("params", params);
            } else {
                    NewRelic.ignoreTransaction();
            }
	}
        @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = false)
        @Trace (dispatcher = true)
        public void onPlayerChat(AsyncPlayerChatEvent e){
            if (configGetter.getConfig().getBoolean("enabled") == true && 
                configGetter.getConfig().getBoolean("track.server.chat") == true ) {
                Chunk ChunkLocation = e.getPlayer().getWorld().getChunkAt(e.getPlayer().getLocation());
                NewRelic.setTransactionName(null, "chatEvent");
                HashMap<String, String> position = new HashMap<String, String>();
                position.put("x",Integer.toString(e.getPlayer().getLocation().getBlockX()));
                position.put("y",Integer.toString(e.getPlayer().getLocation().getBlockY()));
                position.put("z",Integer.toString(e.getPlayer().getLocation().getBlockZ()));
                position.put("world",e.getPlayer().getLocation().getWorld().getName());
                
                NewRelic.addCustomParameter("playerName", e.getPlayer().getName());
                NewRelic.addCustomParameter("message", e.getMessage());
                NewRelic.addCustomParameter("position", mapToString(position));
                NewRelic.addCustomParameter("position_X", position.get("x"));
                NewRelic.addCustomParameter("position_Y",  position.get("y"));
                NewRelic.addCustomParameter("position_Z",  position.get("z"));
                NewRelic.addCustomParameter("chunk_X",  ChunkLocation.getX());
                NewRelic.addCustomParameter("chunk_Z", ChunkLocation.getZ());
                NewRelic.addCustomParameter("world", ChunkLocation.getWorld().getName());
            } else {
                NewRelic.ignoreTransaction();
            }
        }
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onChunkLoad(ChunkLoadEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.chunk.load") == true ) {
			NewRelic.setTransactionName(null, "ChunkLoadEvent");
			NewRelic.addCustomParameter("chunkName", e.getChunk().toString());
			String newchunk =  (e.isNewChunk() == true) ? "true" : "false";
			NewRelic.addCustomParameter("chunkNew", newchunk);
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onChunkUnload(ChunkUnloadEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") == true && 
				configGetter.getConfig().getBoolean("track.chunk.unload") == true ) {
			NewRelic.setTransactionName(null, "ChunkUnloadEvent");
			NewRelic.addCustomParameter("chunkName", e.getChunk().toString());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
        
        
        public String mapToString(HashMap<String,String> hashmap){
            String returnString = "{";
            Iterator<Map.Entry<String,String>> itr1 = hashmap.entrySet().iterator();
            boolean first = true;
            while(itr1.hasNext()) {
                Map.Entry<String,String> entry = itr1.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if(!first){
                    returnString += ", ";
                }
                returnString += key+":"+value;
                first = false;
            }
            return returnString+"}";
        }
	
}
