package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.DoubleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

public class DoubleSliderWidget extends OptionSliderWidget {

    private final DoubleOption AttachedOption;
    public final String key;
    private double minVal;
    private double maxVal;

    private double roundTo;

    public DoubleSliderWidget(MinecraftClient mc, int x, int y, int width, int height, DoubleOption doubleOption, String key, double minVal, double maxVal, double roundTo) {
        super(mc.options, x, y, width, height, doubleOption.variable);
        this.AttachedOption = doubleOption;
        this.key = key;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.roundTo = roundTo;
        this.updateMessage();
        this.applyValue();
    }

    @Override
    protected void updateMessage() {
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = MathHelper.clamp(scaledVal, minVal, maxVal);
        this.setMessage(new LiteralText(new TranslatableText(key).getString() + (Math.round(scaledVal * roundTo) / roundTo)));
    }

    @Override
    protected void applyValue() {
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = MathHelper.clamp(scaledVal, minVal, maxVal);
        ((DoubleOption) Osmium.options.get(AttachedOption.identifier)).variable = Math.round(scaledVal * roundTo) / roundTo;
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.value = MathHelper.clamp(value, 0, 1);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
