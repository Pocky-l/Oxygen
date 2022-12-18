package su.gamepoint.pocky.oxygen.blocks.entities.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.mrscauthd.beyond_earth.capabilities.energy.EnergyStorageBasic;
import net.mrscauthd.beyond_earth.capabilities.oxygen.OxygenUtil;
import net.mrscauthd.beyond_earth.crafting.BeyondEarthRecipeType;
import net.mrscauthd.beyond_earth.crafting.BeyondEarthRecipeTypes;
import net.mrscauthd.beyond_earth.crafting.OxygenMakingRecipeAbstract;
import net.mrscauthd.beyond_earth.machines.tile.NamedComponentRegistry;
import net.mrscauthd.beyond_earth.machines.tile.PowerSystemEnergyCommon;
import net.mrscauthd.beyond_earth.machines.tile.PowerSystemRegistry;
import net.mrscauthd.beyond_earth.registries.EffectsRegistry;
import su.gamepoint.pocky.oxygen.data.StateSpaceship;
import su.gamepoint.pocky.oxygen.gui.OxygenControllerGui;
import su.gamepoint.pocky.oxygen.registration.BlockEntityRegister;
import su.gamepoint.pocky.oxygen.utils.TickLimiter;

import java.util.List;

public class OxygenControllerTileEntity extends AbstractOxygenController {

    public static final int SLOT_OUTPUT_SINK = 2;
    public static final int SLOT_OUTPUT_SOURCE = 3;

    private final TickLimiter LIMITER = new TickLimiter(200, true);

    private final StateSpaceship STATE_SPACESHIP = new StateSpaceship();

    private boolean isActived = false;

    public OxygenControllerTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.OXYGEN_CONTROLLER.get(), pos, state);
    }

    @Override
    public void tickProcessing() {
        if (!this.getLevel().isClientSide()) {

            if (LIMITER.check()) {
                areaCheck();
            }

            if (STATE_SPACESHIP.getStatus()) {
                if (oxygenConsumption(1) && energyConsumption(10)) {
                    isActived = true;
                    if (LIMITER.check()) {
                        fillRoomWithOxygen();
                    }
                }
            } else {
                isActived = false;
            }
            LIMITER.inc();
        }
    }

    @Override
    protected boolean canActivated() {
        return isActived;
    }

    public boolean oxygenConsumption(int count) {
        if (getOxygenTank().getOxygenStored() >= count) {
            getOxygenTank().extractOxygen(count, false);
            return true;
        }
        return false;
    }

    public boolean energyConsumption(int count) {
        if (getPrimaryEnergyStorage().getEnergyStored() >= count) {
            getPrimaryEnergyStorage().extractEnergy(count, false);
            return true;
        }
        return false;
    }

    public void fillRoomWithOxygen() {
        for(Player player : this.getLevel().players()) {
            if (STATE_SPACESHIP.getArea().contains(player.getOnPos().above())) {
                player.addEffect(new MobEffectInstance(
                        EffectsRegistry.OXYGEN_EFFECT.get(),
                        15 * 20,
                        1,
                        false,
                        false)
                );
            }
        }
    }

    public boolean areaCheck() {
        STATE_SPACESHIP.findAreaRoom(this.getLevel(), this.getBlockPos());
        return STATE_SPACESHIP.getStatus();
    }

    protected void drainSources() {
        OxygenUtil.drainSource(this.getItemHandler(), this.getOutputSourceSlot(), this.getOxygenTank(), this.getTransferPerTick());
    }

    protected void fillSinks() {
        OxygenUtil.fillSink(this.getItemHandler(), this.getOutputSinkSlot(), this.getOxygenTank(), this.getTransferPerTick());
    }

    @Override
    protected boolean onCanPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == this.getOutputSourceSlot()) {
            return OxygenUtil.canExtract(stack);
        } else if (index == this.getOutputSinkSlot()) {
            return OxygenUtil.canReceive(stack);
        }

        return super.onCanPlaceItemThroughFace(index, stack, direction);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == this.getOutputSourceSlot()) {
            return !OxygenUtil.canExtract(stack);
        } else if (index == this.getOutputSinkSlot()) {
            return !OxygenUtil.canReceive(stack);
        }

        return super.canTakeItemThroughFace(index, stack, direction);
    }

    @Override
    protected void getSlotsForFace(Direction direction, List<Integer> slots) {
        super.getSlotsForFace(direction, slots);
        slots.add(this.getOutputSourceSlot());
        slots.add(this.getOutputSinkSlot());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new OxygenControllerGui.GuiContainer(id, inventory, this);
    }

    @Override
    protected void createFluidHandlers(NamedComponentRegistry<IFluidHandler> registry) {
        super.createFluidHandlers(registry);
        this.createOxygenTank(this.getOutputTankName());
    }

    @Override
    protected void createEnergyStorages(NamedComponentRegistry<IEnergyStorage> registry) {
        super.createEnergyStorages(registry);
        int capacity = 50000;
        int maxTransfer = 1000;
        registry.put(new EnergyStorageBasic(this, capacity, maxTransfer, capacity));
    }

    @Override
    public boolean hasSpaceInOutput() {
        return false;
    }

    @Override
    protected int getInitialTankCapacity(ResourceLocation name) {
        if (name.equals(this.getOutputTankName())) {
            return 10000;
        } else {
            return super.getInitialTankCapacity(name);
        }
    }

    @Override
    public int getTransferPerTick() {
        return 10;
    }

    @Override
    protected void createPowerSystems(PowerSystemRegistry map) {
        super.createPowerSystems(map);

        map.put(new PowerSystemEnergyCommon(this) {
            @Override
            public int getBasePowerForOperation() {
                return OxygenControllerTileEntity.this.getBasePowerForOperation();
            }
        });
    }

    public int getBasePowerForOperation() {
        return 10;
    }

    @Override
    public BeyondEarthRecipeType<? extends OxygenMakingRecipeAbstract> getRecipeType() {
        return BeyondEarthRecipeTypes.OXYGEN_LOADING;
    }

    @Override
    protected int getInitialInventorySize() {
        return super.getInitialInventorySize() + 2;
    }

    public int getOutputSourceSlot() {
        return SLOT_OUTPUT_SOURCE;
    }

    public int getOutputSinkSlot() {
        return SLOT_OUTPUT_SINK;
    }
}
