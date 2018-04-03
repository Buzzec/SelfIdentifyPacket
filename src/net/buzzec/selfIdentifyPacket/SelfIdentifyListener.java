package net.buzzec.selfIdentifyPacket;

import net.buzzec.selfIdentifyPacket.exceptions.RepliedPacketException;
import nl.pvdberg.pnet.client.Client;
import nl.pvdberg.pnet.event.PNetListener;
import nl.pvdberg.pnet.packet.Packet;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.function.Function;

/**
 * This class is a listener to be implemented on a PNet Server or Client.
 *
 * <p>
 *     This listener launches different functions asynchronously in order to allow for multiple connections and
 *     disconnections to be caught by the same server quickly. These functions are defined by {@code FunctionValues}. If
 *     the function is null the thread is never launched. This class only handles connections and disconnections, packet
 *     receiving is handled on a packet id basis and is stored in {@code SelfIdentifyPacketRegistry}.
 * </p>
 *
 * @see nl.pvdberg.pnet.server.Server
 * @see nl.pvdberg.pnet.client.Client
 * @see SelfIdentifyPacketRegistry
 * @author Buzzec
 */
//TODO Add Logging
@SuppressWarnings("unused")
public class SelfIdentifyListener implements PNetListener{
    /**
     * The function to be called when a connection is made. Can be null if not meant to be called.
     */
    @Nullable private Function<Client, Void> onConnectAsync;

    /**
     * The function to be called when something disconnected. Can be null if not meant to be called.
     */
    @Nullable private Function<Client, Void> onDisconnectAsync;

    /**
     * The {@code boolean} that tells the listener whether it is attached to a server.
     */
    private boolean isServer;

    /**
     * Constructor for {@code SelfIdentifyLauncher}.
     *
     * @param onConnectAsync The function to be called when a connection is made. Can be null if not meant to be called.
     * @param onDisconnectAsync The function to be called when something disconnected. Can be null if not meant to be
     *                         called.
     * @param isServer Whether or not this is intended to listen on a server.
     */
    public SelfIdentifyListener(@Nullable Function<Client, Void> onConnectAsync, @Nullable Function<Client, Void> onDisconnectAsync, boolean isServer){
        this.onConnectAsync = onConnectAsync;
        this.onDisconnectAsync = onDisconnectAsync;
        this.isServer = isServer;
    }

    /**
     * Launches a new thread that runs the on connect function asynchronously when a connection is made.
     *
     * @param c The connected client.
     */
    @Override
    public void onConnect(Client c){
        if(onConnectAsync != null){
            Thread thread = new Thread(() -> onConnectAsync.apply(c));
            thread.start();
        }
    }

    /**
     * Launches a new thread that runs the on disconnect function asynchronously when disconnection happens.
     *
     * @param c The disconnected client.
     */
    @Override
    public void onDisconnect(Client c){
        if(onDisconnectAsync != null){
            Thread thread = new Thread(() -> onDisconnectAsync.apply(c));
            thread.start();
        }
    }

    /**
     * The function called when a packet is received. Calls {@code SelfIdentifyPacketRegistry.applyServerFunction} or
     * {@code SelfIdentifyPacketRegistry.applyClientFunction} depending on {@code isServer}.
     *
     * @see SelfIdentifyPacketRegistry#applyServerFunction(Packet, Client)
     * @see SelfIdentifyPacketRegistry#applyClientFunction(Packet, Client)
     * @param p The packet received.
     * @param c The client that sent the packet.
     * @throws IOException If something is wrong when reading the packet. Can happen if it's a bad packet so it
     * shouldn't crash the client or server.
     */
    @Override
    public void onReceive(Packet p, Client c) throws IOException{
        if(p.getPacketID() == SelfIdentifyBaseIDs.PACKET_ERROR_ID){
            throw new RepliedPacketException(p);
        }
        if(isServer){
            SelfIdentifyPacketRegistry.applyServerFunction(p, c);
        }
        else{
            SelfIdentifyPacketRegistry.applyClientFunction(p, c);
        }
    }
}
