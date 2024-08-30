package dev.bobbynooby.namespoofer.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.bobbynooby.namespoofer.config.Config;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class MainCommand {


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("namespoofer")
                //Setname Command
                .then(ClientCommandManager.literal("setname")
                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    String name = StringArgumentType.getString(context, "name");
                                    Config.setSpoofedName(name);
                                    context.getSource().sendFeedback(Text.literal("Set spoofed name to " + name));
                                    return 1;
                                })))

                //Config Commands
                .then(createConfigCommand()));
    }


    private static LiteralArgumentBuilder<FabricClientCommandSource> createConfigCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> configCommand = ClientCommandManager.literal("config");
        for (String configOption : Config.validOptions) {
            configCommand.then(ClientCommandManager.literal(configOption)
                    .then(ClientCommandManager.literal("set")
                            .then(ClientCommandManager.literal("true")
                                    .executes(context -> {
                                        Config.setCommands.get(configOption).accept(true);
                                        context.getSource().sendFeedback(Text.literal("Set " + configOption + " to true"));
                                        return 1;
                                    }))
                            .then(ClientCommandManager.literal("false")
                                    .executes(context -> {
                                        Config.setCommands.get(configOption).accept(false);
                                        context.getSource().sendFeedback(Text.literal("Set " + configOption + " to false"));
                                        return 1;
                                    })))
                    .then(ClientCommandManager.literal("get")
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.literal(configOption + " is " + Config.getCommands.get(configOption).get()));
                                return 1;
                            }))
                    .then(ClientCommandManager.literal("toggle").executes(context -> {
                        Config.runCommands.get(configOption).run();
                        context.getSource().sendFeedback(Text.literal("Toggled " + configOption + " to " + Config.getCommands.get(configOption).get()));
                        return 1;
                    })));

        }

        return configCommand;
    }

}
