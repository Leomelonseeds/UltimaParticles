package com.leomelonseeds.ultimaparticles.custom;

import com.leomelonseeds.ultimaparticles.UltimaParticles;

public enum UParticleStyle {
    PLAYER,
    WINGS,
    PROJECTILE;
    
    /**
     * Check if the given style internal name string corresponds
     * to the particle style
     * 
     * @param str
     * @return
     */
    public boolean isType(String str) {
        switch (this) {
            case PLAYER:
                return UltimaParticles.getPlugin().getConfig().getConfigurationSection("playertrails.style.styles")
                        .getKeys(false).contains(str);
            case WINGS:
                return str.equals("wings");
            case PROJECTILE:
                return str.equals("arrows") || str.equals("fishing");
        }
        
        return false;
    }

}
