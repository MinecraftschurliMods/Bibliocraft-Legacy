package com.github.minecraftschurlimods.bibliocraft.test;

import net.minecraft.gametest.framework.GameTestAssertException;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

@NonExtendable
public interface GametestAssertions {
    @Contract("false, _ -> fail")
    static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    @Contract("true, _ -> fail")
    static void assertFalse(boolean condition, String message) {
        if (condition) {
            fail(message);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    static <T> T assertInstance(@Nullable Object instance, Class<T> clazz, String message) {
        if (!clazz.isInstance(instance)) {
            fail(message);
            return null; // never reached
        } else {
            return clazz.cast(instance);
        }
    }

    @Contract("!null, _ -> fail")
    static void assertNull(@Nullable Object o, String message) {
        if (o != null) {
            fail(message);
        }
    }

    @Contract("null, _ -> fail")
    static void assertNotNull(@Nullable Object o, String message) {
        if (o == null) {
            fail(message);
        }
    }

    static void assertEquals(@Nullable Object expected, @Nullable Object actual, String message) {
        if (expected == null) {
            assertNull(actual, message);
        }
        if (expected instanceof BigDecimal bd1 && actual instanceof BigDecimal bd2) {
            assertEquals(bd1, bd2, message);
        }
        if (!Objects.equals(expected, actual)) {
            fail(message);
        }
    }

    static void assertEquals(@Nullable BigDecimal expected, @Nullable BigDecimal actual, String message) {
        if (expected == null) {
            assertNull(actual, message);
        }
        if (Objects.compare(expected, actual, Comparable::compareTo) != 0) {
            fail(message);
        }
    }

    @Contract("_ -> fail")
    static void fail(String message) throws GameTestAssertException {
        throw new GameTestAssertException(message);
    }
}
