package com.leomelonseeds.ultimaparticles.inv;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.leomelonseeds.ultimaparticles.custom.UParticleStyle;
import com.leomelonseeds.ultimaparticles.util.Utils;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;

public class Wings extends UPPaginatedInventory {
    
    private static ParticleStyle wingsStyle = DefaultStyles.WINGS;
    
    public Wings(Player player) {
        super(player, 54, "Wings", 9);
    }

    @Override
    protected boolean isSelected(ParticleEffect effect) {
        for (ParticlePair pp : activeParticles) {
            if (pp.getEffect().equals(effect)  && pp.getStyle().equals(wingsStyle)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    protected void updateNonPaginatedInventory() {
        // Glass panes
        for (int i : new int[] {0, 45}) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i + j, Utils.createBlankItem(Material.PINK_STAINED_GLASS_PANE));
            }
        }
        
        // Remove
        inv.setItem(4, Utils.createItem("wings.remove"));
    }

    @Override
    protected ConfigurationSection getSection() {
        return Utils.getSec("wings.trails");
    }

    @Override
    protected void registerPaginatedClick(int slot, ClickType type, ItemStack item, String id) {}

    @Override
    protected UParticleStyle getUStyle() {
        return UParticleStyle.WINGS;
    }

    @Override
    protected void applyTrailHelper(ConfigurationSection sec, ParticleStyle pstyle) {}

    @Override
    protected List<String> getStyles() {
        return List.of(wingsStyle.getInternalName());
    }

}
