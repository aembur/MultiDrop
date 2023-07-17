package com.aembr.multidrop;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.input.Keyboard;

import java.util.regex.Pattern;

@Mod(modid = MultiDrop.MOD_ID, name = MultiDrop.MOD_NAME, version = MultiDrop.VERSION)
public class MultiDrop {
    public static final String MOD_ID = "multidrop";
    public static final String MOD_NAME = "MultiDrop";
    public static final String VERSION = "1.12.2-0.1";

    public static int countKey = Keyboard.KEY_C;
    public static int countRunningTotalKey = Keyboard.KEY_V;
    public static Pattern searchPattern = Pattern.compile("§.(\\d+) §fremaining uses");
    public static int runningTotal = 0;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
