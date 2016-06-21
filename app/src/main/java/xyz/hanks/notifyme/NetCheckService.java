package xyz.hanks.notifyme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 后台检测手机网络状态
 * Created by hanks on 16/6/21.
 */
public class NetCheckService extends Service {


    public static final String LAST_STATUS = "lastStatus";
    public static final String LAST_MODIFY = "lastModify";
    public static final String PHONE = "13212341234";

    /**
     * 检测当的网络状态
     *
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

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    System.out.print("res:" + result + "event:" + event);
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

        handleIntent();

        return super.onStartCommand(intent, flags, startId);
    }

    protected void handleIntent() {
        new Thread() {
            @Override public void run() {
                while (true) {
                    // 该方法内执行耗时任务，比如下载文件，此处只是让线程等待20秒
                    System.out.println("onStart");
                    hanldeStatus();
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("----耗时任务执行完成---");
                }
            }
        }.start();
    }

    private void hanldeStatus() {
        boolean lastStatus = getStatus();
        // 检测网络状况
        boolean isNetworkAvailable = isNetworkAvailable(getApplicationContext());
        // 如果原来网络不可用 && 现在可用了
        if (!lastStatus && isNetworkAvailable) {
            // 发送短信
            sendSMS();
        }

        System.out.println("isNetworkAvailable = " + isNetworkAvailable);

        // 更新网络状态
        updateStatus(isNetworkAvailable);
    }

    private void sendSMS() {
        SMSSDK.getSupportedCountries();
        SMSSDK.getVerificationCode("+86", PHONE);
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
