package su.gamepoint.pocky.oxygen.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class SealedPlatingBlock extends Block {

    public SealedPlatingBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .explosionResistance(-1)
                .sound(SoundType.METAL)
        );
    }
}
