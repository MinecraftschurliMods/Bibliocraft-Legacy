package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.api.RegisterBibliocraftWoodTypesEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public final class BibliocraftWoodTypeRegistryImpl implements BibliocraftWoodTypeRegistry {
    private final SortedMap<ResourceLocation, BibliocraftWoodType> values;
    private boolean loaded = false;

    @ApiStatus.Internal
    public BibliocraftWoodTypeRegistryImpl() {
        this.values = new TreeMap<>(BibliocraftWoodTypeRegistryImpl::compareRLMinecraftFirst);
    }

    @ApiStatus.Internal
    public void register() {
        ModLoader.get().postEvent(new RegisterBibliocraftWoodTypesEvent(this.values));
        this.loaded = true;
    }

    @Override
    @Nullable
    public BibliocraftWoodType get(ResourceLocation id) {
        if (!this.loaded) throw new IllegalStateException("Tried to access BibliocraftWoodType#get() before registration was done!");
        return this.values.get(id);
    }

    @Override
    public Collection<BibliocraftWoodType> getAll() {
        if (!this.loaded) throw new IllegalStateException("Tried to access BibliocraftWoodType#getAll() before registration was done!");
        return this.values.values();
    }

    private static int compareRLMinecraftFirst(ResourceLocation a, ResourceLocation b) {
        if (a.equals(b)) return 0;
        int i = compareNamespace(a.getNamespace(), b.getNamespace());
        return i != 0 ? i : a.getPath().compareTo(b.getPath());
    }

    private static int compareNamespace(String namespaceA, String namespaceB) {
        int i = namespaceA.compareTo(namespaceB);
        if (i == 0) return 0;
        if (namespaceA.equals("minecraft")) return 1;
        if (namespaceB.equals("minecraft")) return -1;
        return i;
    }
}
