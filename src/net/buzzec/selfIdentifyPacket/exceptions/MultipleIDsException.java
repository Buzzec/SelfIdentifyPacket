package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.ISelfIdentifyObjectEnum;

/**
 * Error indicating that multiple of the same id are present in the same ISelfIdentifyObjectEnum
 *
 * @author Buzzec
 * @see ISelfIdentifyObjectEnum
 */
public class MultipleIDsException extends RuntimeException{
    public MultipleIDsException(){
        super("Multiple IDs found");
    }
}
