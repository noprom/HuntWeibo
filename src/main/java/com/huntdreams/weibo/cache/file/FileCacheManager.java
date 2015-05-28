package com.huntdreams.weibo.cache.file;

import android.content.Context;

import com.huntdreams.weibo.cache.Constants;
import com.huntdreams.weibo.support.common.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件缓存Manager
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class FileCacheManager {

    public static interface ProgressCallback{
        void onProgressChanged(int read,int total);
        boolean shouldContinue();
    }

    private static FileCacheManager mInstance;
    private File mCacheDir;

    private FileCacheManager(Context context){
        mCacheDir = context.getExternalCacheDir();
    }

    public static synchronized FileCacheManager instance(Context context){
        if(mInstance == null){
            mInstance = new FileCacheManager(context);
        }
        return mInstance;
    }

    /**
     * 创建缓存文件
     * @param type
     * @param name
     * @param datas
     * @throws IOException
     */
    public void createCache(String type,String name,byte[] datas) throws IOException{
        String path = mCacheDir.getPath() + "/" + type + "/" + name;
        File f = new File(path);
        if(f.exists()){
            f.delete();
        }
        f.getParentFile().mkdirs();
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(datas);
        fos.close();
    }

    /**
     * 复制缓存文件
     * @param type
     * @param name
     * @param dist
     * @return 目标文件绝对路径
     * @throws IOException
     */
    public String copyCacheTo(String type,String name, String dist) throws IOException{
        String path = mCacheDir.getPath() + "/" + type + "/" + name;
        try{
            new File(dist).mkdirs();
        }catch (Exception e){
            Runtime.getRuntime().exec("mkdir -p " + dist);
        }

        File origFile = new File(path);
        File distFile = new File(dist + "/" + name);
        if(distFile.createNewFile()){
            FileInputStream fis = new FileInputStream(origFile);
            FileOutputStream fos = new FileOutputStream(distFile);

            byte[] buf = new byte[1024];
            int len = 0;
            while((len = fis.read(buf))!= -1){
                fos.write(buf,0,len);
            }

            fis.close();
            fos.close();
        }
        return distFile.getAbsolutePath();
    }

    public InputStream createCacheFromNetWork(String type, String name, String url)throws IOException{
        return createCacheFromNetwork(type, name, url, null);
    }

    public InputStream createCacheFromNetwork(String type, String name, String url, ProgressCallback callback)throws IOException{
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        byte[] buf = readInputStream(conn.getInputStream(),conn.getContentLength(),callback);
        createCache(type,name,buf);
        conn.disconnect();
        return getCache(type,name);
    }

    public InputStream createLargeCacheFromNetwork(String type, String name, String url, ProgressCallback callback) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        createCacheFromStream(type, name, conn.getInputStream(), conn.getContentLength(), callback);
        conn.disconnect();
        return getCache(type, name);
    }

    public void createCacheFromStream(String type, String name, InputStream ipt, int total, ProgressCallback callback) throws IOException {
        String path = getCachePath(type, name);
        File  f = new File(path);
        if(f.exists()){
            f.delete();
        }
        f.getParentFile().mkdirs();
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] buf = new byte[512];
        int len = 0, read = 0;
        while((len = ipt.read(buf)) != -1){
            fos.write(buf, 0, len);
            read += len;
            callback.onProgressChanged(read, total);

            if(!callback.shouldContinue()){
                fos.close();
                ipt.close();
                f.delete();
                return;
            }
        }
        fos.close();
        ipt.close();
    }

    public InputStream getCache(String type, String name) throws IOException{
        String path = mCacheDir.getPath() + "/" + type + "/" + name;
        File f = new File(path);
        if(!f.exists()){
            return null;
        }else{
            FileInputStream fis = new FileInputStream(path);
            return fis;
        }
    }

    public String getCachePath(String path, String name){
        return mCacheDir.getPath() + "/" + path + "/" +name;
    }

    private byte[] readInputStream(InputStream in,int total, ProgressCallback callback) throws IOException{
        ByteArrayOutputStream opt = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0,read = 0;
        while((len = in.read(buf)) != -1){
            opt.write(buf,0,len);
            read += len;
            if(callback != null){
                callback.onProgressChanged(read,total);
            }
        }
        in.close();
        byte[] ret ;
        try{
            ret = opt.toByteArray();
        }catch (OutOfMemoryError e){
            ret = null;
        }
        opt.close();
        return ret;
    }

    /**
     * 清除缓存
     */
    public void clearUnavailable(){
        try{
            clearUnavailable(mCacheDir);
        }catch (NullPointerException e){}
    }

    private void clearUnavailable(File dir){
        File[] files = dir.listFiles();
        for(File f:files){
            if(f.isDirectory()){
                clearUnavailable(f);
            }else{
                long time = f.lastModified();
                if(!Utility.isCacheAvailable(time, Constants.FILE_CACHE_DAYS)){
                    f.delete();
                }
            }
        }
    }
}
