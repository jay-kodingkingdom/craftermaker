package com.kodingkingdom.craftermaker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.kodingkingdom.craftercoordinator.CrafterCoordinator;
import com.kodingkingdom.craftercoordinator.CrafterCoordinatorPlugin;
import com.kodingkingdom.craftercoordinator.CrafterPlayer;
import com.kodingkingdom.craftercoordinator.CrafterRegion;
import com.kodingkingdom.craftercoordinator.CrafterSchool;
import com.kodingkingdom.kodebuilder.KodeBuilderPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;
import com.worldcretornica.plotme.PlotMapInfo;
import com.worldcretornica.plotme.SqlManager;

public enum CrafterCommand {

	listPlayer("listPlayer",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("listPlayer")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}		
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Players:");
							for (CrafterPlayer player : coordinator.getPlayers().values()){
								String playerName = CrafterWrapper.getPlayerName(player.getId());
								sender.sendMessage(playerName==null?"Unknown!":playerName);}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	listSchool("listSchool",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("listSchool")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}		
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Schools:");
							for (CrafterSchool school : coordinator.getSchools().values()){
								sender.sendMessage(school.getName());}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	listSchoolPlayer("listSchoolPlayer",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("listSchoolPlayer")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, final String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							CrafterSchool school;
							try{
								school=coordinator.getSchool(args[1]);}
							catch (Exception e){
								sender.sendMessage("Could not find school"+args[1]+"!");
								return;}
							sender.sendMessage("School Players:");
							for (UUID playerId : school.getPlayers()){
								String playerName = CrafterWrapper.getPlayerName(playerId);
								sender.sendMessage(playerName==null?"Unknown!":playerName);}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	listPlotLimit("listPlotLimit",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("listPlotLimit")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}	
						(new BukkitRunnable(){public void run(){
							for (CrafterRegion region : coordinator.getPlotLimit()){
								sender.sendMessage("Plot limit in "+region.getWorld().getName()+" from ("+region.getMinX()+","+region.getMinY()+","+region.getMinZ()+") to ("+region.getMaxX()+","+region.getMaxY()+","+region.getMaxZ()+")");}
						}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	listSchoolPlot("listSchoolPlot",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("listSchoolPlot")){
				return new CrafterHandler(){
					public boolean handle(CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}	
						(new BukkitRunnable(){public void run(){}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						sender.sendMessage("Plots of school "+args[1]+":");
						for (Map.Entry<String,CrafterRegion> regionEntry : coordinator.getSchoolRegion(args[1]).entrySet()){
							CrafterRegion region = regionEntry.getValue();
							sender.sendMessage("Plot "+regionEntry.getKey()+" in "+region.getWorld().getName()+" from ("+region.getMinX()+","+region.getMinY()+","+region.getMinZ()+") to ("+region.getMaxX()+","+region.getMaxY()+","+region.getMaxZ()+")");}
						return true;}};}
			else return null;}}),
	addSchoolPlayer("addSchoolPlayer",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("addSchoolPlayer")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, final String[] args){
						if (args.length!=3){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							CrafterSchool school;
							try{
								school=coordinator.getSchool(args[1]);}
							catch (Exception e){
								sender.sendMessage("Could not find school"+args[1]+"!");
								return;}
							UUID playerId = CrafterWrapper.getPlayerId(args[2]);
							if (playerId==null){
								sender.sendMessage("Could not find player "+args[2]+" from school "+school.getName()+"!");
								return;}
							try {
								HashSet<CrafterRegion> regions = new HashSet<CrafterRegion> (coordinator.getSchoolRegion(args[1]).values());
																						
								for (CrafterRegion region : regions){
									
									String minId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMinX(),region.getMinY(),region.getMinZ()));
									String maxId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMaxX(),region.getMaxY(),region.getMaxZ()));
																
									int minX = PlotManager.getIdX(minId);
									int minZ = PlotManager.getIdZ(minId);
									int maxX = PlotManager.getIdX(maxId);
									int maxZ = PlotManager.getIdZ(maxId);
									
									for (int x = maxX;x>=minX;x--){
										for (int z = maxZ;z>=minZ;z--){
											PlotManager.getPlots(region.getWorld()).get(x+";"+z).addAllowed(playerId);}}}
								
								sender.sendMessage("Player "+args[2]+" added to school "+school.getName());
								
