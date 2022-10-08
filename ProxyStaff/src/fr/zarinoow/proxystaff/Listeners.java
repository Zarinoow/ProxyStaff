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
		if(message[0] == null) return; // Stop si aucun message n'existe

		String server = player.getServer().getInfo().getName();

		// Variables de configuration pour les tests
		String prefix = main.getConfig("config").getString("config.prefix.global.prefix");
		boolean stickytext = main.getConfig("config").getBoolean("config.prefix.global.stickytext");

		/*
		 * 
		 * Global Chat
		 * 
		 */		

		// Teste si le message contient un prefix et qu'il a la permission de parler au staff et qu'il n'est pas désactivée
		if(player.hasPermission("proxystaff.global.send") && isPrefixedMessage(message[0], prefix, stickytext)) {
			
			// Error Null
			if(message.length <= 1 && message[0].equalsIgnoreCase(prefix)) {
				event.setCancelled(true);
				String errorNull = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.global.errornull"));
				player.sendMessage(new TextComponent(errorNull));
				return;
			}
			
			if(main.getConfig("config").getBoolean("messages.global.servername.shrinkname")) {
				
				int shrinkSize = main.getConfig("config").getInt("messages.global.servername.shrinksize");
				
				// Redéfinis le nombre de caractère du nom du serveur
				if(shrinkSize < server.length()) {
					server = server.substring(0, shrinkSize);
				}
			}
			
			// Display message
			
			String messageContent = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.global.message")
					.replace("%server%", server)
					.replace("%player%", player.getName()));
							
			if(stickytext || message[1] != null) {
				
				// Annule le message
				event.setCancelled(true);
				
				String msg = "";

				if(stickytext) {
					msg = message[0].replace(prefix, "") + " ";
				}
				
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

		prefix = main.getConfig("config").getString("config.prefix.server.prefix");
		stickytext = main.getConfig("config").getBoolean("config.prefix.server.stickytext");
		
		// Test que le message n'est vide, check le prefix, test la permission par
		if(player.hasPermission("proxystaff.server.send." + server) && isPrefixedMessage(message[0], prefix, stickytext)) {
			
			// Error Null
			if(message.length <= 1 && message[0].equalsIgnoreCase(prefix)) {
				event.setCancelled(true);
				String errorNull = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.server.errornull"));
				player.sendMessage(new TextComponent(errorNull));
				return;
			}

			// Var shrink
			String serverString = server;
			
			if(main.getConfig("config").getBoolean("messages.server.servername.shrinkname")) {
				
				int shrinkSize = main.getConfig("config").getInt("messages.server.servername.shrinksize");

				// Redéfinis le nombre de caractère du nom du serveur
				if(shrinkSize < server.length()) {
					serverString = server.substring(0, shrinkSize);
				}
			}
			
			// Display message
			
			String messageContent = ChatColor.translateAlternateColorCodes('&', main.getConfig("config").getString("messages.server.message")
					.replace("%server%", serverString)
					.replace("%player%", player.getName()));
						
			//
			if(stickytext || message[1] != null) {
				
				// Annule le message
				event.setCancelled(true);
				
				String msg = "";

				if(stickytext) {
					msg = message[0].replace(prefix, "") + " ";
				}
				
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

	// Teste si le message doit être collé ou non et que le message en paramètre est bien le prefix choisis
	private boolean isPrefixedMessage(String message, String prefix, boolean stickytext) {
		if(message.equalsIgnoreCase("none")) return false; // Teste si la fonctionnalité est activé
		if(stickytext && message.startsWith(prefix)) return true; // Teste si le message commence bien par le prefix
		if(!stickytext && message.equalsIgnoreCase(prefix)) return true; // Teste si le message est bel est bien le prefix
		return false;
	}

}
