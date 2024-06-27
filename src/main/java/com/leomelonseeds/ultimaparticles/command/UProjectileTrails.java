package com.leomelonseeds.ultimaparticles.command;

import org.bukkit.entity.Player;

import com.leomelonseeds.ultimaparticles.inv.ProjectileTrails;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;

@CommandAlias("projectiletrails")
public class UProjectileTrails extends BaseCommand {
    
    @Default
    public void onTrail(Player p) {
        new ProjectileTrails(p);
    }

}
