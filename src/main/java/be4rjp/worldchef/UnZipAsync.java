package be4rjp.worldchef;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZipAsync extends Thread{

    private final WorldChefAPI worldChefAPI;
    private final String zipFilePath;
    private final int bufferSize;

    private boolean success = false;

    public UnZipAsync(WorldChefAPI worldChefAPI, String zipFilePath, int bufferSize){
        this.worldChefAPI = worldChefAPI;
        this.zipFilePath = zipFilePath;
        this.bufferSize = bufferSize;
    }

    public boolean isSuccess(){return this.success;}

    public void run(){
        File baseFile = new File(this.zipFilePath);
        if(this.worldChefAPI.isShowLogs())
            this.worldChefAPI.putLogs("Extracting : " + baseFile.getName(), LogType.INFO);
        File baseDir = new File(baseFile.getParent(), baseFile.getName().substring(0, baseFile.getName().lastIndexOf(".")));
        baseDir.mkdir();

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(this.zipFilePath);
            Enumeration<? extends ZipEntry> enumZip = zipFile.entries();

            while ( enumZip.hasMoreElements() ) {
                ZipEntry zipEntry = (java.util.zip.ZipEntry)enumZip.nextElement();
                File unzipFile = new File(this.worldChefAPI.getWorldFolderPath());
                unzipFile.mkdir();
                File outFile = new File(unzipFile.getAbsolutePath() + "/" + baseDir.getName(), zipEntry.getName());

                if ( zipEntry.isDirectory() )
                    outFile.mkdir();
                else {
                    BufferedInputStream in = new BufferedInputStream(zipFile.getInputStream(zipEntry));

                    if ( !outFile.getParentFile().exists() )
                        outFile.getParentFile().mkdirs();

                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));

                    byte[] buffer = new byte[this.bufferSize];

                    int readSize = 0;
                    while ( (readSize = in.read(buffer)) != -1 ) {
                        out.write(buffer, 0, readSize);
                    }
                    try { out.close(); } catch (Exception e) {}
                    try { in.close(); } catch (Exception e) {}
                }
            }
        } catch(Exception e) {
            if(this.worldChefAPI.isShowErrors()) {
                this.worldChefAPI.putLogs("Some trouble occurred when tried to unzip!", LogType.WARNING);
                e.printStackTrace();
            }
            this.success = false;
        } finally {
            this.success = true;
            if ( zipFile != null )
                try { zipFile.close();  } catch (Exception e) {}
        }
    }
}
