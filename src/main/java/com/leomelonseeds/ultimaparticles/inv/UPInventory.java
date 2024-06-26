package com.leomelonseeds.ultimaparticles.inv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.util.Utils;

public abstract class UPInventory {
    
    protected static InventoryManager manager = UltimaParticles.getPlugin().getInvs();
    protected Inventory inv;
    protected Player player;
    
    public UPInventory(Player player, int size, String title) {
        inv = Bukkit.createInventory(null, size, Utils.toComponent(title));
        register(player);
    }
    
    public UPInventory(Player player, InventoryType type, String title) {
        inv = Bukkit.createInventory(null, type, Utils.toComponent(title));
        register(player);
    }
    
    private void register(Player player) {
        this.player = player;
        manager.registerInventory(player, this);
    }
    
    public abstract void updateInventory();
    
    public abstract void registerClick(int slot, ClickType type);
    
    public Inventory getInventory() {
        return inv;
    }
}
