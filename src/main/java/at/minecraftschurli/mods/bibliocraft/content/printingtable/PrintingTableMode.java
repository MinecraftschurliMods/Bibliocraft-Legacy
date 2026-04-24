package at.minecraftschurli.mods.bibliocraft.content.printingtable;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.util.CodecUtil;
import at.minecraftschurli.mods.bibliocraft.util.StringRepresentableEnum;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum PrintingTableMode implements StringRepresentableEnum {
    BIND, CLONE, MERGE;
    public static final Codec<PrintingTableMode> CODEC = CodecUtil.enumCodec(PrintingTableMode::values);
    public static final StreamCodec<ByteBuf, PrintingTableMode> STREAM_CODEC = CodecUtil.enumStreamCodec(PrintingTableMode::values, PrintingTableMode::ordinal);

    public String getTranslationKey() {
        return "gui." + BibliocraftApi.MOD_ID + ".printing_table.mode." + getSerializedName();
    }
}
