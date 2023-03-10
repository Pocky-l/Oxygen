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
import net.mrscauthd.beyond_earth.machines.tile.NamedComponentRegistry;
import net.mrscauthd.beyond_earth.machines.tile.PowerSystemEnergyCommon;
import net.mrscauthd.beyond_earth.machines.tile.PowerSystemRegistry;
import net.mrscauthd.beyond_earth.registries.EffectsRegistry;
import org.jetbrains.annotations.NotNull;
import su.gamepoint.pocky.oxygen.data.StateSpaceship;
import su.gamepoint.pocky.oxygen.gui.OxygenControllerGui;
import su.gamepoint.pocky.oxygen.registration.BlockEntityRegister;
import su.gamepoint.pocky.oxygen.utils.TickLimiter;

import java.util.List;
import java.util.Objects;

public class OxygenControllerTileEntity extends AbstractOxygenController {

    public static final int SLOT_OUTPUT_SINK = 2;
    public static final int SLOT_OUTPUT_SOURCE = 3;

    public static final int POWER_CONSUMPTION = 10;

    public static final int OXYGEN_CONSUMPTION = 1;

    private final TickLimiter SERVER_LIMITER = new TickLimiter(200, true);
    private final TickLimiter CLIENT_LIMITER = new TickLimiter(200, true);

    private final StateSpaceship STATE_SPACESHIP = new StateSpaceship();

    private boolean isActive = false;

    public OxygenControllerTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.OXYGEN_CONTROLLER.get(), pos, state);
    }

    @Override
    public void tickProcessing() {
        serverTick();
    }

    @Override
    public void tick() {
        super.tick();
        clientTick();
    }

    private void clientTick() {
        if (Objects.requireNonNull(this.getLevel()).isClientSide()) {
            if (CLIENT_LIMITER.check()) {
                areaCheck();
            }
            CLIENT_LIMITER.inc();
        }
    }

    private void serverTick() {
        if (SERVER_LIMITER.check()) {
            areaCheck();
        }

        if (STATE_SPACESHIP.getStatus()) {
            if (oxygenConsumption(OXYGEN_CONSUMPTION) && energyConsumption(POWER_CONSUMPTION)) {
                isActive = true;
                if (SERVER_LIMITER.check()) {
                    fillRoomWithOxygen();
                }
            }
        } else {
            isActive = false;
        }
        SERVER_LIMITER.inc();
    }

    @Override
    protected boolean canActivated() {
        return isActive;
    }

    public boolean oxygenConsumption(int count) {
        if (getOxygenTank().getOxygenStored() >= count) {
            getOxygenTank().extractOxygen(count, false);
            return true;
        }
        return false;
    }

    public boolean energyConsumption(int count) {
        if (Objects.requireNonNull(getPrimaryEnergyStorage()).getEnergyStored() >= count) {
            getPrimaryEnergyStorage().extractEnergy(count, false);
            return true;
        }
        return false;
    }

    public void fillRoomWithOxygen() {
        for(Player player : Objects.requireNonNull(this.getLevel()).players()) {
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

    public void areaCheck() {
        STATE_SPACESHIP.findAreaRoom(this.getLevel(), this.getBlockPos());
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
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
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
    protected int getInitialInventorySize() {
        return super.getInitialInventorySize() + 2;
    }

    public int getOutputSourceSlot() {
        return SLOT_OUTPUT_SOURCE;
    }

    public int getOutputSinkSlot() {
        return SLOT_OUTPUT_SINK;
    }

    public StateSpaceship getStateSpaceship() {
        return STATE_SPACESHIP;
    }
}
