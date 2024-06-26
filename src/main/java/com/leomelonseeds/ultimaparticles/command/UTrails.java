package com.leomelonseeds.ultimaparticles.command;

import org.bukkit.entity.Player;

import com.leomelonseeds.ultimaparticles.inv.MainMenu;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;

@CommandAlias("trail|trails")
public class UTrails extends BaseCommand {
    
    @Default
    public void onTrail(Player p) {
        new MainMenu(p);
    }
}
