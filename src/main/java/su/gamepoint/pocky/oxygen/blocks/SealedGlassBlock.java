package su.gamepoint.pocky.oxygen.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

public class SealedGlassBlock extends Block {

    public SealedGlassBlock() {
        super(Properties.of(Material.GLASS)
                .strength(0.3F)
                .explosionResistance(-1)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isValidSpawn(SealedGlassBlock::never)
                .isRedstoneConductor(SealedGlassBlock::never)
                .isSuffocating(SealedGlassBlock::never)
                .isViewBlocking(SealedGlassBlock::never)
        );
    }

    @Override
    public float getShadeBrightness(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    public boolean skipRendering(@NotNull BlockState blockState, BlockState adjacentBlockState, @NotNull Direction direction) {
        return adjacentBlockState.getBlock() instanceof SealedGlassBlock;
    }

    private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return false;
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}