								coordinator.addPlayer(playerId);
								school.getPlayers().add(playerId);}
							catch(Exception e){
								sender.sendMessage("Could not add player "+args[2]+" from school "+school.getName()+"!");}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	addSchool("addSchool",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("addSchool")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, final String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try {
								coordinator.addSchool(args[1]);
								sender.sendMessage("School "+args[1]+" added");}
							catch(Exception e){
								sender.sendMessage("Could not add school "+args[1]+"!");}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	addPlotLimit("addPlotLimit",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("addPlotLimit")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						if (!(sender instanceof Player)){
							sender.sendMessage("Only players can do this!");
							return false;}		
						(new BukkitRunnable(){public void run(){
							Selection selection = worldEdit.getSelection(((Player)sender));			
							if (selection==null) {
								sender.sendMessage("Selection is null!");}
							else{
								CrafterRegion region = new CrafterRegion(selection.getMinimumPoint(), selection.getMaximumPoint());
								coordinator.addPlotLimit(region);
								sender.sendMessage("Plot limit in "+region.getWorld().getName()+" from ("+region.getMinX()+","+region.getMinY()+","+region.getMinZ()+") to ("+region.getMaxX()+","+region.getMaxY()+","+region.getMaxZ()+") added");}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	addSchoolPlot("addSchoolPlot",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("addSchoolPlot")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, final String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						if (!(sender instanceof Player)){
							sender.sendMessage("Only players can do this!");
							return false;}		
						(new BukkitRunnable(){public void run(){
							Selection selection = worldEdit.getSelection(((Player)sender));			
							if (selection==null) {
								sender.sendMessage("Selection is null!");
								return;}
							else if (!PlotManager.isPlotWorld(selection.getWorld())){
								sender.sendMessage("This is not a plot world!");
								return;}
							else {
								PlotMapInfo pmi = PlotManager.getMap(selection.getWorld());
								int measureSize = pmi.PlotSize + pmi.PathWidth;
								
								int minX = (int) Math.ceil((double)(selection.getMinimumPoint().getBlockX()) / measureSize);
								int minZ = (int) Math.ceil((double)(selection.getMinimumPoint().getBlockZ()) / measureSize);
								int maxX = (int) Math.ceil((double)(selection.getMaximumPoint().getBlockX()) / measureSize);
								int maxZ = (int) Math.ceil((double)(selection.getMaximumPoint().getBlockZ()) / measureSize);
								
								String minId=minX+";"+minZ;
								String maxId=maxX+";"+maxZ;

								Location minLoc = PlotManager.getPlotBottomLoc(selection.getWorld(), minId);
								Location maxLoc = PlotManager.getPlotTopLoc(selection.getWorld(), maxId);
								minLoc.setY(coordinator.getHeightMinLimit());
								maxLoc.setY(coordinator.getHeightMaxLimit());
								
								final CrafterRegion region = new CrafterRegion(minLoc,maxLoc);
								
								final Plot idPlot = new Plot(args[1], PlotManager.getPlotTopLoc(region.getWorld(), maxId), PlotManager.getPlotBottomLoc(region.getWorld(), minId), minId, PlotManager.getMap(region.getWorld()).DaysToExpiration);
										
								HashSet<String> unavailablePlots = new HashSet<String>();
								for (int x=minX;x<=maxX;x++){
									for (int z=minZ;z<=maxZ;z++){
										if (!PlotManager.isPlotAvailable(x+";"+z, (Player)sender)) {
											unavailablePlots.add(x+";"+z);}}}
								if (unavailablePlots.isEmpty()){
									
									(new BukkitRunnable(){public void run(){PlotManager.setOwnerSign(region.getWorld(), idPlot);}}).runTask(CrafterMakerPlugin.getPlugin());
						            								
									for (int x=minX;x<=maxX;x++){
										for (int z=minZ;z<=maxZ;z++){
											Plot plot = new Plot(args[1], PlotManager.getPlotTopLoc(region.getWorld(), maxId), PlotManager.getPlotBottomLoc(region.getWorld(), minId), x+";"+z, PlotManager.getMap(region.getWorld()).DaysToExpiration);
											PlotManager.getPlots(region.getWorld()).put(x+";"+z, plot);
											SqlManager.addPlot(plot, x, z, region.getWorld());
											for (UUID playerId : coordinator.getSchool(args[1]).getPlayers()){
												plot.addAllowed(playerId);}}}
									
									coordinator.addSchoolRegion(args[1],minId,region);
									sender.sendMessage("School plot '"+minId+"' added to school "+args[1]);
									return;}
								else {
									sender.sendMessage("Cannot make school plot here!");
									sender.sendMessage("The following plots are unavailable!");
									for (String plotId : unavailablePlots){
										sender.sendMessage(plotId);}
									return;}}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	removeSchoolPlayer("removeSchoolPlayer",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("removeSchoolPlayer")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, final String[] args){
						if (args.length!=3){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							CrafterSchool school;
							try{
								school=coordinator.getSchool(args[1]);}
							catch (Exception e){
								sender.sendMessage("Could not find school"+args[1]+"!");
								return;}
							UUID playerId = CrafterWrapper.getPlayerId(args[2]);
							if (playerId==null){
								sender.sendMessage("Could not remove player "+args[2]+" from school "+school.getName()+"!");
								return;}
							try {
								school.getPlayers().remove(playerId);
								
								HashSet<CrafterRegion> regions = new HashSet<CrafterRegion> (coordinator.getSchoolRegion(args[1]).values());
								
								for (CrafterRegion region : regions){
									
									String minId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMinX(),region.getMinY(),region.getMinZ()));
									String maxId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMaxX(),region.getMaxY(),region.getMaxZ()));
																
									int minX = PlotManager.getIdX(minId);
									int minZ = PlotManager.getIdZ(minId);
									int maxX = PlotManager.getIdX(maxId);
									int maxZ = PlotManager.getIdZ(maxId);
									
									for (int x = maxX;x>=minX;x--){
										for (int z = maxZ;z>=minZ;z--){
											PlotManager.getPlots(region.getWorld()).get(x+";"+z).removeAllowed(playerId);}}}
								
								sender.sendMessage("Player "+args[2]+" removed from school "+school.getName());
								return;}
							catch(Exception e){
								sender.sendMessage("Could not remove player "+args[2]+" from school "+school.getName()+"!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	removeSchool("removeSchool",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("removeSchool")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, final String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try {
								HashSet<CrafterRegion> regions = new HashSet<CrafterRegion> (coordinator.getSchoolRegion(args[1]).values());
			
								coordinator.removeSchool(args[1]);
															
								for (CrafterRegion region : regions){
									final World w = region.getWorld();

									final String minId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMinX(),region.getMinY(),region.getMinZ()));
									final String maxId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMaxX(),region.getMaxY(),region.getMaxZ()));
									
									final int minX = PlotManager.getIdX(minId);
									final int minZ = PlotManager.getIdZ(minId);
									final int maxX = PlotManager.getIdX(maxId);
									final int maxZ = PlotManager.getIdZ(maxId);
									
									for (int x = maxX;x>=minX;x--){
										for (int z = maxZ;z>=minZ;z--){
											PlotManager.getPlots(w).remove(x+";"+z);
											SqlManager.deletePlot(x, z, w.getName().toLowerCase());}}
									
									(new BukkitRunnable(){public void run(){PlotManager.removeOwnerSign(w, minId);}}).runTask(CrafterMakerPlugin.getPlugin());}
								
								sender.sendMessage("School "+args[1]+" removed");
								return;}
							catch(Exception e){
								sender.sendMessage("Could not remove school "+args[1]+"!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	removePlotLimit("removePlotLimit",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("removePlotLimit")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						if (!(sender instanceof Player)){
							sender.sendMessage("Only players can do this!");
							return false;}	
						(new BukkitRunnable(){public void run(){
							Location loc=((Player)sender).getLocation();
							CrafterRegion region=null;
							for (CrafterRegion r : coordinator.getPlotLimit()){
								if (r.isIn(loc)){region=r;break;}}
							if (region==null){
								sender.sendMessage("No plot limit exists here!");}
							else{
								coordinator.getPlotLimit().remove(region);
								sender.sendMessage("Plot limit in "+region.getWorld().getName()+" from ("+region.getMinX()+","+region.getMinY()+","+region.getMinZ()+") to ("+region.getMaxX()+","+region.getMaxY()+","+region.getMaxZ()+") removed");}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	removeSchoolPlot("removeSchoolPlot",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("removeSchoolPlot")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						if (!(sender instanceof Player)){
							sender.sendMessage("Only players can do this!");
							return false;}			
						(new BukkitRunnable(){public void run(){						
							Player player = ((Player)sender);
							Location loc=player.getLocation();
							loc.setY(coordinator.getHeightMinLimit());
							String schoolName=null;
							CrafterRegion region=null;
							for (CrafterSchool school : coordinator.getSchools().values()){
								for (CrafterRegion r : coordinator.getSchoolRegion(school.getName()).values()){
									if (r.isIn(loc)){region=r;schoolName=school.getName();break;}}}
							
							if (region==null){
								sender.sendMessage("No school plot exists here!");
								return;}
							else{
								final World w = loc.getWorld();

								final String minId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMinX(),region.getMinY(),region.getMinZ()));
								final String maxId=CrafterWrapper.getRawPlotId(new Location(region.getWorld(),region.getMaxX(),region.getMaxY(),region.getMaxZ()));
															
								coordinator.getSchoolRegion(schoolName).remove(minId);
															
								final int minX = PlotManager.getIdX(minId);
								final int minZ = PlotManager.getIdZ(minId);
								final int maxX = PlotManager.getIdX(maxId);
								final int maxZ = PlotManager.getIdZ(maxId);
								
								for (int x = maxX;x>=minX;x--){
									for (int z = maxZ;z>=minZ;z--){
										PlotManager.getPlots(player).remove(x+";"+z);
										SqlManager.deletePlot(x, z, w.getName().toLowerCase());}}
								
								(new BukkitRunnable(){public void run(){PlotManager.removeOwnerSign(w, minId);}}).runTask(CrafterMakerPlugin.getPlugin());								
								
								sender.sendMessage("School plot '"+minId+"' removed from school "+schoolName);
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	getLoadAmount("getLoadAmount",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("getLoadAmount")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Load amount is "+coordinator.getLoadAmountLimit());}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	getLoadTime("getLoadTime",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("getLoadTime")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Load time is "+coordinator.getLoadTimeLimit());}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
			getKodeAmount("getKodeAmount",new HandlerFactory(){
				public CrafterHandler makeHandler(CommandSender sender, Command command,
						String label, String[] args){
					if (args.length>0&&args[0].equalsIgnoreCase("getKodeAmount")){
						return new CrafterHandler(){
							public boolean handle(final CommandSender sender, Command command,
									String label, String[] args){
								if (args.length!=1){
									sender.sendMessage("Wrong number of arguments!");
									return false;}
								(new BukkitRunnable(){public void run(){
									sender.sendMessage("Kode amount is "+KodeBuilderPlugin.getPlugin().getKodeBuilder().getLoadAmountLimit());}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
								return true;}};}
					else return null;}}),
			getKodeTime("getKodeTime",new HandlerFactory(){
				public CrafterHandler makeHandler(CommandSender sender, Command command,
						String label, String[] args){
					if (args.length>0&&args[0].equalsIgnoreCase("getKodeTime")){
						return new CrafterHandler(){
							public boolean handle(final CommandSender sender, Command command,
									String label, String[] args){
								if (args.length!=1){
									sender.sendMessage("Wrong number of arguments!");
									return false;}
								(new BukkitRunnable(){public void run(){
									sender.sendMessage("Kode time is "+KodeBuilderPlugin.getPlugin().getKodeBuilder().getLoadTimeLimit());}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
								return true;}};}
					else return null;}}),
	getHeightMax("getHeightMax",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("getHeightMax")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Height max is "+coordinator.getHeightMaxLimit());}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	getHeightMin("getHeightMin",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("getHeightMin")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Height min is "+coordinator.getHeightMinLimit());}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),//KodeBuilderPlugin.getPlugin().getKodeBuilder()
	setLoadAmount("setLoadAmount",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("setLoadAmount")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						final int loadAmount;
						try{
							loadAmount=Integer.parseInt(args[1]);}
						catch(NumberFormatException e){
							sender.sendMessage("Unreadable load amount!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								coordinator.setLoadAmountLimit(loadAmount);
								sender.sendMessage("Load amount set to "+loadAmount);
								return;}
							catch(IllegalArgumentException e){
								sender.sendMessage("Illegal load amount!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	setLoadTime("setLoadTime",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("setLoadTime")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						final int loadTime;
						try{
							loadTime=Integer.parseInt(args[1]);}
						catch(NumberFormatException e){
							sender.sendMessage("Unreadable load time!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								coordinator.setLoadTimeLimit(loadTime);
								sender.sendMessage("Load time set to "+loadTime);
								return;}
							catch(IllegalArgumentException e){
								sender.sendMessage("Illegal load time!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	setKodeAmount("setKodeAmount",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("setKodeAmount")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						final int loadAmount;
						try{
							loadAmount=Integer.parseInt(args[1]);}
						catch(NumberFormatException e){
							sender.sendMessage("Unreadable load amount!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								KodeBuilderPlugin.getPlugin().getKodeBuilder().setLoadAmountLimit(loadAmount);
								sender.sendMessage("Kode amount set to "+loadAmount);
								return;}
							catch(IllegalArgumentException e){
								sender.sendMessage("Illegal kode amount!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	setKodeTime("setKodeTime",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("setKodeTime")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						final int loadTime;
						try{
							loadTime=Integer.parseInt(args[1]);}
						catch(NumberFormatException e){
							sender.sendMessage("Unreadable kode time!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								KodeBuilderPlugin.getPlugin().getKodeBuilder().setLoadTimeLimit(loadTime);
								sender.sendMessage("Kode time set to "+loadTime);
								return;}
							catch(IllegalArgumentException e){
								sender.sendMessage("Illegal kode time!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	setHeightMax("setHeightMax",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("setHeightMax")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						final int heightMax;
						try{
							heightMax=Integer.parseInt(args[1]);}
						catch(NumberFormatException e){
							sender.sendMessage("Unreadable height!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								coordinator.setHeightMaxLimit(heightMax);
								sender.sendMessage("Height max set to "+heightMax);
								return;}
							catch(IllegalArgumentException e){
								sender.sendMessage("Illegal height!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	setHeightMin("setHeightMin",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("setHeightMin")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=2){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						final int heightMin;
						try{
							heightMin=Integer.parseInt(args[1]);}
						catch(NumberFormatException e){
							sender.sendMessage("Unreadable height!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								coordinator.setHeightMinLimit(heightMin);
								sender.sendMessage("Height min set to "+heightMin);
								return;}
							catch(IllegalArgumentException e){
								sender.sendMessage("Illegal height!");
								return;}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	updatePlayerRegions("updatePlayerRegions",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("updatePlayerRegions")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							sender.sendMessage("Updating player regions...");
							for (CrafterPlayer player : coordinator.getPlayers().values()){
								coordinator.getPlayerRegion(player.getId()).clear();}
							sender.sendMessage("Player regions cleared");
							try {
								for(World w : Bukkit.getServer().getWorlds()){ 
									if(PlotManager.isPlotWorld(w)){ 
										HashMap<String, Plot> plots = PlotManager.getPlots(w); 
										for(Plot plot : plots.values()){ 
											Location top = PlotManager.getPlotTopLoc(w, plot.id);top.setY(coordinator.getHeightMaxLimit());
								            Location bottom = PlotManager.getPlotBottomLoc(w, plot.id);bottom.setY(coordinator.getHeightMinLimit());
								            UUID playerId = plot.ownerId==null? CrafterWrapper.getPlayerId(plot.owner) : plot.ownerId;
											if (playerId != null) coordinator.addPlayerRegion(playerId, plot.id, new CrafterRegion(top, bottom));
											for (String playerName : new HashSet<String>(plot.allowed())){
												playerId = CrafterWrapper.getPlayerId(playerName);
												if (playerId != null) coordinator.addPlayerRegion(playerId, plot.id, new CrafterRegion(top, bottom));}
											sender.sendMessage("Plot "+plot.id+" regions updated");}}}		
								sender.sendMessage("Player regions updated");}
							catch(Exception ex){
								sender.sendMessage("Exception "+ex.getCause()+" thrown!");
							}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}}),
	updateSchoolPlayers("updateSchoolPlayers",new HandlerFactory(){
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args){
			if (args.length>0&&args[0].equalsIgnoreCase("updateSchoolPlayers")){
				return new CrafterHandler(){
					public boolean handle(final CommandSender sender, Command command,
							String label, String[] args){
						if (args.length!=1){
							sender.sendMessage("Wrong number of arguments!");
							return false;}
						(new BukkitRunnable(){public void run(){
							try{
								sender.sendMessage("Updating school players...");
								for(String schoolName : coordinator.getSchools().keySet()){
									for (UUID playerId : new HashSet<UUID> (coordinator.getSchool(schoolName).getPlayers())){
										if (CrafterWrapper.getPlayerName(playerId)==null) {
											coordinator.getSchool(schoolName).getPlayers().remove(playerId);
											sender.sendMessage("Player "+playerId.toString()+" deleted from school "+schoolName);}}}		
								sender.sendMessage("School players updated");}
							catch(Exception ex){
								sender.sendMessage("Exception "+ex.getCause()+" thrown!");
							}}}).runTaskAsynchronously(CrafterMakerPlugin.getPlugin());
						return true;}};}
			else return null;}});
	

	static CrafterCoordinator coordinator = CrafterCoordinatorPlugin.getPlugin().getCoordinator();
	static WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	
	String commandName;
	HandlerFactory handlerFactory;

	private CrafterCommand(String CommandName, HandlerFactory HandlerFactory){commandName=CommandName;handlerFactory=HandlerFactory;}
	
	public String getName(){return commandName;}
	public CrafterHandler getHandler(CommandSender sender, Command command,
			String label, String[] args){return handlerFactory.makeHandler(sender, command, label, args);}
	
	public interface CrafterHandler{
		public boolean handle(CommandSender sender, Command command,
				String label, String[] args);}
	private interface HandlerFactory{
		public CrafterHandler makeHandler(CommandSender sender, Command command,
				String label, String[] args);}}
