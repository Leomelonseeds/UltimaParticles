package com.leomelonseeds.ultimaparticles.inv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.DyeColor;
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
import com.leomelonseeds.ultimaparticles.custom.UParticleStyle;
import com.leomelonseeds.ultimaparticles.util.Utils;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.ParticleStyle;

public abstract class UPPaginatedInventory extends UPInventory {

    protected PlayerParticlesAPI ppapi;
    protected ConfigurationSection sec;
    protected Collection<ParticlePair> activeParticles;
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
        this.ppapi = UltimaParticles.getPlugin().getParticles();
        this.start = start;
        this.sec = getSection();
        this.page = 0;
        updateActiveParticles();
    }
    
    @Override
    public void updateInventory() {
        // Fill all other items first
        updateNonPaginatedInventory();
        
        // Back item
        int invSize = inv.getSize();
        inv.setItem(invSize - 5, Utils.createItem("back-item"));
        
        // Pagination
        Set<String> keys = sec.getKeys(false);
        List<String> keyList = new ArrayList<>(keys);
        int useableSize = invSize - 9 - start;
        int keySize = keyList.size();
        double maxPages = Math.ceil(keySize / (double) useableSize);
        
        if (page > 0) {
            inv.setItem(invSize - 9, Utils.createItem("previous-item"));
        }
        
        if (page < maxPages - 1) {
            inv.setItem(invSize - 1, Utils.createItem("next-item"));
        }
        
        // Get display item
        for (int i = page * useableSize; i < Math.min(keySize, page * useableSize + useableSize); i++) {
            ConfigurationSection section = sec.getConfigurationSection(keyList.get(i));
            ParticleEffect peffect = ParticleEffect.valueOf(section.getString("effect"));
            ItemStack item = new ItemStack(section.contains("material") ? 
                    Material.valueOf(section.getString("material")) : peffect.getGuiIconMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Utils.toComponent(section.getString("name")));

            String loreNode = "text-unlocked";
            if (!player.hasPermission(section.getString("permission"))) {
                loreNode = "text-locked";
            } else if (isSelected(peffect, section.getString("data"))) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                loreNode = "text-selected";
            }
            
            String lore = UltimaParticles.getPlugin().getConfig().getString(loreNode);
            meta.lore(List.of(Utils.toComponent(lore)));
            
            meta.getPersistentDataContainer().set(UltimaParticles.itemKey, PersistentDataType.STRING, section.getName());
            item.setItemMeta(meta);
            inv.setItem(start + (i % useableSize), item);
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
            new MainMenu(player);
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
        
        if (id.equals("remove")) {
            Utils.clearParticles(player, getUStyle());
            updateInventory();
            Utils.playSelectSound(player, true);
            return;
        }
        
        ConfigurationSection trail = sec.getConfigurationSection(id);
        if (trail != null) {
            if (item.containsEnchantment(Enchantment.DURABILITY)) {
                return;
            }
            
            if (!player.hasPermission(trail.getString("permission"))) {
                Utils.playSelectSound(player, false);
                player.sendMessage(Utils.toComponent("&cYou do not own this trail!"));
                return;
            }

            // Find effect and apply it
            String effect = trail.getString("effect");
            String data = trail.getString("data");
            for (String style : getStyles()) {
                ParticleStyle pstyle = ParticleStyle.fromInternalName(style);
                ppapi.removeActivePlayerParticles(player, pstyle);
                switch (effect) {
                    case "ITEM":
                        ppapi.addActivePlayerParticle(player, ParticleEffect.ITEM, pstyle, Material.valueOf(data));
                        break;
                    case "DUST":
                        ppapi.addActivePlayerParticle(player, ParticleEffect.DUST, pstyle, getColor(data));
                        break;
                    case "DUST_COLOR_TRANSITION":
                        ppapi.addActivePlayerParticle(player, ParticleEffect.DUST_COLOR_TRANSITION, pstyle, 
                                new ColorTransition(OrdinaryColor.RANDOM, OrdinaryColor.RANDOM));
                        break;
                    case "NOTE":
                        ppapi.addActivePlayerParticle(player, ParticleEffect.NOTE, pstyle, 
                                data.equals("RANDOM") ? NoteColor.RANDOM : NoteColor.RAINBOW);
                        break;
                    default:
                        ppapi.addActivePlayerParticle(player, ParticleEffect.valueOf(effect), pstyle);
                }
                
                // Do additional tasks if needed
                applyTrailHelper(trail, pstyle); 
            }
            
            // Update particles and inventory
            Utils.playSelectSound(player, true);
            updateActiveParticles();
            updateInventory();
            return;
        }
        
        // Check for other items per GUI
        registerPaginatedClick(slot, type, item, id);
    }
    
    private OrdinaryColor getColor(String data) {
        if (data.equals("RANDOM")) {
            return OrdinaryColor.RANDOM;
        }
        
        if (data.equals("RAINBOW")) {
            return OrdinaryColor.RAINBOW;
        }
        
        Color color = DyeColor.valueOf(data).getColor();
        return new OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    /**
     * Data must not be null if effect is DUST
     * 
     * @param effect
     * @param data
     * @return
     */
    private boolean isSelected(ParticleEffect effect, String data) {
        List<String> styles = getStyles();
        for (ParticlePair pp : activeParticles) {
            if (!styles.contains(pp.getStyle().getInternalName()) || !pp.getEffect().equals(effect)) {
                continue;
            }
            
            if (effect.equals(ParticleEffect.DUST) && !pp.getColor().equals(getColor(data))) {
                continue;
            }
            
            return true;
        }
        
        return false;
    }
    
    protected void updateActiveParticles() {
        this.activeParticles = ppapi.getActivePlayerParticles(player);
    }
    
    protected abstract void applyTrailHelper(ConfigurationSection sec, ParticleStyle pstyle);
    
    protected abstract void updateNonPaginatedInventory();
    
    protected abstract void registerPaginatedClick(int slot, ClickType type, ItemStack item, String id);
    
    protected abstract ConfigurationSection getSection();
    
    protected abstract UParticleStyle getUStyle();
    
    protected abstract List<String> getStyles();
}
