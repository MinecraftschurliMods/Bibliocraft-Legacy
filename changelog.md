# Fixes

- Fix double-tall blocks (fancy armor stands, grandfather clocks) not breaking correctly
- Fix some wooden blocks not breaking faster when using an axe
- Fix non-vanilla wood type loot tables causing log spam if the corresponding mod isn't present
- Optimize the table model (thanks to @embeddedt for this fix!)

# API Changes

- Adds new BlockLootTableProvider class
- Changed the signatures of BibliocraftDatagenHelper#generateLootTablesFor and BibliocraftDatagenHelper#generateLootTables to use that new BlockLootTableProvider
