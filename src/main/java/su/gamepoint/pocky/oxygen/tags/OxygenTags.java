package su.gamepoint.pocky.oxygen.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import su.gamepoint.pocky.oxygen.OxygenMod;

public class OxygenTags {
    public static class Blocks {

        public static final TagKey<Block> SEALED = createTag("sealed");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(new ResourceLocation(OxygenMod.MODID, name));
        }
    }
}
