package com.mega.revelationfix.common.capability.entity;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.endinglib.api.capability.CapabilityEntityData;
import com.mega.endinglib.api.capability.CapabilitySyncType;
import com.mega.endinglib.api.capability.EntitySyncCapabilityBase;
import com.mega.endinglib.api.capability.syncher.CapabilityDataSerializer;
import com.mega.endinglib.api.capability.syncher.CapabilityDataSerializers;
import com.mega.revelationfix.common.data.FloatWrapped;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.config.ModConfig;

import java.util.function.Predicate;

public class MegaCapability extends EntitySyncCapabilityBase {
    public static CapabilityDataSerializer<FloatWrapped> FLOAT_WRAPPED = CapabilityDataSerializer.simple(FloatWrapped.F_WRITER, FloatWrapped.F_READER, FloatWrapped.NBT_WRITER, FloatWrapped.NBT_READER);
    public final CapabilityEntityData<FloatWrapped> DATA = this.dataManager.define(0, "data", new FloatWrapped(ModConfig.APOLLYON_HEALTH.get().floatValue()), FLOAT_WRAPPED);
    public static ResourceLocation NAME = new ResourceLocation(ModMain.MODID, "mega");

    @Override
    public ResourceLocation getRegistryName() {
        return NAME;
    }

    @Override
    protected @NotNull Predicate<Entity> canAttach() {
        return entity -> entity instanceof Apostle;
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

    /*public float getData() {
        return this.dataManager.getValue(DATA);
    }
    public void setData(float data) {
        this.dataManager.setValue(DATA, data);
    }

     */
    public float getData() {
        return 148F;
    }
    public void setData(float data) {
        System.out.println(":)");
    }
}