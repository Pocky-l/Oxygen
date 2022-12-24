package su.gamepoint.pocky.oxygen.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrscauthd.beyond_earth.gauge.GaugeTextHelper;
import net.mrscauthd.beyond_earth.gauge.GaugeValueHelper;
import net.mrscauthd.beyond_earth.guis.helper.GuiHelper;
import net.mrscauthd.beyond_earth.utils.Rectangle2d;
import org.jetbrains.annotations.NotNull;
import su.gamepoint.pocky.oxygen.OxygenMod;
import su.gamepoint.pocky.oxygen.blocks.entities.tile.OxygenControllerTileEntity;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class OxygenControllerGuiWindow extends AbstractContainerScreen<OxygenControllerGui.GuiContainer> {
    public static final ResourceLocation texture = new ResourceLocation(OxygenMod.MODID, "textures/screen/oxygen_controller.png");

    public static final int TANK_OXYGEN_LEFT = 9;
    public static final int TANK_OXYGEN_TOP = 21;

    public static final int ENERGY_LEFT = 144;
    public static final int ENERGY_TOP = 21;

    private final OxygenControllerTileEntity blockEntity = this.getMenu().getBlockEntity();

    public OxygenControllerGuiWindow(OxygenControllerGui.GuiContainer container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 177;
        this.imageHeight = 172;
        this.inventoryLabelY = this.imageHeight - 92;
    }

    @Override
    public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);

        if (GuiHelper.isHover(this.getOxygenTankBounds(), mouseX, mouseY)) {
            this.renderTooltip(ms, GaugeTextHelper.getStorageText(GaugeValueHelper.getOxygen(blockEntity.getOxygenTank())).build(), mouseX, mouseY);
        } else if (GuiHelper.isHover(this.getEnergyBounds(), mouseX, mouseY)) {
            this.renderTooltip(ms, GaugeTextHelper.getStorageText(GaugeValueHelper.getEnergy(blockEntity)).build(), mouseX, mouseY);
        }

        this.font.draw(ms,
                new TranslatableComponent("oxygen.gui.message.station.size", blockEntity.getStateSpaceship().getSize()),
                this.leftPos + 25,
                this.topPos + 27,
                3947580);

        this.font.draw(ms,
                new TranslatableComponent("oxygen.gui.message.station.power", OxygenControllerTileEntity.POWER_CONSUMPTION),
                this.leftPos + 25,
                this.topPos + 27 + 14,
                3947580);

        this.font.draw(ms,
                new TranslatableComponent("oxygen.gui.message.station.oxygen", OxygenControllerTileEntity.OXYGEN_CONSUMPTION),
                this.leftPos + 25,
                this.topPos + 27 + 14 + 14,
                3947580);

    }

    @Override
    protected void renderBg(@NotNull PoseStack ms, float p_97788_, int p_97789_, int p_97790_) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        GuiComponent.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        OxygenControllerTileEntity blockEntity = this.getMenu().getBlockEntity();
        GuiHelper.drawEnergy(ms, this.leftPos + ENERGY_LEFT, this.topPos + ENERGY_TOP, Objects.requireNonNull(blockEntity.getPrimaryEnergyStorage()));

        GuiHelper.drawOxygenTank(ms, this.leftPos + TANK_OXYGEN_LEFT, this.topPos + TANK_OXYGEN_TOP, blockEntity.getOxygenTank());
    }

    public Rectangle2d getOxygenTankBounds() {
        return GuiHelper.getFluidTankBounds(this.leftPos + TANK_OXYGEN_LEFT, this.topPos + TANK_OXYGEN_TOP);
    }

    public Rectangle2d getEnergyBounds() {
        return GuiHelper.getEnergyBounds(this.leftPos + ENERGY_LEFT, this.topPos + ENERGY_TOP);
    }
}
