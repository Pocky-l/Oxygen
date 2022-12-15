package su.gamepoint.pocky.oxygen;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import su.gamepoint.pocky.oxygen.registration.BlockEntityRegister;
import su.gamepoint.pocky.oxygen.registration.BlockRegister;
import su.gamepoint.pocky.oxygen.registration.ItemRegister;
import su.gamepoint.pocky.oxygen.registration.ScreenRegister;
import su.gamepoint.pocky.oxygen.tabs.GroupOxygenMod;

@Mod(OxygenMod.MODID)
@Mod.EventBusSubscriber(modid = OxygenMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OxygenMod {

    public static final CreativeModeTab CREATIVE_MODE_TAB = new GroupOxygenMod(OxygenMod.MODID);

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "oxygen";

    public OxygenMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockRegister.REGISTRY_BLOCK.register(modEventBus);
        BlockRegister.REGISTRY_ITEM.register(modEventBus);
        ItemRegister.REGISTRY.register(modEventBus);
        BlockEntityRegister.BLOCK_ENTITIES.register(modEventBus);
        ScreenRegister.REGISTRY_SCREEN.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
