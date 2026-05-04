package dev.nitron.flutter;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

public class Flutter implements ModInitializer {
    public static final String MOD_ID = "flutter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final HashMap<String, Optional<Pair<SkinTextures, SkinTextures>>> SKIN_CACHE = new HashMap<>();

    @Override
    public void onInitialize() {
        ResourceLoader.get(ResourceType.CLIENT_RESOURCES)
                .registerReloader(id("cache_invalidator"), (SynchronousResourceReloader) manager -> SKIN_CACHE.clear());
    }

    public static Optional<SkinTextures> getFlutterSkin(GameProfile gameProfile, int age, SkinTextures original) {
        var rawName = gameProfile.name();

        var cachedSkin = SKIN_CACHE.get(rawName);
        if (cachedSkin == null) {
            cachedSkin = createSkin(rawName, original);
            SKIN_CACHE.put(rawName, cachedSkin);
        }

        return cachedSkin.map((pair) -> (age % 80 < 2) ? pair.getLeft() : pair.getRight());
    }

    public static Optional<Pair<SkinTextures, SkinTextures>> createSkin(String rawName, SkinTextures original) {
        String username = Flutter.sanitizeName(rawName);
        LOGGER.info("How many times do we run?");

        Identifier skin = id("skin/" + username + "/skin");
        Identifier blink = id("skin/" + username + "/blink");

        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        if (manager.getResource(id("textures/" + skin.getPath() + ".png")).isPresent()
                && manager.getResource(id("textures/" + blink.getPath() + ".png")).isPresent()) {

            AssetInfo.TextureAssetInfo skinAsset = new AssetInfo.TextureAssetInfo(skin);
            AssetInfo.TextureAssetInfo blinkAsset = new AssetInfo.TextureAssetInfo(blink);

            return Optional.of(new Pair<>(
                    new SkinTextures(
                            blinkAsset,
                            original.cape(),
                            original.elytra(),
                            original.model(),
                            original.secure()
                    ),
                    new SkinTextures(
                            skinAsset,
                            original.cape(),
                            original.elytra(),
                            original.model(),
                            original.secure()
                    )
            ));
        }
        return Optional.empty();
    }

    public static String sanitizeName(String rawName) {
        return rawName
                .toLowerCase(Locale.ROOT)
                .trim()
                .replace(" ", "_")
                .replaceAll("[^\\w\\/\\.\\-]", "_");
    }

    static @NotNull Identifier id(String path) {
        return Identifier.of(Flutter.MOD_ID, path);
    }

}