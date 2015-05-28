package com.huntdreams.weibo.cache.file;

import android.content.Context;

import java.io.File;
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


}
