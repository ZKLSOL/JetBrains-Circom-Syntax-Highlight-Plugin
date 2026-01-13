# Circom Language Support Plugin Specification

## Overview

A JetBrains IDE plugin providing syntax highlighting and basic IDE features for Circom 2.x circuit files.

## Reference Implementation

Based on: `/Users/ohaddahan/RustroverProjects/zklsol/intellij-circom`

## Plugin Identity

| Property | Value |
|----------|-------|
| Plugin ID | `com.ohaddahan.circom` |
| Plugin Name | Circom Language Support |
| Vendor | ohaddahan |
| Vendor URL | https://github.com/ohaddahan |
| Initial Version | 0.1.0 |
| Distribution | JetBrains Marketplace |

## Version Compatibility

| Property | Value |
|----------|-------|
| Minimum IDE Build | 243 (2024.3) |
| Maximum IDE Build | None (no upper bound) |
| Target IDEs | All JetBrains IDEs |
| Platform Dependency | `com.intellij.modules.platform`, `com.intellij.modules.lang` |
| Java Version | 21 |
| Implementation Language | Kotlin |

## File Type Support

- **Extension**: `.circom`
- **Language Name**: Circom
- **MIME Type**: `text/circom`
- **Case Sensitive**: Yes
- **Charset**: UTF-8

## Features

### Core Features (from reference)

1. **Syntax Highlighting**
   - Keywords
   - Comments (line `//` and block `/* */`)
   - Strings (double-quoted with escape sequences)
   - Numbers (decimal and hexadecimal 0x prefix)
   - Operators
   - Constraint operators (`===`, `<==`, `==>`, `<--`, `-->`)
   - Signal types (`input`, `output`)
   - Delimiters (parentheses, brackets, braces)

2. **ANTLR-based Lexer/Parser**
   - Full Circom 2.x grammar support
   - Proper token categorization

### Additional Features (beyond reference)

1. **Modifier Highlighting**
   - `parallel`, `custom`, `bus` keywords highlighted with annotation style (METADATA color)

2. **Bracket Matching**
   - Matching pairs: `()`, `[]`, `{}`

3. **Code Folding**
   - Templates and functions
   - All block structures (if/else, for, while)
   - Multi-line comments

4. **Line Commenting**
   - Toggle line comments with Cmd+/ (or Ctrl+/)
   - Uses `//` comment style

## Syntax Highlighting Colors

### Token Categories and Default Colors

| Token Category | Attribute Key | Default Color Source |
|---------------|---------------|---------------------|
| Keywords | `CIRCOM_KEYWORD` | `DefaultLanguageHighlighterColors.KEYWORD` |
| Line Comments | `CIRCOM_LINE_COMMENT` | `DefaultLanguageHighlighterColors.LINE_COMMENT` |
| Block Comments | `CIRCOM_BLOCK_COMMENT` | `DefaultLanguageHighlighterColors.BLOCK_COMMENT` |
| Strings | `CIRCOM_STRING` | `DefaultLanguageHighlighterColors.STRING` |
| Numbers | `CIRCOM_NUMBER` | `DefaultLanguageHighlighterColors.NUMBER` |
| Commas | `CIRCOM_COMMA` | `DefaultLanguageHighlighterColors.COMMA` |
| Semicolons | `CIRCOM_SEMICOLON` | `DefaultLanguageHighlighterColors.SEMICOLON` |
| Parentheses | `CIRCOM_PARENTHESIS` | `DefaultLanguageHighlighterColors.PARENTHESES` |
| Braces | `CIRCOM_BRACES` | `DefaultLanguageHighlighterColors.BRACES` |
| Brackets | `CIRCOM_BRACKETS` | `DefaultLanguageHighlighterColors.BRACKETS` |

### Custom Colors (user configurable via IDE settings)

