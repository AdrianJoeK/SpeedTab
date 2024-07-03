package xyz.adrian_kilgour.speedtab;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.awt.*;

public class TabManager {
    private final ProxyServer server;
    private final String tabTitle;

    public TabManager(ProxyServer server, String tabTitle) {
        this.server = server;
        this.tabTitle = tabTitle;
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();

        player.sendPlayerListHeaderAndFooter(Component.text(tabTitle), Component.empty());
    }
}
