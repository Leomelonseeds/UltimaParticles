package com.leomelonseeds.ultimaparticles.inv;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.util.Utils;

public class MainMenu extends UPInventory {
    
    private Player player;

    public MainMenu(Player player) {
        super(player, 45, "Trails");
        this.player = player;
    }

    @Override
    public void updateInventory() {
        // Glass panes
        for (int i : new int[] {0, 36}) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i + j, Utils.createBlankItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
            }
        }

        ConfigurationSection menuConfig = UltimaParticles.getPlugin().getConfig().getConfigurationSection("mainmenu");
        for (String key : menuConfig.getKeys(false)) {
            ConfigurationSection item = menuConfig.getConfigurationSection(key);
            int slot = item.getInt("slot");
            inv.setItem(slot, Utils.createItem(item));
        }
        
        inv.setItem(40, Utils.createItem("back-item"));
    }

    @Override
    public void registerClick(int slot, ClickType type) {
        // TODO Auto-generated method stub
        
    }
    
}
