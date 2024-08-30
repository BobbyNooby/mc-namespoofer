package dev.bobbynooby.namespoofer.mixins;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.JsonOps;
import dev.bobbynooby.namespoofer.config.Config;
import dev.bobbynooby.namespoofer.utils.LogPrinter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.*;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        if (Config.getSpoofAll() && Config.getSpoofChat()) {
            Optional<MessageBody> optional = packet.body().toBody(this.signatureStorage);
            if (optional.isEmpty()) {
                this.getConnection().disconnect(INVALID_PACKET_TEXT);
            } else {

                try {
                    if (client.player != null && packet.sender().equals(client.player.getGameProfile().getId())) {

                        MessageType.Parameters spoofedParameters = new MessageType.Parameters(packet.serializedParameters().type(), Text.literal(Config.getSpoofedName()), null);
                        ChatMessageS2CPacket spoofedPacket = new ChatMessageS2CPacket(packet.sender(), packet.index(), packet.signature(), packet.body(), packet.unsignedContent(), packet.filterMask(), spoofedParameters);
                        MessageLink messageLink;
                        messageLink = new MessageLink(spoofedPacket.index(), packet.sender(), UUID.randomUUID());
                        GameProfile spoofedProfile = new GameProfile(packet.sender(), Config.getSpoofedName());

                        SignedMessage signedMessage = new SignedMessage(messageLink, spoofedPacket.signature(), (MessageBody) optional.get(), spoofedPacket.unsignedContent(), spoofedPacket.filterMask());

                        this.client.getMessageHandler().onChatMessage(signedMessage, spoofedProfile, spoofedPacket.serializedParameters());

                        System.out.println(spoofedPacket.serializedParameters());


                        ci.cancel();
                    }
                } catch (Exception e) {
                    LogPrinter.println("Error : " + e);
                }
            }
        }
    }


    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {

        if (Config.getSpoofAll() && Config.getSpoofGameMessages()) {
            GameMessageS2CPacket spoofedPacket = new GameMessageS2CPacket(nameReplacer(packet.content()), packet.overlay());

            this.client.getMessageHandler().onGameMessage(spoofedPacket.content(), spoofedPacket.overlay());
            ci.cancel();
        }
    }

    @Unique
    public Text nameReplacer(Text inputText) {
        try {
            Gson gson = new Gson();

            String rawJson = gson.toJson(TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, inputText).getOrThrow());

            String stringToReplace = "\"text\":\"" + client.player.getName().getString() + "\"";
            String replacement = "\"text\":\"" + Config.getSpoofedName() + "\"";
            String spoofedJson = rawJson.replace(stringToReplace, replacement);

            Text returnText = TextCodecs.CODEC.decode(JsonOps.INSTANCE, gson.fromJson(spoofedJson, JsonElement.class)).getOrThrow().getFirst();

            return returnText;
        } catch (Exception e) {
            LogPrinter.println("Error in getting client player name");
            return inputText;
        }
    }

}
