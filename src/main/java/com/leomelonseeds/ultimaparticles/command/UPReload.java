package com.leomelonseeds.ultimaparticles.command;

import org.bukkit.command.CommandSender;

import com.leomelonseeds.ultimaparticles.UltimaParticles;
import com.leomelonseeds.ultimaparticles.util.Utils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;

@CommandAlias("upreload")
public class UPReload extends BaseCommand {
    
    @Default
    public void onReload(CommandSender sender) {
        UltimaParticles.getPlugin().reload();
        sender.sendMessage(Utils.toComponent("&7UltimaParticles reloaded"));
    }

}
