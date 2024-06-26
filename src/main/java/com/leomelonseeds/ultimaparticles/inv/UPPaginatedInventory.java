package com.leomelonseeds.ultimaparticles.inv;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.leomelonseeds.ultimaparticles.util.Utils;

public abstract class UPPaginatedInventory extends UPInventory {
    
    protected ConfigurationSection sec;
    private int start;
    private int page;

    /**
     * Creates a paginated inventory, where the last row is used for page/back button.
     * Start must be less than or equal to size - 9!!!
     * 
     * @param player
     * @param size
     * @param title
     * @param start where the inventory should start recording items
     */
    public UPPaginatedInventory(Player player, int size, String title, int start) {
        super(player, size, title);
        this.start = start;
        this.sec = getSection();
        this.page = 0;
    }
    
    @Override
    public void updateInventory() {
        Set<String> keys = sec.getKeys(false);
        List<String> keyList = new ArrayList<>(keys);
        int useableSize = inv.getSize() - 9 - start;
        int keySize = keyList.size();
        double maxPages = Math.ceil(keySize / (double) useableSize);
        
        if (page > 0) {
            inv.setItem(inv.getSize() - 9, Utils.createItem("previous-item"));
        }
        
        if (page < maxPages - 1) {
            inv.setItem(inv.getSize() - 1, Utils.createItem("next-item"));
        }
        
        for (int i = page * useableSize; i < Math.min(keySize, page * useableSize + useableSize); i++) {
            ItemStack item = getDisplayItem(sec.getConfigurationSection(keyList.get(i)));
            inv.setItem(start + (i % useableSize), item);
        }
        
        updateNonPaginatedInventory();
    }
    
    @Override
    public void registerClick(int slot, ClickType type) {
        ItemStack item = inv.getItem(slot);
        if (item == null) {
            return;
        }
        
        String id = Utils.getItemID(item);
        if (id == null) {
            return;
        }
        
        if (id.equals("previous-item")) {
            page--;
            updateInventory();
            return;
        }
        
        if (id.equals("next-item")) {
            page++;
            updateInventory();
            return;
        }
        
        registerPaginatedClick(slot, type, item, id);
    }
    
    protected abstract void updateNonPaginatedInventory();
    
    protected abstract void registerPaginatedClick(int slot, ClickType type, ItemStack item, String id);
    
    protected abstract ConfigurationSection getSection();
    
    protected abstract ItemStack getDisplayItem(ConfigurationSection section);
}
