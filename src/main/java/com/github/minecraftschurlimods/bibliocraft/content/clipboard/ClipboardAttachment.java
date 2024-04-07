package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class ClipboardAttachment implements INBTSerializable<CompoundTag> {
    private static final int MAX_PAGES = 50;
    private static final String TITLE_KEY = "title";
    private static final String ACTIVE_KEY = "active";
    private static final String PAGES_KEY = "pages";
    private final List<ClipboardPage> pages;
    private int activePage;
    public String title = "";

    public ClipboardAttachment() {
        pages = new ArrayList<>();
        pages.add(new ClipboardPage());
        activePage = 0;
    }

    public ClipboardPage getActivePage() {
        return pages.get(activePage);
    }

    public int getActivePageIndex() {
        return activePage;
    }

    public void setActivePageIndex(int activePage) {
        this.activePage = activePage;
    }

    public int getPageCount() {
        return pages.size();
    }

    public boolean canHaveNewPage() {
        if (activePage >= pages.size() - 1) {
            activePage = pages.size() - 1;
            return activePage < MAX_PAGES;
        }
        return true;
    }

    public void nextPage() {
        if (activePage >= pages.size() - 1 && canHaveNewPage()) {
            addPage();
            activePage++;
        } else if (activePage < pages.size()) {
            activePage++;
        }
    }

    public void prevPage() {
        if (activePage > 0) {
            activePage--;
        }
    }

    public void addPage() {
        if (pages.size() < MAX_PAGES) {
            pages.add(new ClipboardPage());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(TITLE_KEY, title);
        tag.putInt(ACTIVE_KEY, activePage);
        ListTag listTag = new ListTag();
        pages.forEach(e -> listTag.add(e.serializeNBT()));
        tag.put(PAGES_KEY, listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        title = tag.getString(TITLE_KEY);
        activePage = tag.getInt(ACTIVE_KEY);
        pages.clear();
        ListTag listTag = tag.getList(PAGES_KEY, Tag.TAG_COMPOUND);
        for (Tag t : listTag) {
            ClipboardPage page = new ClipboardPage();
            page.deserializeNBT((CompoundTag) t);
            pages.add(page);
        }
    }
}
