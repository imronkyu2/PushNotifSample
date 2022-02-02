package com.nexsoft.pushnotiftestingservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        Intent dataFromNotification = getIntent();
        if (dataFromNotification != null) {
            String value = dataFromNotification.getStringExtra("value");
            ((TextView) findViewById(R.id.tv_notif)).setText("Congratulations, Notification clicked by you.");
            ((TextView) findViewById(R.id.tv_data)).setText("Here is the data: " + value);

        } else {
            ((TextView) findViewById(R.id.tv_notif)).setText("Congratulations, Notification clicked by you. Unfortunately there is no data");
        }

    }
}