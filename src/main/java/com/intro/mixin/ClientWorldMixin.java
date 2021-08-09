package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Shadow protected abstract EntityLookup<Entity> getEntityLookup();



    @Inject(at = @At("HEAD"), method = "addEntity")
    public void addEntityPre(int id, Entity entity, CallbackInfo info){
        Osmium.EVENT_BUS.postEvent(new EventSpawnEntity(EventDirection.PRE, entity), EventType.EVENT_SPAWN_ENTITY);
    }

    @Inject(at = @At("TAIL"), method = "addEntity")
    public void addEntityPost(int id, Entity entity, CallbackInfo info){
        Osmium.EVENT_BUS.postEvent(new EventSpawnEntity(EventDirection.POST, entity), EventType.EVENT_SPAWN_ENTITY);
    }

    @Inject(at = @At("HEAD"), method = "addPlayer")
    public void addPlayerPre(int id, AbstractClientPlayerEntity player, CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventAddPlayer(player), EventType.EVENT_ADD_PLAYER);
    }

    // @Inject(at = @At("HEAD"), method = "removeEntity")
    public void removeEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        Entity entity = this.getEntityLookup().get(entityId);
        if(entity.isPlayer()) {
            Osmium.EVENT_BUS.postEvent(new EventRemovePlayer((AbstractClientPlayerEntity) entity), EventType.EVENT_REMOVE_PLAYER);
        }
    }





}
