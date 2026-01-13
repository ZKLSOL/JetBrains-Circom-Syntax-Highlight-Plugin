# Development Guide

This document describes the implementation details of the Circom Language Support plugin.

## Architecture Overview

The plugin uses ANTLR4 for lexing and parsing Circom code, with adapter classes bridging ANTLR to IntelliJ's PSI (Program Structure Interface) system.

```
┌─────────────────────────────────────────────────────────────┐
│                    IntelliJ Platform                        │
├─────────────────────────────────────────────────────────────┤
│  plugin.xml (Extension Points)                              │
│    ├── fileType → CircomFileType                           │
│    ├── syntaxHighlighterFactory → CircomSyntaxHighlighter  │
│    ├── braceMatcher → CircomBraceMatcher                   │
│    ├── foldingBuilder → CircomFoldingBuilder               │
│    └── commenter → CircomCommenter                         │
├─────────────────────────────────────────────────────────────┤
│  Kotlin Implementation                                      │
│    ├── lang/ (Language definitions)                        │
│    ├── adaptors/ (ANTLR-IntelliJ bridge)                   │
│    └── ide/ (IDE features)                                 │
├─────────────────────────────────────────────────────────────┤
│  ANTLR4 Grammar                                            │
│    ├── CircomLexer.g4                                      │
│    └── CircomParser.g4                                     │
└─────────────────────────────────────────────────────────────┘
```

## Project Structure

```
src/main/
├── antlr/                          # ANTLR grammar files
│   ├── CircomLexer.g4              # Lexer grammar
│   └── CircomParser.g4             # Parser grammar
├── kotlin/com/ohaddahan/circom/
│   ├── lang/                       # Language definitions
│   │   ├── CircomLanguage.kt       # Language & file type
│   │   ├── CircomFileRoot.kt       # PSI root element
│   │   ├── CircomTokenTypes.kt     # Token categorization
│   │   ├── CircomSyntaxHighlighter.kt
│   │   └── CircomSyntaxHighlighterFactory.kt
│   ├── adaptors/                   # ANTLR-IntelliJ bridge
│   │   ├── CircomLexerAdaptor.kt   # Lexer adapter
│   │   ├── CircomLexerState.kt     # Lexer state
│   │   └── CircomGrammarParser.kt  # Parser adapter
│   └── ide/                        # IDE features
│       ├── CircomIcons.kt          # File icons
│       ├── CircomBraceMatcher.kt   # Bracket matching
│       ├── CircomFoldingBuilder.kt # Code folding
│       └── CircomCommenter.kt      # Line comments
└── resources/
    ├── META-INF/plugin.xml         # Plugin configuration
    ├── colorSchemes/CircomDefault.xml  # Custom colors
    └── icons/                      # Icon files
        ├── circom-file.png
        └── circom-file@2x.png
```

## Implementation Details

### 1. Grammar (ANTLR)

**Files:** `src/main/antlr/CircomLexer.g4`, `CircomParser.g4`

The grammar defines all Circom 2.x language constructs:

**Lexer tokens:**
- Keywords: `pragma`, `circom`, `template`, `function`, `signal`, `var`, `component`, etc.
- Operators: arithmetic, bitwise, logical, assignment
- Constraint operators: `===`, `<==`, `==>`, `<--`, `-->`
- Literals: numbers (decimal, hex), strings
- Comments: line (`//`) and block (`/* */`)

**Parser rules:**
- `circuit` - Top-level rule
- `templateDefinition`, `functionDefinition` - Block definitions
- `signalDeclaration`, `varDeclaration` - Declarations
- `expression` - Expressions with operator precedence

### 2. Language Foundation

**File:** `src/main/kotlin/com/ohaddahan/circom/lang/CircomLanguage.kt`

Defines the language and file type:

