package com.github.minecraftschurlimods.bibliocraft.content.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SwordPedestalBlockEntity extends BCBlockEntity {
    private static final int TICK_INTERVAL = 20;
    private static final int RANGE = 2;
    private static final String COLOR_KEY = "color";
    private DyedItemColor color = SwordPedestalBlock.DEFAULT_COLOR;

    public SwordPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SWORD_PEDESTAL.get(), 1, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SwordPedestalBlockEntity blockEntity) {
        if (level.isClientSide()) return;
        if (level.getGameTime() % TICK_INTERVAL != 0) return;
        ItemStack stack = blockEntity.getItem(0).copy();
        if (!stack.isDamaged()) return;
        List<Holder<Enchantment>> list = stack.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT))
            .keySet()
            .stream()
            .filter(holder -> holder.value().effects().has(EnchantmentEffectComponents.REPAIR_WITH_XP))
            .toList();
        if (list.isEmpty()) return;
        Vec3 vec = pos.getCenter();
        for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class, new AABB(vec.add(-RANGE, -RANGE, -RANGE), vec.add(RANGE, RANGE, RANGE)))) {
            int i = blockEntity.repairItem((ServerLevel) level, stack, orb.getValue());
            orb.discard();
            if (!stack.isDamaged()) break;
        }
        blockEntity.setItem(0, stack);
    }

    public DyedItemColor getColor() {
        return color;
    }

    public void setColor(DyedItemColor color) {
        this.color = color;
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.SWORD_PEDESTAL_SWORDS);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(COLOR_KEY)) {
            setColor(CodecUtil.decodeNbt(DyedItemColor.CODEC, tag.get(COLOR_KEY)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(COLOR_KEY, CodecUtil.encodeNbt(DyedItemColor.CODEC, getColor()));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        setColor(componentInput.getOrDefault(DataComponents.DYED_COLOR, SwordPedestalBlock.DEFAULT_COLOR));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (!color.equals(SwordPedestalBlock.DEFAULT_COLOR)) {
            components.set(DataComponents.DYED_COLOR, color);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(COLOR_KEY);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (!color.equals(SwordPedestalBlock.DEFAULT_COLOR)) {
            tag.put(COLOR_KEY, CodecUtil.encodeNbt(DyedItemColor.CODEC, getColor()));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        if (tag.contains(COLOR_KEY)) {
            setColor(CodecUtil.decodeNbt(DyedItemColor.CODEC, tag.get(COLOR_KEY)));
        }
    }

    private int repairItem(ServerLevel level, ItemStack stack, int value) {
        int i = EnchantmentHelper.modifyDurabilityToRepairFromXp(level, stack, (int) (value * stack.getXpRepairRatio()));
        int j = Math.min(i, stack.getDamageValue());
        stack.setDamageValue(stack.getDamageValue() - j);
        if (j > 0) {
            int k = value - j * value / i;
            if (k > 0) {
                return repairItem(level, stack, k);
            }
        }
        return 0;
    }
}
