package com.huntdreams.weibo.cache.file;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

}
