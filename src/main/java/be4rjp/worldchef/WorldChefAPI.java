package be4rjp.worldchef;

import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class WorldChefAPI {
    private List<String> zipList = new ArrayList<>();
    private List<String> worldNameList = new ArrayList<>();
    
    private final JavaPlugin plugin;
    
    private final String zipFolderPath;
    
    private final String worldFolderPath;
    
    private final boolean showErrors;
    
    private final boolean showLogs;
    
    
    /**
     * Create WorldChefAPI
     *
     * @param plugin JavaPlugin that tasks will be allocated on.
     * @param zipFolderPath Path of the folder where the zip files is located.
     * @param worldFolderPath Path of the folder where the world folders is located.
     * @param showLogs Whether to output the logs to console.
     * @param showErrors Whether to output the error logs to console.
     */
    public WorldChefAPI(JavaPlugin plugin, String zipFolderPath, String worldFolderPath, boolean showLogs, boolean showErrors){
        this.plugin = plugin;
        this.zipFolderPath = zipFolderPath;
        this.worldFolderPath = worldFolderPath;
        this.showLogs = showLogs;
        this.showErrors = showErrors;
    }
    
    /**
     * Output logs
     */
    private void putLogs(String log, LogType logType){
        if(!this.showLogs) return;
        switch(logType){
            case INFO:
                plugin.getLogger().info(log);
                break;
            case WARNING:
                plugin.getLogger().warning(log);
                break;
        }
    }
    
    /**
     * Load zip files from specified folder
     */
    public boolean loadZips(){
        putLogs("Finding zip files...", LogType.INFO);
        
        try {
            File dir = new File(zipFolderPath);
            dir.mkdir();
            File[] list = dir.listFiles();
            for (int i = 0; i < list.length; i++) {
                File file = list[i];
                if (file.getName().contains(".zip")) {
                    zipList.add(file.toString());
                    String worldName = file.getName().replace(".zip", "");
                    worldNameList.add(worldName);
    
                    putLogs("ZipFile : " + worldName + ".zip", LogType.INFO);
                }
            }
            return true;
        }catch (Exception e){
            if(this.showErrors) {
                putLogs("Some trouble occurred when tried to load zips!", LogType.WARNING);
                e.printStackTrace();
            }
            return false;
        }
    }
    
    /**
     * Unzip world files.
     *
     * @param bufferSize The buffer size to unzip.
     *                   If you specify the void, it run at bufferSize=1024.
     * @return boolean if the unzip was successful.
     */
    public boolean unZip(int bufferSize){
        return doUnZip(bufferSize);
    }
    
    public boolean unZip(){
        return doUnZip(1024);
    }
    
    private boolean doUnZip(int bufferSize){
        putLogs("Extracting zip files...", LogType.INFO);
        
        int index = 0;
        boolean success = true;
        for(String zipFilePath : zipList){
    
            File baseFile = new File(zipFilePath);
            File baseDir = new File(baseFile.getParent(), baseFile.getName().substring(0, baseFile.getName().lastIndexOf(".")));
            baseDir.mkdir();
    
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(zipFilePath);
                Enumeration<? extends ZipEntry> enumZip = zipFile.entries();
                
                while ( enumZip.hasMoreElements() ) {
                    ZipEntry zipEntry = (java.util.zip.ZipEntry)enumZip.nextElement();
                    File unzipFile = new File(this.worldFolderPath);
                    unzipFile.mkdir();
                    File outFile = new File(unzipFile.getAbsolutePath() + "/" + baseDir.getName(), zipEntry.getName());
            
                    if ( zipEntry.isDirectory() )
                        outFile.mkdir();
                    else {
                        BufferedInputStream in = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                        
                        if ( !outFile.getParentFile().exists() )
                            outFile.getParentFile().mkdirs();
                        
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
                        
                        byte[] buffer = new byte[bufferSize];
                        
                        int readSize = 0;
                        while ( (readSize = in.read(buffer)) != -1 ) {
                            out.write(buffer, 0, readSize);
                        }
                        try { out.close(); } catch (Exception e) {}
                        try { in.close(); } catch (Exception e) {}
                    }
                }
                index++;
            } catch(Exception e) {
                index++;
                success = false;
                if(this.showErrors) {
                    putLogs("Some trouble occurred when tried to unzip!", LogType.WARNING);
                    e.printStackTrace();
                }
            } finally {
                if ( zipFile != null )
                    try { zipFile.close();  } catch (Exception e) {}
            }
        }
        
        return success;
    }
    
    /**
     * Load worlds
     *
     * @return boolean if loading world was successful.
     */
    public boolean loadWorlds(){
        putLogs("Loading worlds...", LogType.INFO);
        
        boolean success = true;
        for(String worldName : this.worldNameList) {
            try {
                plugin.getServer().createWorld(new WorldCreator(worldName));
            }catch (Exception e){
                success = false;
                if(this.showErrors) {
                    putLogs("Some trouble occurred when tried to load worlds!", LogType.WARNING);
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
}
