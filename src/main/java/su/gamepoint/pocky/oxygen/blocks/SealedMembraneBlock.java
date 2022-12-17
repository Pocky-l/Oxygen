package su.gamepoint.pocky.oxygen.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SealedMembraneBlock extends IronBarsBlock implements BeaconBeamBlock {

    private final DyeColor color;

    public SealedMembraneBlock() {
        super(Properties.of(Material.MOSS)
                .explosionResistance(0.2F)
                .sound(SoundType.MOSS)
                .noOcclusion()
                .noCollission()
                .isSuffocating(SealedMembraneBlock::never)
                .isViewBlocking(SealedMembraneBlock::never)
        );

        this.color = DyeColor.BLUE;

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(NORTH, Boolean.FALSE)
                        .setValue(EAST, Boolean.FALSE)
                        .setValue(SOUTH, Boolean.FALSE)
                        .setValue(WEST, Boolean.FALSE)
                        .setValue(WATERLOGGED, Boolean.FALSE)
        );
    }

    @Override
    public DyeColor getColor() {
        return this.color;
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}
