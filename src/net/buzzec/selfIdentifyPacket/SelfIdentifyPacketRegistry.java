package net.buzzec.selfIdentifyPacket;

import javafx.util.Pair;
import net.buzzec.selfIdentifyPacket.exceptions.InvalidIDException;
import net.buzzec.selfIdentifyPacket.exceptions.UnknownPacketException;
import nl.pvdberg.pnet.client.Client;
import nl.pvdberg.pnet.packet.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class is used to register all of your {@code ISelfIdentifyEnum}s against a packet id so they are searchable when
 * a packet is received.
 *
 * <p>
 *     The same {@code ISelfIdentifyEnum} can be registered for different packet ids as the packet id is the key for the
 *     map. This also means that different functions can be used for the same {@code ISelfIdentifyEnum}. When a packet
 *     is received by a {@code SelfIdentifyListener}, it will search through the map of values. The functions can be
 *     defended as null to show that they should not be run.
 * </p>
 * <p>
 *     When an unknown packet is received the {@code onUnknownPacketServer} or {@code onUnknownPacketClient} are called
 *     and can be set with {@code setOnUnknownPacketServer} and {@code setOnUnknownPacketClient}.
 * </p>
 */
//TODO Add logging
@SuppressWarnings({"WeakerAccess", "unused"})
public class SelfIdentifyPacketRegistry{
    private static Map<Short, FunctionList> map = new HashMap<>();
    private static Function<Pair<Packet, Client>, Void> onUnknownPacketServer = null;
    private static Function<Pair<Packet, Client>, Void> onUnknownPacketClient = null;

    private static Logger logger = LoggerFactory.getLogger(SelfIdentifyPacketRegistry.class);

    /**
     * Registers a {@code ISelfIdentifyObjectEnum} under a packet id with functions that will run on the data received
     * in that packet id.
     *
     * @param packetID The packet id to be registered under.
     * @param clazz The {@code ISelfIdentifyObjectEnum} class to be registered.
     * @param onReceiveServerAsync The function that will run if the packet is received on the server.
     * @param onReceiveClientAsync The function that will run if the packet is received on the client.
     * @return {@code true} if successful, {@code false} if not.
     */
    public static <T extends Enum<T> & ISelfIdentifyObjectEnum> boolean registerPacketID(
            short packetID,
            @NotNull Class<T> clazz,
            @Nullable Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> onReceiveServerAsync,
            @Nullable Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> onReceiveClientAsync
    ){
        if(packetID < SelfIdentifyBaseIDs.MIN_PACKET_VALUE){
            throw new InvalidIDException(packetID);
        }
        if(map.containsKey(packetID)){
            return false;
        }
        map.put(packetID, new FunctionList()
                .clazz(clazz)
                .onReceiveServerAsync(onReceiveServerAsync)
                .onReceiveClientAsync(onReceiveClientAsync)
        );
        return true;
    }

    /**
     * Sets {@code onUnknownPacketServer} function.
     *
     * @param onUnknownPacketServer The function to be set to.
     */
    public static void setOnUnknownPacketServer(Function<Pair<Packet, Client>, Void> onUnknownPacketServer){
        SelfIdentifyPacketRegistry.onUnknownPacketServer = onUnknownPacketServer;
    }

    /**
     * Sets {@code ononUnknownPacketClient} function.
     * @param onUnknownPacketClient The function to be set to.
     */
    public static void setOnUnknownPacketClient(Function<Pair<Packet, Client>, Void> onUnknownPacketClient){
        SelfIdentifyPacketRegistry.onUnknownPacketClient = onUnknownPacketClient;
    }

    static void applyServerFunction(Packet packet, Client client) throws IOException{
        try{
            if(map.containsKey(packet.getPacketID())){
                FunctionList list = map.get(packet.getPacketID());
                if(list.onReceiveClientAsync != null){
                    //noinspection unchecked
                    list.onReceiveClientAsync.apply(new Pair<>(
                            new SelfIdentifyPacketReader<>(packet, list.clazz).getInfoListFromPacket(),
                            client
                    ));
                }
            } else{
                throw new UnknownPacketException(packet, client);
            }
        }
        catch(UnknownPacketException e){
            logger.error("Unknown Packet: ", e);
            if(onUnknownPacketServer != null){
                onUnknownPacketServer.apply(new Pair<>(packet, client));
            }
        }
    }

    static void applyClientFunction(Packet packet, Client client) throws IOException{
        try{
            if(map.containsKey(packet.getPacketID())){
                FunctionList list = map.get(packet.getPacketID());
                if(list.onReceiveServerAsync != null){
                    //noinspection unchecked
                    list.onReceiveServerAsync.apply(new Pair<>(
                            new SelfIdentifyPacketReader<>(packet, list.clazz).getInfoListFromPacket(),
                            client
                    ));
                }
            } else{
                throw new UnknownPacketException(packet, client);
            }
        }
        catch(UnknownPacketException e){
            logger.error("Unknown Packet: ", e);
            if(onUnknownPacketClient != null){
                onUnknownPacketClient.apply(new Pair<>(packet, client));
            }
        }
    }

    private static class FunctionList{
        private Class clazz;
        private Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> onReceiveServerAsync = null;
        private Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> onReceiveClientAsync = null;

        private FunctionList clazz(Class clazz){
            this.clazz = clazz;
            return this;
        }

        private FunctionList onReceiveServerAsync(@Nullable Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> onReceiveServerAsync){
            this.onReceiveServerAsync = onReceiveServerAsync;
            return this;
        }

        private FunctionList onReceiveClientAsync(@Nullable Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> onReceiveClientAsync){
            this.onReceiveClientAsync = onReceiveClientAsync;
            return this;
        }
    }
}
