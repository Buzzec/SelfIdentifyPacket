package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.ISelfIdentifyObjectEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Error indicating that multiple of the same id are present in the same ISelfIdentifyObjectEnum
 *
 * @author Buzzec
 * @see ISelfIdentifyObjectEnum
 */
public class MultipleIDsException extends RuntimeException{
    private static Logger logger = LoggerFactory.getLogger(MultipleIDsException.class);

    public MultipleIDsException(){
        super("Multiple IDs found");
        logger.trace("Built MultipleIDsException");
    }
}
