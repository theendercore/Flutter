package dev.nitron.flutter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientMannequinEntity;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.nitron.flutter.Flutter.getFlutterSkin;

@Environment(EnvType.CLIENT)
@Mixin(ClientMannequinEntity.class)
public abstract class ClientMannequinEntityMixin extends MannequinEntity {

    public ClientMannequinEntityMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private SkinTextures flutter$blinking(SkinTextures original) {
        return getFlutterSkin(getMannequinProfile().getGameProfile(), age, original).orElse(original);
    }

}
