package su.gamepoint.pocky.oxygen.registration;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import su.gamepoint.pocky.oxygen.OxygenMod;

public class ItemRegister {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, OxygenMod.MODID);

    //public static final RegistryObject<Item> ITEM_ID = REGISTRY.register("item_id", ItemClass::new);
}
