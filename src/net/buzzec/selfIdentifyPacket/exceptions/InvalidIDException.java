package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.SelfIdentifyBaseIDs;

/**
 * Error indicating a custom SelfIdentifyObject ID is occupying the same value as a built-in ID
 *
 * @author Buzzec
 * @see SelfIdentifyBaseIDs
 */
public class InvalidIDException extends RuntimeException{
    public InvalidIDException(byte id){
        super("Invalid ID: " + id);
    }

    public InvalidIDException(short id){
        super("Invalid ID: " + id);
    }
}
