package com.huntdreams.weibo.support.http;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import static com.huntdreams.weibo.BuildConfig.DEBUG;

/**
 * 网络访问工具类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/30.
 */
public class HttpUtility {

    private static final String TAG = HttpUtility.class.getSimpleName();
    public static final String POST = "POST";
    public static final String GET = "GET";

    public static String doRequest(String url, WeiboParameters params, String method) throws Exception{
        boolean isGet = false;
        if(method.equals(GET)){
            isGet = true;
        }

        String myUrl = url;
        String send = params.encode();
        if(isGet){
            myUrl += "?" + send;
        }
        if(DEBUG){
            Log.d(TAG, "send = " + send);
            Log.d(TAG, "myUrl = " + myUrl);
        }

        URL u = new URL(myUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(!isGet);
        if(!isGet){
            conn.setDoInput(true);
        }
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");

        if (send != null) {
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");

            conn.connect();

            if (!isGet) {
                DataOutputStream o = new DataOutputStream(conn.getOutputStream());

                o.write(send.getBytes());
                o.flush();
                o.close();
            }

        } else {
            Object[] r = params.toBoundaryMsg();
            String b = (String) r[0];
            Bitmap bmp = (Bitmap) r[1];
            String s = (String) r[2];
            byte[] bs = ("--" + b + "--\r\n").getBytes("UTF-8");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            byte[] img = bytes.toByteArray();

            int l =  s.getBytes("UTF-8").length + img.length + 2 * bs.length;

            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + b);
            conn.setRequestProperty("Content-Length", String.valueOf(l));
            conn.setFixedLengthStreamingMode(l);

            conn.connect();

            DataOutputStream o = new DataOutputStream(conn.getOutputStream());
            o.write(s.getBytes("UTF-8"));

            o.write(img);
            o.write(bs);
            o.write(bs);
            o.flush();
            o.close();

            if (DEBUG) {
                Log.d(TAG, b);
                Log.d(TAG, s);
            }
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        } else {
            InputStream in = conn.getInputStream();
            String en = conn.getContentEncoding();
            if (en != null && en.equals("gzip")) {
                in = new GZIPInputStream(in);
            }
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            String s;
            StringBuilder str = new StringBuilder();
            while ((s = buffer.readLine()) != null) {
                str.append(s);
            }
            return str.toString();
        }
    }
}
