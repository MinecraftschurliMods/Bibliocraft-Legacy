package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface BCRegistries {
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BibliocraftApi.MOD_ID);
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(BibliocraftApi.MOD_ID);
    DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, BibliocraftApi.MOD_ID);
    DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, BibliocraftApi.MOD_ID);
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BibliocraftApi.MOD_ID);
    DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BibliocraftApi.MOD_ID);
    DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, BibliocraftApi.MOD_ID);
    DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BibliocraftApi.MOD_ID);

    /**
     * Central registration method. Classloads the registration classes and registers the registries to the mod bus.
     */
    static void init(IEventBus bus) {
        BCBlocks.init();
        BCItems.init();
        BCCreativeTabs.init();
        BCDataComponents.init();
        BCBlockEntities.init();
        BCEntities.init();
        BCMenus.init();
        BCSoundEvents.init();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_TABS.register(bus);
        DATA_COMPONENTS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ENTITIES.register(bus);
        MENUS.register(bus);
        SOUND_EVENTS.register(bus);
    }
}
