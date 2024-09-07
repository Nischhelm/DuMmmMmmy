package net.mehvahdjukaar.dummmmmmy.network;

import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.common.CritRecord;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.dummmmmmy.configs.ClientConfigs;
import net.mehvahdjukaar.dummmmmmy.configs.CritMode;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public record ClientBoundDamageNumberMessage
        (int entityID, float damageAmount, Holder<DamageType> damageType, boolean isCrit, float critMult)
        implements Message {

    public static final CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundDamageNumberMessage> TYPE =
            Message.makeType(Dummmmmmy.res("s2c_damage_number"), ClientBoundDamageNumberMessage::of);


    public static ClientBoundDamageNumberMessage of(RegistryFriendlyByteBuf buf) {
        var entityID = buf.readInt();
        var damageAmount = buf.readFloat();
        var damageType = DamageType.STREAM_CODEC.decode(buf);
        var isCrit = buf.readBoolean();
        var critMult = isCrit ? buf.readFloat() : 0;
        return new ClientBoundDamageNumberMessage(entityID, damageAmount, damageType, isCrit, critMult);
    }

    public ClientBoundDamageNumberMessage(int id, float damage, DamageSource source, @Nullable CritRecord critical) {
        this(id, damage, encodeDamage(source), critical != null, critical == null ? 0 : critical.getMultiplier());
    }

    public static Holder<DamageType> encodeDamage(DamageSource source) {
        if (source == null) return Dummmmmmy.TRUE_DAMAGE.getHolder();
        //if (critical) return Dummmmmmy.CRITICAL_DAMAGE;
        var damageType = source.typeHolder();
        if (damageType == null) throw new AssertionError("Damage source has null type. How?: " + source);
        return damageType;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeFloat(this.damageAmount);
        DamageType.STREAM_CODEC.encode(buf, damageType));
        buf.writeBoolean(this.isCrit);
        if (isCrit) buf.writeFloat(this.critMult);
    }

    @Override
    public void handle(Context context) {
        Entity entity = Minecraft.getInstance().level.getEntity(this.entityID);
        if (entity instanceof TargetDummyEntity dummy) {
            if (ClientConfigs.DAMAGE_NUMBERS.get()) {
                int i = dummy.getNextNumberPos();
                spawnParticle(entity, i);
            }
        } else if (entity != null) {
            spawnParticle(entity, 0);
        }
    }

    private void spawnParticle(Entity entity, int animationPos) {
        ResourceLocation type = damageType;
        float mult = 0;
        CritMode critMode = ClientConfigs.CRIT_MODE.get();
        if (critMode != CritMode.OFF && isCrit) {
            type = Dummmmmmy.CRITICAL_DAMAGE;
            if (critMode == CritMode.COLOR_AND_MULTIPLIER) {
                mult = critMult;
            }
        }
        double z = CritMode.encodeIntFloatToDouble(animationPos, mult);
        int color = ClientConfigs.getDamageColor(type);

        entity.level().addParticle(Dummmmmmy.NUMBER_PARTICLE.get(),
                entity.getX(), entity.getY() + 1, entity.getZ(), damageAmount, color, z);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }
}

