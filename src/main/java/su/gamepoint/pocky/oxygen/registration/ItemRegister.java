package su.gamepoint.pocky.oxygen.registration;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.items.SealantItem;

public class ItemRegister {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, OxygenMod.MODID);

    //public static final RegistryObject<Item> ITEM_ID = REGISTRY.register("item_id", ItemClass::new);
    public static final RegistryObject<Item> SEALANT = REGISTRY.register("sealant", SealantItem::new);
}
