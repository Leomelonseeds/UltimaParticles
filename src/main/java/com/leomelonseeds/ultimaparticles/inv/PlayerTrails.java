package com.leomelonseeds.ultimaparticles.inv;

import java.util.Collection;
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

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;

public class PlayerTrails extends UPPaginatedInventory {
    
    private static String defaultStyle = "normal";
    
    private PlayerParticlesAPI ppapi;
    private Player player;
    private Collection<ParticlePair> activeParticles;

    public PlayerTrails(Player player) {
        super(player, 54, "Player Trails", 9);
        this.ppapi = UltimaParticles.getPlugin().getParticles();
        this.player = player;
        updateActiveParticles();
    }
    
    private void updateActiveParticles() {
        this.activeParticles = ppapi.getActivePlayerParticles(player);
    }

    @Override
    protected void updateNonPaginatedInventory() {
        // Glass panes
        for (int i : new int[] {0, 45}) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i + j, Utils.createBlankItem(Material.ORANGE_STAINED_GLASS_PANE));
            }
        }
        
        // TODO: Style button
        // ItemStack styleItem = Utils.createItem'
        
        // Clear button
        inv.setItem(5, Utils.createItem("playertrails.remove"));
        
        // Back button
        inv.setItem(49, Utils.createItem("back-item"));
    }

    @Override
    protected ConfigurationSection getSection() {
        return Utils.getSec("playertrails.trails");
    }

    @Override
    protected ItemStack getDisplayItem(ConfigurationSection section) {
        ParticleEffect peffect = ParticleEffect.valueOf(section.getString("effect"));
        ItemStack item = new ItemStack(section.contains("material") ? 
                Material.valueOf(section.getString("material")) : peffect.getGuiIconMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Utils.toComponent(section.getString("name")));

        String style = getStyle();
        String loreNode = "text-unlocked";
        if (!player.hasPermission(section.getString("permission"))) {
            loreNode = "text-locked";
        } else {
            for (ParticlePair pp : activeParticles) {
                if (!pp.getEffect().equals(peffect) || !pp.getStyle().getInternalName().equals(style)) {
                    continue;
                }
                
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                loreNode = "text-selected";
                break;
            }
        }
        
        String lore = UltimaParticles.getPlugin().getConfig().getString(loreNode);
        meta.lore(List.of(Utils.toComponent(lore)));
        
        meta.getPersistentDataContainer().set(UltimaParticles.itemKey, PersistentDataType.STRING, section.getName());
        item.setItemMeta(meta);
        
        return item;
    }

    @Override
    public void registerPaginatedClick(int slot, ClickType type, ItemStack item, String id) {
        if (id.equals("remove")) {
            clearParticles();
            updateInventory();
            player.sendMessage(Utils.toComponent("&7Cleared your player trail."));
            return;
        }
        
        // TODO: Style code
        
        ConfigurationSection trail = sec.getConfigurationSection(id);
        if (trail == null) {
            return;
        }
        
        String effect = trail.getString("effect");
        String data = trail.getString("data");
        applyParticle(effect, data);
        updateInventory();
    }
    
    private String getStyle() {
        for (ParticlePair pp : activeParticles) {
            if (pp.getBlockMaterial() != Material.BARRIER) {
                continue;
            }
            
            return pp.getStyle().getInternalName();
        }
        
        return defaultStyle;
    }
    
    private void clearParticles() {
        String style = getStyle();
        ParticleStyle pstyle = ParticleStyle.fromInternalName(style);
        ppapi.resetActivePlayerParticles(player);
        if (style.equals(defaultStyle)) {
            return;
        }
        
        ppapi.addActivePlayerParticle(player, ParticleEffect.BLOCK, pstyle, Material.BARRIER);
        updateActiveParticles();
    }
    
    /**
     * Change style for a player.
     * 
     * @param player
     * @param style
     */
    private void applyStyle(String style) {
        int storedid = -1;
        for (ParticlePair pp : activeParticles) {
            if (pp.getBlockMaterial() == Material.BARRIER) {
                storedid = pp.getId();
                break;
            }
        }
        
        ParticleStyle pstyle = ParticleStyle.fromInternalName(style);
        if (storedid == -1) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.BLOCK, pstyle, Material.BARRIER);
        } else {
            ppapi.editActivePlayerParticle(player, storedid, pstyle);
        }
        
        if (style.equals("rain")) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.CLOUD, DefaultStyles.OVERHEAD);
        } else {
            ppapi.removeActivePlayerParticles(player, ParticleEffect.CLOUD);
        }
        
        updateActiveParticles();
    }
    
    /**
     * Data is an optional string that can be included in the section for Material
     * 
     * @param player
     * @param effect
     * @param data
     */
    private void applyParticle(String effect, String data) {
        String style = getStyle();
        ParticleStyle pstyle = ParticleStyle.fromInternalName(style);
        ppapi.removeActivePlayerParticles(player, pstyle);
        switch (effect) {
            case "ITEM":
                ppapi.addActivePlayerParticle(player, ParticleEffect.ITEM, pstyle, Material.valueOf(data));
                break;
            case "DUST":
                ppapi.addActivePlayerParticle(player, ParticleEffect.DUST, pstyle, OrdinaryColor.RAINBOW);
                break;
            case "NOTE":
                ppapi.addActivePlayerParticle(player, ParticleEffect.NOTE, pstyle, NoteColor.RAINBOW);
                break;
            default:
                ppapi.addActivePlayerParticle(player, ParticleEffect.valueOf(effect), pstyle);
        }
        
        if (!style.equals(defaultStyle)) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.BLOCK, pstyle, Material.BARRIER);;
        }
        
        updateActiveParticles();
    }
}
