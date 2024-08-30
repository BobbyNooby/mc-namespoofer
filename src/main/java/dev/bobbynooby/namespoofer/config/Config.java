package dev.bobbynooby.namespoofer.config;

import net.minecraft.client.MinecraftClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Config {
    public static final Map<String, Supplier<Boolean>> getCommands = new HashMap<>();
    public static final Map<String, Consumer<Boolean>> setCommands = new HashMap<>();
    public static final Map<String, Runnable> runCommands = new HashMap<>();
    public static String spoofedName = MinecraftClient.getInstance().getGameProfile().getName();
    public static Boolean spoofAll = true;
    public static Boolean spoofChat = true;
    public static Boolean spoofPlayerList = true;
    public static Boolean spoofGameMessages = true;
    public static String[] validOptions = {"spoofAll", "spoofChat", "spoofPlayerList", "spoofGameMessages"};

    static {
        getCommands.put("spoofAll", Config::getSpoofAll);
        getCommands.put("spoofChat", Config::getSpoofChat);
        getCommands.put("spoofPlayerList", Config::getSpoofPlayerList);
        getCommands.put("spoofGameMessages", Config::getSpoofGameMessages);
        setCommands.put("spoofAll", Config::setSpoofAll);
        setCommands.put("spoofChat", Config::setSpoofChat);
        setCommands.put("spoofPlayerList", Config::setSpoofPlayerList);
        setCommands.put("spoofGameMessages", Config::setSpoofGameMessages);
        runCommands.put("spoofAll", Config::toggleSpoofAll);
        runCommands.put("spoofChat", Config::toggleSpoofChat);
        runCommands.put("spoofPlayerList", Config::toggleSpoofPlayerList);
        runCommands.put("spoofGameMessages", Config::toggleSpoofGameMessages);
    }


    public static String getSpoofedName() {
        return spoofedName;
    }

    public static void setSpoofedName(String spoofedName) {
        Config.spoofedName = spoofedName;
    }

    public static Boolean getSpoofAll() {
        return spoofAll;
    }

    public static void setSpoofAll(Boolean spoofAll) {
        Config.spoofAll = spoofAll;
    }

    public static void toggleSpoofAll() {
        Config.spoofAll = !Config.spoofAll;
    }

    public static Boolean getSpoofChat() {
        return spoofChat;
    }

    public static void setSpoofChat(Boolean spoofChat) {
        Config.spoofChat = spoofChat;
    }

    public static void toggleSpoofChat() {
        Config.spoofChat = !Config.spoofChat;
    }

    public static Boolean getSpoofPlayerList() {
        return spoofPlayerList;
    }

    public static void setSpoofPlayerList(Boolean spoofPlayerList) {
        Config.spoofPlayerList = spoofPlayerList;
    }

    public static void toggleSpoofPlayerList() {
        Config.spoofPlayerList = !Config.spoofPlayerList;
    }

    public static Boolean getSpoofGameMessages() {
        return spoofGameMessages;
    }

    public static void setSpoofGameMessages(Boolean spoofGameMessages) {
        Config.spoofGameMessages = spoofGameMessages;
    }

    public static void toggleSpoofGameMessages() {
        Config.spoofGameMessages = !Config.spoofGameMessages;
    }
}


