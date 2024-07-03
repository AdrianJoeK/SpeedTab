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

@Plugin(
        id = "speedtab",
        name = "SpeedTab",
        version = "0.0.1-SNAPSHOT",
        description = "A customizable tab plugin for Velocity.",
        authors = {"Adrian Kilgour"}
)
public class SpeedTab {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private String tabTitle;

    @Inject
    public SpeedTab(ProxyServer server , Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("SpeedTab has started!");
        loadConfig();
        TabManager tabManager = new TabManager(server, tabTitle);
        server.getEventManager().register(this, tabManager);
    }

    private void loadConfig() {
        Path configPath = dataDirectory.resolve("config.conf");
        if(!Files.exists(configPath)) {
            try {
                Files.createDirectories(dataDirectory);
                Files.copy(getClass().getResourceAsStream("/config.conf"), configPath);
            } catch (IOException e) {
                logger.error("Failed to create default config file", e);
            }
        }

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(configPath).build();
        try {
            ConfigurationNode node = loader.load();
            tabTitle = node.node("tabTitle").getString("Default Tab Title");
        } catch(IOException e) {
            logger.error("Failed to load config file.", e);
            tabTitle = "Default Tab Title";
        }
    }
}
