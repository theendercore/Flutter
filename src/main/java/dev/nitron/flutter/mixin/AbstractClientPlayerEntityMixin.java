package dev.nitron.flutter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.nitron.flutter.Flutter.getFlutterSkin;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private SkinTextures flutter$blinking(SkinTextures original) {
        return getFlutterSkin((PlayerEntity) (Object) this, original).orElse(original);
    }
}
