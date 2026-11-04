package at.minecraftschurli.mods.bibliocraft.client.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.content.seat.SeatBackItem;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.util.BCModelTemplates;
import at.minecraftschurli.mods.bibliocraft.util.BlockModelDatagenUtil;
import at.minecraftschurli.mods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.stream.Stream;

import static at.minecraftschurli.mods.bibliocraft.util.BlockModelDatagenUtil.GroupedModelTemplate;
import static at.minecraftschurli.mods.bibliocraft.util.BlockModelDatagenUtil.GroupedModelTemplate2;
import static at.minecraftschurli.mods.bibliocraft.util.BlockModelDatagenUtil.WOODEN;
import static at.minecraftschurli.mods.bibliocraft.util.BlockModelDatagenUtil.WOODEN_COLORED;
import static at.minecraftschurli.mods.bibliocraft.util.BlockModelDatagenUtil.createItemModel;

/// Block states, block models and item models of the Bibliocraft blocks of the given wood types.
final class WoodTypeModels extends ModelProvider {
    private final List<BibliocraftWoodType> woodTypes;

    WoodTypeModels(PackOutput output, List<BibliocraftWoodType> woodTypes) {
        super(output, BibliocraftApi.MOD_ID);
        this.woodTypes = woodTypes;
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.concat(
                BCBlocks.WOODEN.stream().flatMap(holder -> woodTypes.stream().map(holder::holder)),
                BCBlocks.WOODEN_COLORED.stream().flatMap(holder -> woodTypes.stream().flatMap(holder::streamGroup))
        );
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.concat(
                BCItems.WOODEN.stream().flatMap(holder -> woodTypes.stream().map(holder::holder)),
                BCItems.WOODEN_COLORED.stream().flatMap(holder -> woodTypes.stream().flatMap(holder::streamGroup))
        );
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (BibliocraftWoodType woodType : woodTypes) {
            for (GroupedModelTemplate<BibliocraftWoodType> template : WOODEN) {
                template.build(blockModels, woodType);
            }
            for (DyeColor color : DyeColor.values()) {
                for (GroupedModelTemplate2<BibliocraftWoodType, DyeColor> template : WOODEN_COLORED) {
                    template.build(blockModels, woodType, color);
                }
                seatBackItemModel(itemModels, BCItems.SMALL_SEAT_BACK, woodType, color);
                seatBackItemModel(itemModels, BCItems.RAISED_SEAT_BACK, woodType, color);
                seatBackItemModel(itemModels, BCItems.FLAT_SEAT_BACK, woodType, color);
                seatBackItemModel(itemModels, BCItems.TALL_SEAT_BACK, woodType, color);
                seatBackItemModel(itemModels, BCItems.FANCY_SEAT_BACK, woodType, color);
            }
            createItemModel(itemModels, BCItems.FANCY_CLOCK, BCModelTemplates.FANCY_CLOCK_INVENTORY, woodType);
            createItemModel(itemModels, BCItems.GRANDFATHER_CLOCK, BCModelTemplates.GRANDFATHER_CLOCK_INVENTORY, woodType);
            createItemModel(itemModels, BCItems.FANCY_ARMOR_STAND, BCModelTemplates.FANCY_ARMOR_STAND_INVENTORY, woodType);
        }
    }

    private static void seatBackItemModel(ItemModelGenerators itemModels, GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> seatBack, BibliocraftWoodType woodType, DyeColor color) {
        SeatBackItem item = seatBack.get(woodType, color);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item).withPath("block/" + BlockModelDatagenUtil.nameFor(woodType, color, "seat_back") + "_" + item.type.getSerializedName())));
    }

    @Override
    public String getName() {
        return "Bibliocraft wood type models";
    }
}
