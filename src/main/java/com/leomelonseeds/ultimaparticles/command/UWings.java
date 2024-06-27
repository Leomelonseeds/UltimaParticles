package com.leomelonseeds.ultimaparticles.command;

import org.bukkit.entity.Player;

import com.leomelonseeds.ultimaparticles.inv.Wings;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;

@CommandAlias("wings")
public class UWings extends BaseCommand {
    
    @Default
    @CommandPermission("trails.wings")
    public void onWing(Player p) {
        new Wings(p);
    }

}
