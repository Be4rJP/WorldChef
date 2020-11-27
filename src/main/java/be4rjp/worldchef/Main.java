package be4rjp.worldchef;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    
    private Config config = null;
    private WorldChefAPI worldChefAPI = null;
    
    private static Main plugin;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        
        //---------------------------Load config-----------------------------
        getLogger().info("Loading config files...");
        config = new Config(this, "config.yml");
        config.saveDefaultConfig();
        config.getConfig();
        //-------------------------------------------------------------------
        
        
        //---------------------------API access------------------------------
        if(config.getConfig().getBoolean("stand-alone")){
            String zipFolderPath = config.getConfig().getString("folder-path.zip-files");
            String worldFolderPath = config.getConfig().getString("folder-path.world-folder");
            boolean showLogs = config.getConfig().getBoolean("show-logs");
            boolean showErrors = config.getConfig().getBoolean("show-errors");
            worldChefAPI = new WorldChefAPI(this, zipFolderPath, worldFolderPath, showLogs, showErrors);
            worldChefAPI.loadZips();
            worldChefAPI.unZip(config.getConfig().getInt("buffer-size"));
            if(config.getConfig().getBoolean("load-worlds"))
                worldChefAPI.loadWorlds();
        }
        //-------------------------------------------------------------------
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    public Config getWorldChefConfig(){
        return this.config;
    }
    
    public WorldChefAPI getWorldChefAPI(){
        return worldChefAPI;
    }
    
    
    public static Main getPlugin(){
        return plugin;
    }
}
