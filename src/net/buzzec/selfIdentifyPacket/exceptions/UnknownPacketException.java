package net.buzzec.selfIdentifyPacket.exceptions;

import net.buzzec.selfIdentifyPacket.SelfIdentifyBaseIDs;
import net.buzzec.selfIdentifyPacket.SelfIdentifyBasic;
import net.buzzec.selfIdentifyPacket.SelfIdentifyPacketBuilder;
import nl.pvdberg.pnet.client.Client;
import nl.pvdberg.pnet.packet.Packet;

import java.util.Arrays;

/**
 * This exception is thrown when an unknown packet is received. Make sure you registered the sent packet.
 *
 * @author Buzzec
 */
public class UnknownPacketException extends RuntimeException{
    public UnknownPacketException(Packet packet, Client client){
        super("Unknown packet id: " + packet.getPacketID());
        client.send(new SelfIdentifyPacketBuilder<>(Packet.PacketType.Reply, SelfIdentifyBasic.class, SelfIdentifyBaseIDs.PACKET_ERROR_ID)
            .withString(UnknownPacketException.class.getSimpleName() + ": " + Arrays.toString(getStackTrace()))
            .build()
        );
    }
}
