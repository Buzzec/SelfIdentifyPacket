package net.buzzec.selfIdentifyPacket.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Error indicates when an Unknown object is found while reading a packet
 *
 * @author Buzzec
 */
public class UnknownObjectException extends RuntimeException{
    private static Logger logger = LoggerFactory.getLogger(UnknownObjectException.class);

    public UnknownObjectException(byte id){
        super("Unknown Object: " + id);
        logger.trace("Built InvalidIDException with byte id=" + id);
    }
}
