package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface BCRegistries {
    DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Bibliocraft.MOD_ID);
    DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Bibliocraft.MOD_ID);

    static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BCBlocks.init();
        BCItems.init();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
