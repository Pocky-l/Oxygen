package su.gamepoint.pocky.oxygen.registration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.blocks.SealedGlassBlock;
import su.gamepoint.pocky.oxygen.blocks.SealedMembraneBlock;
import su.gamepoint.pocky.oxygen.blocks.SealedPlatingBlock;
import su.gamepoint.pocky.oxygen.blocks.entities.block.OxygenControllerBlock;
import su.gamepoint.pocky.oxygen.utils.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockRegister {

    public static final DeferredRegister<Block> REGISTRY_BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, OxygenMod.MODID);
    public static final DeferredRegister<Item> REGISTRY_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, OxygenMod.MODID);

    //blocks
    //public static final RegistryObject<Block> BLOCK_NAME = REGISTRY_BLOCK.register("block_id", BlockClass::new);
    public static final RegistryObject<Block> SEALED_PLATING = REGISTRY_BLOCK.register("sealed_plating", SealedPlatingBlock::new);
    public static final RegistryObject<Block> SEALED_PLATING_STAIRS = REGISTRY_BLOCK.register("sealed_plating_stairs", () -> new StairBlock(() -> SEALED_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.copy(SEALED_PLATING.get())));
    public static final RegistryObject<Block> SEALED_PLATING_SLAB = REGISTRY_BLOCK.register("sealed_plating_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(SEALED_PLATING.get())));;

    public static final Map<String, RegistryObject<Block>> COLORED_BLOCKS = createColoredBlocks();

    public static final RegistryObject<Block> SEALED_GLASS = REGISTRY_BLOCK.register("sealed_glass", SealedGlassBlock::new);
    public static final RegistryObject<Block> SEALED_MEMBRANE = REGISTRY_BLOCK.register("sealed_membrane", SealedMembraneBlock::new);
    public static final RegistryObject<Block> OXYGEN_CONTROLLER = REGISTRY_BLOCK.register("oxygen_controller", OxygenControllerBlock::new);

    //items
    //public static final RegistryObject<Item> BLOCK_NAME_ITEM = block(BLOCK_NAME, OxygenMod.CREATIVE_MODE_TAB);
    public static final RegistryObject<Item> SEALED_PLATING_ITEM = block(SEALED_PLATING);
    public static final RegistryObject<Item> SEALED_PLATING_STAIRS_ITEM = block(SEALED_PLATING_STAIRS);
    public static final RegistryObject<Item> SEALED_PLATING_SLAB_ITEM = block(SEALED_PLATING_SLAB);
    public static final RegistryObject<Item> SEALED_GLASS_ITEM = block(SEALED_GLASS);
    public static final RegistryObject<Item> SEALED_MEMBRANE_ITEM = block(SEALED_MEMBRANE);

    public static final RegistryObject<Item> OXYGEN_CONTROLLER_ITEM = block(OXYGEN_CONTROLLER);

    public static final Map<String, RegistryObject<Item>> COLORED_ITEMS = createColoredItems();

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return REGISTRY_ITEM.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(OxygenMod.CREATIVE_MODE_TAB)));
    }

    private static Map<String, RegistryObject<Block>> createColoredBlocks() {
        var coloredBlocks = new HashMap<String, RegistryObject<Block>>();

        for (Color color : Color.values()) {
            coloredBlocks.put("sealed_plating_" + color.getColorName(),
                    REGISTRY_BLOCK.register("sealed_plating_" + color.getColorName(), SealedPlatingBlock::new));

            coloredBlocks.put("sealed_plating_stairs_" + color.getColorName(),
                    REGISTRY_BLOCK.register("sealed_plating_stairs_" + color.getColorName(), () -> new StairBlock(() -> SEALED_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.copy(SEALED_PLATING.get()))));
            coloredBlocks.put("sealed_plating_slab_" + color.getColorName(),
                    REGISTRY_BLOCK.register("sealed_plating_slab_" + color.getColorName(), () -> new SlabBlock(BlockBehaviour.Properties.copy(SEALED_PLATING.get()))));
        }

        return coloredBlocks;
    }

    private static Map<String, RegistryObject<Item>> createColoredItems() {
        var coloredItems = new HashMap<String, RegistryObject<Item>>();

        COLORED_BLOCKS.forEach((k, e) -> coloredItems.put(k, block(e)));

        return coloredItems;
    }
}
