package com.leomelonseeds.ultimaparticles;

import org.bukkit.plugin.java.JavaPlugin;

import com.leomelonseeds.ultimaparticles.command.ParticleTrails;
import com.leomelonseeds.ultimaparticles.command.ProjectileTrails;
import com.leomelonseeds.ultimaparticles.command.Trails;
import com.leomelonseeds.ultimaparticles.command.Wings;
import com.leomelonseeds.ultimaparticles.inv.InventoryManager;

import co.aikar.commands.PaperCommandManager;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;

public class UltimaParticles extends JavaPlugin {
    
    private static UltimaParticles plugin;
    
    private PlayerParticlesAPI ppAPI;
    private InventoryManager invs;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        this.ppAPI = PlayerParticlesAPI.getInstance();
        PaperCommandManager cmdManager = new PaperCommandManager(this);
        cmdManager.registerCommand(new ParticleTrails());
        cmdManager.registerCommand(new ProjectileTrails());
        cmdManager.registerCommand(new Wings());
        cmdManager.registerCommand(new Trails());
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
