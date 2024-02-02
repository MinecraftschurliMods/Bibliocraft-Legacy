package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BibliocraftWoodTypeRegistryImpl implements BibliocraftWoodTypeRegistry {
    private static final BibliocraftWoodTypeRegistryImpl INSTANCE = new BibliocraftWoodTypeRegistryImpl();
    private List<BibliocraftWoodType> values = new ArrayList<>();
    private boolean stillRegistering = true;

    private BibliocraftWoodTypeRegistryImpl() {}

    /**
     * @return The only instance of this class.
     */
    public static BibliocraftWoodTypeRegistryImpl get() {
        return INSTANCE;
    }

    @Override
    public synchronized void register(ResourceLocation id, WoodType woodType, Supplier<BlockBehaviour.Properties> properties, ResourceLocation texture, BlockFamily family) {
        values.add(new BibliocraftWoodTypeImpl(id, woodType, properties, texture, family));
    }

    @Override
    @Nullable
    public BibliocraftWoodType get(ResourceLocation id) {
        if (stillRegistering)
            throw new IllegalStateException("Tried to access BibliocraftWoodType#get() while registration was still in progress!");
        return values.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<BibliocraftWoodType> getAll() {
        if (stillRegistering)
            throw new IllegalStateException("Tried to access BibliocraftWoodType#getAll() while registration was still in progress!");
        return values;
    }

    /**
     * After everything is registered, sorts the registered wood types based on the mod id, beginning with minecraft and then sorting alphabetically.
     * Within one and the same mod, the order is kept. So for example, if a mod registers "somewood" and "anywood" (in that order), "somewood" will remain first.
     */
    public void postRegister() {
        values = values.stream().sorted((a, b) -> {
            String namespaceA = a.getNamespace();
            String namespaceB = b.getNamespace();
            if (namespaceA.equals("minecraft")) return 1;
            if (namespaceB.equals("minecraft")) return -1;
            return namespaceA.compareTo(namespaceB);
        }).toList();
        stillRegistering = false;
    }
}