| Token Category | Attribute Key | Default Hex Color |
|---------------|---------------|-------------------|
| Signal Types (input/output) | `CIRCOM_SIGNAL_TYPE` | `#879DE0` (blue-violet) |
| Constraint Operators | `CIRCOM_CONSTRAINT_GEN` | `#4EC9B0` (teal) |
| Modifiers (parallel/custom/bus) | `CIRCOM_MODIFIER` | METADATA color (annotation style) |

Users can customize all colors via: **Settings > Editor > Color Scheme > Circom**

## Keywords

```
pragma, circom, custom_templates, include, custom, parallel, bus,
template, function, main, public, component, var, signal, input,
output, if, else, for, while, do, log, assert, return
```

## Operators

### Constraint Operators
```
===  <==  -->  ==>  <--
```

### Assignment Operators
```
=  +=  -=  *=  **=  /=  \=  %=  <<=  >>=  &=  ^=  |=
```

### Arithmetic Operators
```
+  -  *  /  \  %  **
```

### Bitwise Operators
```
&  |  ^  ~  <<  >>
```

### Logical Operators
```
&&  ||  !
```

### Other Operators
```
++  --  ?  :
```

## Project Structure

```
JetBrains-Circom-Syntax-Highlight-Plugin/
├── src/
│   ├── main/
│   │   ├── antlr/
│   │   │   ├── CircomLexer.g4
│   │   │   └── CircomParser.g4
│   │   ├── kotlin/
│   │   │   └── com/ohaddahan/circom/
│   │   │       ├── adaptors/
│   │   │       │   ├── CircomGrammarParser.kt
│   │   │       │   ├── CircomLexerAdaptor.kt
│   │   │       │   └── CircomLexerState.kt
│   │   │       ├── ide/
│   │   │       │   ├── CircomBraceMatcher.kt
│   │   │       │   ├── CircomCommenter.kt
│   │   │       │   ├── CircomFoldingBuilder.kt
│   │   │       │   └── icons.kt
│   │   │       └── lang/
│   │   │           ├── CircomFileRoot.kt
│   │   │           ├── CircomLanguage.kt
│   │   │           ├── CircomSyntaxHighlighter.kt
│   │   │           ├── CircomSyntaxHighlighterFactory.kt
│   │   │           └── CircomTokenTypes.kt
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   ├── plugin.xml
│   │       │   └── pluginIcon.svg
│   │       ├── colorSchemes/
│   │       │   └── CircomDefault.xml
│   │       └── icons/
│   │           ├── circom-file.png
│   │           └── circom-file@2x.png
│   └── test/
│       └── testData/
│           └── (minimal test data files)
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── CHANGELOG.md
└── PLAN.md
```

## Build Configuration

### gradle.properties

```properties
pluginGroup = com.ohaddahan.circom
pluginName = Circom Language Support
pluginVersion = 0.1.0
pluginSinceBuild = 243
pluginUntilBuild =
platformType = IC
platformVersion = 2024.3
javaVersion = 21
javaTargetVersion = 21
```

### Dependencies

- IntelliJ Platform Plugin (Gradle)
- Kotlin Plugin
- ANTLR Plugin (version 4.13.1)
- Changelog Plugin

## plugin.xml Extensions

```xml
<extensions defaultExtensionNs="com.intellij">
    <!-- File Type -->
    <fileType
        name="Circom file"
        language="Circom"
        implementationClass="com.ohaddahan.circom.lang.CircomFileType"
        extensions="circom"/>

    <!-- Syntax Highlighter -->
    <lang.syntaxHighlighterFactory
        language="Circom"
        implementationClass="com.ohaddahan.circom.lang.CircomSyntaxHighlighterFactory"/>

    <!-- Color Scheme -->
    <additionalTextAttributesScheme scheme="Default" file="colorSchemes/CircomDefault.xml"/>
    <additionalTextAttributesScheme scheme="Darcula" file="colorSchemes/CircomDefault.xml"/>
    <additionalTextAttributesScheme scheme="Dark" file="colorSchemes/CircomDefault.xml"/>

    <!-- Bracket Matching -->
    <lang.braceMatcher
        language="Circom"
        implementationClass="com.ohaddahan.circom.ide.CircomBraceMatcher"/>

    <!-- Code Folding -->
    <lang.foldingBuilder
        language="Circom"
        implementationClass="com.ohaddahan.circom.ide.CircomFoldingBuilder"/>

    <!-- Commenting -->
    <lang.commenter
        language="Circom"
        implementationClass="com.ohaddahan.circom.ide.CircomCommenter"/>
</extensions>
```

