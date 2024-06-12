package com.leomelonseeds.ultimaparticles;

import org.bukkit.plugin.java.JavaPlugin;

import com.leomelonseeds.ultimaparticles.inv.InventoryManager;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;

public class UltimaParticles extends JavaPlugin {
    
    private static UltimaParticles plugin;
    
    private PlayerParticlesAPI ppAPI;
    private InventoryManager invs;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        this.ppAPI = PlayerParticlesAPI.getInstance();
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
     
}
