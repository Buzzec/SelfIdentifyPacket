package net.buzzec.selfIdentifyPacket;

import javafx.util.Pair;
import net.buzzec.selfIdentifyPacket.exceptions.InvalidIDException;
import net.buzzec.selfIdentifyPacket.exceptions.UnknownPacketException;
import nl.pvdberg.pnet.client.Client;
import nl.pvdberg.pnet.packet.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class SelfIdentifyPacketRegistry{
    private static Map<Short, Pair<Class, Pair<Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void>, Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void>>>> map = new HashMap<>();

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
        map.put(packetID, new Pair<>(clazz, new Pair<>(onReceiveServerAsync, onReceiveClientAsync)));
        return true;
    }

    static void applyServerFunction(Packet packet, Client client) throws IOException{
        if(map.containsKey(packet.getPacketID())){
            Class clazz = map.get(packet.getPacketID()).getKey();
            Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> function = map.get(packet.getPacketID()).getValue().getKey();
            if(function != null){
                //noinspection unchecked
                function.apply(new Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>(
                        new SelfIdentifyPacketReader<>(packet, clazz).getInfoListFromPacket(),
                        client
                ));
            }
        }
        else{
            throw new UnknownPacketException(packet, client);
        }
    }

    static void applyClientFunction(Packet packet, Client client) throws IOException{
        if(map.containsKey(packet.getPacketID())){
            Class clazz = map.get(packet.getPacketID()).getKey();
            Function<Pair<ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>, Void> function = map.get(packet.getPacketID()).getValue().getValue();
            if(function != null){
                //noinspection unchecked
                function.apply(new Pair<java.util.ArrayList<SelfIdentifyPacketReader.ClassQualifiedObject>, Client>(
                        new SelfIdentifyPacketReader<>(packet, clazz).getInfoListFromPacket(),
                        client
                ));
            }
        }
        else{
            throw new UnknownPacketException(packet, client);
        }
    }
}
