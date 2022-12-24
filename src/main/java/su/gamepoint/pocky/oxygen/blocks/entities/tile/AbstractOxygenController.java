package su.gamepoint.pocky.oxygen.blocks.entities.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.mrscauthd.beyond_earth.capabilities.oxygen.IOxygenStorage;
import net.mrscauthd.beyond_earth.capabilities.oxygen.OxygenStorage;
import net.mrscauthd.beyond_earth.compats.CompatibleManager;
import net.mrscauthd.beyond_earth.compats.mekanism.MekanismHelper;
import net.mrscauthd.beyond_earth.compats.mekanism.OxygenStorageGasAdapter;
import net.mrscauthd.beyond_earth.crafting.BeyondEarthRecipeType;
import net.mrscauthd.beyond_earth.crafting.OxygenMakingRecipeAbstract;
import net.mrscauthd.beyond_earth.gauge.GaugeValueHelper;
import net.mrscauthd.beyond_earth.gauge.IGaugeValue;
import net.mrscauthd.beyond_earth.machines.tile.AbstractMachineBlockEntity;
import net.mrscauthd.beyond_earth.machines.tile.NamedComponentRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractOxygenController extends AbstractMachineBlockEntity {

    public static final ResourceLocation TANK_OXYGEN = new ResourceLocation("beyond_earth", "output");
    private OxygenStorage oxygenTank;

    public AbstractOxygenController(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        this.getOxygenTank().deserializeNBT(compound.getCompound("outputTank"));
    }

    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("outputTank", this.getOxygenTank().serializeNBT());
    }

    public List<IGaugeValue> getGaugeValues() {
        List<IGaugeValue> list = super.getGaugeValues();
        if (!CompatibleManager.MEKANISM.isLoaded()) {
            list.add(GaugeValueHelper.getOxygen(this.getOxygenTank()));
        }

        return list;
    }

    protected void createFluidHandlers(NamedComponentRegistry<IFluidHandler> registry) {
        super.createFluidHandlers(registry);
        this.oxygenTank = this.createOxygenTank(this.getOutputTankName());
    }

    protected int getInitialTankCapacity(ResourceLocation name) {
        return 3000;
    }

    protected OxygenStorage createOxygenTank(ResourceLocation name) {
        return new OxygenStorage((oxygenStorage, oxygenDelta) -> AbstractOxygenController.this.setChanged(),
                this.getInitialTankCapacity(name));
    }

    public abstract void tickProcessing();

    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        return CompatibleManager.MEKANISM.isLoaded()
                && capability == MekanismHelper.getGasHandlerCapability()
                ? LazyOptional.of(
                        () -> new OxygenStorageGasAdapter(this.getOxygenTank(), true, true)
                )
                .cast() : super.getCapability(capability, facing);
    }

    protected void getSlotsForFace(Direction direction, List<Integer> slots) {
        super.getSlotsForFace(direction, slots);
    }

    protected int getInitialInventorySize() {
        return super.getInitialInventorySize() + 2;
    }

    public int getMaxStackSize() {
        return 1;
    }

    public ResourceLocation getOutputTankName() {
        return TANK_OXYGEN;
    }

    public IOxygenStorage getOxygenTank() {
        return this.oxygenTank;
    }

    public int getTransferPerTick() {
        return 256;
    }

}
