package com.leomelonseeds.ultimaparticles.command;

import org.bukkit.entity.Player;

import com.leomelonseeds.ultimaparticles.inv.PlayerTrails;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;

@CommandAlias("playertrails")
public class UPlayerTrails extends BaseCommand {
    
    @Default
    public void onTrail(Player p) {
        new PlayerTrails(p);
    }

}
