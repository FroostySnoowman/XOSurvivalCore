package fwoostybots.com.xosurvivalcore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinEvent implements Listener {

    private Main main;

    public JoinEvent(Main main) {
        this.main = main;
    }

    // resourcePackStatus map
    Map<UUID, Boolean> resourcePackStatus = new HashMap<>();

    // Give the player the resource pack prompt
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String join_message = main.getConfig().getString("join-message");
        for (Player e : Bukkit.getOnlinePlayers()) {
            e.sendMessage(ChatColor.translateAlternateColorCodes('&', join_message).replaceAll("<prefix>", "Prefix").replaceAll("<name>", e.getPlayer().getName()));
        }
        player.setResourcePack("https://www.dropbox.com/s/rieh4n8tvkjh1gw/dd.zip?dl=1");
    }

    // Resource Pack Status
    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        String prefix_message = main.getConfig().getString("prefix-message");
        String failed_resource_pack_message = main.getConfig().getString("failed-resource-pack-message");
        String declined_resource_pack_message = main.getConfig().getString("declined-resource-pack-message");
        // If the resource pack download failed
        if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            resourcePackStatus.put(player.getUniqueId(), false);
            player.sendMessage(MiniMessage.miniMessage().deserialize(prefix_message + ' ' + failed_resource_pack_message));
        }

        // If the user declined the resource pack download
        if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
            resourcePackStatus.put(player.getUniqueId(), false);
            player.sendMessage(MiniMessage.miniMessage().deserialize(prefix_message + ' ' + declined_resource_pack_message));
        }

        // If the user accepted the resource pack download
        else {
            resourcePackStatus.put(player.getUniqueId(), true);
        }
    }
}