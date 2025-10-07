package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface BCRegistries {
    // @formatter:off
    DeferredRegister.Blocks                  BLOCKS             = DeferredRegister.createBlocks(BibliocraftApi.MOD_ID);
    DeferredRegister.Items                   ITEMS              = DeferredRegister.createItems(BibliocraftApi.MOD_ID);
    DeferredRegister.Entities                ENTITIES           = DeferredRegister.createEntities(BibliocraftApi.MOD_ID);
    DeferredRegister<DataComponentType<?>>   DATA_COMPONENTS    = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BibliocraftApi.MOD_ID);
    DeferredRegister<CreativeModeTab>        CREATIVE_TABS      = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB,          BibliocraftApi.MOD_ID);
    DeferredRegister<BlockEntityType<?>>     BLOCK_ENTITIES     = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE,          BibliocraftApi.MOD_ID);
    DeferredRegister<MenuType<?>>            MENUS              = DeferredRegister.create(BuiltInRegistries.MENU,                       BibliocraftApi.MOD_ID);
    DeferredRegister<LootNumberProviderType> NUMBER_PROVIDERS   = DeferredRegister.create(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE,  BibliocraftApi.MOD_ID);
    DeferredRegister<RecipeType<?>>          RECIPE_TYPES       = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE,                BibliocraftApi.MOD_ID);
    DeferredRegister<RecipeSerializer<?>>    RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER,          BibliocraftApi.MOD_ID);
    DeferredRegister<SoundEvent>             SOUND_EVENTS       = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT,                BibliocraftApi.MOD_ID);
    // @formatter:on

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
        BCRecipes.init();
        BCSoundEvents.init();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_TABS.register(bus);
        DATA_COMPONENTS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ENTITIES.register(bus);
        MENUS.register(bus);
        NUMBER_PROVIDERS.register(bus);
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        SOUND_EVENTS.register(bus);
    }
}
