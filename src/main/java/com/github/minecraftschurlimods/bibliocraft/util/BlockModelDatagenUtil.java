package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseBlockStateModel;
import com.github.minecraftschurlimods.bibliocraft.client.model.TableBlockStateModel;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.AbstractFancyLightBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import com.mojang.datafixers.util.Function3;
import net.minecraft.Util;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.PropertyValueList;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class BlockModelDatagenUtil {
    public static final Map<DyeColor, ResourceLocation> CANDLE_TEXTURES = Util.make(new HashMap<>(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, BCUtil.mcLoc("block/" + color.getName() + "_candle_lit"))));
    public static final Map<DyeColor, ResourceLocation> GLASS_TEXTURES = Util.make(new HashMap<>(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, BCUtil.mcLoc("block/" + color.getName() + "_stained_glass"))));
    public static final Map<DyeColor, ResourceLocation> WOOL_TEXTURES = Util.make(new HashMap<>(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, BCUtil.mcLoc("block/" + color.getName() + "_wool"))));

    public static final Function<BibliocraftWoodType, TextureMapping> DEFAULT_WOOD_TYPE_TEXTURE_MAPPING = wood -> TextureMapping.defaultTexture(wood.texture());
    public static final BiFunction<BibliocraftWoodType, DyeColor, TextureMapping> DEFAULT_WOOD_TYPE_COLOR_TEXTURE_MAPPING = (wood, color) -> BCModelTemplates.coloredAndTexturedMaterial(wood.texture(), WOOL_TEXTURES.get(color));

    public static final GroupedModelTemplate<BibliocraftWoodType> BOOKCASE = new GroupedModelTemplate<>(
            BCBlocks.BOOKCASE,
            DEFAULT_WOOD_TYPE_TEXTURE_MAPPING,
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withWrappedVariantFunction(BookcaseBlockStateModel::builder)
                    .withSingleModel(BCModelTemplates.BOOKCASE, textureMapping)
                    .withHorizontalRotation()
                    .withUVLock()
                    .withItemModel());
    public static final GroupedModelTemplate<BibliocraftWoodType> FANCY_CLOCK = horizontalWoodTemplateNoItem(BCBlocks.FANCY_CLOCK, BCModelTemplates.FANCY_CLOCK);
    public static final GroupedModelTemplate<BibliocraftWoodType> WALL_FANCY_CLOCK = horizontalWoodTemplateNoItem(BCBlocks.WALL_FANCY_CLOCK, BCModelTemplates.WALL_FANCY_CLOCK);
    public static final GroupedModelTemplate<BibliocraftWoodType> FANCY_SIGN = horizontalWoodTemplate(BCBlocks.FANCY_SIGN, FancySignBlock.HANGING, BCModelTemplates.FANCY_SIGN_HANGING, BCModelTemplates.FANCY_SIGN, false);
    public static final GroupedModelTemplate<BibliocraftWoodType> WALL_FANCY_SIGN = horizontalWoodTemplateNoItem(BCBlocks.WALL_FANCY_SIGN, BCModelTemplates.WALL_FANCY_SIGN);
    public static final GroupedModelTemplate<BibliocraftWoodType> FANCY_CRAFTER = horizontalWoodTemplate(BCBlocks.FANCY_CRAFTER, BCModelTemplates.FANCY_CRAFTER);
    public static final GroupedModelTemplate<BibliocraftWoodType> LABEL = horizontalWoodTemplate(BCBlocks.LABEL, BCModelTemplates.LABEL);
    public static final GroupedModelTemplate<BibliocraftWoodType> POTION_SHELF = horizontalWoodTemplate(BCBlocks.POTION_SHELF, BCModelTemplates.POTION_SHELF);
    public static final GroupedModelTemplate<BibliocraftWoodType> SHELF = horizontalWoodTemplate(BCBlocks.SHELF, BCModelTemplates.SHELF);
    public static final GroupedModelTemplate<BibliocraftWoodType> TOOL_RACK = horizontalWoodTemplate(BCBlocks.TOOL_RACK, BCModelTemplates.TOOL_RACK);
    public static final GroupedModelTemplate<BibliocraftWoodType> FANCY_ARMOR_STAND = horizontalDoubleHighWoodTemplate(BCBlocks.FANCY_ARMOR_STAND, BCModelTemplates.FANCY_ARMOR_STAND_TOP, BCModelTemplates.FANCY_ARMOR_STAND_BOTTOM);
    public static final GroupedModelTemplate<BibliocraftWoodType> GRANDFATHER_CLOCK = horizontalDoubleHighWoodTemplate(BCBlocks.GRANDFATHER_CLOCK, BCModelTemplates.GRANDFATHER_CLOCK_TOP, BCModelTemplates.GRANDFATHER_CLOCK_BOTTOM);
    public static final GroupedModelTemplate2<BibliocraftWoodType, DyeColor> DISPLAY_CASE = new GroupedModelTemplate2<>(
            BCBlocks.DISPLAY_CASE,
            DEFAULT_WOOD_TYPE_COLOR_TEXTURE_MAPPING,
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(BlockStateProperties.OPEN, BCModelTemplates.DISPLAY_CASE_OPEN, BCModelTemplates.DISPLAY_CASE_CLOSED, textureMapping)
                    .withHorizontalRotation()
                    .withItemModelFromDispatch(BlockStateProperties.OPEN, true));
    public static final GroupedModelTemplate2<BibliocraftWoodType, DyeColor> WALL_DISPLAY_CASE = new GroupedModelTemplate2<>(
            BCBlocks.WALL_DISPLAY_CASE,
            DEFAULT_WOOD_TYPE_COLOR_TEXTURE_MAPPING,
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(BlockStateProperties.OPEN, BCModelTemplates.WALL_DISPLAY_CASE_OPEN, BCModelTemplates.WALL_DISPLAY_CASE_CLOSED, textureMapping)
                    .withHorizontalRotation()
                    .withUVLock());
    public static final GroupedModelTemplate2<BibliocraftWoodType, DyeColor> SEAT = new GroupedModelTemplate2<>(
            BCBlocks.SEAT,
            DEFAULT_WOOD_TYPE_COLOR_TEXTURE_MAPPING,
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withSingleModel(BCModelTemplates.SEAT, textureMapping)
                    .withItemModel());
    public static final GroupedModelTemplate2<BibliocraftWoodType, DyeColor> SEAT_BACK = new GroupedModelTemplate2<>(
            BCBlocks.SEAT_BACK,
            DEFAULT_WOOD_TYPE_COLOR_TEXTURE_MAPPING,
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(SeatBackBlock.TYPE, BCModelTemplates.SEAT_BACK::get, textureMapping)
                    .withHorizontalRotation()
                    .withUVLock());
    public static final GroupedModelTemplate<BibliocraftWoodType> TABLE = new GroupedModelTemplate<>(
            BCBlocks.TABLE,
            DEFAULT_WOOD_TYPE_TEXTURE_MAPPING,
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withWrappedVariantFunction(TableBlockStateModel::builder)
                    .withModelDispatch(TableBlock.TYPE, BCModelTemplates.TABLE::get, textureMapping)
                    .withItemModelFromDispatch(TableBlock.TYPE, TableBlock.Type.NONE)
                    .withHorizontalRotation()
                    .withUVLock());
    public static final GroupedModelTemplate<DyeColor> TYPEWRITER = new GroupedModelTemplate<>(
            BCBlocks.TYPEWRITER,
            color -> BCModelTemplates.color(BCUtil.mcLoc("block/" + color.getSerializedName() + "_terracotta")),
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(TypewriterBlock.PAPER, i -> BCModelTemplates.TYPEWRITER[i], textureMapping)
                    .withHorizontalRotation()
                    .withItemModelFromDispatch(TypewriterBlock.PAPER, 0));
    public static final GroupedModelTemplate<DyeColor> FANCY_GOLD_LAMP = new GroupedModelTemplate<>(
            BCBlocks.FANCY_GOLD_LAMP,
            (color) -> BCModelTemplates.lampMaterial(GLASS_TEXTURES.get(color), BCUtil.mcLoc("block/gold_block")),
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(AbstractFancyLightBlock.TYPE, lightBlockTypeDispatch(BCModelTemplates.FANCY_LAMP_STANDING, BCModelTemplates.FANCY_LAMP_HANGING, BCModelTemplates.FANCY_LAMP_WALL), textureMapping)
                    .withHorizontalRotation()
                    .withItemModelFromDispatch(AbstractFancyLightBlock.TYPE, AbstractFancyLightBlock.Type.STANDING));
    public static final GroupedModelTemplate<DyeColor> FANCY_IRON_LAMP = new GroupedModelTemplate<>(
            BCBlocks.FANCY_IRON_LAMP,
            (color) -> BCModelTemplates.lampMaterial(GLASS_TEXTURES.get(color), BCUtil.mcLoc("block/iron_block")),
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(AbstractFancyLightBlock.TYPE, lightBlockTypeDispatch(BCModelTemplates.FANCY_LAMP_STANDING, BCModelTemplates.FANCY_LAMP_HANGING, BCModelTemplates.FANCY_LAMP_WALL), textureMapping)
                    .withHorizontalRotation()
                    .withItemModelFromDispatch(AbstractFancyLightBlock.TYPE, AbstractFancyLightBlock.Type.STANDING));
    public static final GroupedModelTemplate<DyeColor> FANCY_GOLD_LANTERN = new GroupedModelTemplate<>(
            BCBlocks.FANCY_GOLD_LANTERN,
            (color) -> BCModelTemplates.lanternMaterial(CANDLE_TEXTURES.get(color), BCUtil.bcLoc("block/gold_chain"), BCUtil.mcLoc("block/gold_block")),
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(AbstractFancyLightBlock.TYPE, lightBlockTypeDispatch(BCModelTemplates.FANCY_LANTERN_STANDING, BCModelTemplates.FANCY_LANTERN_HANGING, BCModelTemplates.FANCY_LANTERN_WALL), textureMapping)
                    .withHorizontalRotation()
                    .withItemModelFromDispatch(AbstractFancyLightBlock.TYPE, AbstractFancyLightBlock.Type.STANDING));
    public static final GroupedModelTemplate<DyeColor> FANCY_IRON_LANTERN = new GroupedModelTemplate<>(
            BCBlocks.FANCY_IRON_LANTERN,
            (color) -> BCModelTemplates.lanternMaterial(CANDLE_TEXTURES.get(color), BCUtil.mcLoc("block/iron_chain"), BCUtil.mcLoc("block/iron_block")),
            BlockModelDatagenUtil::nameFor,
            (modelBuilder, textureMapping) -> modelBuilder
                    .withModelDispatch(AbstractFancyLightBlock.TYPE, lightBlockTypeDispatch(BCModelTemplates.FANCY_LANTERN_STANDING, BCModelTemplates.FANCY_LANTERN_HANGING, BCModelTemplates.FANCY_LANTERN_WALL), textureMapping)
                    .withHorizontalRotation()
                    .withItemModelFromDispatch(AbstractFancyLightBlock.TYPE, AbstractFancyLightBlock.Type.STANDING));

    public static final List<GroupedModelTemplate<BibliocraftWoodType>> WOODEN = List.of(
            BOOKCASE,
            FANCY_CLOCK,
            WALL_FANCY_CLOCK,
            FANCY_CRAFTER,
            LABEL,
            POTION_SHELF,
            SHELF,
            TOOL_RACK,
            WALL_FANCY_SIGN,
            FANCY_ARMOR_STAND,
            GRANDFATHER_CLOCK,
            FANCY_SIGN,
            TABLE
    );
    public static final List<GroupedModelTemplate2<BibliocraftWoodType, DyeColor>> WOODEN_COLORED = List.of(
            DISPLAY_CASE,
            WALL_DISPLAY_CASE,
            SEAT,
            SEAT_BACK
    );
    public static final List<GroupedModelTemplate<DyeColor>> COLORED = List.of(
            TYPEWRITER,
            FANCY_GOLD_LAMP,
            FANCY_IRON_LAMP,
            FANCY_GOLD_LANTERN,
            FANCY_IRON_LANTERN
    );

    private BlockModelDatagenUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static GroupedModelTemplate<BibliocraftWoodType> horizontalWoodTemplate(GroupedHolder<BibliocraftWoodType, Block, ? extends Block> holder, ModelTemplate template) {
        return new GroupedModelTemplate<>(holder, DEFAULT_WOOD_TYPE_TEXTURE_MAPPING, BlockModelDatagenUtil::nameFor, (modelBuilder, textureMapping) -> modelBuilder.withSingleModel(template, textureMapping).withHorizontalRotation().withUVLock().withItemModel());
    }

    public static GroupedModelTemplate<BibliocraftWoodType> horizontalWoodTemplateNoItem(GroupedHolder<BibliocraftWoodType, Block, ? extends Block> holder, ModelTemplate template) {
        return new GroupedModelTemplate<>(holder, DEFAULT_WOOD_TYPE_TEXTURE_MAPPING, BlockModelDatagenUtil::nameFor, (modelBuilder, textureMapping) -> modelBuilder.withSingleModel(template, textureMapping).withHorizontalRotation().withUVLock());
    }

    public static GroupedModelTemplate<BibliocraftWoodType> horizontalDoubleHighWoodTemplate(GroupedHolder<BibliocraftWoodType, Block, ? extends Block> holder, ModelTemplate templateTop, ModelTemplate templateBottom) {
        return new GroupedModelTemplate<>(holder, DEFAULT_WOOD_TYPE_TEXTURE_MAPPING, BlockModelDatagenUtil::nameFor, (modelBuilder, textureMapping) -> modelBuilder.withModelDispatch(BlockStateProperties.DOUBLE_BLOCK_HALF, doubleBlockHalfDispatch(templateTop, templateBottom), textureMapping).withHorizontalRotation().withUVLock());
    }

    public static GroupedModelTemplate<BibliocraftWoodType> horizontalWoodTemplate(GroupedHolder<BibliocraftWoodType, Block, ? extends Block> holder, BooleanProperty property, ModelTemplate templateOnTrue, ModelTemplate templateOnFalse, boolean itemModelFrom) {
        return new GroupedModelTemplate<>(holder, DEFAULT_WOOD_TYPE_TEXTURE_MAPPING, BlockModelDatagenUtil::nameFor, (modelBuilder, textureMapping) -> modelBuilder.withModelDispatch(property, templateOnTrue, templateOnFalse, textureMapping).withHorizontalRotation().withUVLock().withItemModelFromDispatch(property, itemModelFrom));
    }

    public static Function<AbstractFancyLightBlock.Type, ModelTemplate> lightBlockTypeDispatch(ModelTemplate standing, ModelTemplate hanging, ModelTemplate wall) {
        return type -> switch (type) {
            case STANDING -> standing;
            case HANGING -> hanging;
            case WALL -> wall;
        };
    }

    public static Function<DoubleBlockHalf, ModelTemplate> doubleBlockHalfDispatch(ModelTemplate upper, ModelTemplate lower) {
        return half -> switch (half) {
            case UPPER -> upper;
            case LOWER -> lower;
        };
    }

    public static void createItemModel(ItemModelGenerators itemModels, GroupedHolder<BibliocraftWoodType, Item, ?> holder, ModelTemplate template, BibliocraftWoodType woodType) {
        Item item = holder.get(woodType);
        ResourceLocation location = ModelLocationUtils.getModelLocation(item).withPath("item/" + BlockModelDatagenUtil.nameFor(woodType, holder.getName()) + template.suffix.orElse(""));
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(template.create(location, TextureMapping.defaultTexture(woodType.texture()), itemModels.modelOutput)));
    }

    public static String nameFor(BibliocraftWoodType wood, String name) {
        return "wood/" + wood.getRegistrationPrefix() + "/" + name;
    }

    public static String nameFor(DyeColor color, String name) {
        return "color/" + color.getName() + "/" + name;
    }

    public static String nameFor(BibliocraftWoodType wood, DyeColor color, String name) {
        return "color/" + color.getName() + "/wood/" + wood.getRegistrationPrefix() + "/" + name;
    }

    public static <T1, T2> ModelBuilder builder(BlockModelGenerators generators, GroupedHolder.Nested<T1, T2, Block, ? extends Block> holder, T1 value1, T2 value2, Function3<T1, T2, String, String> nameOverrideFunction) {
        return new ModelBuilder(generators, holder.get(value1, value2), nameOverrideFunction.apply(value1, value2, holder.getName()));
    }

    public static ModelBuilder builder(BlockModelGenerators generators, Block block) {
        return new ModelBuilder(generators, block, null);
    }

    public static ModelBuilder builder(BlockModelGenerators generators, Block block, @Nullable String nameOverride) {
        return new ModelBuilder(generators, block, nameOverride);
    }

    public static ModelBuilder builder(BlockModelGenerators generators, Supplier<? extends Block> block) {
        return builder(generators, block.get());
    }

    public static ModelBuilder builder(BlockModelGenerators generators, Supplier<? extends Block> block, @Nullable String nameOverride) {
        return builder(generators, block.get(), nameOverride);
    }

    public static class GroupedModelTemplate2<T1, T2> {
        private final GroupedHolder.Nested<T1, T2, Block, ? extends Block> holder;
        private final BiFunction<T1, T2, TextureMapping> textureMappingFunction;
        private final Function3<T1, T2, String, String> nameOverrideFunction;
        private final BiFunction<ModelBuilder, TextureMapping, ModelBuilder> modelBuilderFunction;

        public GroupedModelTemplate2(GroupedHolder.Nested<T1, T2, Block, ? extends Block> holder, BiFunction<T1, T2, TextureMapping> textureMappingFunction, Function3<T1, T2, String, String> nameOverrideFunction, BiFunction<ModelBuilder, TextureMapping, ModelBuilder> modelBuilderFunction) {
            this.holder = holder;
            this.textureMappingFunction = textureMappingFunction;
            this.nameOverrideFunction = nameOverrideFunction;
            this.modelBuilderFunction = modelBuilderFunction;
        }

        public ModelBuilder builder(BlockModelGenerators generators, T1 value1, T2 value2) {
            return modelBuilderFunction.apply(new ModelBuilder(generators, holder.get(value1, value2), nameOverrideFunction.apply(value1, value2, holder.getName())), textureMappingFunction.apply(value1, value2));
        }

        public void build(BlockModelGenerators generators, T1 value1, T2 value2) {
            builder(generators, value1, value2).build();
        }
    }

    public static class GroupedModelTemplate<T> {
        private final GroupedHolder<T, Block, ? extends Block> holder;
        private final Function<T, TextureMapping> textureMappingFunction;
        private final BiFunction<T, String, String> nameOverrideFunction;
        private final BiFunction<ModelBuilder, TextureMapping, ModelBuilder> modelBuilderFunction;

        public GroupedModelTemplate(GroupedHolder<T, Block, ? extends Block> holder, Function<T, TextureMapping> textureMappingFunction, BiFunction<T, String, String> nameOverrideFunction, BiFunction<ModelBuilder, TextureMapping, ModelBuilder> modelBuilderFunction) {
            this.holder = holder;
            this.textureMappingFunction = textureMappingFunction;
            this.nameOverrideFunction = nameOverrideFunction;
            this.modelBuilderFunction = modelBuilderFunction;
        }

        public ModelBuilder builder(BlockModelGenerators generators, T value) {
            return modelBuilderFunction.apply(new ModelBuilder(generators, holder.get(value), nameOverrideFunction.apply(value, holder.getName())), textureMappingFunction.apply(value));
        }

        public void build(BlockModelGenerators generators, T value) {
            builder(generators, value).build();
        }
    }

    @SuppressWarnings("unused")
    public static class ModelBuilder {
        private final BlockModelGenerators generators;
        private final Block block;
        private final @Nullable String nameOverride;
        private boolean generateItemModel = false;
        private @Nullable ResourceLocation itemModelLocation;
        private @Nullable MultiVariantGenerator variantGenerator;
        private @Nullable PropertyDispatch<MultiVariant> dispatchMap;
        private Function<ResourceLocation, MultiVariant> variantFunction = BlockModelGenerators::plainVariant;

        private ModelBuilder(BlockModelGenerators generators, Block block, @Nullable String nameOverride) {
            this.generators = generators;
            this.block = block;
            this.nameOverride = nameOverride;
        }

        public ModelBuilder withWrappedVariantFunction(Function<MultiVariant, CustomBlockStateModelBuilder> wrapper) {
            return withWrappedVariantFunction((MultiVariant variant) -> MultiVariant.of(wrapper.apply(variant)));
        }

        public ModelBuilder withWrappedVariantFunction(UnaryOperator<MultiVariant> wrapper) {
            Function<ResourceLocation, MultiVariant> function = variantFunction;
            return withVariantFunction((ResourceLocation modelLoc) -> wrapper.apply(function.apply(modelLoc)));
        }

        public ModelBuilder withVariantFunction(Function<ResourceLocation, MultiVariant> variantFunction) {
            this.variantFunction = variantFunction;
            return this;
        }

        public ModelBuilder withItemModel() {
            if (block.asItem() == Items.AIR) {
                ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
                throw new IllegalStateException("You cannot use withItemModel() on a block that has no item! (" + location + ")");
            }
            this.generateItemModel = true;
            return this;
        }

        public ModelBuilder withFlatItemModel() {
            this.itemModelLocation = null;
            return withItemModel();
        }

        public ModelBuilder withItemModel(ResourceLocation itemModelLocation) {
            this.itemModelLocation = itemModelLocation;
            return withItemModel();
        }

        public <T extends Comparable<T>> ModelBuilder withItemModelFromDispatch(Property<T> property, T value) {
            return withItemModelFromDispatch(PropertyValueList.of(property.value(value)));
        }

        public ModelBuilder withModelDispatch(BooleanProperty property, ModelTemplate templateOnTrue, ModelTemplate templateOnFalse, TextureMapping textureMapping) {
            ResourceLocation modelOnTrue = buildBlockModel(templateOnTrue, textureMapping);
            ResourceLocation modelOnFalse = buildBlockModel(templateOnFalse, textureMapping);
            return withModelDispatch(property, modelOnTrue, modelOnFalse);
        }

        public ModelBuilder withModelDispatch(BooleanProperty property, ResourceLocation modelOnTrue, ResourceLocation modelOnFalse) {
            return withModelDispatch(BlockModelGenerators.createBooleanModelDispatch(property, variantFunction.apply(modelOnTrue), variantFunction.apply(modelOnFalse)));
        }

        public ModelBuilder withModelDispatch(IntegerProperty property, TextureMapping textureMapping, ModelTemplate... models) {
            if (property.getPossibleValues().size() != models.length) {
                throw new IllegalArgumentException("The number of models must match the number of possible values for the given property!");
            }
            return withModelDispatch(property, transformArray(models, m -> buildBlockModel(m, textureMapping)));
        }

        public ModelBuilder withModelDispatch(IntegerProperty property, ResourceLocation... models) {
            if (property.getPossibleValues().size() != models.length) {
                throw new IllegalArgumentException("The number of models must match the number of possible values for the given property!");
            }
            return withModelDispatch(PropertyDispatch.initial(property).generate(i -> variantFunction.apply(models[i])));
        }

        public <T extends Comparable<T>> ModelBuilder withModelDispatch(Property<T> property, Function<T, ModelTemplate> modelTemplateFunction, TextureMapping textureMapping) {
            return withModelDispatch(property, t -> buildBlockModel(modelTemplateFunction.apply(t), textureMapping));
        }

        public <T extends Comparable<T>> ModelBuilder withModelDispatch(Property<T> property, Function<T, ResourceLocation> modelLocationFunction) {
            return withModelDispatch(PropertyDispatch.initial(property).generate(t -> variantFunction.apply(modelLocationFunction.apply(t))));
        }

        public <T1 extends Comparable<T1>, T2 extends Comparable<T2>> ModelBuilder withModelDispatch(Property<T1> property1, Property<T2> property2, BiFunction<T1, T2, ModelTemplate> modelTemplateFunction, TextureMapping textureMapping) {
            return withModelDispatch(property1, property2, (t1, t2) -> buildBlockModel(modelTemplateFunction.apply(t1, t2), textureMapping));
        }

        public <T1 extends Comparable<T1>, T2 extends Comparable<T2>> ModelBuilder withModelDispatch(Property<T1> property1, Property<T2> property2, BiFunction<T1, T2, ResourceLocation> modelLocationFunction) {
            return withModelDispatch(PropertyDispatch.initial(property1, property2).generate((t1, t2) -> variantFunction.apply(modelLocationFunction.apply(t1, t2))));
        }

        public ModelBuilder withVariantGenerator(MultiVariantGenerator variantGenerator) {
            this.variantGenerator = variantGenerator;
            return this;
        }

        public ModelBuilder withModelDispatch(PropertyDispatch<MultiVariant> propertyDispatch) {
            if (variantGenerator != null) {
                throw new IllegalStateException("You cannot use withModelDispatch() after withVariantGenerator()!");
            }
            this.dispatchMap = propertyDispatch;
            return withVariantGenerator(MultiVariantGenerator.dispatch(block).with(propertyDispatch));
        }

        public ModelBuilder withDefaultExistingModel() {
            return withSingleModel(ModelLocationUtils.getModelLocation(block));
        }

        public ModelBuilder withSingleModel(ResourceLocation modelLocation) {
            if (variantGenerator != null) {
                throw new IllegalStateException("You cannot use withSingleVariant() after withVariantGenerator()!");
            }
            this.itemModelLocation = modelLocation;
            return withVariantGenerator(MultiVariantGenerator.dispatch(block, variantFunction.apply(modelLocation)));
        }

        public ModelBuilder withSingleModel(ModelTemplate template, TextureMapping textureMapping) {
            return withSingleModel(buildBlockModel(template, textureMapping));
        }

        public ModelBuilder withVariantDispatch(PropertyDispatch<VariantMutator> variantDispatch) {
            if (this.variantGenerator == null) {
                throw new IllegalStateException("You must call withVariantGenerator() before calling withVariantDispatch()!");
            }
            return withVariantGenerator(this.variantGenerator.with(variantDispatch));
        }

        public ModelBuilder withVariantDispatch(VariantMutator variantDispatch) {
            if (variantGenerator == null) {
                throw new IllegalStateException("You must call withVariantGenerator() before calling withVariantDispatch()!");
            }
            return withVariantGenerator(this.variantGenerator.with(variantDispatch));
        }

        public ModelBuilder withHorizontalRotation() {
            return withVariantDispatch(BlockModelGenerators.ROTATION_HORIZONTAL_FACING);
        }

        public ModelBuilder withUVLock() {
            return withVariantDispatch(BlockModelGenerators.UV_LOCK);
        }

        public void build() {
            if (generateItemModel) {
                if (itemModelLocation != null) {
                    generators.registerSimpleItemModel(block, itemModelLocation);
                } else {
                    generators.registerSimpleFlatItemModel(block);
                }
            }
            generators.blockStateOutput.accept(variantGenerator);
        }

        private ResourceLocation buildBlockModel(ModelTemplate template, TextureMapping textureMapping) {
            ResourceLocation baseName = ModelLocationUtils.getModelLocation(block);
            if (nameOverride != null) {
                baseName = baseName.withPath("block/" + nameOverride);
            }
            return template.create(baseName.withSuffix(template.suffix.orElse("")), textureMapping, generators.modelOutput);
        }

        private ModelBuilder withItemModelFromDispatch(PropertyValueList key) {
            if (dispatchMap == null) {
                throw new IllegalStateException("You must call withModelDispatch() before calling withItemModelFromDispatch()");
            }
            MultiVariant multiVariant = dispatchMap.getEntries().get(key);
            return extractAndSetItemModelVariant(multiVariant);
        }

        private ModelBuilder extractAndSetItemModelVariant(MultiVariant multiVariant) {
            List<Weighted<Variant>> list = multiVariant.variants().unwrap();
            if (list.isEmpty()) {
                List<Weighted<CustomBlockStateModelBuilder>> list1 = multiVariant.customBlockStateModels().unwrap();
                if (list1.size() == 1 && list1.getFirst().value() instanceof WrappingCustomBlockStateModelBuilder wrapping) {
                    return extractAndSetItemModelVariant(wrapping.wrapped);
                }
            }
            if (list.size() != 1) {
                throw new IllegalStateException("The dispatch map for item model generation must only contain a single variant!");
            }
            return withItemModel(list.getFirst().value().modelLocation());
        }

        private static ResourceLocation[] transformArray(ModelTemplate[] models, Function<ModelTemplate, ResourceLocation> mapper) {
            ResourceLocation[] result = new ResourceLocation[models.length];
            for (int i = 0; i < models.length; i++) {
                result[i] = mapper.apply(models[i]);
            }
            return result;
        }
    }
}
