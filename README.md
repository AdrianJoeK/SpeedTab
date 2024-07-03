# SpeedTab

[![Velocity](https://img.shields.io/badge/Velocity-3.3.0-brightgreen)](https://velocitypowered.com/)
[![License](https://img.shields.io/badge/License-AGPL-blue.svg)](LICENSE)

SpeedTab is a customizable tab plugin for the Velocity Minecraft proxy. It allows you to set custom headers and footers in the Minecraft tab list across different servers, with support for colour codes and formatting.

## Features

- Customizable tab headers and footers.
- Different headers and footers for different servers.
- Support for Minecraft colour codes and formatting.
- Reload config with a command `/speedtabreload`.

## Installation

1. Download the latest release of SpeedTab from the [Releases](https://github.com/AdrianJoeK/speedtab/releases) page.
2. Place the downloaded JAR file in the `plugins` directory of your Velocity server.
3. Start your Velocity server to generate the default configuration file.

## Commands
- Reload config - `/speedtabreload`

## Configuration

After running the server for the first time with SpeedTab installed, a default configuration file will be created in the `plugins/speedtab` directory.

### Configuration File

The configuration file `config.conf` allows you to customize the tab headers and footers. Here is the default configuration:

```hocon
default {
    tabTitle = "&aWelcome to &bSpeedTab!"
    tabFooter = "&cEnjoy your stay!"
}

servers {
    lobby {
        tabTitle = "&aWelcome to the &bLobby!"
        tabFooter = "&cEnjoy your time in the Lobby!"
    }
    survival {
        tabTitle = "&aWelcome to &bSurvival!"
        tabFooter = "&cEnjoy your time in Survival!"
    }
    // Add more servers as required.
}
```

For headers and footers for different servers to work, the server names should match exactly what you have in your velocity.toml file. The plugin will fallback to the default headers and footers if needed.

### Colour Codes
SpeedTab supports Minecraft colour codes and formatting codes. Use the following codes in your configuration file:

* &0 - Black
* &1 - Dark Blue
* &2 - Dark Green
* &3 - Dark Aqua
* &4 - Dark Red
* &5 - Dark Purple
* &6 - Gold
* &7 - Gray
* &8 - Dark Gray
* &9 - Blue
* &a - Green
* &b - Aqua
* &c - Red
* &d - Light Purple
* &e - Yellow
* &f - White
* &k - Obfuscated
* &l - Bold
* &m - Strikethrough
* &n - Underlined
* &o - Italic
* &r - Reset

## Development
### Prerequisites
* Java 21
* Maven
### Building
Clone the repository and build the plugin using Maven:

```sh
git clone https://github.com/AdrianJoeK/speedtab.git
cd speedtab
mvn package
```

The compiled JAR file will be located in the target directory.

## License
SpeedTab is licensed under the AGPL-3.0 License. See the [LICENSE](https://github.com/AdrianJoeK/SpeedTab/blob/master/LICENSE) file for more details.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request on GitHub.

## Acknowledgements
* [Velocity](https://papermc.io/software/velocity) - The Minecraft proxy server that SpeedTab is built for.
* [Configurate](https://github.com/SpongePowered/Configurate) - The library used for configuration management.
* [MiniMessage](https://github.com/KyoriPowered/adventure-text-minimessage) - The library used for text formatting.