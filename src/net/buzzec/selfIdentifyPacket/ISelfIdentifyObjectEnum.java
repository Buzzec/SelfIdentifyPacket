package net.buzzec.selfIdentifyPacket;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface is used to signify an {@code Enum} list of possible objects to be put in a packet.
 * <p>
 *     This interface is designed to be implemented into an {@code Enum}, all the classes in this library assume just
 *     that and will not function otherwise. The values of this {@code Enum} are to be associated with classes that can
 *     be sent in a packet. Each {@code Enum} implementing this is meant to be a single packet type. This does not mean
 *     however that each {@code Enum} should have its own packet id, rather each method of handling packets should be
 *     registered into {@code SelfIdentifyPacketRegistry} under a different packet id.
 * </p>
 * <p>
 *     These methods are not intended to be used outside of the included classes, but can be used so if they are
 *     desired. It shouldn't break anything if you do.
 * </p>
 *
 * @author Buzzec
 */
public interface ISelfIdentifyObjectEnum {
    Logger logger = LoggerFactory.getLogger(ISelfIdentifyObjectEnum.class);
    /**
     * This method is used to determine the type id for the selected object.
     * <p>
     *     The minimum value this can be set to is defined as {@code SelfIdentifyBaseIDs#MIN_ID_VALUE}. Anything below
     *     this will throw an error when trying to build a packet with {@code SelfIdentifyPacketBuilder} as these values
     *     are used for built in types. These also cannot be repeated within the same enum.
     * </p>
     *
     * @see SelfIdentifyBaseIDs
     * @return The type id for each object in the enum.
     */
    byte getId();

    /**
     * This method is used to determine the actual object type associated with the enum value.
     *
     * @return The class of the object associated with the enum value.
     */
    Class<?> getObjectClass();

    /**
     * This method is used to change an object from bytes in a packet to an instance of that object.
     *
     * This method should be a reverse of {@code #getBytes}. The return type should also be changed to that of the
     * object this enum value references.
     *
     * @param bytes The bytes to be converted from.
     * @return The object that comes from the input bytes. (change type)
     */
    Object getFromBytes(byte[] bytes);

    /**
     * This method is used to convert an object into bytes.
     *
     * This method should be the reverse of {@code #getFromBytes}. The input parameter type should also be changed to
     * match that of the object this enum value references.
     *
     * @param object The object to be converted into bytes. (change type)
     * @return The byte {@code array} that represents the input object.
     */
    byte[] getBytes(Object object);

    /**
     * This method is used to search through a prescribed enum for a certain ID.
     *
     * @param id The id to be searched for.
     * @param clazz The class of the enum to be searched through.
     * @param <T> The enum to be searched through.
     * @return The enum value of the id or null if not found.
     */
    @Nullable
    static <T extends Enum<T> & ISelfIdentifyObjectEnum> T findType(byte id, Class<T> clazz){
        logger.trace("Finding id=" + id + " in class " + clazz.getName());
        for(T value : clazz.getEnumConstants()){
            if(value.getId() == id){
                logger.trace("Found! answer = " + value.name());
                return value;
            }
        }
        logger.warn("Did not find id=" + id + " in class " + clazz.getName() + "!");
        return null;
    }
}
