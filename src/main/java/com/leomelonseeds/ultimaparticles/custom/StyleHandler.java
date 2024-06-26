package com.leomelonseeds.ultimaparticles.custom;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.esophose.playerparticles.event.ParticleStyleRegistrationEvent;
import dev.esophose.playerparticles.styles.ParticleStyle;

/**
 * Handles custom styles
 */
public class StyleHandler implements Listener {
    
    private Set<ParticleStyle> customStyles;
    
    public StyleHandler() {
        this.customStyles = new HashSet<>();
        
        // Create custom styles
        // customStyles.add(new ParticleStyleRain());
    }
    
    @EventHandler
    public void onParticleStyleRegistration(ParticleStyleRegistrationEvent event) {
        customStyles.forEach(ps -> event.registerStyle(ps));
    }
}
