package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.intro.client.util.EnumUtil;
import com.intro.common.config.options.EnumOption;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class EnumSelectWidget extends AbstractScalableButton {

    public final String optionId;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, @NotNull String optionId, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                Enum<?> attachedEnum;
                attachedEnum = EnumUtil.nextEnum(OsmiumClient.options.getEnumOption(optionId).variable);
                OsmiumClient.options.put(optionId, new EnumOption(optionId, attachedEnum));
                button.setMessage(new TranslatableComponent(key + attachedEnum.name().toLowerCase()));
            } else {
                System.out.println("inactive");
                button.active = false;
            }
        }, 1f);


        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        Enum<?> attachedEnum = OsmiumClient.options.getEnumOption(optionId).variable;
        this.setMessage(new TranslatableComponent(key + attachedEnum.name().toLowerCase()));

        this.optionId = optionId;
        this.key = key;
    }

    public EnumSelectWidget(int x, int y, int width, int height, @NotNull String optionId, String key, float scale) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                Enum<?> attachedEnum;
                attachedEnum = EnumUtil.nextEnum(OsmiumClient.options.getEnumOption(optionId).variable);
                OsmiumClient.options.put(optionId, new EnumOption(optionId, attachedEnum));
                button.setMessage(new TranslatableComponent(key + attachedEnum.name().toLowerCase()));
            } else {
                System.out.println("inactive");
                button.active = false;
            }
        }, scale);


        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        Enum<?> attachedEnum = OsmiumClient.options.getEnumOption(optionId).variable;
        this.setMessage(new TranslatableComponent(key + attachedEnum.name().toLowerCase()));

        this.optionId = optionId;
        this.key = key;
    }


}
