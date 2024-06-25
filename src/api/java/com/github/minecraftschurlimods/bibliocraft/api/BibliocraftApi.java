package com.github.minecraftschurlimods.bibliocraft.api;

import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * The main accessor class for Bibliocraft's API. Use this to get references to the singleton instances of {@link BibliocraftWoodTypeRegistry} and {@link BibliocraftDatagenHelper}.
 */
@ApiStatus.NonExtendable
public interface BibliocraftApi {
    String MOD_ID = "bibliocraft";

    /**
     * @return The only instance of {@link BibliocraftDatagenHelper}.
     */
    static BibliocraftDatagenHelper getDatagenHelper() {
        return InstanceHolder.DATAGEN_HELPER.get();
    }

    /**
     * @return The only instance of {@link BibliocraftWoodTypeRegistry}.
     */
    static BibliocraftWoodTypeRegistry getWoodTypeRegistry() {
        return InstanceHolder.WOOD_TYPE_REGISTRY.get();
    }

    /**
     * The internal class used to hold the instances. DO NOT ACCESS YOURSELF!
     */
    @ApiStatus.Internal
    final class InstanceHolder {
        private static final Lazy<BibliocraftDatagenHelper> DATAGEN_HELPER = Lazy.of(fromServiceLoader(BibliocraftDatagenHelper.class));
        private static final Lazy<BibliocraftWoodTypeRegistry> WOOD_TYPE_REGISTRY = Lazy.of(fromServiceLoader(BibliocraftWoodTypeRegistry.class));
        private InstanceHolder() {}

        private static <T> Supplier<T> fromServiceLoader(Class<T> clazz) {
            return () -> {
                Optional<T> impl = ServiceLoader.load(FMLLoader.getGameLayer(), clazz).findFirst();
                String msg = "Unable to find implementation for " + clazz.getSimpleName() + "!";
                if (!FMLEnvironment.production) {
                    return impl.orElseThrow(() -> {
                        IllegalStateException exception = new IllegalStateException(msg);
                        LoggerFactory.getLogger(MOD_ID).error(exception.getMessage(), exception);
                        return exception;
                    });
                }
                return impl.orElseGet(() -> {
                    LoggerFactory.getLogger(MOD_ID).error(msg);
                    return null;
                });
            };
        }
    }
}
