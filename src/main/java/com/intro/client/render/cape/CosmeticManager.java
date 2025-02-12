package com.intro.client.render.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventAddPlayer;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.client.util.TextureUtil;
import com.intro.common.ModConstants;
import com.intro.common.config.Options;
import com.intro.common.util.Util;
import com.mojang.blaze3d.platform.NativeImage;
import io.netty.buffer.ByteBufInputStream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipFile;

/**
 * <p>This class contains all code for managing cosmetics</p>
 * <p>Previously done by CapeHandler</p>
 *
 * @since 1.2.3
 * @author Intro
 * @see Cape
 * @see CapeRenderer
 */
public class CosmeticManager {

    // contains a map of all loaded capes
    private static final HashMap<String, Cape> capes = new HashMap<>();

    // map of all players capes
    public static final HashMap<String, Cape> playerCapes = new HashMap<>();



    private static Cape preLoadedPlayerCape;

    public static Cape getPreLoadedPlayerCape() {
        return preLoadedPlayerCape;
    }

    private static final Minecraft mc = Minecraft.getInstance();

    private static void sendToast(Minecraft client, Component title, Component description) {
        client.getToasts().addToast(SystemToast.multiline(client, SystemToast.SystemToastIds.PACK_LOAD_FAILURE, title, description));
    }

    public static void put(String id, Cape cape) {
        capes.put(id, cape);
    }

    public static Cape get(String id) {
        return capes.get(id);
    }

    public static void refresh() {
        capes.clear();
        genCapes();
    }

    public static Cape readCapeFromByteBuf(FriendlyByteBuf byteBuf) throws IOException {
        String creator = byteBuf.readUtf();
        String identifier = byteBuf.readUtf();

        // check if cape is already downloaded, and if it is skip the rest of the reading
        if(get(identifier) != null) {
            return get(identifier);
        }

        boolean animated = byteBuf.readBoolean();
        int frameDelay = byteBuf.readInt();

        NativeImage capeImage = NativeImage.read(new ByteBufInputStream(byteBuf));

        return new Cape(new DynamicAnimation(capeImage, creator + "-" + identifier, 64 ,32, frameDelay), false, animated, "Server", creator + "-" + identifier, creator);
    }

    public static void preLoadPlayerCape() {
        preLoadedPlayerCape = capes.get(OsmiumClient.options.getStringOption(Options.SetCape).variable);
    }

