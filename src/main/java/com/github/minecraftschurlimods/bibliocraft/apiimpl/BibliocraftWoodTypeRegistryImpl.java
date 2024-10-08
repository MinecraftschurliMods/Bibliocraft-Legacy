package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.api.RegisterBibliocraftWoodTypesEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.SequencedMap;

public final class BibliocraftWoodTypeRegistryImpl implements BibliocraftWoodTypeRegistry {
    private final SequencedMap<ResourceLocation, BibliocraftWoodType> values;
    private boolean loaded = false;

    @ApiStatus.Internal
    public BibliocraftWoodTypeRegistryImpl() {
        values = new LinkedHashMap<>();
    }

    @ApiStatus.Internal
    public void register() {
        SequencedMap<ResourceLocation, BibliocraftWoodType> registrar = new LinkedHashMap<>();
        ModLoader.postEvent(new RegisterBibliocraftWoodTypesEvent(registrar));
        registrar.sequencedEntrySet()
                .stream()
                .sorted((a, b) -> compareRLMinecraftFirst(a.getKey(), b.getKey()))
                .forEach(e -> values.put(e.getKey(), e.getValue()));
        loaded = true;
    }

    @Override
    @Nullable
    public BibliocraftWoodType get(ResourceLocation id) {
        if (!loaded)
            throw new IllegalStateException("Tried to access BibliocraftWoodTypeRegistry#get() before registration was done!");
        return values.get(id);
    }

    @Override
    public Collection<BibliocraftWoodType> getAll() {
        if (!loaded)
            throw new IllegalStateException("Tried to access BibliocraftWoodTypeRegistry#getAll() before registration was done!");
        return values.sequencedValues();
    }

    private static int compareRLMinecraftFirst(ResourceLocation a, ResourceLocation b) {
        if (a.equals(b)) return 0;
        String namespaceA = a.getNamespace(), namespaceB = b.getNamespace();
        if (namespaceA.equals("minecraft")) return 1;
        if (namespaceB.equals("minecraft")) return -1;
        int i = namespaceA.compareTo(namespaceB);
        return i == 0 ? 1 : i;
    }
}
