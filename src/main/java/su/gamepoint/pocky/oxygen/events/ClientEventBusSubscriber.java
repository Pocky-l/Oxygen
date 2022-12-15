package su.gamepoint.pocky.oxygen.events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.gui.OxygenControllerGuiWindow;
import su.gamepoint.pocky.oxygen.registration.ScreenRegister;

@Mod.EventBusSubscriber(modid = OxygenMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //GUIS
        MenuScreens.register(ScreenRegister.OXYGEN_CONTROLLER_GUI.get(), OxygenControllerGuiWindow::new);
    }
}
