package xyz.adrian_kilgour.speedtab;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Class for the SpeedTab plugin.
 * Handles loading configuration and initializing the tab manager.
 */
@Plugin(
        id = "speedtab",
        name = "SpeedTab",
        version = "0.0.2-SNAPSHOT",
        description = "A customizable tab plugin for Velocity.",
        authors = {"Adrian Kilgour"}
)
public class SpeedTab {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private String defaultTabTitle;
    private String defaultTabFooter;
    private final Map<String, String> serverTabTitles = new HashMap<>();
    private final Map<String, String> serverTabFooters = new HashMap<>();

    /**
     * Constructor for the SpeedTab class.
     * @param server        the proxy server
     * @param logger        the logger
     * @param dataDirectory the data directory
     */
    @Inject
    public SpeedTab(ProxyServer server , Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    /**
     * Called when the proxy is initialixed.
     * Loads the configuration and registers the tab manager.
     * @param event the proxy initialization event
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("SpeedTab has started!");
        // Load the config file.
        loadConfig();
        // Initialize the tab manager using the loaded config file.
        TabManager tabManager = new TabManager(server, defaultTabTitle, defaultTabFooter, serverTabTitles, serverTabFooters);
        // Register the tab manager to lisen for events.
        server.getEventManager().register(this, tabManager);
    }

    /**
     * Loads the configuration from the config file.
     */
    private void loadConfig() {
        Path configPath = dataDirectory.resolve("config.conf");
        if(!Files.exists(configPath)) {
            // Create the config directory and default config file if it does not exit.
            try {
                Files.createDirectories(dataDirectory);
                Files.copy(getClass().getResourceAsStream("/config.conf"), configPath);
            } catch (IOException e) {
                logger.error("Failed to create default config file", e);
            }
        }

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(configPath).build();
        try {
            // Load the config file.
            ConfigurationNode node = loader.load();
            // Load default tab title and footer.
            defaultTabTitle = node.node("default").getString("Default Tab Title");
            defaultTabFooter = node.node("default").getString("Default Tab Footer");

            // Load tab titles and footers for specific servers.
            ConfigurationNode serversNode = node.node("servers");
            for(Map.Entry<Object, ? extends ConfigurationNode> entry : serversNode.childrenMap().entrySet()) {
                String serverName = entry.getKey().toString();
                String tabTitle = entry.getValue().node("tabTitle").getString(defaultTabTitle);
                String tabFooter = entry.getValue().node("tabFooter").getString(defaultTabFooter);
                serverTabTitles.put(serverName, tabTitle);
                serverTabFooters.put(serverName, tabFooter);
            }
        } catch(IOException e) {
            logger.error("Failed to load config file.", e);
            // Set defaults if loading fails.
            defaultTabTitle = "Default Tab Title";
            defaultTabFooter = "Default Tab Footer";
        }
    }

    /**
     * Gets the default tab title.
     * @return the default tab title
     */
    public String getDefaultTabTitle() {
        return defaultTabTitle;
    }

    /**
     * Gets the default tab footer.
     * @return the default tab footer
     */
    public String getDefaultTabFooter() {
        return defaultTabFooter;
    }

    /**
     * Gets the tab titles for different servers.
     * @return the tab titles for different servers
     */
    public Map<String, String> getServerTabTitles() {
        return serverTabTitles;
    }

    /**
     * Gets the tab footers for different servers.
     * @return the tab footers for different servers
     */
    public Map<String, String> getServerTabFooters() {
        return serverTabFooters;
    }
}
