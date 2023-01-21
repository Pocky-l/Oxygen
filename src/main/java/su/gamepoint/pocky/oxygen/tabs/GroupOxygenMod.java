package su.gamepoint.pocky.oxygen.tabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.gamepoint.pocky.oxygen.registration.BlockRegister;

public class GroupOxygenMod extends CreativeModeTab {
    public GroupOxygenMod(String label) {
        super(label);
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(BlockRegister.OXYGEN_CONTROLLER_ITEM.get());
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }
}
