package xyz.adrian_kilgour.speedtab;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;

/**
 * Manages the tab list header and footer for players.
 */
public class TabManager {
    private final ProxyServer server;
    private final String defaultTabTitle;
    private final String defaultTabFooter;
    private final Map<String, String> serverTabTitles;
    private final Map<String, String> serverTabFooters;

    /**
     * Constructor for the TabManager class.
     * @param server            the proxy server
     * @param defaultTabTitle   the default tab title
     * @param defaultTabFooter  the default tab footer
     * @param serverTabTitles   the tab titles for different servers
     * @param serverTabFooters  the tab footers for different servers
     */
    public TabManager(ProxyServer server, String defaultTabTitle, String defaultTabFooter,
                      Map<String, String> serverTabTitles, Map<String, String> serverTabFooters) {
        this.server = server;
        this.defaultTabTitle = defaultTabTitle;
        this.defaultTabFooter = defaultTabFooter;
        this.serverTabTitles = serverTabTitles;
        this.serverTabFooters = serverTabFooters;
    }

    /**
     * Called when a player connects to a server.
     * Sets the tab list header and footer based on the server the player is connected to.
     * @param event the server connected event
     */
    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        String serverName = event.getServer().getServerInfo().getName();

        // Get tab title and foorter for the server, or use defaults if not specified.
        String tabTitle = serverTabTitles.getOrDefault(serverName, defaultTabTitle);
        String tabFooter = serverTabFooters.getOrDefault(serverName, defaultTabFooter);

        // Parse the tab title and footer using MiniMessage for color codes and other formatting.
        MiniMessage minimessage = MiniMessage.miniMessage();
        Component parsedTitle = minimessage.deserialize(tabTitle);
        Component parsedFooter = minimessage.deserialize(tabFooter);

        // Set the player's tab list header and footer.
        player.sendPlayerListHeaderAndFooter(parsedTitle, parsedFooter);
    }
}
