package com.github.minecraftschurlimods.bibliocraft.api;

import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface BibliocraftApi {
    String MOD_ID = "bibliocraft";

    static BibliocraftDatagenHelper getDatagenHelper() {
        return InstanceHolder.DATAGEN_HELPER.get();
    }

    static BibliocraftWoodTypeRegistry getWoodTypeRegistry() {
        return InstanceHolder.WOOD_TYPE_REGISTRY.get();
    }

    @ApiStatus.Internal
    final class InstanceHolder {
        private InstanceHolder() {}

        private static final Lazy<BibliocraftDatagenHelper> DATAGEN_HELPER = Lazy.concurrentOf(fromServiceLoader(BibliocraftDatagenHelper.class));
        private static final Lazy<BibliocraftWoodTypeRegistry> WOOD_TYPE_REGISTRY = Lazy.concurrentOf(fromServiceLoader(BibliocraftWoodTypeRegistry.class));

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
