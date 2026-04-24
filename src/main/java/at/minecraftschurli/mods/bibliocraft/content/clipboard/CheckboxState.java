package at.minecraftschurli.mods.bibliocraft.content.clipboard;

import at.minecraftschurli.mods.bibliocraft.util.CodecUtil;
import at.minecraftschurli.mods.bibliocraft.util.StringRepresentableEnum;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum CheckboxState implements StringRepresentableEnum {
    EMPTY, CHECK, X;
    public static final Codec<CheckboxState> CODEC = CodecUtil.enumCodec(CheckboxState::values);
    public static final StreamCodec<ByteBuf, CheckboxState> STREAM_CODEC = CodecUtil.enumStreamCodec(CheckboxState::values, CheckboxState::ordinal);
}
