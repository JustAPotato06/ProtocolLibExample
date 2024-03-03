package dev.potato.packetexample.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import dev.potato.packetexample.PacketExample;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoomCommand implements CommandExecutor {
    private final ProtocolManager manager = PacketExample.getProtocolManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            Block block = p.getTargetBlockExact(5);

            if (block == null) return true;

            Location blockLocation = block.getLocation();

            // Creating packets
            PacketContainer packet = manager.createPacket(PacketType.Play.Server.EXPLOSION);
            packet.getDoubles().write(0, blockLocation.getX());
            packet.getDoubles().write(1, blockLocation.getY());
            packet.getDoubles().write(2, blockLocation.getZ());
            packet.getFloat().write(0, 100.0f);
            packet.getFloat().write(1, 2.0f);
            packet.getFloat().write(2, 2.0f);
            packet.getFloat().write(3, 2.0f);
            manager.sendServerPacket(p, packet);
            System.out.println("SENT");
        }

        return true;
    }
}