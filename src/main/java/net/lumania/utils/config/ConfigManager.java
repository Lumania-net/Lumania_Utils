package net.lumania.utils.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load and manipulate
 * Bukkit configs from resources.
 * @author sparkofyt
 * @version 1.0
 * @since 1.0
*/
public class ConfigManager {

    private final Logger logger;
    private final File file;
    private FileConfiguration config;

    /**
     * Creates an ConfigManager instance
     * @param plugin The Bukkit Plugin where to get the resource file from
     * @param resource The filename of the wanted resource
     */
    public ConfigManager(Plugin plugin, String resource) {
        String fileName = resource.replace(".yml", "") + ".yml";

        this.logger = LoggerFactory.getLogger(ConfigManager.class);
        this.file = new File(plugin.getDataFolder(), fileName);

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Method to save the current FileConfiguration to the file.
     * @since 1.0
     */
    public void save() {
        try {
            config.save(file);
            reload();
        } catch (IOException exception) {
            logger.error("[!] FAILED TO SAVE A CONFIG FILE", exception);
        }
    }

    /**
     * Method to reload the current FileConfiguration
     * @since 1.0
     */
    public void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            logger.error("[!] FAILED TO RELOAD A CONFIG FILE", exception);
        }
    }

    /**
     * Method to get a String from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return A String with the value of given path
     * @since 1.0
     */
    public String getString(String path) {
        if(!config.contains(path)) return null;
        return config.getString(path);
    }

    /**
     * Method to get a formatted String from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return A ChatColor formatted String with the value of given path
     * @since 1.0
     */
    public String getFormatted(String path) {
        if(!config.contains(path)) return null;
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    /**
     * Method to get a String List from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return A String List with the value of given path
     * @since 1.0
     */
    public List<String> getStringList(String path) {
        if(!config.contains(path)) return null;
        return config.getStringList(path);
    }

    /**
     * Method to get an Integer from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return An Integer with the value of given path
     * @since 1.0
     */
    public int getInt(String path) {
        if(!config.contains(path)) return 0;
        return config.getInt(path);
    }

    /**
     * Method to get a Double from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return A Double with the value of given path
     * @since 1.0
     */
    public double getDouble(String path) {
        if(!config.contains(path)) return 0;
        return config.getDouble(path);
    }

    /**
     * Method to get a Long from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return A Long with the value of given path
     * @since 1.0
     */
    public long getLong(String path) {
        if(!config.contains(path)) return 0;
        return config.getLong(path);
    }

    /**
     * Method to get a Boolean from the current ConfigManager File
     * @param path The path to the wanted config entry
     * @return A Boolean with the value of given path
     * @since 1.0
     */
    public boolean getBoolean(String path) {
        if(!config.contains(path)) return false;
        return config.getBoolean(path);
    }

    /**
     * Method to save a Bukkit Location to a Config
     * @param path The path where to save the Location
     * @param location The Location to save
     * @param overwrite When true, location will be overwritten if exists
     * @since 1.0
     */
    public void setLocation(String path, Location location, boolean overwrite) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        if(overwrite) {
            config.set(path + ".World", world);
            config.set(path + ".X", x);
            config.set(path + ".Y", y);
            config.set(path + ".Z", z);
            config.set(path + ".Yaw", yaw);
            config.set(path + ".Pitch", pitch);
        } else {
            if (!config.contains(path)) {
                config.set(path + ".World", world);
                config.set(path + ".X", x);
                config.set(path + ".Y", y);
                config.set(path + ".Z", z);
                config.set(path + ".Yaw", yaw);
                config.set(path + ".Pitch", pitch);
            }
        }

        save();
    }

    /**
     * Method to read a location from Config
     * @param path The path to read a location from
     * @return A Bukkit Location from given path
     * @since 1.0
     */
    public Location getLocation(String path) {
        String world = getString(path + ".World");
        double x = getDouble(path + ".X");
        double y = getDouble(path + ".Y");
        double z = getDouble(path + ".Z");
        float yaw = (float) getDouble(path + ".Yaw");
        float pitch = (float) getDouble(path + ".Pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public void setItem(String path, ItemStack item) {
        String material = item.getType().toString();
        String name = ((TextComponent) item.displayName()).content();
        int amount = item.getAmount();

        List<String> itemFlags = new ArrayList<>();
        if(!item.getItemFlags().isEmpty()) {
            item.getItemFlags().forEach(flag -> {
                itemFlags.add(flag.toString());
            });
        }

        List<String> lore = new ArrayList<>();
        if(item.lore() != null) {
            List<Component> components = item.lore();
            if(components != null) {
                components.forEach(component -> {
                    if(component instanceof TextComponent) {
                        lore.add(((TextComponent) component).content());
                    }
                });
            }
        }

        config.set(path + ".Name", name);
        config.set(path + ".Material", material);
        config.set(path + ".Amount", amount);

        if(!itemFlags.isEmpty())
            config.set(path + ".ItemFlags", itemFlags);

        if(!lore.isEmpty())
            config.set(path + ".Lore", lore);

        save();
    }


    /**
     * Method to read an itemstack from Config
     * @param path The path to read an itemstack from
     * @return A Bukkit ItemStack from given path
     * @since 1.0
     */
    public ItemStack getItem(String path) {
        ItemStack item = new ItemStack(Material.valueOf(getString(path + ".Material")));

        if (getInt(path + ".Amount") != 0)
            item.setAmount(getInt(path + ".Amount"));

        ItemMeta meta = item.getItemMeta();
        meta.displayName(net.kyori.adventure.text.Component.text(getFormatted(path + ".Name")));

        if (getStringList(path + ".ItemFlags") != null) {
            for (String itemFlag : getStringList(path + ".ItemFlags")) {
                meta.addItemFlags(ItemFlag.valueOf(itemFlag));
            }
        }

        if (getStringList(path + ".Lore") != null) {
            List<net.kyori.adventure.text.Component> finalLore = new ArrayList<>();
            for (String lore : getStringList(path + ".Lore")) {
                finalLore.add(Component.text(ChatColor.translateAlternateColorCodes('&', lore)));
            }

            if (!finalLore.isEmpty())
                meta.lore(finalLore);
        }

        item.setItemMeta(meta);
        return item;
    }
}
