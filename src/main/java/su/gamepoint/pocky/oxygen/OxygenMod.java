package su.gamepoint.pocky.oxygen;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import su.gamepoint.pocky.oxygen.registration.BlockEntityRegister;
import su.gamepoint.pocky.oxygen.registration.BlockRegister;
import su.gamepoint.pocky.oxygen.registration.ItemRegister;
import su.gamepoint.pocky.oxygen.registration.ScreenRegister;
import su.gamepoint.pocky.oxygen.tabs.GroupOxygenMod;

@Mod(OxygenMod.MODID)
@Mod.EventBusSubscriber(modid = OxygenMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OxygenMod {

    public static final CreativeModeTab CREATIVE_MODE_TAB = new GroupOxygenMod(OxygenMod.MODID);

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

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegister.SEALED_GLASS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlockRegister.SEALED_MEMBRANE.get(), RenderType.translucent());
    }
}
