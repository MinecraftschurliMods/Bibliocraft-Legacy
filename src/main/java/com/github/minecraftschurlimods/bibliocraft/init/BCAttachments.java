package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

public interface BCAttachments {
    Supplier<AttachmentType<ClipboardAttachment>> CLIPBOARD = BCRegistries.ATTACHMENT_TYPES.register("clipboard", () -> AttachmentType.serializable(ClipboardAttachment::new).build());

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
