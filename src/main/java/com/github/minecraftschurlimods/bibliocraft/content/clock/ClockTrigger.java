package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClockTrigger(int hour, int minute, boolean redstone, boolean sound) implements Comparable<ClockTrigger> {
    public static final Codec<ClockTrigger> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("hour").forGetter(ClockTrigger::hour),
            Codec.INT.fieldOf("minute").forGetter(ClockTrigger::minute),
            Codec.BOOL.fieldOf("redstone").forGetter(ClockTrigger::redstone),
            Codec.BOOL.fieldOf("sound").forGetter(ClockTrigger::sound)
    ).apply(inst, ClockTrigger::new));
    public static final StreamCodec<ByteBuf, ClockTrigger> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClockTrigger::hour,
            ByteBufCodecs.INT, ClockTrigger::minute,
            ByteBufCodecs.BOOL, ClockTrigger::redstone,
            ByteBufCodecs.BOOL, ClockTrigger::sound,
            ClockTrigger::new);

    public int getInGameTime() {
        // 1 in-game hour is 1000 ticks, 1 in-game minute is 50/3 ticks.
        return ((int) (hour * 1000 + minute * 50 / 3d) + 18000) % 24000;
    }

    @Override
    public int compareTo(ClockTrigger that) {
        int hour = Integer.compare(this.hour, that.hour);
        return hour != 0 ? hour : Integer.compare(this.minute, that.minute);
    }
}
