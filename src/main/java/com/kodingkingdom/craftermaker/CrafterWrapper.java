package com.kodingkingdom.craftermaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.worldcretornica.plotme.PlotManager;
import com.worldcretornica.plotme.PlotMapInfo;
import com.worldcretornica.plotme.utils.NameFetcher;
import com.worldcretornica.plotme.utils.UUIDFetcher;

public class CrafterWrapper {
	private static final long bufferTime = 2*1000;
	private static ReentrantLock lock = new ReentrantLock(); 
	public static UUID getPlayerId(String playerName){		
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

        if (player.getUniqueId() != null) {
            return player.getUniqueId();}
        else {
        	
    		lock.lock();
    		try {
    			Thread.sleep(bufferTime);}
    		catch (InterruptedException e) {}
    		finally{
    			lock.unlock();}
            List<String> names = new ArrayList<String>();
            names.add(playerName);
            UUIDFetcher fetcher = new UUIDFetcher(names);

            try {
                CrafterMakerPlugin.say("Fetching " + playerName + " UUID from Mojang servers...");
                Map<String, UUID> response = fetcher.call();                    
                if (response.size() > 0) {
                	UUID playerId = response.values().toArray(new UUID[0])[0];
                	CrafterMakerPlugin.say("Fetched " + playerId.toString() + "for" + playerName);
                    return playerId;}}
            catch (IOException e) {
            	CrafterMakerPlugin.say("Unable to connect to Mojang server!");
            	if (e.getMessage()!=null&&e.getMessage().contains("HTTP response code: 429")){
            		CrafterMakerPlugin.say("HTTP response code 429");
                    CrafterMakerPlugin.say("Retrying...");
            		return getPlayerId(playerName);}} 
            catch (Exception e) {
            	CrafterMakerPlugin.say("Exception while running UUIDFetcher");
                e.printStackTrace();}}
        return null;}
	public static String getPlayerName(UUID playerId){			
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerId);

        if (player.getName() != null) {
            return player.getName();}
        else {
    		lock.lock();
    		try {
    			Thread.sleep(bufferTime);}
    		catch (InterruptedException e) {}
    		finally{
    			lock.unlock();}
            List<UUID> names = new ArrayList<UUID>();
            names.add(playerId);
            NameFetcher fetcher = new NameFetcher(names);

            try {
                CrafterMakerPlugin.say("Fetching " + playerId.toString() + " Name from Mojang servers...");
                Map<UUID, String> response = fetcher.call();
                
                if (response.size() > 0) {
                	String playerName = response.values().toArray(new String[0])[0];
                	CrafterMakerPlugin.say("Fetched " + playerName + "for" + playerId.toString());
                	return playerName;}}
            catch (IOException e) {
            	CrafterMakerPlugin.say("Unable to connect to Mojang server!");
            	if (e.getMessage()!=null&&e.getMessage().contains("HTTP response code: 429")){
            		CrafterMakerPlugin.say("HTTP response code 429");
                    CrafterMakerPlugin.say("Retrying...");
            		return getPlayerName(playerId);}} 
            catch (Exception e) {
            	CrafterMakerPlugin.say("Exception while running NameFetcher");
                e.printStackTrace();}}
		return null;}
	public static boolean isOnRoad(Location loc){		
		PlotMapInfo pmi = PlotManager.getMap(loc);
		
		if(pmi != null){
			int valx = loc.getBlockX();
			int valz = loc.getBlockZ();
			
			int size = pmi.PlotSize + pmi.PathWidth;
			int pathsize = pmi.PathWidth;
			boolean road = false;
			
			double n3;
			int mod2 = 0;
			int mod1 = 1;
			
			if(pathsize % 2 == 1){
				n3 = Math.ceil(((double)pathsize)/2);
				mod2 = -1;}
			else{
				n3 = Math.floor(((double)pathsize)/2);}
						
			for(double i = n3; i >= 0; i--){
				if((valx - i + mod1) % size == 0 ||
				   (valx + i + mod2) % size == 0){
					road = true;}
				if((valz - i + mod1) % size == 0 ||
				   (valz + i + mod2) % size == 0){
					road = true;}}
			
			return road;}
		else
			return true;}
	
	public static String getRawPlotId(Location loc){
		PlotMapInfo pmi = PlotManager.getMap(loc);
		
		if(pmi != null){
			int valx = loc.getBlockX();
			int valz = loc.getBlockZ();
			
			int size = pmi.PlotSize + pmi.PathWidth;
			int pathsize = pmi.PathWidth;
			boolean road = false;
			
			double n3;
			int mod2 = 0;
			int mod1 = 1;
			
			
			int x = (int) Math.ceil((double)valx / size);
			int z = (int) Math.ceil((double)valz / size);
			
			if(pathsize % 2 == 1){
				n3 = Math.ceil(((double)pathsize)/2);
				mod2 = -1;}
			else{
				n3 = Math.floor(((double)pathsize)/2);}
						
			for(double i = n3; i >= 0; i--){
				if((valx - i + mod1) % size == 0 ||
				   (valx + i + mod2) % size == 0){
					road = true;
					
					x = (int) Math.ceil((double)(valx - n3) / size);}
				if((valz - i + mod1) % size == 0 ||
				   (valz + i + mod2) % size == 0){
					road = true;
					
					z = (int) Math.ceil((double)(valz - n3) / size);}}
			
			if(road)
				return "";
			else
				return "" + x + ";" + z;}
		else
			return "";}}
