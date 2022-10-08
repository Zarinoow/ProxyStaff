package fr.zarinoow.proxystaff;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
	
	private static Main instance;
    public static double CONFIG_VERSION = 1.0;
    
    @Override
    public void onEnable() {
        instance = this;
        
        System.out.println(ChatColor.DARK_GREEN + "========");
        System.out.println(ChatColor.DARK_GREEN + "ProxySTAFF");
        System.out.println(ChatColor.DARK_GREEN + "By Zarinoow");
        System.out.println(ChatColor.DARK_GREEN + "v " + getDescription().getVersion());
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

        InputStream input = getResourceAsStream("config.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(input, StandardCharsets.UTF_8));
        
        if(file.exists()) {
            Configuration oldConfig = getConfig(fileName);
            if(oldConfig.contains("config-version") && oldConfig.getDouble("config-version") == CONFIG_VERSION) return;
            getLogger().log(Level.INFO, "Your configuration seems outdated. Update in progress...");
            getLogger().log(Level.WARNING, "When upgrading the plugin, all comments will be removed. You can find the default configuration with the comments in the same folder as the actual config.");
            String[] configpath = {
                    "messages.global.errornull",
                    "messages.global.message",
                    "messages.global.servername.shrinkname",
                    "messages.global.servername.shrinksize",
                    "messages.server.errornull",
                    "messages.server.message",
                    "messages.server.servername.shrinkname",
                    "messages.server.servername.shrinksize"
            };

            for(String path : configpath) {
                if(oldConfig.contains(path)) config.set(path, oldConfig.get(path));
            }

            updateConfig(oldConfig, config, oldConfig.getString("config-version"));
            getLogger().log(Level.INFO, "Your configuration was successfully updated");
        }

        try {
            File configFile;
            if(file.exists()) {
                configFile = new File(file.getParentFile(), "defaultconfig.yml");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            } else configFile = file;
            byte[] buf = new byte[1024];
            int bytesRead;
            OutputStream outputStream = new FileOutputStream(configFile);
            InputStream inputStream = getResourceAsStream("config.yml");
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            input.close();
            outputStream.close();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Cannot generate default config !");
            e.printStackTrace();
        }
    }

    private void updateConfig(Configuration oldConfig, Configuration newConfig, String configversion) {
        if(configversion == null) {
            if(oldConfig.contains("config.globalprefix")) newConfig.set("config.prefix.global.prefix", oldConfig.get("config.globalprefix"));
            if(oldConfig.contains("config.serverprefix")) newConfig.set("config.prefix.server.prefix", oldConfig.get("config.serverprefix"));
            newConfig.set("config-version", 1.0);
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
