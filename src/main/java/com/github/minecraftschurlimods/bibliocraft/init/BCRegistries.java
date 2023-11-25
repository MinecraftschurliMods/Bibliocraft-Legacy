package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface BCRegistries {
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bibliocraft.MOD_ID);
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bibliocraft.MOD_ID);
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bibliocraft.MOD_ID);
    DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Bibliocraft.MOD_ID);

    static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BCBlocks.init();
        BCItems.init();
        BCBlockEntities.init();
        BCMenuTypes.init();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENU_TYPES.register(bus);
    }
}
