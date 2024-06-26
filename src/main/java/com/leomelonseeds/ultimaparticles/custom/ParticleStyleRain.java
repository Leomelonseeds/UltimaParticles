package com.leomelonseeds.ultimaparticles.custom;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;

/**
 * Dummy class to represent the rain style
 */
public class ParticleStyleRain implements ParticleStyle {

    @Override
    public boolean canBeFixed() {
        return false;
    }

    @Override
    public String getInternalName() {
        return "rain";
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return Collections.emptyList();
    }

    @Override
    public void updateTimers() {}

}
