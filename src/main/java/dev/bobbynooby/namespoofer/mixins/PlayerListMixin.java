package dev.bobbynooby.namespoofer.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListMixin {


    private final MinecraftClient client = MinecraftClient.getInstance();

    @Shadow
    public abstract GameProfile getProfile();

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<Text> cir) {
        if (Objects.equals(this.getProfile().getId(), client.player.getUuid())) {

            Text spoofedName = Text.literal("Skibi");
            cir.setReturnValue(spoofedName);
            cir.cancel();
        }
    }
}
