package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.SelfIdentifyBaseIDs;
import net.buzzec.selfIdentifyPacket.SelfIdentifyBasic;
import net.buzzec.selfIdentifyPacket.SelfIdentifyPacketBuilder;
import nl.pvdberg.pnet.client.Client;
import nl.pvdberg.pnet.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This exception is thrown when an unknown packet is received. Make sure you registered the sent packet.
 *
 * @author Buzzec
 */
public class UnknownPacketException extends Exception{
    private static Logger logger = LoggerFactory.getLogger(UnknownPacketException.class);

    public UnknownPacketException(Packet packet, Client client){
        super("Unknown packet id: " + packet.getPacketID());
        client.send(new SelfIdentifyPacketBuilder<>(Packet.PacketType.Reply, SelfIdentifyBasic.class, SelfIdentifyBaseIDs.PACKET_ERROR_ID)
            .withString(UnknownPacketException.class.getSimpleName() + ": " + Arrays.toString(getStackTrace()))
            .build()
        );
        logger.trace("Built UnknownPacketException with packet.id=" + packet.getPacketID() + ", and client.InetAddress=" + client.getInetAddress());
    }
}
