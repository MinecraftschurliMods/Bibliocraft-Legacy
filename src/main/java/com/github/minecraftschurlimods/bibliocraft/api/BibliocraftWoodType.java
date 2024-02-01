package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class BibliocraftWoodType {
    private static List<BibliocraftWoodType> values = new ArrayList<>();
    private static boolean stillRegistering = true;
    public final ResourceLocation id;
    public final WoodType woodType;
    // Providing block suppliers themselves doesn't work (it's too early), but properties will be enough.
    public final Supplier<BlockBehaviour.Properties> properties;
    public final ResourceLocation texture;
    public final BlockFamily family;

    // Add vanilla variants.
    static {
        registerVanilla("oak", WoodType.OAK, Blocks.OAK_PLANKS, BlockFamilies.OAK_PLANKS);
        registerVanilla("spruce", WoodType.SPRUCE, Blocks.SPRUCE_PLANKS, BlockFamilies.SPRUCE_PLANKS);
        registerVanilla("birch", WoodType.BIRCH, Blocks.BIRCH_PLANKS, BlockFamilies.BIRCH_PLANKS);
        registerVanilla("jungle", WoodType.JUNGLE, Blocks.JUNGLE_PLANKS, BlockFamilies.JUNGLE_PLANKS);
        registerVanilla("acacia", WoodType.ACACIA, Blocks.ACACIA_PLANKS, BlockFamilies.ACACIA_PLANKS);
        registerVanilla("dark_oak", WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS, BlockFamilies.DARK_OAK_PLANKS);
        registerVanilla("crimson", WoodType.CRIMSON, Blocks.CRIMSON_PLANKS, BlockFamilies.CRIMSON_PLANKS);
        registerVanilla("warped", WoodType.WARPED, Blocks.WARPED_PLANKS, BlockFamilies.WARPED_PLANKS);
        registerVanilla("mangrove", WoodType.MANGROVE, Blocks.MANGROVE_PLANKS, BlockFamilies.MANGROVE_PLANKS);
        registerVanilla("bamboo", WoodType.BAMBOO, Blocks.BAMBOO_PLANKS, BlockFamilies.BAMBOO_PLANKS);
        registerVanilla("cherry", WoodType.CHERRY, Blocks.CHERRY_PLANKS, BlockFamilies.CHERRY_PLANKS);
    }

    /**
     * Private constructor. Call {@link BibliocraftWoodType#register(ResourceLocation, WoodType, Supplier, ResourceLocation, BlockFamily)} instead.
     */
    private BibliocraftWoodType(ResourceLocation id, WoodType woodType, Supplier<BlockBehaviour.Properties> properties, ResourceLocation texture, BlockFamily family) {
        this.id = id;
        this.woodType = woodType;
        this.properties = properties;
        this.texture = texture;
        this.family = family;
    }

    /**
     * @return An unmodifiable list of all registered wood types. Must only be called after registration is finished.
     */
    public static List<BibliocraftWoodType> getAll() {
        if (stillRegistering)
            throw new IllegalStateException("Tried to access BibliocraftWoodType#getAll() while registration was still in progress!");
        return values;
    }

    /**
     * @return The wood type prefix used for registration. Keeps the mod id for cases when two mods add identically named wood types.
     */
    public String getRegistrationPrefix() {
        return id.getNamespace().equals("minecraft") ? id.getPath() : id.toString().replace(':', '_');
    }

    /**
     * @param id The id of the wood type to get.
     * @return The wood type with the given id. May return null if no wood type with the given id exists.
     */
    @Nullable
    public static BibliocraftWoodType get(ResourceLocation id) {
        if (stillRegistering)
            throw new IllegalStateException("Tried to access BibliocraftWoodType#get() while registration was still in progress!");
        return values.stream().filter(e -> e.id.equals(id)).findFirst().orElse(null);
    }

    /**
     * @param id The id of the wood type to get.
     * @return The wood type with the given id. May return null if no wood type with the given id exists.
     */
    @Nullable
    public static BibliocraftWoodType get(String id) {
        return get(new ResourceLocation(id));
    }

    /**
     * Registers a new wood type. Must be called during the mod constructor.
     *
     * @param id         The id of the wood type. Should be the id of the mod the wood type comes from, and the name of the wood type.
     * @param woodType   The vanilla {@link WoodType} associated with this wood type.
     * @param properties A supplier for the {@link BlockBehaviour.Properties} associated with this wood type. Typically, this is a copy of the wood type's planks' properties.
     * @param texture    The location of the wood type's planks texture, for use in datagen.
     * @param family     The {@link BlockFamily} for the associated wood type, for use in datagen.
     */
    public static synchronized void register(ResourceLocation id, WoodType woodType, Supplier<BlockBehaviour.Properties> properties, ResourceLocation texture, BlockFamily family) {
        values.add(new BibliocraftWoodType(id, woodType, properties, texture, family));
    }

    /**
     * After everything is registered, sorts the registered wood types based on the mod id, beginning with minecraft and then sorting alphabetically.
     * Within one and the same mod, the order is kept. So for example, if a mod registers "somewood" and "anywood" (in that order), "somewood" will remain first.
     * DO NOT CALL THIS YOURSELF! This is called by Bibliocraft after all wood types are registered.
     */
    @ApiStatus.Internal
    public static void postRegister() {
        values = values.stream().sorted((a, b) -> {
            String namespaceA = a.id.getNamespace();
            String namespaceB = b.id.getNamespace();
            if (namespaceA.equals("minecraft")) return 1;
            if (namespaceB.equals("minecraft")) return -1;
            return namespaceA.compareTo(namespaceB);
        }).toList();
        stillRegistering = false;
    }

    /**
     * Private helper for registering the vanilla variants.
     */
    private static void registerVanilla(String name, WoodType woodType, Block planks, BlockFamily family) {
        register(new ResourceLocation(name), woodType, () -> BlockBehaviour.Properties.ofFullCopy(planks), new ResourceLocation("block/" + name + "_planks"), family);
    }
}
