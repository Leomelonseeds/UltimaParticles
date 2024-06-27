package com.leomelonseeds.ultimaparticles.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.util.Utils;

public class MainMenu extends UPInventory {

    public MainMenu(Player player) {
        super(player, 45, "Trails");
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
        ItemStack item = inv.getItem(slot);
        if (item == null) {
            return;
        }
        
        String id = Utils.getItemID(item);
        if (id == null) {
            return;
        }
        
        if (id.equals("back-item")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), UltimaParticles.getPlugin().getConfig()
                    .getString("back-command").replace("%player%", player.getName()));
            return;
        }
        
        if (id.equals("remove")) {
            Utils.clearParticles(player, null);
            return;
        }
        
        if (id.equals("playertrails")) {
            new PlayerTrails(player);
            return;
        }
        
        if (id.equals("projectiletrails")) {
            new ProjectileTrails(player);
            return;
        }
        
        if (id.equals("wings")) {
            if (!player.hasPermission("group.divine")) {
                player.sendMessage(Utils.toComponent("&cThese trails require the &6&lDIVINE &crank!"));
                Utils.playSelectSound(player, false);
                return;
            }
            new Wings(player);
            return;
        }
    }
    
}
