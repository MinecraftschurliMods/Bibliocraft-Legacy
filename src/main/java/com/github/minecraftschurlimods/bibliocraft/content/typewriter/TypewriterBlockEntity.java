package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class TypewriterBlockEntity extends BCBlockEntity implements WorldlyContainer {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private static final int[] INPUTS = new int[]{INPUT};
    private static final int[] OUTPUTS = new int[]{OUTPUT};
    private static final String PAGE_KEY = "page";
    private final EnumMap<Direction, SidedInvWrapper> wrappers = Util.make(new EnumMap<>(Direction.class), map -> {
        for (Direction direction : Direction.values()) {
            map.put(direction, new SidedInvWrapper(this, direction));
        }
    });
    private TypewriterPage page = new TypewriterPage();

    public TypewriterBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TYPEWRITER.get(), 2, pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        page = CodecUtil.decodeNbt(TypewriterPage.CODEC, tag.getCompound(PAGE_KEY));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(PAGE_KEY, CodecUtil.encodeNbt(TypewriterPage.CODEC, page));
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == INPUT && stack.is(BCTags.Items.TYPEWRITER_PAPER);
    }

    @Override
    @Nullable
    public IItemHandler getItemCapability(@Nullable Direction side) {
        return side == null ? null : wrappers.get(side);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? OUTPUTS : INPUTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return direction != Direction.DOWN && canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == OUTPUT;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        if (slot == OUTPUT && getItem(OUTPUT).isEmpty()) {
            setPage(TypewriterPage.DEFAULT);
            setChanged();
        }
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = super.removeItem(slot, count);
        if (slot == OUTPUT && getItem(OUTPUT).isEmpty()) {
            setPage(TypewriterPage.DEFAULT);
            setChanged();
        }
        return stack;
    }

    public boolean insertPaper(ItemStack stack) {
        ItemStack input = getItem(INPUT);
        if (!input.isEmpty() && !ItemStack.isSameItemSameComponents(input, stack)) return false;
        if (input.isEmpty()) {
            setItem(INPUT, stack.copyWithCount(1));
        } else if (input.getCount() < input.getMaxStackSize()) {
            input.grow(1);
        } else return false;
        stack.shrink(1);
        setChanged();
        return true;
    }

    public ItemStack takeOutput() {
        ItemStack output = getItem(OUTPUT);
        setItem(OUTPUT, ItemStack.EMPTY);
        level().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TypewriterBlock.PAPER, 0));
        setChanged();
        return output;
    }

    public TypewriterPage getPage() {
        return page;
    }

    public void setPage(TypewriterPage page) {
        this.page = page;
        if (page.line() == TypewriterPage.MAX_LINES) {
            ItemStack output = new ItemStack(BCItems.TYPEWRITER_PAGE.get());
            output.set(BCDataComponents.TYPEWRITER_PAGE, page);
            setItem(OUTPUT, output);
            getItem(INPUT).shrink(1);
            this.page = TypewriterPage.DEFAULT;
        }
        BlockState state = getBlockState().setValue(TypewriterBlock.PAPER, page.line() / 2);
        if (getBlockState() != state) {
            level().setBlockAndUpdate(getBlockPos(), state);
        }
        setChanged();
    }
}
