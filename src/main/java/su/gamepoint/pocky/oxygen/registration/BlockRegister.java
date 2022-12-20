package su.gamepoint.pocky.oxygen.registration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.blocks.SealedGlassBlock;
import su.gamepoint.pocky.oxygen.blocks.SealedMembraneBlock;
import su.gamepoint.pocky.oxygen.blocks.entities.block.OxygenControllerBlock;
import su.gamepoint.pocky.oxygen.blocks.SealedPlatingBlock;
import su.gamepoint.pocky.oxygen.utils.Color;

import java.util.HashMap;
import java.util.Map;

public class BlockRegister {

    public static final DeferredRegister<Block> REGISTRY_BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, OxygenMod.MODID);
    public static final DeferredRegister<Item> REGISTRY_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, OxygenMod.MODID);

    //blocks
    //public static final RegistryObject<Block> BLOCK_NAME = REGISTRY_BLOCK.register("block_id", BlockClass::new);
    public static final RegistryObject<Block> SEALED_PLATING = REGISTRY_BLOCK.register("sealed_plating", SealedPlatingBlock::new);

    public static final Map<String, RegistryObject<Block>> COLORED_BLOCKS = createColoredBlocks();

    public static final RegistryObject<Block> SEALED_GLASS = REGISTRY_BLOCK.register("sealed_glass", SealedGlassBlock::new);
    public static final RegistryObject<Block> SEALED_MEMBRANE = REGISTRY_BLOCK.register("sealed_membrane", SealedMembraneBlock::new);
    public static final RegistryObject<Block> OXYGEN_CONTROLLER = REGISTRY_BLOCK.register("oxygen_controller", OxygenControllerBlock::new);

    //items
    //public static final RegistryObject<Item> BLOCK_NAME_ITEM = block(BLOCK_NAME, OxygenMod.CREATIVE_MODE_TAB);
    public static final RegistryObject<Item> SEALED_PLATING_ITEM = block(SEALED_PLATING, OxygenMod.CREATIVE_MODE_TAB);
    public static final RegistryObject<Item> SEALED_GLASS_ITEM = block(SEALED_GLASS, OxygenMod.CREATIVE_MODE_TAB);
    public static final RegistryObject<Item> SEALED_MEMBRANE_ITEM = block(SEALED_MEMBRANE, OxygenMod.CREATIVE_MODE_TAB);

    public static final RegistryObject<Item> OXYGEN_CONTROLLER_ITEM = block(OXYGEN_CONTROLLER, OxygenMod.CREATIVE_MODE_TAB);

    public static final Map<String, RegistryObject<Item>> COLORED_ITEMS = createColoredItems();

    private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
        return REGISTRY_ITEM.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    private static Map<String, RegistryObject<Block>> createColoredBlocks() {
        var coloredBlocks = new HashMap<String, RegistryObject<Block>>();

        for (Color color : Color.values()) {
            coloredBlocks.put("sealed_plating_" + color.getColorName(),
                    REGISTRY_BLOCK.register("sealed_plating_" + color.getColorName(), SealedPlatingBlock::new));
        }

        return coloredBlocks;
    }

    private static Map<String, RegistryObject<Item>> createColoredItems() {
        var coloredItems = new HashMap<String, RegistryObject<Item>>();

        COLORED_BLOCKS.forEach((k, e) -> {
            coloredItems.put(k, block(e, OxygenMod.CREATIVE_MODE_TAB));
        });

        return coloredItems;
    }
}
