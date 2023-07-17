package com.aembr.multidrop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = MultiDrop.MOD_ID, name = MultiDrop.MOD_NAME, version = MultiDrop.VERSION)
public class MultiDrop {
    public static final String MOD_ID = "multidrop";
    public static final String MOD_NAME = "MultiDrop";
    public static final String VERSION = "1.12.2-0.1";

    private static final String CONFIG_FILE_NAME = "multidrop.json";
    public List<String> itemNames;
    private File configDir;
    public static int dropKey = Keyboard.KEY_GRAVE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = event.getModConfigurationDirectory();

        loadConfig();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void keyboardEvent(GuiScreenEvent.KeyboardInputEvent.Post event) {
        GuiScreen screen = event.getGui();
        if (!isValidGui(screen)) {
            return;
        }

        GuiContainer c = (GuiContainer) screen;
        Container container = c.inventorySlots;

        if (Keyboard.getEventKey() == dropKey) {
            for (Slot slot : container.inventorySlots) {
                ItemStack item = slot.getStack();
                String registryName = item.getItem().getRegistryName().toString();

                if (registryName.equals("minecraft:air")) {
                    continue;
                }

                for (String itemName : itemNames) {
                    if (registryName.equals(itemName)) {
                        Minecraft.getMinecraft().playerController.windowClick(
                                container.windowId,
                                slot.slotNumber,
                                0,
                                ClickType.THROW,
                                Minecraft.getMinecraft().player);
                    }
                }
            }
        }
    }

    private Boolean isValidGui(GuiScreen screen) {
        return screen instanceof GuiChest || screen instanceof GuiInventory;
    }

    private void loadConfig() {
        File configFile = new File(configDir, CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            try {
                InputStream defaultConfigStream = getClass().getResourceAsStream("/default-config.json");
                Files.copy(defaultConfigStream, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonElement element = new JsonParser().parse(new FileReader(configFile));
            JsonObject obj = element.getAsJsonObject();

            // Load item names from config
            JsonArray itemNamesArray = obj.getAsJsonArray("itemNames");
            itemNames = new ArrayList<>();
            for (JsonElement notificationElement : itemNamesArray) {
                String itemNameString = notificationElement.getAsString();
                itemNames.add(itemNameString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
