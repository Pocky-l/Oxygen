package su.gamepoint.pocky.oxygen.blocks.entities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.mrscauthd.beyond_earth.machines.AbstractMachineBlock;
import su.gamepoint.pocky.oxygen.blocks.entities.tile.OxygenControllerTileEntity;

public class OxygenControllerBlock extends AbstractMachineBlock<OxygenControllerTileEntity> {

    public OxygenControllerBlock() {
        super(Block.Properties
                .of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(5f, 1f)
        );
    }

    @Override
    protected boolean useLit() {
        return true;
    }

    @Override
    protected boolean useFacing() {
        return true;
    }

    @Override
    public OxygenControllerTileEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OxygenControllerTileEntity(pos, state);
    }
}
