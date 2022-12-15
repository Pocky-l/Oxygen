package su.gamepoint.pocky.oxygen.registration;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.gui.OxygenControllerGui;

public class ScreenRegister {

    public static final DeferredRegister<MenuType<?>> REGISTRY_SCREEN = DeferredRegister.create(ForgeRegistries.CONTAINERS, OxygenMod.MODID);

    public static final RegistryObject<MenuType<OxygenControllerGui.GuiContainer>> OXYGEN_CONTROLLER_GUI = REGISTRY_SCREEN
            .register("oxygen_controller_gui", () -> new MenuType(new OxygenControllerGui.GuiContainerFactory()));

}
