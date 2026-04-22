package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BCModelTemplates {
    private static final TextureSlot COLOR = TextureSlot.create("color");
    private static final TextureSlot METAL = TextureSlot.create("metal");
    private static final TextureSlot CHAIN = TextureSlot.create("chain");

    public static final ModelTemplate BOOKCASE = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/bookcase/bookcase")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate POTION_SHELF = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/potion_shelf")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate SHELF = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/shelf")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate TOOL_RACK = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/tool_rack")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate LABEL = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/label")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_CLOCK = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/fancy")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate WALL_FANCY_CLOCK = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/wall_fancy")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_CLOCK_INVENTORY = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/fancy_inventory")),
            Optional.of("_inventory"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_CRAFTER = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_crafter")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_ARMOR_STAND_BOTTOM = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/bottom")),
            Optional.of("_bottom"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_ARMOR_STAND_TOP = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/top")),
            Optional.of("_top"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_ARMOR_STAND_INVENTORY = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/inventory")),
            Optional.of("_inventory"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate GRANDFATHER_CLOCK_BOTTOM = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/grandfather_bottom")),
            Optional.of("_bottom"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate GRANDFATHER_CLOCK_TOP = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/grandfather_top")),
            Optional.of("_top"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate GRANDFATHER_CLOCK_INVENTORY = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/grandfather_inventory")),
            Optional.of("_inventory"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate WALL_FANCY_SIGN = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_sign/wall")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_SIGN_HANGING = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_sign/hanging")),
            Optional.of("_hanging"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_SIGN = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_sign/standing")),
            Optional.empty(),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_LAMP_STANDING = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lamp/standing")),
            Optional.of("_standing"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LAMP_HANGING = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lamp/hanging")),
            Optional.of("_hanging"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LAMP_WALL = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lamp/wall")),
            Optional.of("_wall"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LANTERN_STANDING = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lantern/standing")),
            Optional.of("_standing"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LANTERN_HANGING = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lantern/hanging")),
            Optional.of("_hanging"),
            COLOR,
            METAL,
            CHAIN
    );
    public static final ModelTemplate FANCY_LANTERN_WALL = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lantern/wall")),
            Optional.of("_wall"),
            COLOR,
            METAL
    );
    public static final ModelTemplate DISPLAY_CASE_OPEN = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/open")),
            Optional.of("_open"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate DISPLAY_CASE_CLOSED = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/closed")),
            Optional.of("_closed"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate WALL_DISPLAY_CASE_OPEN = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/wall_open")),
            Optional.of("_open"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate WALL_DISPLAY_CASE_CLOSED = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/wall_closed")),
            Optional.of("_closed"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate SEAT = new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/seat/seat")),
            Optional.empty(),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate[] TYPEWRITER = TypewriterBlock.PAPER.getPossibleValues().stream().map(i -> new BCModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/typewriter/" + i)),
            Optional.of("_" + i),
            COLOR
    )).toArray(ModelTemplate[]::new);
    public static final Map<SeatBackType, ModelTemplate> SEAT_BACK = Util.makeEnumMap(SeatBackType.class, type -> 
            new BCModelTemplate(
                    Optional.of(BCUtil.bcLoc("block/template/seat/" + type.getSerializedName() + "_seat_back")),
                    Optional.of("_" + type.getSerializedName()),
                    TextureSlot.TEXTURE,
                    COLOR));
    public static final Map<TableBlock.Type, ModelTemplate> TABLE_CLOTH = Util.makeEnumMap(TableBlock.Type.class, type -> 
            new BCModelTemplate(
                    Optional.of(BCUtil.bcLoc("block/template/table/" + type.getSerializedName() + "_cloth")),
                    Optional.of("/cloth_" + type.getSerializedName()),
                    COLOR));
    public static final Map<TableBlock.Type, ModelTemplate> TABLE = Util.makeEnumMap(TableBlock.Type.class, type ->
            new BCModelTemplate(
                    Optional.of(BCUtil.bcLoc("block/template/table/" + type.getSerializedName())),
                    Optional.of("/" + type.getSerializedName()),
                    TextureSlot.TEXTURE));

    public static TextureMapping lampMaterial(Identifier color, Identifier metal) {
        return color(color).put(METAL, metal);
    }

    public static TextureMapping color(Identifier color) {
        return new TextureMapping().put(COLOR, color);
    }

    public static TextureMapping lanternMaterial(Identifier candle, Identifier chain, Identifier metal) {
        return lampMaterial(candle, metal).put(CHAIN, chain);
    }

    public static TextureMapping coloredAndTexturedMaterial(Identifier texture, Identifier color) {
        return color(color).put(TextureSlot.TEXTURE, texture);
    }

    private static class BCModelTemplate extends ModelTemplate {
        private static final Set<String> COLORS = Arrays.stream(DyeColor.values()).map(DyeColor::getName).collect(Collectors.toUnmodifiableSet());

        public BCModelTemplate(Optional<Identifier> model, Optional<String> suffix, TextureSlot... slots) {
            super(model, suffix, slots);
        }

        @Override
        public Identifier create(Block block, TextureMapping textureMapping, BiConsumer<Identifier, ModelInstance> output) {
            Identifier resourcelocation = BuiltInRegistries.BLOCK.getKey(block);
            return this.create(resourcelocation.withPath(path -> "block/" + convertColorPath(path) + this.suffix.orElse("")), textureMapping, output);
        }

        private String convertColorPath(String path) {
            for (String color : COLORS) {
                // path is in the format '<color>_<thing>' and should be converted to 'color/<color>/<thing>'
                if (path.startsWith(color + "_")) {
                    return "color/" + color + "/" + path.substring(color.length() + 1);
                }
            }
            return path;
        }
    }
}