## Marketplace Submission

### Plugin Description (draft)

```
Circom Language Support provides syntax highlighting and IDE features for Circom,
the domain-specific language for writing arithmetic circuits used in zero-knowledge proofs.

Features:
- Full syntax highlighting for Circom 2.x
- Special highlighting for constraint operators (===, <==, ==>, <--, -->)
- Signal type highlighting (input, output)
- Template modifier highlighting (parallel, custom, bus)
- Bracket matching for (), [], {}
- Code folding for templates, functions, and blocks
- Line comment toggling (Cmd+/ or Ctrl+/)
- Customizable colors via IDE settings

Supports all JetBrains IDEs: IntelliJ IDEA, WebStorm, PyCharm, RustRover, and more.
```

### Initial Change Notes (draft)

```
<h3>0.1.0</h3>
<ul>
    <li>Initial release</li>
    <li>Syntax highlighting for Circom 2.x</li>
    <li>Constraint operator highlighting</li>
    <li>Signal type and modifier highlighting</li>
    <li>Bracket matching</li>
    <li>Code folding</li>
    <li>Line commenting support</li>
</ul>
```

## Testing

Minimal tests matching reference approach:
- Basic lexer token verification
- Sample Circom files for manual verification
- Test data files in `src/test/testData/`

## Implementation Steps

1. **Project Setup**
   - Initialize Gradle project with IntelliJ Platform Plugin
   - Configure gradle.properties and build.gradle.kts
   - Set up ANTLR integration

2. **Copy and Adapt Grammar**
   - Copy CircomLexer.g4 and CircomParser.g4 from reference
   - Update package names to `com.ohaddahan.circom`

3. **Implement Language Foundation**
   - CircomLanguage.kt - Language definition
   - CircomFileType - File type registration
   - CircomFileRoot - PSI root element

4. **Implement Lexer Adaptors**
   - CircomLexerAdaptor.kt - ANTLR to IntelliJ bridge
   - CircomLexerState.kt - Lexer state management
   - CircomGrammarParser.kt - Parser adaptor

5. **Implement Syntax Highlighting**
   - CircomTokenTypes.kt - Token categorization (add MODIFIER category)
   - CircomSyntaxHighlighter.kt - Color mappings
   - CircomSyntaxHighlighterFactory.kt - Factory registration
   - CircomDefault.xml - Custom color scheme

6. **Implement IDE Features**
   - CircomBraceMatcher.kt - Bracket matching for (), [], {}
   - CircomFoldingBuilder.kt - Code folding regions
   - CircomCommenter.kt - Line comment support

7. **Add Resources**
   - Copy icon files from reference
   - Create plugin.xml with all extensions
   - Create CHANGELOG.md

8. **Build and Test**
   - Run `./gradlew buildPlugin`
   - Test in sandbox IDE
   - Verify all features work

9. **Marketplace Submission**
   - Sign plugin
   - Submit to JetBrains Marketplace
   - Await approval

## Notes

- No upper bound on IDE version to maximize forward compatibility
- User can customize all colors via standard IDE settings (Editor > Color Scheme > Circom)
- Uses reference PNG icons (circom-file.png and @2x variant)
- Targets Circom 2.x syntax only
- Changelog plugin integrated for release management
