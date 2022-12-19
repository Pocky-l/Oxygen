package su.gamepoint.pocky.oxygen.items;

import net.minecraft.world.item.Item;
import su.gamepoint.pocky.oxygen.OxygenMod;

public class SealantItem extends Item {

    public SealantItem() {
        super(new Properties()
                .tab(OxygenMod.CREATIVE_MODE_TAB)
                .stacksTo(64)
        );
    }
}
