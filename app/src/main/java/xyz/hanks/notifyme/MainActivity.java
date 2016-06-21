package xyz.hanks.notifyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startIntentService(View source) {
        // 创建需要启动的IntentService的Intent
        Intent intent = new Intent(this, NetCheckIntentService.class);
        startService(intent);
    }
}
