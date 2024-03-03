package dev.potato.packetexample;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.potato.packetexample.commands.BoomCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PacketExample extends JavaPlugin {
    private static ProtocolManager protocolManager;

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onEnable() {
        // Get our protocol lib manager
        protocolManager = ProtocolLibrary.getProtocolManager();

        // Listening for incoming packets - Client to Server
        getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player p = event.getPlayer();
                // Extracting information from the packet
                double x = packet.getDoubles().read(0);
                double y = packet.getDoubles().read(1);
                double z = packet.getDoubles().read(2);
                boolean isOnGround = packet.getBooleans().read(0);
            }
        });

        // Listening for incoming packets - Server to Client
        getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.REL_ENTITY_MOVE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player p = event.getPlayer();
                // Extracting information
                short deltaX = packet.getShorts().read(0);
                short deltaY = packet.getShorts().read(1);
                short deltaZ = packet.getShorts().read(2);
                int entityID = packet.getIntegers().read(0);
                Entity entity = getProtocolManager().getEntityFromID(p.getWorld(), entityID);
            }
        });

        // Cancelling packets
        getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                event.setCancelled(true);
            }
        });

        // Commands
        getCommand("boom").setExecutor(new BoomCommand());
    }
}