package com.leomelonseeds.ultimaparticles.command;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;

@CommandAlias("wings")
public class Wings extends BaseCommand {
    
    @Default
    @CommandPermission("trails.wings")
    public void onWing(Player p) {
        // TODO
    }

}
