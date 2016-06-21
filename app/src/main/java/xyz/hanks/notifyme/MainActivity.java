package xyz.hanks.notifyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SMSSDK.initSDK(this, "1426505bd0b02", "4abf2e726b1db62b0389712a96b32398");
        startIntentService();
    }


    public void startIntentService() {
        // 创建需要启动的IntentService的Intent
        Intent intent = new Intent(this, NetCheckService.class);
        startService(intent);
    }
}
