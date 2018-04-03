package net.buzzec.selfIdentifyPacket;

import net.buzzec.selfIdentifyPacket.exceptions.InvalidIDException;
import net.buzzec.selfIdentifyPacket.exceptions.MultipleIDsException;
import nl.pvdberg.pnet.packet.Packet;
import nl.pvdberg.pnet.packet.PacketBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to build packets that will be read by a {@code SelfIdentifyListener}.
 *
 * <p>
 *     This class extends PNet's {@code PacketBuilder} and builds a packet that has object identification bytes. All the
 *     methods in that class can be used with this one and the sent packet is meant to be read by a
 *     {@code ISelfIdentifyPacketReader}. Param {@code T} is used to tell what {@code ISelfIdentifyObjectEnum} this
 *     packet is to be built from.
 * </p>
 *
 * @param <T> The {@code SelfIdentifyObjectEnum} the packet is meant to be built from.
 */
@SuppressWarnings({"unchecked", "unused"})
public class SelfIdentifyPacketBuilder<T extends Enum<T> & ISelfIdentifyObjectEnum> extends PacketBuilder {
    private static Logger logger = LoggerFactory.getLogger(SelfIdentifyPacketBuilder.class);

    public SelfIdentifyPacketBuilder(Packet.PacketType packetType, Class<T> clazz, short packetID){
        super(packetType);
        Set<Byte> bytes = new HashSet<>();
        for(T value : clazz.getEnumConstants()){
            if(value.getId() < SelfIdentifyBaseIDs.MIN_ID_VALUE || bytes.add(value.getId())){
                throw value.getId() < SelfIdentifyBaseIDs.MIN_ID_VALUE ? new InvalidIDException(value.getId()) : new MultipleIDsException();
            }
        }
        withID(packetID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Packet build() {
        super.withByte(SelfIdentifyBaseIDs.EOP_ID);
        return super.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withBoolean(boolean b) {
        super.withByte(SelfIdentifyBaseIDs.BOOLEAN_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withBoolean(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withByte(byte b) {
        super.withByte(SelfIdentifyBaseIDs.BYTE_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withByte(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withBytes(byte[] b){
        super.withByte(SelfIdentifyBaseIDs.BYTES_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withBytes(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withDouble(double d) {
        super.withByte(SelfIdentifyBaseIDs.DOUBLE_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withDouble(d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withFloat(float f) {
        super.withByte(SelfIdentifyBaseIDs.FLOAT_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withFloat(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withInt(int i) {
        super.withByte(SelfIdentifyBaseIDs.INT_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withInt(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withLong(long l) {
        super.withByte(SelfIdentifyBaseIDs.LONG_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withLong(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withShort(short s) {
        super.withByte(SelfIdentifyBaseIDs.SHORT_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withShort(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SelfIdentifyPacketBuilder<T> withString(String s) {
        super.withByte(SelfIdentifyBaseIDs.STRING_ID);
        return (SelfIdentifyPacketBuilder<T>)super.withString(s);
    }

    /**
     * Adds a specific object type from {@code ISelfIdentifyObjectEnum T}.
     *
     * @param type The {@code ISelfIdentifyObjectEnum T} value that represents {@code object}
     * @param object The {@code ISelfIdentifyObjectEnum} object to be sent.
     */
    public synchronized SelfIdentifyPacketBuilder<T> withSelfIdentifyObject(T type, Object object){
        super.withByte(type.getId());
        return (SelfIdentifyPacketBuilder<T>)super.withBytes(type.getBytes(object));
    }
}
