package at.minecraftschurli.mods.bibliocraft.content.printingtable;

import net.minecraft.world.item.ItemStackTemplate;

public abstract class PrintingTableBindingRecipe extends PrintingTableRecipe {
    public PrintingTableBindingRecipe(ItemStackTemplate result, int duration, String group, boolean showNotification) {
        super(result, duration, group, showNotification);
    }

    @Override
    public PrintingTableMode getMode() {
        return PrintingTableMode.BIND;
    }
}
