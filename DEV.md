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
│   ├── psi/                        # PSI element classes
│   │   ├── CircomElementTypes.kt   # Element type definitions
│   │   ├── CircomParserDefinition.kt
│   │   ├── CircomPsiFile.kt        # Enhanced file PSI
│   │   ├── CircomNamedElement.kt   # Named element interface
│   │   └── impl/                   # PSI implementations
│   │       ├── CircomNamedElementImpl.kt
│   │       ├── CircomTemplateDefinitionImpl.kt
│   │       ├── CircomFunctionDefinitionImpl.kt
│   │       ├── CircomSignalDeclarationImpl.kt
│   │       ├── CircomVariableDeclarationImpl.kt
│   │       ├── CircomComponentDeclarationImpl.kt
│   │       ├── CircomIncludeStatementImpl.kt
│   │       └── CircomElementFactory.kt
│   ├── stubs/                      # Stub indices
│   │   └── CircomNameIndex.kt      # Project-wide symbol lookup
│   ├── reference/                  # Reference system
│   │   ├── CircomReference.kt      # Reference resolution
│   │   └── CircomReferenceContributor.kt
│   ├── refactoring/                # Refactoring support
│   │   ├── CircomRefactoringSupportProvider.kt
│   │   └── CircomNamesValidator.kt
│   └── ide/                        # IDE features
│       ├── CircomIcons.kt          # File icons
│       ├── CircomBraceMatcher.kt   # Bracket matching
│       ├── CircomFoldingBuilder.kt # Code folding
│       ├── CircomCommenter.kt      # Line comments
│       ├── CircomFindUsagesProvider.kt
│       ├── CircomChooseByNameContributor.kt
│       └── CircomStructureViewFactory.kt
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

## Navigation Features

### 10. PSI Element Types

**File:** `src/main/kotlin/com/ohaddahan/circom/psi/CircomElementTypes.kt`

Maps ANTLR parser rules to IntelliJ element types:

```kotlin
object CircomElementTypes {
    val FILE = IFileElementType(CircomLanguage)
    val TEMPLATE_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_templateDefinition]
    val FUNCTION_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_functionDefinition]
    val SIGNAL_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_signalDeclaration]
    val VARIABLE_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_varDeclaration]
    val COMPONENT_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_componentDeclaration]
    val INCLUDE_STATEMENT: IElementType = ruleTypes[CircomParser.RULE_includeStatement]
}
```

### 11. Parser Definition

**File:** `src/main/kotlin/com/ohaddahan/circom/psi/CircomParserDefinition.kt`

Creates PSI elements from AST nodes:

```kotlin
class CircomParserDefinition : ParserDefinition {
    override fun createElement(node: ASTNode): PsiElement {
        return when (node.elementType) {
            CircomElementTypes.TEMPLATE_DEFINITION -> CircomTemplateDefinitionImpl(node)
            CircomElementTypes.FUNCTION_DEFINITION -> CircomFunctionDefinitionImpl(node)
            CircomElementTypes.SIGNAL_DECLARATION -> CircomSignalDeclarationImpl(node)
            CircomElementTypes.VARIABLE_DECLARATION -> CircomVariableDeclarationImpl(node)
            CircomElementTypes.COMPONENT_DECLARATION -> CircomComponentDeclarationImpl(node)
            CircomElementTypes.INCLUDE_STATEMENT -> CircomIncludeStatementImpl(node)
            else -> ANTLRPsiNode(node)
        }
    }
}
```

### 12. Named Elements

**Interface:** `CircomNamedElement`

```kotlin
interface CircomNamedElement : PsiNameIdentifierOwner, NavigatablePsiElement
```

**Base Implementation:** `CircomNamedElementImpl`

All named elements (templates, functions, signals, variables, components) extend this base class which provides:
- `getName()` / `setName()` - Name manipulation for refactoring
- `getNameIdentifier()` - Returns the ID token for navigation

### 13. Reference System

**File:** `src/main/kotlin/com/ohaddahan/circom/reference/CircomReference.kt`

Resolves identifier references to their declarations:

```kotlin
class CircomReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>, PsiPolyVariantReference {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        // 1. Search local scope (same file - templates, functions)
        // 2. Search containing block (signals, variables, components)
        // 3. Fall back to project-wide index search
    }
}
```

**Resolution order:**
1. Local file templates and functions
2. Local declarations in containing template/function
3. Project-wide index lookup

### 14. Project-Wide Symbol Index

**File:** `src/main/kotlin/com/ohaddahan/circom/stubs/CircomNameIndex.kt`

Uses `FileTypeIndex` to find all Circom files and extract symbols:

```kotlin
object CircomNameIndex {
    fun findAllTemplates(project: Project): List<CircomTemplateDefinitionImpl>
    fun findAllFunctions(project: Project): List<CircomFunctionDefinitionImpl>
    fun findTemplates(project: Project, name: String): List<CircomTemplateDefinitionImpl>
    fun findFunctions(project: Project, name: String): List<CircomFunctionDefinitionImpl>
}
```

### 15. Find Usages Provider

**File:** `src/main/kotlin/com/ohaddahan/circom/ide/CircomFindUsagesProvider.kt`

Enables Find Usages (Alt+F7):

```kotlin
class CircomFindUsagesProvider : FindUsagesProvider {
    override fun canFindUsagesFor(element: PsiElement) = element is CircomNamedElement

    override fun getType(element: PsiElement): String {
        return when (element) {
            is CircomTemplateDefinitionImpl -> "template"
            is CircomFunctionDefinitionImpl -> "function"
            is CircomSignalDeclarationImpl -> "signal"
            is CircomVariableDeclarationImpl -> "variable"
            is CircomComponentDeclarationImpl -> "component"
            else -> "element"
        }
    }
}
```

### 16. Go to Symbol

**File:** `src/main/kotlin/com/ohaddahan/circom/ide/CircomChooseByNameContributor.kt`

Enables Go to Symbol (Cmd+O / Ctrl+N):

```kotlin
class CircomChooseByNameContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        // Returns all template and function names in the project
    }
}
```

### 17. Structure View

**File:** `src/main/kotlin/com/ohaddahan/circom/ide/CircomStructureViewFactory.kt`

Displays file structure in the Structure panel:

```
File.circom
├── template TemplateA
│   ├── input signalA
│   ├── output signalB
│   └── component comp1
├── template TemplateB
│   └── var x
└── function helperFunc
    └── var result
```

### 18. Refactoring Support

**File:** `src/main/kotlin/com/ohaddahan/circom/refactoring/CircomRefactoringSupportProvider.kt`

Enables in-place rename refactoring (Shift+F6):

```kotlin
class CircomRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?) =
        element is CircomNamedElement
}
```

**File:** `src/main/kotlin/com/ohaddahan/circom/refactoring/CircomNamesValidator.kt`

Validates Circom identifiers:
- Must start with letter, `_`, or `$`
- Can contain letters, digits, `_`, or `$`
- Cannot be a keyword

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
