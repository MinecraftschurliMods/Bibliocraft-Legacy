package at.minecraftschurli.mods.bibliocraft.content.table;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class TableBlockEntity extends BCBlockEntity {
    public static final ModelProperty<DyeColor> CLOTH_COLOR = new ModelProperty<>();

    public TableBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TABLE.get(), 2, pos, state);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
        requestModelDataUpdate();
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        level().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        requestModelDataUpdate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        requestModelDataUpdate();
        if (level() instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.getChunkSource().chunkMap.getPlayers(ChunkPos.containing(worldPosition), false)) {
                player.connection.send(getUpdatePacket());
            }
        }
    }

    @Override
    public boolean isValid(int slot, ItemResource stack) {
        if (slot == 1) {
            return isEmpty(1) && stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof WoolCarpetBlock;
        }
        return super.isValid(slot, stack);
    }

    @Override
    public ModelData getModelData() {
        if (isEmpty(1) || !(getItemHandler().getResource(1).getItem() instanceof BlockItem bi) || !(bi.getBlock() instanceof WoolCarpetBlock carpet)) return super.getModelData();
        return ModelData.builder().with(CLOTH_COLOR, carpet.getColor()).build();
    }
}
