package com.leomelonseeds.ultimaparticles.inv;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.util.Utils;

import dev.esophose.playerparticles.styles.ParticleStyle;

// TODO Make this abstract and available for every class, to add more projectile/wing styles
public class StyleSelector extends UPInventory {
    
    private PlayerTrails prevMenu;
    private ConfigurationSection styleConfig;
    private int start;

    public StyleSelector(Player player, PlayerTrails prevMenu) {
        super(player, 45, "Style Selection");
        this.prevMenu = prevMenu;
        this.styleConfig = UltimaParticles.getPlugin().getConfig().getConfigurationSection("playertrails.style.styles");
        this.start = 19;
    }

    @Override
    public void updateInventory() {
        // Glass panes
        for (int i : new int[] {0, 36}) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i + j, Utils.createBlankItem(Material.ORANGE_STAINED_GLASS_PANE));
            }
        }
        
        // Back item
        inv.setItem(40, Utils.createItem("back-item"));
        
        // Styles
        int curIndex = start;
        for (String style : styleConfig.getKeys(false)) {
            ConfigurationSection section = styleConfig.getConfigurationSection(style);
            ParticleStyle pstyle = ParticleStyle.fromInternalName(style);
            ItemStack item = new ItemStack(section.contains("material") ? 
                    Material.valueOf(section.getString("material")) : pstyle.getGuiIconMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Utils.toComponent(section.getString("name")));

            String loreNode = "text-unlocked";
            if (!player.hasPermission(section.getString("permission"))) {
                loreNode = "text-locked";
            } else if (prevMenu.getStyle().equals(style)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                loreNode = "text-selected";
            }
            
            String lore = UltimaParticles.getPlugin().getConfig().getString(loreNode);
            meta.lore(List.of(Utils.toComponent(lore)));
            
            meta.getPersistentDataContainer().set(UltimaParticles.itemKey, PersistentDataType.STRING, section.getName());
            item.setItemMeta(meta);
            inv.setItem(curIndex++, item);
        }
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
            new PlayerTrails(player);
        }
        
        ConfigurationSection style = styleConfig.getConfigurationSection(id);
        if (style == null) {
            return;
        }
        
        if (item.containsEnchantment(Enchantment.DURABILITY)) {
            return;
        }
        
        if (!player.hasPermission(style.getString("permission"))) {
            Utils.playSelectSound(player, false);
            player.sendMessage(Utils.toComponent("&cYou do not own this trail!"));
            return;
        }

        prevMenu.applyStyle(id);
        Utils.playSelectSound(player, true);
        updateInventory();
        return;
    }

}
