package at.minecraftschurli.mods.bibliocraft.content.typewriter;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.init.BCDataComponents;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.block.BCInputItemHandler;
import at.minecraftschurli.mods.bibliocraft.util.block.BCOutputItemHandler;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

public class TypewriterBlockEntity extends BCBlockEntity {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private static final IntList INPUTS = IntList.of(INPUT);
    private static final IntList OUTPUTS = IntList.of(OUTPUT);
    private static final String PAGE_KEY = "page";
    private final BCInputItemHandler inputItemHandler = new BCInputItemHandler(getContainerSize(), this, INPUTS);
    private final BCOutputItemHandler outputItemHandler = new BCOutputItemHandler(getContainerSize(), this, OUTPUTS);
    private TypewriterPage page = TypewriterPage.DEFAULT;

    public TypewriterBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TYPEWRITER.get(), 2, pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read(PAGE_KEY, TypewriterPage.CODEC).ifPresent(page -> this.page = page);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(PAGE_KEY, TypewriterPage.CODEC, page);
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        return slot == INPUT && resource.is(BCTags.Items.TYPEWRITER_PAPER);
    }

    @Override
    @Nullable
    public ResourceHandler<ItemResource> getItemCapability(@Nullable Direction side) {
        return side == null ? null : side == Direction.DOWN ? outputItemHandler : inputItemHandler;
    }

    public boolean insertPaper(ItemStack stack) {
        ItemStack input = getItem(INPUT);
        if (!input.isEmpty() && !ItemStack.isSameItemSameComponents(input, stack)) return false;
        if (input.isEmpty()) {
            getItemHandler().set(INPUT, ItemResource.of(stack), 1);
        } else if (input.getCount() < input.getMaxStackSize()) {
            input.grow(1);
        } else return false;
        stack.shrink(1);
        setChanged();
        return true;
    }

    public ItemStack takeOutput() {
        ItemStack output = getItem(OUTPUT);
        getItemHandler().set(OUTPUT, ItemResource.EMPTY, 0);
        level().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TypewriterBlock.PAPER, 0));
        setChanged();
        return output;
    }

    public TypewriterPage getPage() {
        if (getItem(OUTPUT).isEmpty()) {
            setPage(TypewriterPage.DEFAULT);
            setChanged();
        }
        return page;
    }

    public void setPage(TypewriterPage page) {
        this.page = page;
        if (page.line() == TypewriterPage.MAX_LINES) {
            ItemStack output = new ItemStack(BCItems.TYPEWRITER_PAGE.get());
            output.set(BCDataComponents.TYPEWRITER_PAGE, page);
            getItemHandler().set(OUTPUT, ItemResource.of(output), 1);
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
