package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.api.RegisterBibliocraftWoodTypesEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BibliocraftWoodTypeRegistryImpl implements BibliocraftWoodTypeRegistry {
    private final Map<ResourceLocation, BibliocraftWoodType> values;
    private final List<BibliocraftWoodType> sortedValues;
    private boolean loaded = false;

    @ApiStatus.Internal
    public BibliocraftWoodTypeRegistryImpl() {
        values = new HashMap<>();
        sortedValues = new ArrayList<>();
    }

    @ApiStatus.Internal
    public void register(IEventBus bus) {
        bus.post(new RegisterBibliocraftWoodTypesEvent(values, sortedValues));
        loaded = true;
    }

    @Override
    @Nullable
    public BibliocraftWoodType get(ResourceLocation id) {
        if (!loaded)
            throw new IllegalStateException("Tried to access BibliocraftWoodType#get() before registration was done!");
        return values.get(id);
    }

    @Override
    public Collection<BibliocraftWoodType> getAll() {
        if (!loaded)
            throw new IllegalStateException("Tried to access BibliocraftWoodType#getAll() before registration was done!");
        return Collections.unmodifiableList(sortedValues);
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
