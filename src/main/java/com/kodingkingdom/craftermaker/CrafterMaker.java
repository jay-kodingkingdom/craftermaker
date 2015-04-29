package com.kodingkingdom.craftermaker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.kodingkingdom.craftermaker.CrafterCommand.CrafterHandler;

public class CrafterMaker implements Listener, CommandExecutor{	
		
	CrafterMakerPlugin plugin;

	CrafterMaker(CrafterMakerPlugin Plugin){
		plugin=Plugin;
		plugin.getCommand("craftermaker").setExecutor(this);
		plugin.getCommand("cm").setExecutor(this);}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (sender instanceof Player){
			if (!(CrafterCommand.coordinator.checkPlayerLimit(((Player)sender).getUniqueId()))){
				sender.sendMessage("ACCESS DENIED: No permission");
				return false;}}
		
		if (args.length==0){
			sender.sendMessage("Crafter Maker commands:");
			for (CrafterCommand crafterCommand : CrafterCommand.values()){
				sender.sendMessage(crafterCommand.getName());}
			return true;}
		else{
			for (CrafterCommand crafterCommand : CrafterCommand.values()){
				CrafterHandler handler=crafterCommand.getHandler(sender, command, label, args);
				if (handler!=null)return handler.handle(sender, command, label, args);}
			sender.sendMessage("Command not understood!");
			return false;}}}
