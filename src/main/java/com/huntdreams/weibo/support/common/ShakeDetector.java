package com.huntdreams.weibo.support.common;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.FloatMath;

import java.util.ArrayList;
import static com.huntdreams.weibo.BuildConfig.DEBUG;
/**
 * 重力感应类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class ShakeDetector implements SensorEventListener{

    private static final String TAG = ShakeDetector.class.getSimpleName();
    private static final int INTERVAL = 100;
    private static final int THRESHOLD = 100;   // 检测极限
    private static ShakeDetector sInstance = null; // 单例模式
    private long mLastTime;
    private float mLastX, mLastY, mLastZ;
    private float mLastV = -1.0f;
    private Vibrator mVibrator;
    private ArrayList<ShakeListener> mListeners = new ArrayList<ShakeListener>();

    public static interface ShakeListener{
        public void onShake();
    }

    public synchronized static ShakeDetector getInstance(Context context){
        if(sInstance == null){
            sInstance = new ShakeDetector(context);
        }
        return sInstance;
    }

    private ShakeDetector(Context context){
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor != null){
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void addListener(ShakeListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(ShakeListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long millis = System.currentTimeMillis();
        long diff = millis - mLastTime;
        if (diff >= INTERVAL) {
            mLastTime = millis;
            float x = event.values[0],
                    y = event.values[1],
                    z = event.values[2];

            float dX = x - mLastX,
                    dY = y - mLastY,
                    dZ = z - mLastZ;

            float eV = FloatMath.sqrt(dX * dX + dY * dY + dZ * dZ) / diff * 10000;

            if (mLastV > -1) {
                float dV = eV - mLastV;
                float a = Math.abs(dV / diff); // We do not need to detect direction

                if (a >= THRESHOLD) {
                    // Call all listeners
                    notifyListeners();
                }

                if (DEBUG) {
					/*Log.d(TAG, "a = " + a);*/
                }
            }

            if (DEBUG) {
				/*Log.d(TAG, "x = " + x + "; y = " + y + "; z = " + z);
				Log.d(TAG, "dX = " + dX + "; dY = " + dY + "; dZ = " + dZ);
				Log.d(TAG, "mLastX = " + mLastX + "; mLastY = " + mLastY + "; mLastZ = " + mLastZ);
				Log.d(TAG, "eV = " + eV + "; mLastV = " + mLastV);*/
            }

            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mLastV = eV;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void notifyListeners() {
        for (ShakeListener listener : mListeners) {
            if (listener != null) {
                // Vibrate
                mVibrator.vibrate(new long[]{0, 100}, -1);

                // Call the listener
                listener.onShake();
            }
        }
    }
}
