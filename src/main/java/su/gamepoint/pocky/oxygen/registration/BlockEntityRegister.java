package su.gamepoint.pocky.oxygen.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.blocks.entities.tile.OxygenControllerTileEntity;

public class BlockEntityRegister {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITIES, OxygenMod.MODID);

    public static final RegistryObject<BlockEntityType<OxygenControllerTileEntity>> OXYGEN_CONTROLLER = BLOCK_ENTITIES.register(
            "oxygen_controller", () -> BlockEntityType.Builder.of(OxygenControllerTileEntity::new,
                    BlockRegister.OXYGEN_CONTROLLER.get()).build(null));

}
