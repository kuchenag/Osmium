package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.util.TextureUtil;
import com.intro.client.util.Vector2d;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


/**
 * <h1>ye who enter here abandon all hope</h1>
 * <p>This is by far the wost class I have ever had the torment of writing.</p>
 * <p>Nothing here makes sense, please don't look into it.</p>
 * <p>But if you do, see the comments to avoid wasting 8 hours of your life</p>
 *
 * @author Intro
 * @since 1.0.7
 * @see Color
 * @see com.intro.common.config.Options
 * @see com.intro.client.render.screen.OsmiumBlockOptionsScreen
 */
public class ColorOptionWidget extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {

    private final String optionId;

    private static final Minecraft mc = Minecraft.getInstance();

    private final ResourceLocation BAKED_TEXTURE = new ResourceLocation("osmium", "/textures/gui/gradient.png");

    private final NativeImage TEXTURE;

    private final int x;
    private final int y;

    // width and height are always the same as baked image
    private final int width;
    private final int height;


    /*
    I have no idea how this works
    On class init it loads a NativeImage object
    from an identifier, but after that you have to load the image
    again from a shader on the first render.
    you have to do both, or it won't work
    please help me

    edit: turns out im stupid, and it doesn't do all that
    */
    public ColorOptionWidget(int x, int y, int width, int height, String optionId) {
        this.optionId = optionId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.TEXTURE = TextureUtil.getImageAtLocation(BAKED_TEXTURE);
    }


    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BAKED_TEXTURE);
        blit(matrices, x, y, 0, 0, width, height, width, height);
        drawCenteredString(matrices, mc.font, OsmiumClient.options.getColorOption(this.optionId).color.toStringNoAlpha(), x + (width / 2), y + height + 20, 0xffffff);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.widget.color_picker"), x + (width / 2), y - 20, 0xffffff);
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isPositionWithinBounds((int) mouseX, ((int) mouseY))) {
            Vector2d vec2 = this.getImagePixels((int) mouseX, (int) mouseY);
            int color;
            // for some reason NativeImage isn't reading color data properly.
            // so we have to get the base pixel data
            int[] bytes;
            bytes = TEXTURE.makePixelArray();
            color = getColorAtLocation(bytes, TEXTURE, (int) vec2.getX(), (int) vec2.getY());

            OsmiumClient.options.getColorOption(this.optionId).color = new Color(color);
        }
        return GuiEventListener.super.mouseClicked(mouseX, mouseY, button);
    }


    // gets image coordinates from screen coordinates
    public Vector2d getImagePixels(int screenX, int screenY) {
        Screen screen = mc.screen;
        // compute scale
        double imageScale = Math.min(screen.width / width, screen.height / height);

        // compute image offset
        double scaledWidth = width * imageScale;
        double scaledHeight = height * imageScale;

        // clamp image coordinates
        if(screenX < this.x || this.x + scaledWidth < screenX) {
            return null;
        }
        if(screenY < this.y || this.y + scaledHeight < screenY) {
            return null;
        }

        // normalize output
        double x = (screenX - this.x) / imageScale;
        double y = (screenY - this.y) / imageScale;

        // de-scale output
        x *= 2;
        y *= 2;

        return new Vector2d(x, y);
    }

    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.x  && x < this.x + this.width && y > this.y && y < this.y + height;
    }

    
    public int getColorAtLocation(int @NotNull [] arr, @NotNull NativeImage texture, int x, int y) {
        float scale = texture.getWidth() / width;
        int scaledX = (int) (x * scale);
        int scaledY = (int) (y * scale);

        int index = (texture.getWidth() * scaledY) + scaledX;
        return arr[index];
    }


    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }
}
