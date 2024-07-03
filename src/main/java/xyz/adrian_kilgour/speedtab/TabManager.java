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
    private String defaultTabTitle;
    private String defaultTabFooter;
    private Map<String, String> serverTabTitles;
    private Map<String, String> serverTabFooters;

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
        applyTabList(player, event.getServer().getServerInfo().getName());
    }

    /**
     * Applies the tab list header and footer to a player.
     * @param player        the player to apply the tab list to
     * @param serverName    the name of the server the player is connected to
     */
    private void applyTabList(Player player, String serverName) {
        // Get tab title and footer for the server, or use defaults if not specified.
        String tabTitle = serverTabTitles.getOrDefault(serverName, defaultTabTitle);
        String tabFooter = serverTabFooters.getOrDefault(serverName, defaultTabFooter);

        // Convert & colour codes to MiniMessage format.
        String parsedTitle = convertColours(tabTitle);
        String parsedFooter = convertColours(tabFooter);

        // Parse the tab title and footer using MiniMessage for color codes and other formatting.
        MiniMessage minimessage = MiniMessage.miniMessage();
        Component titleComponent = minimessage.deserialize(parsedTitle);
        Component footerComponent = minimessage.deserialize(parsedFooter);

        // Set the player's tab list header and footer.
        player.sendPlayerListHeaderAndFooter(titleComponent, footerComponent);
    }

    /**
     * Converts & style colour codes to MiniMessage format.
     * @param text the text with & style color codes
     * @return the text with MiniMessage color tags
     */
    private String convertColours(String text) {
        return text
                .replaceAll("(?i)&0", "<black>")
                .replaceAll("(?i)&1", "<dark_blue>")
                .replaceAll("(?i)&2", "<dark_green>")
                .replaceAll("(?i)&3", "<dark_aqua>")
                .replaceAll("(?i)&4", "<dark_red>")
                .replaceAll("(?i)&5", "<dark_purple>")
                .replaceAll("(?i)&6", "<gold>")
                .replaceAll("(?i)&7", "<gray>")
                .replaceAll("(?i)&8", "<dark_gray>")
                .replaceAll("(?i)&9", "<blue>")
                .replaceAll("(?i)&a", "<green>")
                .replaceAll("(?i)&b", "<aqua>")
                .replaceAll("(?i)&c", "<red>")
                .replaceAll("(?i)&d", "<light_purple>")
                .replaceAll("(?i)&e", "<yellow>")
                .replaceAll("(?i)&f", "<white>")
                .replaceAll("(?i)&k", "<obfuscated>")
                .replaceAll("(?i)&l", "<bold>")
                .replaceAll("(?i)&m", "<strikethrough>")
                .replaceAll("(?i)&n", "<underlined>")
                .replaceAll("(?i)&o", "<italic>")
                .replaceAll("(?i)&r", "<reset>");
    }

    /**
     * Updates the titles and footers with new values.
     * @param defaultTabTitle   the new default tab title
     * @param defaultTabFooter  the new default tab footer
     * @param serverTabTitles   the new tab titles for different servers
     * @param serverTabFooters  the new tab footers for different servers
     */
    public void updateTitlesAndFooters(String defaultTabTitle, String defaultTabFooter,
                                       Map<String, String> serverTabTitles, Map<String, String> serverTabFooters) {
        this.defaultTabTitle = defaultTabTitle;
        this.defaultTabFooter = defaultTabFooter;
        this.serverTabTitles = serverTabTitles;
        this.serverTabFooters = serverTabFooters;

        // Reapply tab list to all connected players, so they don't need to rejoin.
        for (Player player : server.getAllPlayers()) {
            applyTabList(player, player.getCurrentServer().get().getServerInfo().getName());
        }
    }
}
