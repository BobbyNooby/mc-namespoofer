package dev.bobbynooby.namespoofer.config;

public class Config {
    public static String spoofedName = "SpoofyBoofy";

    public void setSpoofedName(String spoofedName) {
        Config.spoofedName = spoofedName;
    }

    public  static String getSpoofedName() {
        return spoofedName;
    }
}
