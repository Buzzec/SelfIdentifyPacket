package net.buzzec.selfIdentifyPacket;

/**
 * This class stores all the default values for SelfIdentifyPacket.
 *
 * <p>
 *     Custom object ids should be greater than or equal to {@code #MIN_ID_VALUE}, and custom packet ids should be
 *     grater or equal to {@code #MIN_PACKET_VALUE}. Anything lower than those values will result in errors somewhere.
 *     It is recommended that you reference these min values to maintain future compatibility.
 * </p>
 *
 * @author Buzzec
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SelfIdentifyBaseIDs {
    public static final byte
            EOP_ID = -128,
            BOOLEAN_ID = -127,
            BYTE_ID = -126,
            BYTES_ID = -125,
            DOUBLE_ID = -124,
            FLOAT_ID = -123,
            INT_ID = -122,
            LONG_ID = -121,
            SHORT_ID = -120,
            STRING_ID = -119,
            SELF_IDENTIFY_BASIC_ID = -118,
            MIN_ID_VALUE = -117;

    public static final short
            PACKET_ERROR_ID = -32768,
            MIN_PACKET_VALUE = -32767;
}
