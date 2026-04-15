package com.mega.revelationfix.common.data;

import com.mega.endinglib.api.data.CompoundTagReader;
import com.mega.endinglib.api.data.CompoundTagWriter;
import net.minecraft.network.FriendlyByteBuf;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatWrapped {
    public static final FriendlyByteBuf.Reader<FloatWrapped> F_READER = friendlyByteBuf -> new FloatWrapped(friendlyByteBuf.readFloat());
    public static final FriendlyByteBuf.Writer<FloatWrapped> F_WRITER = (friendlyByteBuf, floatWrapped) -> friendlyByteBuf.writeFloat(floatWrapped.value);
    public static final CompoundTagReader<FloatWrapped> NBT_READER = (compoundTag, s) -> new FloatWrapped(decryptFloat(compoundTag.getByteArray(s), 4936));
    public static final CompoundTagWriter<FloatWrapped> NBT_WRITER = (compoundTag, s, floatWrapped) -> compoundTag.putByteArray(s, encryptFloat(floatWrapped.value, 4936));
    public FloatWrapped(float value) {
        this.value = value;
    }
    private float value;
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Float f) return Float.compare(value, f) == 0;
        if (obj instanceof Number n) return Float.compare(value, n.floatValue()) == 0;
        if (obj instanceof CharSequence cs) {
            try {
                return Float.compare(value, Float.parseFloat(cs.toString().trim())) == 0;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }
    static byte[] encryptFloat(float value, int key) {
        int bits = Float.floatToIntBits(value);
        int encrypted = bits ^ key;
        return ByteBuffer.allocate(4)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(encrypted)
                .array();
    }
    static float decryptFloat(byte[] encryptedBytes, int key) {
        if (encryptedBytes == null || encryptedBytes.length != 4) {
            throw new IllegalArgumentException("encryptedBytes must be exactly 4 bytes.");
        }
        int encrypted = ByteBuffer.wrap(encryptedBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        int bits = encrypted ^ key;
        return Float.intBitsToFloat(bits);
    }
}
