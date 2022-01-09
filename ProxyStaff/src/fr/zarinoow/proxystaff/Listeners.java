package fr.zarinoow.proxystaff;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listeners implements Listener {
	
	private Main main;
	
	
	public Listeners(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onChat(ChatEvent event) {
		
		// Variable pour obtenir celui qui a envoyer le message
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		
		
		// Tableau qui sépare le message en argument
		String[] message = event.getMessage().split(" ");
		String server = player.getServer().getInfo().getName();		
		
		/*
		 * 
		 * Global Chat
		 * 
		 */		
		
		// Teste si le message contient un prefix et qu'il a la permission de parler au staff et qu'il n'est pas désactivée
		if(message[0] != null && message[0].equalsIgnoreCase(main.getConfig("config").getString("config.globalprefix")) && player.hasPermission("proxystaff.global.send") && !main.getConfig("config").getString("config.globalprefix").equalsIgnoreCase("none")) {
			
			// Error Null		
			String errorNull = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.global.errornull")); 
			
			if(message.length <= 1) {
				event.setCancelled(true);
				player.sendMessage(new TextComponent(errorNull));
				return;
			}
			
			// Var shrink
			String serverString = server.toString();
			
			if(main.getConfig("config").getBoolean("messages.global.servername.shrinkname")) {
				
				int shrinkSize = main.getConfig("config").getInt("messages.global.servername.shrinksize");
				
				// Erreur shrinksize superieur au nombre de caractère dans le nom du serveur		
				if(shrinkSize > serverString.length()) {
					shrinkSize = serverString.length();			
				}			
				
				// Nom complet
				serverString = serverString.substring(0, shrinkSize);
			}
			
			// Display message
			
			String messageContent = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.global.message")
					.replace("%server%", serverString)
					.replace("%player%", player.getName()));
							
			if(message[1] != null) {
				
				// Annule le message
				event.setCancelled(true);
				
				String msg = "";
				
				for(int i = 1; i < message.length; i++) {
					msg = msg + message[i] + " ";					
				}
				
				
				// Envoie un message a tout les joueurs avec une permission
				for(ProxiedPlayer allplayer : BungeeCord.getInstance().getPlayers()) {
					
					if(allplayer.hasPermission("proxystaff.global.receive")) {
						allplayer.sendMessage(new TextComponent(messageContent + msg));
					}
					
					
				}
				
				return;
			} 
			
			
		}
		
		/*
		 * 
		 * Per server chat
		 * 
		 */
		
		// Test que le message n'est vide, check le prefix, test la permission par
		if(message[0] != null && message[0].equalsIgnoreCase(main.getConfig("config").getString("config.serverprefix")) && player.hasPermission("proxystaff.server.send." + server) && !main.getConfig("config").getString("config.serverprefix").equalsIgnoreCase("none")) {
			
			// Error Null		
			String errorNull = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.server.errornull"));
			
			if(message.length <= 1) {
				event.setCancelled(true);
				player.sendMessage(new TextComponent(errorNull));
				return;
			}
			
			// Var shrink
			String serverString = server.toString();
			
			if(main.getConfig("config").getBoolean("messages.server.servername.shrinkname")) {
				
				int shrinkSize = main.getConfig("config").getInt("messages.server.servername.shrinksize");
				
				// Erreur shrinksize superieur au nombre de caractère dans le nom du serveur		
				if(shrinkSize > serverString.length()) {
					shrinkSize = serverString.length();			
				}			
				
				// Nom complet
				serverString = serverString.substring(0, shrinkSize);
			}
			
			// Display message
			
			String messageContent = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.server.message")
					.replace("%server%", serverString)
					.replace("%player%", player.getName()));
						
			//
			if(message[1] != null) {
				
				// Annule le message
				event.setCancelled(true);
				
				String msg = "";
				
				for(int i = 1; i < message.length; i++) {
					msg = msg + message[i] + " ";					
				}
				
				
				// Envoie un message a tout les joueurs avec une permission
				for(ProxiedPlayer allplayer : player.getServer().getInfo().getPlayers()) {
					
					if(allplayer.hasPermission("proxystaff.server.receive." + server)) {
						allplayer.sendMessage(new TextComponent(messageContent + msg));
					}
					
					
				}
				
				return;
			} 
			
			
		}
		
	}

}
