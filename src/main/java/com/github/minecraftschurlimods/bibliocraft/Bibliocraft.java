package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Bibliocraft.MOD_ID)
public final class Bibliocraft {
    public static final String MOD_ID = "bibliocraft";

    public Bibliocraft(IEventBus modBus) {
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
     * Private helper for registering the vanilla variants.
     */
    private static void registerVanilla(String name, WoodType woodType, Block planks, BlockFamily family) {
        BibliocraftWoodTypeRegistry.get().register(new ResourceLocation(name), woodType, () -> BlockBehaviour.Properties.ofFullCopy(planks), new ResourceLocation("block/" + name + "_planks"), family);
    }
}
