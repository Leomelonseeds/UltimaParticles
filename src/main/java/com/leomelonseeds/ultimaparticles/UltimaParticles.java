package com.leomelonseeds.ultimaparticles;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import com.leomelonseeds.ultimaparticles.command.UPlayerTrails;
import com.leomelonseeds.ultimaparticles.command.UProjectileTrails;
import com.leomelonseeds.ultimaparticles.command.UTrails;
import com.leomelonseeds.ultimaparticles.command.UPReload;
import com.leomelonseeds.ultimaparticles.command.UWings;
import com.leomelonseeds.ultimaparticles.custom.StyleHandler;
import com.leomelonseeds.ultimaparticles.inv.InventoryManager;

import co.aikar.commands.PaperCommandManager;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;

public class UltimaParticles extends JavaPlugin {

    public static NamespacedKey itemKey;
    
    private static UltimaParticles plugin;
    
    private PlayerParticlesAPI ppAPI;
    private InventoryManager invs;
    private StyleHandler styleHandler;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        itemKey = new NamespacedKey(plugin, "upitem");
        
        saveDefaultConfig();
        
        PaperCommandManager cmdManager = new PaperCommandManager(this);
        cmdManager.registerCommand(new UPlayerTrails());
        cmdManager.registerCommand(new UProjectileTrails());
        cmdManager.registerCommand(new UWings());
        cmdManager.registerCommand(new UTrails());
        cmdManager.registerCommand(new UPReload());
        
        this.ppAPI = PlayerParticlesAPI.getInstance();
        this.invs = new InventoryManager();
        this.styleHandler = new StyleHandler();
        
        Bukkit.getPluginManager().registerEvents(this.invs, this);
        Bukkit.getPluginManager().registerEvents(this.styleHandler, this);
    }
    
    public static UltimaParticles getPlugin() {
        return plugin;
    }
    
    public PlayerParticlesAPI getParticles() {
        return ppAPI;
    }
    
    public InventoryManager getInvs() {
        return invs;
    }
    
    public StyleHandler getStyleHandler() {
        return styleHandler;
    }
    
    public void reload() {
        this.reloadConfig();
    }
     
}