```kotlin
object CircomLanguage : Language("Circom", "text/circom") {
    override fun isCaseSensitive() = true
}

object CircomFileType : LanguageFileType(CircomLanguage) {
    override fun getName() = "Circom file"
    override fun getDefaultExtension() = "circom"
    override fun getIcon() = CircomIcons.FILE_ICON
}
```

### 3. Token Types

**File:** `src/main/kotlin/com/ohaddahan/circom/lang/CircomTokenTypes.kt`

Categorizes tokens for syntax highlighting:

| TokenSet | Description | Tokens |
|----------|-------------|--------|
| `KEYWORDS` | Language keywords | `pragma`, `template`, `signal`, etc. |
| `MODIFIERS` | Template modifiers | `parallel`, `custom`, `bus` |
| `SIGNAL_TYPES` | Signal direction | `input`, `output` |
| `CONSTRAINTS_GENERATORS` | Constraint operators | `===`, `<==`, `==>`, `<--`, `-->` |
| `COMMENTS` | Comment tokens | `COMMENT`, `LINE_COMMENT` |
| `INTEGERS` | Number literals | `NUMBER` |
| `STRINGS` | String literals | `STRING` |

### 4. Syntax Highlighting

**File:** `src/main/kotlin/com/ohaddahan/circom/lang/CircomSyntaxHighlighter.kt`

Maps token types to text attributes:

```kotlin
override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
    return when {
        CircomTokenTypes.KEYWORDS.contains(tokenType) -> KEYWORD_KEYS
        CircomTokenTypes.MODIFIERS.contains(tokenType) -> MODIFIER_KEYS
        CircomTokenTypes.SIGNAL_TYPES.contains(tokenType) -> SIGNAL_TYPE_KEYS
        CircomTokenTypes.CONSTRAINTS_GENERATORS.contains(tokenType) -> CONSTRAINT_GEN_KEYS
        // ... more mappings
    }
}
```

**Custom colors** defined in `src/main/resources/colorSchemes/CircomDefault.xml`:

| Attribute | Color | Usage |
|-----------|-------|-------|
| `CIRCOM_SIGNAL_TYPE` | #879DE0 (blue-violet) | `input`, `output` |
| `CIRCOM_CONSTRAINT_GEN` | #4EC9B0 (teal) | `===`, `<==`, etc. |
| `CIRCOM_MODIFIER` | #BBB529 (yellow) | `parallel`, `custom`, `bus` |

### 5. ANTLR-IntelliJ Adapters

**Files:** `src/main/kotlin/com/ohaddahan/circom/adaptors/`

These classes bridge ANTLR4 to IntelliJ's infrastructure:

- **CircomLexerAdaptor** - Wraps ANTLR lexer for IntelliJ
- **CircomLexerState** - Maintains lexer state for incremental parsing
- **CircomGrammarParser** - Wraps ANTLR parser for PSI tree generation

### 6. Bracket Matching

**File:** `src/main/kotlin/com/ohaddahan/circom/ide/CircomBraceMatcher.kt`

Defines matching bracket pairs:

```kotlin
BRACE_PAIRS = arrayOf(
    BracePair(LP, RP, false),   // ()
    BracePair(LB, RB, false),   // []
    BracePair(LC, RC, true)     // {} (structural)
)
```

### 7. Code Folding

**File:** `src/main/kotlin/com/ohaddahan/circom/ide/CircomFoldingBuilder.kt`

Foldable regions:
- Curly brace blocks `{ ... }` (templates, functions, if/else, loops)
- Multi-line block comments `/* ... */`

Implementation walks the PSI tree finding `LC` (left curly) tokens and their matching `RC` (right curly) tokens.

### 8. Line Commenting

**File:** `src/main/kotlin/com/ohaddahan/circom/ide/CircomCommenter.kt`

```kotlin
override fun getLineCommentPrefix(): String = "//"
override fun getBlockCommentPrefix(): String = "/*"
override fun getBlockCommentSuffix(): String = "*/"
```

### 9. Plugin Configuration

**File:** `src/main/resources/META-INF/plugin.xml`

Registers all extension points:

