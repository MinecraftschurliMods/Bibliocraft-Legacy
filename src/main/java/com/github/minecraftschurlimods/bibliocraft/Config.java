package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue JEI_SHOW_WOOD_TYPES;
    public static final ModConfigSpec.BooleanValue JEI_SHOW_COLOR_TYPES;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder
                .comment("Contains compatibility options.")
                .translation("config." + BibliocraftApi.MOD_ID + ".compatibility")
                .push("compatibility");
        builder
                .comment("Contains compatibility options for the JEI mod.")
                .translation("config." + BibliocraftApi.MOD_ID + ".compatibility.jei")
                .push("compatibility");
        JEI_SHOW_WOOD_TYPES = builder
                .comment("Whether to show blocks for all wood types in JEI, or just the default oak.")
                .translation("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.show_wood_types")
                .define("show_wood_types", true);
        JEI_SHOW_COLOR_TYPES = builder
                .comment("Whether to show blocks for all color types in JEI, or just the default white.")
                .translation("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.show_color_types")
                .define("show_color_types", true);
        builder.pop();
        builder.pop();
        SPEC = builder.build();
    }
}
