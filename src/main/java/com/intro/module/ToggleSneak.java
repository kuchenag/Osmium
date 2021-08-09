package com.intro.module;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import com.intro.config.options.Vector2Option;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import com.intro.render.drawables.Text;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;

public class ToggleSneak {

    private boolean sprinting = false;

    public static boolean sneaking = false;

    //private boolean sneaking = false;
    private final Text SprintingText;

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public ToggleSneak() {
        Text tmp;
        try {
            tmp = new Text((int) ((Vector2Option) Osmium.options.get("ToggleSprintPosition").get()).x, (int) ((Vector2Option) Osmium.options.get("ToggleSprintPosition").get()).y, "Sprinting(Toggled)", 0xffffff);
        } catch (NullPointerException e) {
            OptionUtil.ShouldResaveOptions = true;
            OptionUtil.LOGGER.warn("Options file is corrupt, creating a new one!");
            tmp = new Text((int) ((Vector2Option) Osmium.options.get("ToggleSprintPosition").get()).x, (int) ((Vector2Option) Osmium.options.get("ToggleSprintPosition").get()).y, "Sprinting(Toggled)", 0xffffff);
        }
        SprintingText = tmp;
        SprintingText.guiElement = true;
        SprintingText.visible = false;
    }

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable || ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
                if(event instanceof EventTick && event.isPre()) {
                    Osmium.options.put("ToggleSprintPosition", new Vector2Option("ToggleSprintPosition", SprintingText.posX, SprintingText.posY));
                    if(mc.player.forwardSpeed > 0 && !mc.player.isUsingItem() && !mc.player.isSneaking() && !mc.player.horizontalCollision && this.sprinting)
                        mc.player.setSprinting(true);

                    if(mc.options.keySneak.wasPressed() && ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
                        sneaking = !sneaking;
                    }
                    if(mc.options.keySprint.wasPressed() && ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                        this.sprinting = !this.sprinting;
                    }



                    if((this.sprinting && ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) && (!sneaking)) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sprinting(Toggled)";
                    } else if (mc.options.keySprint.isPressed() && ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sprinting(Key Down)";
                    } else if(sneaking && ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sneaking(Toggled)";
                    } else if (mc.options.keySneak.isPressed() && ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sneaking(Key Down)";
                    } else {
                        SprintingText.visible = false;
                    }

                }
            } else if(SprintingText.visible) {
                SprintingText.visible = false;
            }
        }
    }
}
