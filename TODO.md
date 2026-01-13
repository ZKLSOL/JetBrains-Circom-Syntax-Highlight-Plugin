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

### 9. Testing (Pending)
- [ ] Test in sandbox IDE (`./gradlew runIde`)
- [ ] Verify syntax highlighting
- [ ] Verify bracket matching
- [ ] Verify code folding
- [ ] Verify line commenting

### 10. Marketplace Submission (Pending)
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

Plugin ZIP: `build/distributions/circom-language-support-0.1.0.zip`

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
