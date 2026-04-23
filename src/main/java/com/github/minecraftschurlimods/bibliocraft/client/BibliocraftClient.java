package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.BCConfig;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.util.CompatUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BibliocraftApi.MOD_ID, dist = Dist.CLIENT)
public final class BibliocraftClient {
    public BibliocraftClient(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, BCConfig.CLIENT_SPEC);
        if (CompatUtil.hasConfigScreen()) {
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }
}