    public static void genCapes() {
        putCape(new Cape(genCapeAnimation(new ResourceLocation("osmium", "textures/cape/osmium_logo_cape.png"), "osmium_logo_cape", 1), false, true, "local", "osmium_logo_cape", "Intro"));

        try {
            File cosmeticsDir = FabricLoader.getInstance().getGameDir().getParent().resolve("cosmetics").toFile();
            if(!Files.exists(cosmeticsDir.toPath())) {
                Files.createDirectory(cosmeticsDir.toPath());
            }
            for(File file : Objects.requireNonNull(cosmeticsDir.listFiles())) {
                if(!file.isDirectory() && file.toString().endsWith(".zip")) {
                    List<Cape> loaded = loadCapesFromZip(new ZipFile(file, ZipFile.OPEN_READ));
                    loaded.forEach((cape) -> capes.put(cape.registryName, cape));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendToast(mc, new TranslatableComponent("osmium_failed_cape_load_title"), new TranslatableComponent("osmium_failed_cape_load"));
        }
    }

    public static void loadCapes() {
        genCapes();
        preLoadPlayerCape();
    }

    public static List<Cape> loadCapesFromZip(ZipFile file) throws IOException {
        InputStream manifestFile = file.getInputStream(file.getEntry(Util.getZipFileSystemPrefix(file) + "/manifest.json"));

        JsonReader reader = new JsonReader(new InputStreamReader(manifestFile));
        JsonParser parser = new JsonParser();
        JsonObject manifest = (JsonObject) parser.parse(reader);

        List<Cape> returns = new ArrayList<>();

        for(JsonElement element : manifest.get("capes").getAsJsonArray()) {
            try {
                JsonObject object = (JsonObject) element;

                InputStream capeImage = file.getInputStream(file.getEntry(Util.getZipFileSystemPrefix(file) + object.get("path").getAsString()));
                boolean animated = object.get("animated").getAsBoolean();
                String creator = object.get("creator").getAsString().toLowerCase();
                int frameDelay = object.get("frame_delay").getAsInt();
                String identifier = object.get("identifier").getAsString().toLowerCase();
                returns.add(new Cape(new DynamicAnimation(NativeImage.read(capeImage), creator + "-" + identifier, 64 ,32, frameDelay), false, animated, "cape pack", creator + "-" + identifier, creator));
            } catch (Exception e) {
                e.printStackTrace();
                sendToast(mc, new TranslatableComponent("osmium_failed_cape_load_title"), new TranslatableComponent("osmium_failed_cape_load"));
            }
        }


        return returns;
    }

    public static Cape getCape(String location) {
        return capes.get(location);
    }

    private static void putCape(Cape cape) {
        capes.put(cape.registryName, cape);
    }

    public static DynamicAnimation genCapeAnimation(ResourceLocation location, String registryName, int frameDelay) {
        return new DynamicAnimation(TextureUtil.getImageAtLocation(location), registryName, 64, 32, frameDelay);
    }

    public static Collection<Cape> getAllCapes() {
        return capes.values();
    }

    public void handleEvents(Event event) {
        if (event instanceof EventAddPlayer) {
            if(Objects.equals(((EventAddPlayer) event).entity.getStringUUID(), Minecraft.getInstance().player.getStringUUID())) {
                if(CosmeticManager.getPreLoadedPlayerCape() != null) {
                    CosmeticManager.playerCapes.put(Minecraft.getInstance().player.getStringUUID(), CosmeticManager.getPreLoadedPlayerCape());
                }
            } else {
                Thread CapeDownloaderThread = new Thread(new CosmeticManager.StandardCapeDownloader((EventAddPlayer) event));
                CapeDownloaderThread.start();
            }
        }
        if (event instanceof EventRemovePlayer) {
            playerCapes.remove(((EventRemovePlayer) event).entity.getStringUUID());
        }
    }

    public void tickCapes(Event event) {
        if(event.isPost() && OsmiumClient.options.getBooleanOption(Options.AnimateCapes).variable) {
            for (Cape cape : playerCapes.values()) {
                if(cape.isAnimated) {
                    cape.nextFrame();
                }
            }
        }
    }


    public static void setCape(String uuid, String url, boolean animated) {
        try {
            URL capeUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) capeUrl.openConnection();
            connection.setRequestMethod("HEAD");

            if(connection.getResponseCode() == 404) {
                return;
            }

            if(url.startsWith("http://s.optifine.net/capes/")) {
                playerCapes.put(uuid, new Cape(new DynamicAnimation(parseOptifineCape(NativeImage.read(capeUrl.openStream())), uuid.replace("-", ""), 64, 32, 1), true, animated, "optifine", uuid.replace("-", ""), "unknown"));
            } else {
                playerCapes.put(uuid, new Cape(new DynamicAnimation(NativeImage.read(capeUrl.openStream()), uuid.replace("-", ""), 64, 32, 1), false, animated, url, uuid.replace("-", ""), "unknown"));
            }
        } catch (Exception e) {
            OsmiumClient.LOGGER.error("Failed setting player cape!");
            e.printStackTrace();
        }
    }

    public static void setCapeThreaded(String uuid, String url, boolean animated) {
        Thread customDownloader = new Thread(new CosmeticManager.CustomCapeDownloader(uuid, url, animated));
        customDownloader.start();
    }

    /**
     * <p>Used to parse optifine capes to a usable format</p>
     * <p>Has to be like this because optifine uses a different cape uv format</p>
     * <p>adapted from of-capes</p>
     * @param image Source image
     * @return Parsed Image
     */
    public static NativeImage parseOptifineCape(NativeImage image) {
        int imageWidth = 64;
        int imageHeight = 32;
        int imageSrcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        NativeImage parsedImage = new NativeImage(imageWidth, imageHeight, true);

        for (int x = 0; x < imageSrcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                parsedImage.setPixelRGBA(x, y, image.getPixelRGBA(x, y));
            }
        }

        image.close();

        return parsedImage;

    }


    public static void setCapeFromIdentifier(String uuid, String identifier) {
        try {
            playerCapes.put(uuid, CosmeticManager.getCape(identifier).clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCapeFromResourceLocation(String uuid, String identifier) {
        Thread multiThreaded = new Thread(() -> playerCapes.put(uuid, CosmeticManager.getCape(identifier).clone()));
        multiThreaded.start();
    }

    private record StandardCapeDownloader(EventAddPlayer playerJoin) implements Runnable {

        public void run() {
            if(ModConstants.DEVELOPER_UUIDS.contains(playerJoin.entity.getStringUUID())) {
                setCapeFromIdentifier(playerJoin.entity.getStringUUID(), "osmium_logo_cape");
            }
            setCape(playerJoin.entity.getStringUUID(), "http://s.optifine.net/capes/" + playerJoin.entity.getName().getString() + ".png", false);
            if(playerCapes.get(playerJoin.entity.getStringUUID()) == null) {
                setCape(playerJoin.entity.getStringUUID(), "https://minecraftcapes.net/profile/" + playerJoin.entity.getStringUUID().replace("-", "") + "/cape/map", false);
            }
        }
    }

    private record CustomCapeDownloader(String uuid, String url, boolean animated) implements Runnable {

        public void run() {
            setCape(uuid, url, animated);
        }
    }

}
