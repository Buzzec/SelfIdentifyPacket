package net.buzzec.selfIdentifyPacket.exceptions;

/**
 * Error indicates when an Unknown object is found while reading a packet
 *
 * @author Buzzec
 */
public class UnknownObjectException extends RuntimeException{
    public UnknownObjectException(byte id){
        super("Unknown Object: " + id);
    }
}
