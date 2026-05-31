package com.darrenprince.timebatterywidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView batteryText = findViewById(R.id.main_battery_value);
        batteryText.setText(getString(R.string.battery_format, getBatteryPercentage()));

        Button pinWidgetButton = findViewById(R.id.pin_widget_button);
        pinWidgetButton.setOnClickListener(view -> requestPinWidget());
    }

    private void requestPinWidget() {
        AppWidgetManager appWidgetManager = getSystemService(AppWidgetManager.class);
        if (appWidgetManager == null || !appWidgetManager.isRequestPinAppWidgetSupported()) {
            return;
        }

        ComponentName provider = new ComponentName(this, TimeBatteryWidgetProvider.class);
        appWidgetManager.requestPinAppWidget(provider, null, null);
    }

    private int getBatteryPercentage() {
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryStatus == null) {
            return 0;
        }

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (level < 0 || scale <= 0) {
            return 0;
        }
        return Math.round(level * 100f / scale);
    }
}
