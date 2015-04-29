package com.kodingkingdom.craftermaker;
import java.util.logging.Level;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class CrafterMakerPlugin extends JavaPlugin implements Listener{
		
	CrafterMaker craftMaker;
	
	@Override
    public void onEnable(){craftMaker=new CrafterMaker(this);} 
    @Override
    public void onDisable(){}
    
    static CrafterMakerPlugin instance=null;
    public CrafterMakerPlugin(){instance=this;}
    public static CrafterMakerPlugin getPlugin(){
    	return instance;}
    public static void say(String msg){
    	instance.getLogger().log(Level.INFO//Level.FINE
    			, msg);}
    public static void debug(String msg){
    	instance.getLogger().log(Level.INFO//Level.FINE
    			, msg);}}