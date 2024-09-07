package net.mehvahdjukaar.dummmmmmy.network;

import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public record ClientBoundSyncEquipMessage(int entityID, int slotId, ItemStack itemStack) implements Message {
    public static final CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundSyncEquipMessage> TYPE =
            Message.makeType(Dummmmmmy.res("s2c_sync_equip"), ClientBoundSyncEquipMessage::new);


    public ClientBoundSyncEquipMessage(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), ItemStack.STREAM_CODEC.decode(buf));
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(slotId);
        ItemStack.STREAM_CODEC.encode(buf, this.itemStack);
    }

    @Override
    public void handle(Context context) {
        Entity entity = Minecraft.getInstance().level.getEntity(this.entityID);
        if (entity instanceof TargetDummyEntity dummy) {
            dummy.setItemSlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, this.slotId), this.itemStack);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }
}
