package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.SelfIdentifyBasic;
import net.buzzec.selfIdentifyPacket.SelfIdentifyPacketReader;
import nl.pvdberg.pnet.packet.Packet;

import java.io.IOException;

/**
 * Exception that is thrown whenever a packet is replied with an error.
 *
 * @author Buzzec
 */
public class RepliedPacketException extends RuntimeException{
    public RepliedPacketException(Packet packet) throws IOException{
        super("Replied packet error: " + new SelfIdentifyPacketReader<>(packet, SelfIdentifyBasic.class).getInfoListFromPacket().get(0).object);
    }
}
