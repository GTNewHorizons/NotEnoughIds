package com.gtnewhorizons.neid;

public class Common {

    public static boolean thermosTainted;

    static {
        try {
            Class.forName("org.bukkit.World");
            Common.thermosTainted = true;
        } catch (ClassNotFoundException e) {
            Common.thermosTainted = false;
        }
    }
}
