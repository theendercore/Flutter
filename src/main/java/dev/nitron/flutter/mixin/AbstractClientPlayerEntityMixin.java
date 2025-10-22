package dev.nitron.flutter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.nitron.flutter.Flutter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private SkinTextures flutter$blinking(SkinTextures original) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        String username = player.getName().getString().toLowerCase(Locale.ROOT);

        Identifier skin = Identifier.of(Flutter.MOD_ID, "skin/" + username + "/skin");
        Identifier blink = Identifier.of(Flutter.MOD_ID, "skin/" + username + "/blink");

        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        if (manager.getResource(Identifier.of(Flutter.MOD_ID, "textures/" + skin.getPath() + ".png")).isPresent()
                && manager.getResource(Identifier.of(Flutter.MOD_ID, "textures/" + blink.getPath() + ".png")).isPresent()) {

            boolean blinking = player.age % 80 < 2;
            AssetInfo.TextureAssetInfo skinAsset = new AssetInfo.TextureAssetInfo(skin);
            AssetInfo.TextureAssetInfo blinkAsset = new AssetInfo.TextureAssetInfo(blink);

            return new SkinTextures(
                    blinking ? blinkAsset : skinAsset,
                    original.cape(),
                    original.elytra(),
                    original.model(),
                    original.secure()
            );
        }

        return original;
    }

}
