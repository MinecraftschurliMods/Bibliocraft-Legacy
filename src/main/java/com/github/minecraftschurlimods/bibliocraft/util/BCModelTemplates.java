package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import net.minecraft.Util;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

public class BCModelTemplates {
    private static final TextureSlot COLOR = TextureSlot.create("color");
    private static final TextureSlot METAL = TextureSlot.create("metal");
    private static final TextureSlot CHAIN = TextureSlot.create("chain");

    public static final ModelTemplate BOOKCASE = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/bookcase/bookcase")),
            Optional.of("/bookcase"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate POTION_SHELF = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/potion_shelf")),
            Optional.of("/potion_shelf"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate SHELF = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/shelf")),
            Optional.of("/shelf"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate TOOL_RACK = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/tool_rack")),
            Optional.of("/tool_rack"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate LABEL = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/label")),
            Optional.of("/label"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_CLOCK = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/fancy")),
            Optional.of("/fancy_clock"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate WALL_FANCY_CLOCK = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/wall_fancy")),
            Optional.of("/wall_fancy_clock"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_CRAFTER = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_crafter")),
            Optional.of("/fancy_crafter"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_ARMOR_STAND_BOTTOM = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/bottom")),
            Optional.of("/fancy_armor_stand_bottom"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_ARMOR_STAND_TOP = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/top")),
            Optional.of("fancy_armor_stand_top"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate IRON_FANCY_ARMOR_STAND_BOTTOM = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/iron_bottom")),
            Optional.of("/fancy_armor_stand_bottom"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate IRON_FANCY_ARMOR_STAND_TOP = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_armor_stand/iron_top")),
            Optional.of("fancy_armor_stand_top"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate GRANDFATHER_CLOCK_BOTTOM = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/grandfather_bottom")),
            Optional.of("/grandfather_clock_bottom"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate GRANDFATHER_CLOCK_TOP = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/clock/grandfather_top")),
            Optional.of("/grandfather_clock_top"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate WALL_FANCY_SIGN = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_sign/wall")),
            Optional.of("/wall_fancy_sign"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_SIGN_HANGING = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_sign/hanging")),
            Optional.of("/fancy_sign_hanging"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_SIGN = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_sign/standing")),
            Optional.of("/fancy_sign"),
            TextureSlot.TEXTURE
    );
    public static final ModelTemplate FANCY_LAMP_STANDING = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lamp/standing")),
            Optional.of("_lamp_standing"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LAMP_HANGING = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lamp/hanging")),
            Optional.of("_lamp_hanging"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LAMP_WALL = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lamp/wall")),
            Optional.of("_lamp_wall"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LANTERN_STANDING = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lantern/standing")),
            Optional.of("_lantern_standing"),
            COLOR,
            METAL
    );
    public static final ModelTemplate FANCY_LANTERN_HANGING = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lantern/hanging")),
            Optional.of("_lantern_hanging"),
            COLOR,
            METAL,
            CHAIN
    );
    public static final ModelTemplate FANCY_LANTERN_WALL = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/fancy_lantern/wall")),
            Optional.of("_lantern_wall"),
            COLOR,
            METAL
    );
    public static final ModelTemplate DISPLAY_CASE_OPEN = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/open")),
            Optional.of("/display_case_open"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate DISPLAY_CASE_CLOSED = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/closed")),
            Optional.of("/display_case_closed"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate WALL_DISPLAY_CASE_OPEN = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/wall_open")),
            Optional.of("/wall_display_case_open"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate WALL_DISPLAY_CASE_CLOSED = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/display_case/wall_closed")),
            Optional.of("/wall_display_case_closed"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate SEAT = new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/seat/seat")),
            Optional.of("/seat"),
            TextureSlot.TEXTURE,
            COLOR);
    public static final ModelTemplate[] TYPEWRITER = TypewriterBlock.PAPER.getPossibleValues().stream().map(i -> new ModelTemplate(
            Optional.of(BCUtil.bcLoc("block/template/typewriter/" + i)),
            Optional.of("_" + i),
            COLOR
    )).toArray(ModelTemplate[]::new);
    public static final Map<SeatBackType, ModelTemplate> SEAT_BACK = Util.makeEnumMap(SeatBackType.class, type -> 
            new ModelTemplate(
                    Optional.of(BCUtil.bcLoc("block/template/seat/" + type.getSerializedName() + "_seat_back")),
                    Optional.of("/" + type.getSerializedName() + "_seat_back"),
                    TextureSlot.TEXTURE,
                    COLOR));
    public static final Map<TableBlock.Type, ModelTemplate> TABLE_CLOTH = Util.makeEnumMap(TableBlock.Type.class, type -> 
            new ModelTemplate(
                    Optional.of(BCUtil.bcLoc("block/template/table/" + type.getSerializedName() + "_cloth")),
                    Optional.of("/table_cloth_" + type.getSerializedName()),
                    COLOR));
    public static final ModelTemplate CLIPBOARD = basicTemplate("block/clipboard");
    public static final ModelTemplate COOKIE_JAR = basicTemplate("block/cookie_jar");
    public static final ModelTemplate COOKIE_JAR_OPEN = basicTemplate("block/cookie_jar_open");
    public static final ModelTemplate DESK_BELL = basicTemplate("block/desk_bell");
    public static final ModelTemplate DINNER_PLATE = basicTemplate("block/dinner_plate");
    public static final ModelTemplate DISC_RACK = basicTemplate("block/disc_rack");
    public static final ModelTemplate WALL_DISC_RACK = basicTemplate("block/wall_disc_rack");
    public static final ModelTemplate SWORD_PEDESTAL = basicTemplate("block/sword_pedestal");
    public static final ModelTemplate PRINTING_TABLE = basicTemplate("block/printing_table");
    public static final ModelTemplate IRON_PRINTING_TABLE = basicTemplate("block/iron_printing_table");

    private static ModelTemplate basicTemplate(String model) {
        return new ModelTemplate(Optional.of(BCUtil.bcLoc(model)), Optional.empty());
    }

    public static TextureMapping lampMaterial(ResourceLocation color, ResourceLocation metal) {
        return color(color).put(METAL, metal);
    }

    public static TextureMapping color(ResourceLocation color) {
        return new TextureMapping().put(COLOR, color);
    }

    public static TextureMapping lanternMaterial(ResourceLocation candle, ResourceLocation chain, ResourceLocation metal) {
        return lampMaterial(candle, metal).put(CHAIN, chain);
    }

    public static TextureMapping displayCaseMaterial(ResourceLocation texture, ResourceLocation color) {
        return color(color).put(TextureSlot.TEXTURE, texture);
    }

    public static TextureMapping seatMaterial(ResourceLocation texture, ResourceLocation color) {
        return color(color).put(TextureSlot.TEXTURE, texture);
    }
}
