package com.intro.module.event;

import net.minecraft.world.entity.Entity;

public class EventSpawnEntity extends Event {

    public Entity entity;

    public EventSpawnEntity(EventDirection direction, Entity entity) {
        super(direction, false);
        this.entity = entity;
    }
}
