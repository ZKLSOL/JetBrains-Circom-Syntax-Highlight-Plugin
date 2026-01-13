# TODO

## Implementation Status

### 1. Project Setup ✅
- [x] Initialize Gradle project with IntelliJ Platform Plugin
- [x] Configure gradle.properties
- [x] Configure build.gradle.kts
- [x] Configure settings.gradle.kts
- [x] Set up ANTLR integration

### 2. Grammar ✅
- [x] Copy CircomLexer.g4 from reference
- [x] Copy CircomParser.g4 from reference
- [x] Update package names to `com.ohaddahan.circom`

### 3. Language Foundation ✅
- [x] CircomLanguage.kt - Language definition
- [x] CircomFileType.kt - File type registration
- [x] CircomFileRoot.kt - PSI root element

### 4. Lexer Adaptors ✅
- [x] CircomLexerAdaptor.kt - ANTLR to IntelliJ bridge
- [x] CircomLexerState.kt - Lexer state management
- [x] CircomGrammarParser.kt - Parser adaptor

### 5. Syntax Highlighting ✅
- [x] CircomTokenTypes.kt - Token categorization (with MODIFIER category)
- [x] CircomSyntaxHighlighter.kt - Color mappings
- [x] CircomSyntaxHighlighterFactory.kt - Factory registration
- [x] CircomDefault.xml - Custom color scheme

### 6. IDE Features ✅
- [x] CircomBraceMatcher.kt - Bracket matching for (), [], {}
- [x] CircomFoldingBuilder.kt - Code folding regions
- [x] CircomCommenter.kt - Line comment support
- [x] CircomIcons.kt - File icons

### 7. Resources ✅
- [x] Copy circom-file.png from reference
- [x] Copy circom-file@2x.png from reference
- [x] Create plugin.xml with all extensions
- [x] Create CHANGELOG.md
- [x] Update README.md with full documentation
- [x] Create DEV.md with implementation details

### 8. Build ✅
- [x] Run `./gradlew buildPlugin`
- [x] Plugin ZIP created successfully

### 9. Testing - Phase 1 ✅
- [x] Test in sandbox IDE (`./gradlew runIde`)
- [x] Verify syntax highlighting

### 10. Navigation Features ✅

#### 10.1 PSI Infrastructure ✅
- [x] Create `CircomElementTypes.kt` - Define all PSI element types
- [x] Create `CircomParserDefinition.kt` - Parser definition for PSI tree
- [x] Create `CircomPsiFile.kt` - Enhanced file PSI with template/function accessors

#### 10.2 PSI Element Classes ✅
- [x] `CircomNamedElement` - Base interface for named elements
- [x] `CircomNamedElementImpl` - Base implementation with rename support
- [x] `CircomTemplateDefinitionImpl` - Template declarations
- [x] `CircomFunctionDefinitionImpl` - Function declarations
- [x] `CircomSignalDeclarationImpl` - Signal declarations (input/output)
- [x] `CircomVariableDeclarationImpl` - Variable declarations
- [x] `CircomComponentDeclarationImpl` - Component declarations
- [x] `CircomIncludeStatementImpl` - Include statements
- [x] `CircomElementFactory` - Factory for creating PSI elements

#### 10.3 Name Index (Cross-file Navigation) ✅
- [x] `CircomNameIndex` - Project-wide symbol lookup using FileTypeIndex

#### 10.4 Reference System ✅
- [x] `CircomReference` - Reference implementation with local/project scope resolution
- [x] `CircomReferenceContributor` - Register reference providers for ID tokens

#### 10.5 Navigation Providers ✅
- [x] `CircomFindUsagesProvider` - Find usages support with WordsScanner
- [x] `CircomChooseByNameContributor` - Go to Symbol (Cmd+O)
- [x] `CircomStructureViewFactory` - Structure view panel

#### 10.6 Refactoring ✅
- [x] `CircomRefactoringSupportProvider` - Enable in-place rename refactoring
- [x] `CircomNamesValidator` - Validate Circom identifier names

#### 10.7 Update plugin.xml ✅
- [x] Register ParserDefinition
- [x] Register FindUsagesProvider
- [x] Register GotoSymbolContributor
- [x] Register StructureViewFactory
- [x] Register ReferenceContributor
- [x] Register RefactoringSupportProvider
- [x] Register NamesValidator

### 11. Testing - Phase 2 ✅

#### All Features Working:
- [x] Structure View (Cmd+7) - Shows templates, functions, signals, variables, components
- [x] Go to Definition (Cmd+Click) - Navigates to template/function definitions
- [x] Find Usages (Alt+F7) - Finds all usages of templates, functions, signals, variables
- [x] Cross-file navigation - Works within project scope
- [x] Rename (Shift+F6) - Works with references

#### Solution Applied:
Implemented `getReference()` directly on PSI element classes instead of using `PsiReferenceContributor`:
- `CircomIdentifierStatementImpl` - Handles variable/signal references
- `CircomCallExpressionImpl` - Handles function/template calls
- `CircomComponentMainDeclarationImpl` - Handles `component main = Template()` declarations

Fixed `CircomFindUsagesProvider` to use `IDENTIFIERS` token set instead of `KEYWORDS`.

---

### 12. Marketplace Submission (Pending)
- [ ] Update version to 0.2.0
- [ ] Update CHANGELOG.md
- [ ] Sign plugin
- [ ] Submit to JetBrains Marketplace
- [ ] Await approval

---

## Quick Reference

### Build Commands

```bash
# Build plugin
./gradlew buildPlugin

# Test in sandbox IDE
./gradlew runIde

# Clean and rebuild
./gradlew clean buildPlugin
```

### Output

Plugin ZIP: `build/distributions/circom-language-support-0.2.0.zip`

### Manual Installation

1. Open any JetBrains IDE
2. Settings > Plugins > ⚙️ > Install Plugin from Disk...
3. Select the ZIP file

### Cleanup (Optional)

Remove unused template files:
```bash
rm -rf src/main/kotlin/com/github
rm -rf src/test/kotlin
rm src/main/resources/messages/MyBundle.properties
```

---

## Documentation

- [README.md](README.md) - User documentation
- [DEV.md](DEV.md) - Implementation details
- [CHANGELOG.md](CHANGELOG.md) - Version history
- [PLAN.md](PLAN.md) - Original specification
