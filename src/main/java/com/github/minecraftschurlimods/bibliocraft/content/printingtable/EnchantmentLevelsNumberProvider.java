package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.Map;
import java.util.Optional;

public record EnchantmentLevelsNumberProvider(DataComponentType<?> dataComponent, NumberProvider normalCost, Optional<NumberProvider> treasureCost, Map<Holder<Enchantment>, Either<NumberProvider, LevelBasedValue>> overrides) implements NumberProvider {
    public static final MapCodec<EnchantmentLevelsNumberProvider> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            DataComponentType.CODEC.fieldOf("data_component").forGetter(EnchantmentLevelsNumberProvider::dataComponent),
            NumberProviders.CODEC.fieldOf("normal_cost").forGetter(EnchantmentLevelsNumberProvider::normalCost),
            NumberProviders.CODEC.optionalFieldOf("treasure_cost").forGetter(EnchantmentLevelsNumberProvider::treasureCost),
            Codec.unboundedMap(Enchantment.CODEC, Codec.either(NumberProviders.CODEC, LevelBasedValue.CODEC)).optionalFieldOf("overrides", Map.of()).forGetter(EnchantmentLevelsNumberProvider::overrides)
    ).apply(inst, EnchantmentLevelsNumberProvider::new));

    public EnchantmentLevelsNumberProvider(DataComponentType<?> dataComponent, NumberProvider normalCost, NumberProvider treasureCost) {
        this(dataComponent, normalCost, Optional.of(treasureCost), Map.of());
    }

    @Override
    public float getFloat(LootContext context) {
        ItemStack stack = context.getParameter(LootContextParams.TOOL);
        if (stack.isEmpty() || !stack.has(dataComponent)) return 0;
        if (!(stack.get(dataComponent) instanceof ItemEnchantments enchantments)) return 0;
        float cost = 0;
        for (Holder<Enchantment> enchantment : enchantments.keySet()) {
            int level = enchantments.getLevel(enchantment);
            if (overrides.containsKey(enchantment)) {
                cost += Either.unwrap(overrides.get(enchantment).mapBoth(e -> e.getFloat(context) * level, e -> e.calculate(level)));
            } else if (treasureCost.isPresent() && enchantment.is(EnchantmentTags.TREASURE)) {
                cost += treasureCost.get().getFloat(context) * level;
            } else {
                cost += normalCost.getFloat(context) * level;
            }
        }
        return cost;
    }

    @Override
    public int getInt(LootContext lootContext) {
        float value = getFloat(lootContext);
        return value <= 0 ? 0 : value <= 1 ? 1 : (int) value;
    }

    @Override
    public LootNumberProviderType getType() {
        return BCRecipes.ENCHANTMENT_LEVELS_NUMBER_PROVIDER.get();
    }
}
