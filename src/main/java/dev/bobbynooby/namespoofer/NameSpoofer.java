package dev.bobbynooby.namespoofer;

import dev.bobbynooby.namespoofer.commands.MainCommand;
import dev.bobbynooby.namespoofer.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class NameSpoofer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(MainCommand::register);
    }
}
