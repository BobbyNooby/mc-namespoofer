package dev.bobbynooby.namespoofer.utils;

public class LogPrinter {

    public static final String preMessage = "[NameSpoofer] ";
    public static void print(Object message) {
        System.out.print(preMessage + message.toString());

    }

    public static void println(Object message) {
        System.out.println(preMessage + message.toString());
    }
}
