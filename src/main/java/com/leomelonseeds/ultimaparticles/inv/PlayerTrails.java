package com.leomelonseeds.ultimaparticles.inv;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.custom.UParticleStyle;
import com.leomelonseeds.ultimaparticles.util.Utils;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;

public class PlayerTrails extends UPPaginatedInventory {
    
    private static String defaultStyle = "normal";

    public PlayerTrails(Player player) {
        super(player, 54, "Player Trails", 9);
    }

    @Override
    protected void updateNonPaginatedInventory() {
        // Glass panes
        for (int i : new int[] {0, 45}) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i + j, Utils.createBlankItem(Material.ORANGE_STAINED_GLASS_PANE));
            }
        }
        
        // Style button
        ItemStack styleItem = Utils.createItem("playertrails.style.style-item");
        ItemMeta smeta = styleItem.getItemMeta();
        String name = Utils.toPlain(smeta.displayName());
        name = name.replace("%style%", UltimaParticles.getPlugin().getConfig()
                .getString("playertrails.style.styles." + getStyle() + ".name"));
        smeta.displayName(Utils.toComponent(name));
        styleItem.setItemMeta(smeta);
        inv.setItem(3, styleItem);
        
        // Clear button
        inv.setItem(5, Utils.createItem("playertrails.remove"));
    }

    @Override
    protected ConfigurationSection getSection() {
        return Utils.getSec("playertrails.trails");
    }

    @Override
    protected void registerPaginatedClick(int slot, ClickType type, ItemStack item, String id) {
        if (id.equals("style-item")) {
            new StyleSelector(player, this);
            return;
        }
    }
    
    @Override
    protected void applyTrailHelper(ConfigurationSection trail, ParticleStyle pstyle) {
        if (!pstyle.getInternalName().equals(defaultStyle)) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.BLOCK, pstyle, Material.BARRIER);;
        }
        
        if (pstyle.equals(DefaultStyles.OVERHEAD)) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.CLOUD, pstyle);;
        }
    }
    
    @Override
    protected List<String> getStyles() {
        for (ParticlePair pp : activeParticles) {
            if (pp.getBlockMaterial() != Material.BARRIER) {
                continue;
            }
            
            return List.of(pp.getStyle().getInternalName());
        }
        
        return List.of(defaultStyle);
    }
    
    public String getStyle() {
        return getStyles().get(0);
    }
    
    /**
     * Change style for a player.
     * 
     * @param player
     * @param style
     */
    public void applyStyle(String style) {
        // Apply special cloud for rain trails
        if (style.equals("overhead")) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.CLOUD, DefaultStyles.OVERHEAD);
        } else {
            ppapi.removeActivePlayerParticles(player, ParticleEffect.CLOUD);
        }
        
        ParticleStyle pstyle = ParticleStyle.fromInternalName(style);
        int storedid = -1;
        for (ParticlePair pp : activeParticles) {
            if (pp.getBlockMaterial() == Material.BARRIER) {
                storedid = pp.getId();
            }
            
            if (!getUStyle().isType(pp.getStyle().getInternalName())) {
                continue;
            }
            
            ppapi.editActivePlayerParticle(player, pp.getId(), pstyle);
        }
        
        if (storedid == -1 && !style.equals(defaultStyle)) {
            ppapi.addActivePlayerParticle(player, ParticleEffect.BLOCK, pstyle, Material.BARRIER);
        }
        
        updateActiveParticles();
    }

    @Override
    protected UParticleStyle getUStyle() {
        return UParticleStyle.PLAYER;
    }
}
