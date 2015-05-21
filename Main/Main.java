package Main;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	public String at = ChatColor.BLUE + "AdminTools: " + ChatColor.AQUA + "";
	
	public String cmds = ChatColor.BLUE + "/admintools ";
	public String usage = ChatColor.BLACK + " - " + ChatColor.AQUA + "";
	
	public String error = ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "";
	
	public static final String NL = System.getProperty("line.separator");  
	public static Main plugin;
	
	
	public void onEnable() {
		saveConfig();
		loadConf();
	}
	
	public void loadConf() {
		FileConfiguration config = getConfig();
		
		config.addDefault("Test.Reason", "Test");
		config.addDefault("Test.Banner", "WitchCraftMC");

		
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player p = (Player)sender;
		if(cmd.getName().equalsIgnoreCase("admintools")) {

			if(args.length == 0) {
			p.sendMessage(at + "Help");
			p.sendMessage(cmds + "getip <username>" + usage + "Gets a players REAL IP.");
			p.sendMessage(cmds + "stp <username>" + usage + "Secretly TP's To A Player.");
			p.sendMessage(cmds + "reveal <username>" + usage + "Reveals Yourself From The Specified Player (Works With STP)");
			p.sendMessage(cmds + "ban <username>" + usage + "Permanetly Bans the Specified Player with a message.");
			p.sendMessage(cmds + "kick <username>" + usage + "Kicks the Specified Player");
			p.sendMessage(cmds + "inv <username>" + usage + "Sees the Specified Players Inventory");
			p.sendMessage(cmds + "warn <username>" + usage + "Warns A Player"); 
			} 
			else if(args[0].equalsIgnoreCase("getip")) {
				if(p.hasPermission("at.getip") || p.isOp() || p.hasPermission("at.*")) {
				Player target = getServer().getPlayer(args[1]);
					
					if(target == null) {
						p.sendMessage(error + "Please Specifie A Player That Is Online!");
					} else {
						p.sendMessage(at + "Found " + args[1] + " IP. " + target.getAddress());
					}				
				}else {
					p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");

				}
			}else if(args[0].equalsIgnoreCase("stp")) {
				if(p.hasPermission("at.stp") || p.isOp() || p.hasPermission("at.*")) {
				Player target = getServer().getPlayer(args[1]);
				
				if(target == null) {
					p.sendMessage(error + "Please Specifie A Player That is Online!");
				} else {
					p.teleport(target);
					p.sendMessage(at + "Teleported!");
					//TODO: Add Vanish
					target.hidePlayer(p);
				}
				}else {
					p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");

				}
			}else if(args[0].equalsIgnoreCase("reveal")) {
				if(p.hasPermission("at.reveal") || p.isOp() || p.hasPermission("at.*")) {
				Player target = getServer().getPlayer(args[1]);
				
					if(target == null) {
						p.sendMessage(error + "Either The Player Is Offline Or You Didnt Spell The Name Right.");
					} else {
						target.showPlayer(p);
						p.sendMessage(at + "Vuala! Revealed.");

					}
			} else {
				p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");

			}
			}else if(args[0].equalsIgnoreCase("ban")) {
				if(p.hasPermission("at.ban") || p.isOp() || p.hasPermission("at.*")) {

				Player target = getServer().getPlayer(args[1]);
				
				if(target == null) {
					p.sendMessage(error + "Either The Player Is Offline Or You Didnt Spell The Name Right.");
				} else { 
				    String banReason = "";

			        for(int i = 2; i < args.length; i++){
			            String arg = args[i] + " ";
			            banReason = banReason + arg;
			        }
			        
			        target.kickPlayer(ChatColor.RED + "You Were Banned By: " + ChatColor.DARK_RED + p.getName() + ChatColor.RED + "!" + NL + "" + "Experation Date: " + ChatColor.DARK_RED + "NEVER" + NL + "" + ChatColor.RED + "Reason: " + ChatColor.DARK_RED + banReason);
			        getConfig().addDefault(target.getName() + ".Reason", banReason);
			        getConfig().addDefault(target.getName() + ".Banner", "" + p.getName());
			        getConfig().addDefault(target.getName() + ".Banner", "" + p.getName());
			        getConfig().addDefault(target.getName() + ".UUID", "" + p.getUniqueId());
			        getConfig().addDefault(target.getName() + ".isBanned", true);
			        saveConfig();
			        
			        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), banReason, null, p.getName());
			        return true;
					}
				
				}else {
					p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");

				}
			} else if(args[0].equalsIgnoreCase("kick")) {
				if(p.hasPermission("at.kick") || p.isOp() || p.hasPermission("at.*")) {
					
					Player target = getServer().getPlayer(args[1]);
					
					if(target == null) {
						p.sendMessage(error + "Either The Player Is Offline Or You Didnt Spell The Name Right.");
					} else {
					    String kickReason = "";
					    
					    for(int i = 2; i < args.length; i++){
				            String arg = args[i] + " ";
				            kickReason = kickReason + arg;
				        }
					    
				        target.kickPlayer(ChatColor.RED + "You Were Kicked By: " + ChatColor.DARK_RED + p.getName() + "!" + NL + "" + ChatColor.RED + "Reason: " + ChatColor.DARK_RED + kickReason);
				        return true;
					}
					
				}else {
					p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");

				}
			} else if(args[0].equalsIgnoreCase("inv")) {
				if(p.hasPermission("at.inv") || p.isOp() || p.hasPermission("at.*")) {

				Player target = getServer().getPlayer(args[1]);

				Inventory targetInv = target.getInventory();
		
				p.openInventory(targetInv);

				} else {
					p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");
				}
			} else if(args[0].equalsIgnoreCase("warn")) {
				if(p.hasPermission("at.warn") || p.isOp() || p.hasPermission("at.*")) {
				
				Player target = getServer().getPlayer(args[1]);
				
				if(target == null) {
					p.sendMessage(error + "Either The Player Is Offline Or You Didnt Spell The Name Right.");
				} else {
					 String warnReason = "";
					    
					    for(int i = 2; i < args.length; i++){
				            String arg = args[i] + " ";
				            warnReason = warnReason + arg;
					    }
					   
				        target.sendMessage(ChatColor.RED + "You Were Warned By: " + p.getName() + ", Because: " + warnReason);
					    
				}
				
				} else {
					p.sendMessage(error + "You Don't Have The Permissions, If You Think This Is A Error, Contact A Owner.");
				}
			} 
		}
		return false;
}
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
			}
}
