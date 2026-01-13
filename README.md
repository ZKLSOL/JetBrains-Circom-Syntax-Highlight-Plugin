# Circom Language Support

![Build](https://github.com/ZKLSOL/JetBrains-Circom-Syntax-Highlight-Plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

A JetBrains IDE plugin providing syntax highlighting and IDE features for [Circom](https://docs.circom.io/), the domain-specific language for writing arithmetic circuits used in zero-knowledge proofs.

<!-- Plugin description -->
Circom Language Support provides syntax highlighting and IDE features for Circom, the domain-specific language for writing arithmetic circuits used in zero-knowledge proofs.

**Features:**
- Full syntax highlighting for Circom 2.x
- Special highlighting for constraint operators (===, <==, ==>, <--, -->)
- Signal type highlighting (input, output)
- Template modifier highlighting (parallel, custom, bus)
- Bracket matching for (), [], {}
- Code folding for templates, functions, and blocks
- Line comment toggling (Cmd+/ or Ctrl+/)
- Customizable colors via IDE settings

Supports all JetBrains IDEs: IntelliJ IDEA, WebStorm, PyCharm, RustRover, and more.
<!-- Plugin description end -->

## Supported IDEs

- IntelliJ IDEA (Community & Ultimate)
- WebStorm
- PyCharm
- RustRover
- CLion
- GoLand
- PhpStorm
- And all other JetBrains IDEs (2024.3+)

## Installation

### From JetBrains Marketplace (Recommended)

1. Open your JetBrains IDE
2. Go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd>
3. Search for "Circom Language Support"
4. Click <kbd>Install</kbd>

### Manual Installation

1. Download the latest release from [GitHub Releases](https://github.com/ZKLSOL/JetBrains-Circom-Syntax-Highlight-Plugin/releases/latest)
2. Open your JetBrains IDE
3. Go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
4. Select the downloaded ZIP file

## Syntax Highlighting

The plugin provides specialized highlighting for Circom-specific constructs:

| Element | Color | Example |
|---------|-------|---------|
| Keywords | Default keyword color | `template`, `signal`, `component`, `var` |
| Signal Types | Blue-violet (#879DE0) | `input`, `output` |
| Constraint Operators | Teal (#4EC9B0) | `===`, `<==`, `==>`, `<--`, `-->` |
| Modifiers | Yellow (#BBB529) | `parallel`, `custom`, `bus` |
| Comments | Default comment color | `//`, `/* */` |
| Strings | Default string color | `"include.circom"` |
| Numbers | Default number color | `123`, `0x1A2B` |

### Customizing Colors

You can customize all colors via <kbd>Settings</kbd> > <kbd>Editor</kbd> > <kbd>Color Scheme</kbd> > <kbd>Circom</kbd>

## Building from Source

### Prerequisites

- JDK 21 or higher
- Gradle 8.11+ (included via wrapper)

### Build Commands

```bash
# Clone the repository
git clone https://github.com/ZKLSOL/JetBrains-Circom-Syntax-Highlight-Plugin.git
cd circom-language-support

# Build the plugin
./gradlew buildPlugin

# The plugin ZIP will be created at:
# build/distributions/circom-language-support-<version>.zip
```

### Testing Locally

```bash
# Run the plugin in a sandbox IDE instance
./gradlew runIde

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean
```

### Development

For implementation details, see [DEV.md](DEV.md).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Acknowledgments

- Based on the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- Grammar inspired by the [intellij-circom](https://github.com/poma/intellij-circom) project
