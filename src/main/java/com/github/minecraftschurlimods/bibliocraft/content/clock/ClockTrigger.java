package com.github.minecraftschurlimods.bibliocraft.content.clock;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClockTrigger(int hour, int minute, boolean sound, boolean redstone) {
    public static final StreamCodec<ByteBuf, ClockTrigger> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClockTrigger::hour,
            ByteBufCodecs.INT, ClockTrigger::minute,
            ByteBufCodecs.BOOL, ClockTrigger::sound,
            ByteBufCodecs.BOOL, ClockTrigger::redstone,
            ClockTrigger::new);

    public int getInGameTime() {
        // 1 in-game hour is 1000 ticks, 1 in-game minute is 50/3 ticks.
        return (int) (hour * 1000 + minute * 50 / 3d);
    }
}
