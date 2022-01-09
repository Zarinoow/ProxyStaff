package fr.zarinoow.proxystaff;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
	
	private static Main instance;
    
    @Override
    public void onEnable() {
        instance = this;
        
        System.out.println(ChatColor.DARK_GREEN + "========");
        System.out.println(ChatColor.DARK_GREEN + "ProxySTAFF");
        System.out.println(ChatColor.DARK_GREEN + "By Zarinoow");
        System.out.println(ChatColor.DARK_GREEN + "v 1.0.2");
        System.out.println(ChatColor.DARK_GREEN + "========");
        
        createFile("config");
        @SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, 12340);
        
        getProxy().getPluginManager().registerListener(this, new Listeners(this));	
    }
    
    @Override
    public void onDisable() {
        System.out.println(ChatColor.DARK_RED + "========");
        System.out.println(ChatColor.DARK_RED + "ProxySTAFF");
        System.out.println(ChatColor.DARK_RED + "Thanks for use it !");
        System.out.println(ChatColor.DARK_RED + "========");
    }
    
    
    public static Main getInstance() {
        return instance;
    }
    
    private void createFile(String fileName) {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        
        File file = new File(getDataFolder(), fileName + ".yml");
        
        if(!file.exists()) {
            try {
                file.createNewFile();
                
                if(fileName.equals("config")) {
                    Configuration config = getConfig(fileName);
                    
                    // Config
                    config.set("config.globalprefix", "@>");
                    config.set("config.serverprefix", "none");
                    
                    // Messages -> Global
                    config.set("messages.global.errornull", "&c[Error] Please specify a messages after the prefix !");
                    config.set("messages.global.message", "&3[StaffChat] Global &7[%server%] &c%player%: &f");
                    config.set("messages.global.servername.shrinkname", true);
                    config.set("messages.global.servername.shrinksize", 3);
                    
                    // Messages -> Server
                    config.set("messages.server.errornull", "&c[Error] Please specify a messages after the prefix !");
                    config.set("messages.server.message", "&3[StaffChat] Server &c%player%: &f");
                    config.set("messages.server.servername.shrinkname", false);
                    config.set("messages.server.servername.shrinksize", 3);
                    
                    saveConfig(config, fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
    }
    
    public Configuration getConfig(String fileName) {
        try {
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void saveConfig(Configuration config, String fileName) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
