package su.gamepoint.pocky.oxygen.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.IContainerFactory;
import net.mrscauthd.beyond_earth.guis.helper.ContainerHelper;
import su.gamepoint.pocky.oxygen.blocks.entities.tile.OxygenControllerTileEntity;
import su.gamepoint.pocky.oxygen.registration.ScreenRegister;

public class OxygenControllerGui {
    public OxygenControllerGui() {
    }

    public static class GuiContainer extends AbstractContainerMenu {
        private OxygenControllerTileEntity blockEntity;

        public GuiContainer(int id, Inventory inv, OxygenControllerTileEntity blockEntity) {
            super(ScreenRegister.OXYGEN_CONTROLLER_GUI.get(), id);
            this.blockEntity = blockEntity;
            IItemHandlerModifiable internal = blockEntity.getItemHandler();
            this.addSlot(new SlotItemHandler(internal, 0, 26, 22));
            this.addSlot(new SlotItemHandler(internal, 2, 92, 52));
            this.addSlot(new SlotItemHandler(internal, 1, 26, 52));
            this.addSlot(new SlotItemHandler(internal, 3, 92, 22));
            ContainerHelper.addInventorySlots(this, inv, 8, 90, this::addSlot);
        }

        public OxygenControllerTileEntity getBlockEntity() {
            return this.blockEntity;
        }

        public boolean stillValid(Player player) {
            return !this.getBlockEntity().isRemoved();
        }

        public ItemStack quickMoveStack(Player playerIn, int index) {
            return ContainerHelper.transferStackInSlot(this, playerIn, index, this.getBlockEntity(),
                    this::moveItemStackTo);
        }
    }

    public static class GuiContainerFactory implements IContainerFactory<OxygenControllerGui.GuiContainer> {
        public GuiContainerFactory() {
        }

        public OxygenControllerGui.GuiContainer create(int id, Inventory inv, FriendlyByteBuf extraData) {
            BlockPos pos = extraData.readBlockPos();
            OxygenControllerTileEntity blockEntity = (OxygenControllerTileEntity) inv.player.level.getBlockEntity(pos);
            return new OxygenControllerGui.GuiContainer(id, inv, blockEntity);
        }
    }
}
