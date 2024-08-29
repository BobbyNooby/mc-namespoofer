package dev.bobbynooby.namespoofer.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.*;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;


@Mixin(ClientPlayNetworkHandler.class)
public abstract class ChatMixin {
    @Shadow
    @Final
    private static Text INVALID_PACKET_TEXT;
    private MinecraftClient client = MinecraftClient.getInstance();
    @Shadow
    private MessageSignatureStorage signatureStorage;

    @Shadow
    public abstract ClientConnection getConnection();


    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        Optional<MessageBody> optional = packet.body().toBody(this.signatureStorage);
        if (optional.isEmpty()) {
            this.getConnection().disconnect(INVALID_PACKET_TEXT);
        } else {
            try {
                if (client.player != null && packet.sender().equals(client.player.getUuid())) {
                    String spoofedName = "Spoofy";

                    MessageType.Parameters spoofedParameters = new MessageType.Parameters(packet.serializedParameters().type(), Text.literal(spoofedName), null);
                    ChatMessageS2CPacket spoofedPacket = new ChatMessageS2CPacket(packet.sender(), packet.index(), packet.signature(), packet.body(), packet.unsignedContent(), packet.filterMask(), spoofedParameters);
                    MessageLink messageLink;
                    messageLink = new MessageLink(spoofedPacket.index(), packet.sender(), UUID.randomUUID());
                    GameProfile spoofedProfile = new GameProfile(packet.sender(), spoofedName);

                    SignedMessage signedMessage = new SignedMessage(messageLink, spoofedPacket.signature(), (MessageBody) optional.get(), spoofedPacket.unsignedContent(), spoofedPacket.filterMask());

                    this.client.getMessageHandler().onChatMessage(signedMessage, spoofedProfile, spoofedPacket.serializedParameters());

                    System.out.println(spoofedPacket.serializedParameters());


                    ci.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
