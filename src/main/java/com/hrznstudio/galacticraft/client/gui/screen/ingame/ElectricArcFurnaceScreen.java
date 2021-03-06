/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.client.gui.screen.ingame;

import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.api.screen.MachineHandledScreen;
import com.hrznstudio.galacticraft.client.gui.widget.machine.CapacitorWidget;
import com.hrznstudio.galacticraft.screen.ElectricArcFurnaceScreenHandler;
import com.hrznstudio.galacticraft.util.DrawableUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ElectricArcFurnaceScreen extends MachineHandledScreen<ElectricArcFurnaceScreenHandler> {
    private static final Identifier BACKGROUND = new Identifier(Constants.MOD_ID, Constants.ScreenTextures.getRaw(Constants.ScreenTextures.ELECTRIC_ARC_FURNACE_SCREEN));

    private static final int ARROW_X = 78;
    private static final int ARROW_Y = 24;

    private static final int LIT_ARROW_X = 176;
    private static final int LIT_ARROW_Y = 0;

    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 15;

    public ElectricArcFurnaceScreen(ElectricArcFurnaceScreenHandler screenHandler, PlayerInventory playerInventory, Text title) {
        super(screenHandler, playerInventory, screenHandler.machine.getWorld(), screenHandler.machine.getPos(), title);
        addWidget(new CapacitorWidget(screenHandler.machine.getCapacitor(), 8, 29, 48, this::getEnergyTooltipLines, screenHandler.machine::getStatus));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        this.client.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (handler.machine.cookLength != 0 && handler.machine.cookTime != 0) {
            double scale = ((double)handler.machine.cookTime) / ((double)handler.machine.cookLength);

            this.drawTexture(matrices, this.x + ARROW_X, this.y + ARROW_Y, LIT_ARROW_X, LIT_ARROW_Y, (int) (((double)ARROW_WIDTH) * scale), ARROW_HEIGHT);
        }

        DrawableUtils.drawCenteredString(matrices, textRenderer, new TranslatableText("block.galacticraft-rewoven.electric_arc_furnace"), this.width / 2, this.y + 5, Formatting.DARK_GRAY.getColorValue());
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(stack, mouseX, mouseY);
    }
}
