<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Circom Language Support Changelog

## [Unreleased]

## [0.2.0]
### Added
- Go to Definition (Cmd+Click) - Navigate to template/function definitions
- Find Usages (Alt+F7) - Find all usages of templates, functions, signals, variables, components
- Cross-file navigation - Navigate between .circom files in the project
- Structure View (Cmd+7) - View templates, functions, signals, variables, components
- Go to Symbol (Cmd+Shift+O) - Quick navigation to any symbol
- Rename refactoring (Shift+F6) - Rename symbols across the project

### Fixed
- References now resolve correctly using direct PSI element implementation

## [0.1.0]
### Added
- Full syntax highlighting for Circom 2.x
- Special highlighting for constraint operators (===, <==, ==>, <--, -->)
- Signal type highlighting (input, output)
- Template modifier highlighting (parallel, custom, bus)
- Bracket matching for (), [], {}
- Code folding for templates, functions, and blocks
- Line comment toggling (Cmd+/ or Ctrl+/)
- Customizable colors via IDE settings (Editor > Color Scheme > Circom)
