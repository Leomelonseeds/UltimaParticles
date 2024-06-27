package com.leomelonseeds.ultimaparticles.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import com.cryptomorin.xseries.XItemStack;
import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.custom.UParticleStyle;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class Utils {
    
    /**
     * Component to plain text, keeping section color codes!
     * 
     * @param component
     * @return
     */
    public static String toPlain(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
    
    /**
     *  Convert all ampersands to section symbols
     * 
     * @param s
     * @return
     */
    public static String convertAmps(String s) {
        return s.replaceAll("&", "§");
    }
    
    /**
     * Get a line, translate it to a component.
     * 
     * @param line
     * @return
     */
    public static Component toComponent(String line) {
        return LegacyComponentSerializer.legacySection().deserialize(convertAmps(line)).decoration(TextDecoration.ITALIC, false);
    }
    
    /**
     * Get lines to translate to components
     * 
     * @param line
     * @return
     */
    public static List<Component> toComponent(List<String> lines) {
        List<Component> result = new ArrayList<>();
        for (String s : lines) {
            result.add(toComponent(s));
        }
        return result;
    }
    
    /**
     * GUI click sound
     * 
     * @param player
     * @param whether it was successful
     */
    public static void playSelectSound(Player player, boolean success) {
        Sound sound = success ? Sound.ENTITY_EXPERIENCE_ORB_PICKUP : Sound.BLOCK_NOTE_BLOCK_BASS;
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }
    
    /**
     * Shorthand to get config sec from config
     * 
     * @param sec
     * @return
     */
    public static ConfigurationSection getSec(String sec) {
        return UltimaParticles.getPlugin().getConfig().getConfigurationSection(sec);
    }
    
    /**
     * Create an item from the config section, adding
     * persistent data
     * 
     * @param config
     * @param path the path String to add to the item
     * @return
     */
    public static ItemStack createItem(ConfigurationSection sec) {
        ItemStack i = XItemStack.deserialize(sec, s -> ChatColor.translateAlternateColorCodes('&', s));
        ItemMeta meta = i.getItemMeta();
        meta.getPersistentDataContainer().set(UltimaParticles.itemKey, PersistentDataType.STRING, sec.getName());
        i.setItemMeta(meta);
        return i;
    }
    
    public static ItemStack createBlankItem(Material mat) {
        ItemStack i = new ItemStack(mat);
        ItemMeta meta = i.getItemMeta();
        meta.displayName(toComponent(""));
        i.setItemMeta(meta);
        return i;
    }
    
    /**
     * Get an item defined by the key
     * 
     * @param key
     * @return
     */
    public static ItemStack createItem(String key) {
        return createItem(getSec(key));
    }

    public static String getItemID(ItemStack i) {
        if (i == null || i.getItemMeta() == null) {
            return null;
        }
        
        ItemMeta meta = i.getItemMeta();
        if (!meta.getPersistentDataContainer().has(UltimaParticles.itemKey, PersistentDataType.STRING)) {
            return null;
        }
        
        return meta.getPersistentDataContainer().get(UltimaParticles.itemKey, PersistentDataType.STRING);
    }
    
    public static BukkitTask schedule(int ticks, Runnable runnable) {
        return Bukkit.getScheduler().runTaskLater(UltimaParticles.getPlugin(), () -> runnable.run(), ticks);
    }
    
    /**
     * @param style set to null to clear all
     */
    public static void clearParticles(Player player, UParticleStyle style) {
        if (style == null) {
            for (UParticleStyle ups : UParticleStyle.values()) {
                clearParticles(player, ups);
            }
            
            return;
        }
        
        PlayerParticlesAPI ppapi = UltimaParticles.getPlugin().getParticles();
        List<Integer> toRemove = new ArrayList<>();
        ParticleStyle curStyle = null;
        for (ParticlePair pp : ppapi.getActivePlayerParticles(player)) {
            if (!style.isType(pp.getStyle().getInternalName())) {
                continue;
            }
            
            if (style == UParticleStyle.PLAYER) {
                curStyle = pp.getStyle();
            }
            
            toRemove.add(pp.getId());
        }
        
        toRemove.forEach(id -> ppapi.removeActivePlayerParticle(player, id));
        if (style == UParticleStyle.PLAYER && curStyle != null && curStyle != DefaultStyles.NORMAL) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.BLOCK, curStyle, Material.BARRIER);
        }
    }

}
