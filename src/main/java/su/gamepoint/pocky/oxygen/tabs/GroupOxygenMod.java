package su.gamepoint.pocky.oxygen.tabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import su.gamepoint.pocky.oxygen.registration.BlockRegister;

public class GroupOxygenMod extends CreativeModeTab {
    public GroupOxygenMod(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(BlockRegister.SEALED_PLATING_ITEM.get());
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }
}
