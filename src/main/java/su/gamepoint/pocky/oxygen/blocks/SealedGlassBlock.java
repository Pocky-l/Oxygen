package su.gamepoint.pocky.oxygen.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SealedGlassBlock extends Block {

    public SealedGlassBlock() {
        super(Properties.of(Material.GLASS)
                .explosionResistance(-1)
                .sound(SoundType.GLASS)
                .noOcclusion()
        );
    }
}
