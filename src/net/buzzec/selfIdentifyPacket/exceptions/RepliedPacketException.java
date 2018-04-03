package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.SelfIdentifyBasic;
import net.buzzec.selfIdentifyPacket.SelfIdentifyPacketReader;
import nl.pvdberg.pnet.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Exception that is thrown whenever a packet is replied with an error.
 *
 * @author Buzzec
 */
public class RepliedPacketException extends RuntimeException{
    private static Logger logger = LoggerFactory.getLogger(RepliedPacketException.class);

    public RepliedPacketException(Packet packet) throws IOException{
        super("Replied packet error: " + new SelfIdentifyPacketReader<>(packet, SelfIdentifyBasic.class).getInfoListFromPacket().get(0).object);
        logger.trace("Built RepliedPacketException with packet.id=" + packet.getPacketID());
    }
}
