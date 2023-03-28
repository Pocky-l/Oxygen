package su.gamepoint.pocky.oxygen.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import su.gamepoint.pocky.oxygen.tags.OxygenTags;

import java.util.HashSet;
import java.util.Set;

public class StateSpaceship {

    private final Set<BlockPos> area = new HashSet<>();

    private boolean status = false;

    private final int AREA_LIMIT = 4096;

    private Integer size = 0;

    public void findAreaRoom(Level level, BlockPos startPos) {
        area.clear();
        findArea(area, level, startPos);

        if (area.size() > AREA_LIMIT) {
            area.clear();
            status = false;
        } else {
            size = area.size();
            status = true;
        }
    }

    public boolean getStatus() {
        return status;
    }

    public Set<BlockPos> getArea() {
        return area;
    }

    private void findArea(Set<BlockPos> area, Level level, BlockPos startPos) {

        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();

        findArea0(area, level, x, y + 1, z);
        findArea0(area, level, x, y - 1, z);
        findArea0(area, level, x + 1, y, z);
        findArea0(area, level, x - 1, y, z);
        findArea0(area, level, x, y, z + 1);
        findArea0(area, level, x, y, z - 1);
    }

    private void findArea0(Set<BlockPos> area, Level level, int x, int y, int z) {
        if (area.size() > AREA_LIMIT) return;

        BlockPos pos = new BlockPos(x, y, z);

        if (area.contains(pos)) return;

        area.add(pos);
        if (!level.getBlockState(pos).is(OxygenTags.Blocks.SEALED)) {
            findArea(area, level, pos);
        }

    }

    public Integer getSize() {
        return size;
    }
}
