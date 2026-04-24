package at.minecraftschurli.mods.bibliocraft.datagen.data;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class BCEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public BCEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BibliocraftApi.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BCTags.Enchantments.PRINTING_TABLE_CLONING_BLACKLIST);
    }
}
