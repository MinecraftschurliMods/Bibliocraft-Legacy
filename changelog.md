# Fixes

- Fix non-vanilla wood type loot tables causing log spam if the corresponding mod isn't present

# API Changes

- Adds new BlockLootTableProvider class
- Changed the signatures of BibliocraftDatagenHelper#generateLootTablesFor and BibliocraftDatagenHelper#generateLootTables to use that new BlockLootTableProvider
