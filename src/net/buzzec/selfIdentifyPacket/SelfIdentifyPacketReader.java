package net.buzzec.selfIdentifyPacket;

import net.buzzec.selfIdentifyPacket.exceptions.UnknownObjectException;
import nl.pvdberg.pnet.packet.Packet;
import nl.pvdberg.pnet.packet.PacketReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is used to read SelfIdentify packets. It is wrapped up in {@code SelfIdentifyListener}, but can be used
 * manually only.
 *
 * @see SelfIdentifyListener
 * @param <T> The {@code ISelfIdentifyEnum} that this packet is based on.
 */
//TODO Add Logging
@SuppressWarnings("unused")
public class SelfIdentifyPacketReader<T extends Enum<T> & ISelfIdentifyObjectEnum>{
    private PacketReader packetReader;
    private Class<T> clazz;

    public SelfIdentifyPacketReader(Packet packet, Class<T> clazz){
        packetReader = new PacketReader(packet);
        this.clazz = clazz;
    }

    /**
     * Used to get the list of objects that represents the values in the received packet.
     *
     * @return An {@code ArrayList} of {@code ClassQualifiedObject}s of the data tin the packet.
     * @throws IOException When there's a read problem.
     */
    public ArrayList<ClassQualifiedObject> getInfoListFromPacket() throws IOException {
        ArrayList<ClassQualifiedObject> out = new ArrayList<>();

        byte type;
        do{
            type = packetReader.readByte();
            switch(type){
                case SelfIdentifyBaseIDs.EOP_ID:{
                    break;
                }
                case SelfIdentifyBaseIDs.BOOLEAN_ID:{
                    out.add(new ClassQualifiedObject(boolean.class, packetReader.readBoolean()));
                    break;
                }
                case SelfIdentifyBaseIDs.BYTE_ID:{
                    out.add(new ClassQualifiedObject(byte.class, packetReader.readByte()));
                    break;
                }
                case SelfIdentifyBaseIDs.BYTES_ID:{
                    out.add(new ClassQualifiedObject(byte[].class, packetReader.readBytes()));
                    break;
                }
                case SelfIdentifyBaseIDs.DOUBLE_ID:{
                    out.add(new ClassQualifiedObject(double.class, packetReader.readDouble()));
                    break;
                }
                case SelfIdentifyBaseIDs.FLOAT_ID:{
                    out.add(new ClassQualifiedObject(float.class, packetReader.readFloat()));
                    break;
                }
                case SelfIdentifyBaseIDs.INT_ID:{
                    out.add(new ClassQualifiedObject(int.class, packetReader.readInt()));
                    break;
                }
                case SelfIdentifyBaseIDs.LONG_ID:{
                    out.add(new ClassQualifiedObject(long.class, packetReader.readLong()));
                    break;
                }
                default:{
                    T classType = ISelfIdentifyObjectEnum.findType(type, clazz);
                    if(classType == null){
                        throw new UnknownObjectException(type);
                    }
                    out.add(new ClassQualifiedObject(classType.getObjectClass(), classType.getFromBytes(packetReader.readBytes())));
                    break;
                }
            }
        }
        while(type != SelfIdentifyBaseIDs.EOP_ID);

        return out;
    }

    /**
     * Used to qualify the objects returned by reading the packet, otherwise it would just be an array of
     * {@code Object}s!
     */
    @SuppressWarnings("WeakerAccess")
    public class ClassQualifiedObject{
        public Class clazz;
        public Object object;

        private ClassQualifiedObject(Class clazz, Object object){
            this.clazz = clazz;
            this.object = object;
        }
    }
}
