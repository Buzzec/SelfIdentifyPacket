package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.SelfIdentifyBaseIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Error indicating a custom SelfIdentifyObject ID is occupying the same value as a built-in ID
 *
 * @author Buzzec
 * @see SelfIdentifyBaseIDs
 */
public class InvalidIDException extends RuntimeException{
    private static Logger logger = LoggerFactory.getLogger(InvalidIDException.class);

    public InvalidIDException(byte id){
        super("Invalid ID: " + id);
        logger.trace("Built InvalidIDException with byte id=" + id);
    }

    public InvalidIDException(short id){
        super("Invalid ID: " + id);
        logger.trace("Built InvalidIDException with short id=" + id);
    }
}
