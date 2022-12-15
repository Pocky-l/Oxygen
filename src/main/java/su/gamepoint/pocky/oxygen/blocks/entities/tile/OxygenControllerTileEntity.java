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
import net.mrscauthd.beyond_earth.capabilities.energy.EnergyStorageBasic;
import net.mrscauthd.beyond_earth.capabilities.oxygen.IOxygenStorage;
import net.mrscauthd.beyond_earth.capabilities.oxygen.OxygenUtil;
import net.mrscauthd.beyond_earth.crafting.BeyondEarthRecipeType;
import net.mrscauthd.beyond_earth.crafting.BeyondEarthRecipeTypes;
import net.mrscauthd.beyond_earth.crafting.OxygenMakingRecipeAbstract;
import net.mrscauthd.beyond_earth.machines.tile.NamedComponentRegistry;
import net.mrscauthd.beyond_earth.machines.tile.OxygenMakingBlockEntity;
import net.mrscauthd.beyond_earth.machines.tile.PowerSystemEnergyCommon;
import net.mrscauthd.beyond_earth.machines.tile.PowerSystemRegistry;
import net.mrscauthd.beyond_earth.registries.EffectsRegistry;
import su.gamepoint.pocky.oxygen.data.StateSpaceship;
import su.gamepoint.pocky.oxygen.gui.OxygenControllerGui;
import su.gamepoint.pocky.oxygen.registration.BlockEntityRegister;
import su.gamepoint.pocky.oxygen.utils.TickLimiter;

import java.util.List;

public class OxygenControllerTileEntity extends OxygenMakingBlockEntity {

    public static final int SLOT_OUTPUT_SINK = 2;
    public static final int SLOT_OUTPUT_SOURCE = 3;

    private final TickLimiter LIMITER = new TickLimiter(200);

    private final StateSpaceship STATE_SPACESHIP = new StateSpaceship();

    public OxygenControllerTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.OXYGEN_CONTROLLER.get(), pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getLevel().isClientSide()) {

            LIMITER.inc();

            if (LIMITER.check()) {
                areaCheck();
            }

            if (STATE_SPACESHIP.getStatus()) {
                if (oxygenConsumption(1) && energyConsumption(10)) {
                    if (LIMITER.check()) {
                        fillRoomWithOxygen();
                    }
                }
            }
        }
    }

    public boolean oxygenConsumption(int count) {
        if (getOutputTank().getOxygenStored() >= count) {
            getOutputTank().extractOxygen(count, false);
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

    @Override
    protected void drainSources() {
        super.drainSources();
        OxygenUtil.drainSource(this.getItemHandler(), this.getOutputSourceSlot(), this.getOutputTank(), this.getTransferPerTick());
    }

    @Override
    protected void fillSinks() {
        super.fillSinks();
        OxygenUtil.fillSink(this.getItemHandler(), this.getOutputSinkSlot(), this.getOutputTank(), this.getTransferPerTick());
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
    protected void createEnergyStorages(NamedComponentRegistry<IEnergyStorage> registry) {
        super.createEnergyStorages(registry);
        int capacity = 50000;
        int maxTransfer = 1000;
        registry.put(new EnergyStorageBasic(this, capacity, maxTransfer, capacity));
    }

    @Override
    protected int getInitialTankCapacity(ResourceLocation name) {
        if (name.equals(this.getInputTankName())) {
            return 2000;
        } else if (name.equals(this.getOutputTankName())) {
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

    public boolean isSourceSlot(int slot) {
        return slot == this.getOutputSourceSlot() || super.isSourceSlot(slot);
    }

    public boolean isSinkSlot(int slot) {
        return slot == this.getOutputSinkSlot() || super.isSinkSlot(slot);
    }

    public IOxygenStorage slotToOxygenTank(int slot) {
        if (slot == this.getOutputSourceSlot() || slot == this.getOutputSinkSlot()) {
            return this.getOutputTank();
        } else {
            return super.slotToOxygenTank(slot);
        }
    }

    public ResourceLocation slotToTankName(int slot) {
        if (slot == this.getOutputSourceSlot() || slot == this.getOutputSinkSlot()) {
            return this.getOutputTankName();
        } else {
            return super.slotToTankName(slot);
        }
    }

    public int getOutputSourceSlot() {
        return SLOT_OUTPUT_SOURCE;
    }

    public int getOutputSinkSlot() {
        return SLOT_OUTPUT_SINK;
    }
}
