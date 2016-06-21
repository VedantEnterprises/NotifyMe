package xyz.hanks.notifyme;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by hanks on 16/6/21.
 */
public class NetCheckIntentService extends IntentService {


    public static final String LAST_STATUS = "lastStatus";
    public static final String LAST_MODIFY = "lastModify";

    public NetCheckIntentService() {
        super("NetCheckIntentService");
    }

    /**
     * 检测当的网络状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // IntentService会使用单独的线程来执行该方法的代码
        // 该方法内执行耗时任务，比如下载文件，此处只是让线程等待20秒
        System.out.println("onStart");


        boolean lastStatus = getStatus();

        // 检测网络状况
        boolean isNetworkAvailable = isNetworkAvailable(getApplicationContext());
        // 如果原来网络不可用 && 现在可用了
        if (!lastStatus && isNetworkAvailable) {
            // 发送短信

        }

        // 更新网络状态
        updateStatus(isNetworkAvailable);

        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("----耗时任务执行完成---");
    }

    private boolean getStatus() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sp.getBoolean(LAST_STATUS, false);
    }


    public void updateStatus(boolean isAvailable) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LAST_STATUS, isAvailable);
        editor.apply();
    }
}
