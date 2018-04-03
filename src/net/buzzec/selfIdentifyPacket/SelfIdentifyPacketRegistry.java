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

@SuppressWarnings("WeakerAccess")
public class SelfIdentifyPacketRegistry{
    private static Map<Short, FunctionList> map = new HashMap<>();
    private static Function<Pair<Packet, Client>, Void> onUnknownPacketServer = null;
    private static Function<Pair<Packet, Client>, Void> onUnknownPacketClient = null;

    private static Logger logger = LoggerFactory.getLogger(SelfIdentifyPacketRegistry.class);

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

    public static void setOnUnknownPacketServer(Function<Pair<Packet, Client>, Void> onUnknownPacketServer){
        SelfIdentifyPacketRegistry.onUnknownPacketServer = onUnknownPacketServer;
    }

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