```xml
<extensions defaultExtensionNs="com.intellij">
    <fileType name="Circom file" language="Circom"
              implementationClass="...CircomFileType" extensions="circom"/>
    <lang.syntaxHighlighterFactory language="Circom"
              implementationClass="...CircomSyntaxHighlighterFactory"/>
    <lang.braceMatcher language="Circom"
              implementationClass="...CircomBraceMatcher"/>
    <lang.foldingBuilder language="Circom"
              implementationClass="...CircomFoldingBuilder"/>
    <lang.commenter language="Circom"
              implementationClass="...CircomCommenter"/>
    <additionalTextAttributes scheme="Default" file="colorSchemes/CircomDefault.xml"/>
</extensions>
```

## Build System

### Gradle Configuration

**Key files:**
- `build.gradle.kts` - Build configuration with ANTLR plugin
- `gradle.properties` - Plugin metadata and version constraints
- `gradle/libs.versions.toml` - Dependency versions

**ANTLR integration:**
```kotlin
tasks {
    generateGrammarSource {
        arguments = arguments + listOf("-visitor", "-package", "com.ohaddahan.circom.parser")
        outputDirectory = file("${layout.buildDirectory.get()}/generated-src/antlr/main/...")
    }
    compileKotlin { dependsOn(generateGrammarSource) }
}
```

### Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| ANTLR4 | 4.13.1 | Grammar processing |
| antlr4-intellij-adaptor | 0.1 | ANTLR-IntelliJ bridge |
| IntelliJ Platform | 2024.3 | IDE integration |
| Kotlin | 2.2.21 | Implementation language |

## Adding New Features

### Adding a New Keyword

1. Add token to `CircomLexer.g4`:
   ```antlr
   NEW_KEYWORD: 'newkeyword' ;
   ```

2. Add to appropriate TokenSet in `CircomTokenTypes.kt`:
   ```kotlin
   val KEYWORDS: TokenSet = PSIElementTypeFactory.createTokenSet(
       CircomLanguage,
       CircomLexer.NEW_KEYWORD,
       // ...
   )
   ```

3. Rebuild: `./gradlew clean buildPlugin`

### Adding a New Color Category

1. Add TextAttributesKey in `CircomSyntaxHighlighter.kt`:
   ```kotlin
   val NEW_CATEGORY: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
       "CIRCOM_NEW_CATEGORY", DefaultLanguageHighlighterColors.SOME_DEFAULT
   )
   ```

2. Add color to `CircomDefault.xml`:
   ```xml
   <option name="CIRCOM_NEW_CATEGORY">
     <value><option name="FOREGROUND" value="RRGGBB"/></value>
   </option>
   ```

3. Map tokens in `getTokenHighlights()`:
   ```kotlin
   CircomTokenTypes.NEW_TOKENS.contains(tokenType) -> arrayOf(NEW_CATEGORY)
   ```

## Testing

### Manual Testing

```bash
./gradlew runIde
```

This launches a sandbox IDE with the plugin installed. Create a `.circom` file to test:

```circom
pragma circom 2.0.0;

template Multiplier(n) {
    signal input a;
    signal input b;
    signal output c;

    c <== a * b;
}

component main = Multiplier(2);
```

### Automated Tests

Test files are in `src/test/`. Run with:

```bash
./gradlew test
```

## Troubleshooting

### ANTLR Generation Issues

If grammar changes aren't reflected:
```bash
./gradlew clean generateGrammarSource
```

### Class Not Found Errors

Ensure ANTLR sources are generated before compilation:
```bash
./gradlew generateGrammarSource compileKotlin
```

### Plugin Not Loading

Check `build/idea-sandbox/system/log/idea.log` for errors.

## Resources

- [IntelliJ Platform SDK Documentation](https://plugins.jetbrains.com/docs/intellij/)
- [ANTLR4 Documentation](https://www.antlr.org/documentation.html)
- [Circom Documentation](https://docs.circom.io/)
