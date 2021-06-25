package com.intro.module;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import com.intro.config.Vector2Option;
import com.intro.render.Text;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;

public class ToggleSneak extends Module{

    private boolean sprinting = false;
    //private boolean sneaking = false;
    private final Text SprintingText;



    public ToggleSneak() {
        super("ToggleSneak");
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

    public void OnEvent(Event event) {
        if(mc.player != null) {
            if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                if(event instanceof EventTick) {

                    Osmium.options.put("ToggleSprintPosition", new Vector2Option("ToggleSprintPosition", SprintingText.posX, SprintingText.posY));

                    if(mc.player.forwardSpeed > 0 && !mc.player.isUsingItem() && !mc.player.isSneaking() && !mc.player.horizontalCollision && this.sprinting)
                        mc.player.setSprinting(true);
                    if(mc.options.keySprint.wasPressed()) {
                        this.sprinting = !this.sprinting;
                    }

                    if(this.sprinting) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sprinting(Toggled)";
                    } else if (mc.options.keySprint.isPressed()) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sprinting(Key Down)";
                    } else {
                        SprintingText.visible = false;
                        SprintingText.text = "";
                    }

                }
            } else if(SprintingText.visible) {
                SprintingText.visible = false;
            }
        }
    }
}
