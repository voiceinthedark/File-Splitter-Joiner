/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package file.splitter.joiner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author Krasius
 */
public class FilesLoader {
    private String[] fileNames;
    private long fSize;
    private long chunkSize;
    private File[] files;
    private FileInputStream[] inStreams;
    private FileChannel channel;
    private FileOutputStream fout;
    
    public FilesLoader(){        
    }
    
    public FilesLoader(File[] files) throws FileNotFoundException, IOException{
        inStreams = new FileInputStream[files.length];
        this.files=files;
        
        for(int i=0;i<files.length;i++)
            inStreams[i] = new FileInputStream(files[i]);
        channel = inStreams[0].getChannel();
        fSize = channel.size()*files.length;
        chunkSize = channel.size();
        fileNames = getFileNames();
    }
    
    public String[] getFileNames(){
        fileNames = new String[files.length];
        for(int i=0;i<files.length;i++)
            fileNames[i] = files[i].toString();
        return fileNames;            
    }   
    
    public long getFileSize(){
        return fSize;
    }
    
    public long getChunkSize(){
        return chunkSize;
    }
    
    public void closeFiles() throws IOException
    {
        for(FileInputStream is : inStreams)
            is.close();
        fout.close();
    }
    
    public void splitFile(int chunks) throws IOException{
        int portion = (int)fSize/chunks;
        byte[] data = new byte[portion];
        
        for(int i=0;i<chunks;i++){
            inStreams[0].read(data);
            fout = new FileOutputStream(fileNames[0] + ".ks."+i);
            fout.write(data);
            data = new byte[portion];
        }
        closeFiles();
    }
    
    public void joinFiles(String filename) throws FileNotFoundException, IOException{
        fout = new FileOutputStream(filename);
        byte[] portion = new byte[(int)getChunkSize()];
        
        for(int i=0;i<files.length;i++){
            inStreams[i].read(portion);
            fout.write(portion);
        }
        closeFiles();
    }    
}
