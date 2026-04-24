package com.mega.revelationfix.common.capability.entity;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.endinglib.api.capability.CapabilityEntityData;
import com.mega.endinglib.api.capability.CapabilitySyncType;
import com.mega.endinglib.api.capability.EntitySyncCapabilityBase;
import com.mega.endinglib.api.capability.syncher.CapabilityDataSerializer;
import com.mega.endinglib.api.capability.syncher.CapabilityDataSerializers;
import com.mega.revelationfix.api.entity.ITrailRendererEntity;
import com.mega.revelationfix.common.data.FloatWrapped;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.config.ModConfig;

import java.util.function.Predicate;

public class TrailCapability extends EntitySyncCapabilityBase {
    public final CapabilityEntityData<Boolean> RENDER = this.dataManager.define(0, "render", false, CapabilityDataSerializers.BOOLEAN);
    public static ResourceLocation NAME = new ResourceLocation(ModMain.MODID, "trail");

    @Override
    public ResourceLocation getRegistryName() {
        return NAME;
    }

    @Override
    protected @NotNull Predicate<Entity> canAttach() {
        return entity -> entity instanceof ITrailRendererEntity;
    }

    @Override
    public void syncData(CompoundTag compoundTag, Dist dist, CapabilitySyncType capabilitySyncType, Entity entity) {

    }

    @Override
    public void readSyncData(CompoundTag compoundTag, Dist dist, CapabilitySyncType capabilitySyncType, Entity entity) {

    }

    @Override
    public boolean canSyncWhenTick(Entity entity, Level level) {
        return false;
    }

    @Override
    public void customSerializeNBT(CompoundTag compoundTag) {

    }

    @Override
    public void customDeserializeNBT(CompoundTag compoundTag) {

    }
    public boolean shouldRender() {
        return this.dataManager.getValue(RENDER);
    }
    public void setShouldRender(boolean render) {
        this.dataManager.setValue(RENDER, render);
    }
}