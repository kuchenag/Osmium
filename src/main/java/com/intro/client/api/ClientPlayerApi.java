package com.intro.client.api;

import com.intro.common.api.PlayerProperties;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;

public class ClientPlayerApi {

    /**
     * contains a hashmap of all player uuids and corresponding PlayerProperties
     */
    private static HashMap<String, PlayerProperties> playerProperties = new HashMap<>();

    public static void registerPlayer(ServerPlayer player) {
        playerProperties.put(player.getUUID().toString(), new PlayerProperties());
    }

    public static void setRunningOsmium(ServerPlayer player, boolean value) {
        PlayerProperties properties = playerProperties.get(player.getUUID().toString());
        properties.runningOsmium = value;
        playerProperties.put(player.getUUID().toString(), properties);
    }

    public static boolean isRunningOsmium(ServerPlayer player) {
        return playerProperties.get(player.getUUID().toString()).runningOsmium;
    }

    public static void removePlayerRegistry(ServerPlayer player) {
        playerProperties.remove(player.getUUID().toString());
    }

    public static PlayerProperties getPlayerProperties(ServerPlayer player) {
        return playerProperties.get(player.getUUID().toString());
    }

}
