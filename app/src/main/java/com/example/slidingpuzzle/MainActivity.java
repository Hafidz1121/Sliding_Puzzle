package com.example.slidingpuzzle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private Button buttonStartGame;
    private TextView lastStep;
    private TextView lastTimer;
    private TextView bestStep;
    private TextView bestTimer;
    private MyBase myBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartGame = findViewById(R.id.button_start);
        lastStep = findViewById(R.id.text_last_step);
        lastTimer = findViewById(R.id.text_last_timer);
        bestStep = findViewById(R.id.text_best_step);
        bestTimer = findViewById(R.id.text_best_timer);

        myBase = new MyBase(this);

        loadData();

        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), REQUEST_CODE);
            }
        });
    }

    private void loadData() {
        lastStep.setText(String.valueOf(myBase.getLastStep()));
        bestStep.setText(String.valueOf(myBase.getBestStep()));

        int lastTime = myBase.getLastTimer();
        int lastSecond = lastTime % 60;
        int lastHour = lastTime / 3600;
        int lastMinute = (lastTime - lastHour * 3600) / 60;

        lastTimer.setText(String.format("%02d:%02d:%02d", lastHour, lastMinute, lastSecond));

        int bestTime = myBase.getBestTimer();
        int bestSecond = bestTime % 60;
        int bestHour = bestTime / 3600;
        int bestMinute = (bestTime - bestHour * 3600) / 60;

        bestTimer.setText(String.format("%02d:%02d:%02d", bestHour, bestMinute, bestSecond));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            lastStep.setText(String.valueOf(myBase.getLastStep()));
            bestStep.setText(String.valueOf(myBase.getBestStep()));

            int lastTime = myBase.getLastTimer();
            int lastSecond = lastTime % 60;
            int lastHour = lastTime / 3600;
            int lastMinute = (lastTime - lastHour * 3600) / 60;

            lastTimer.setText(String.format("%02d:%02d:%02d", lastHour, lastMinute, lastSecond));

            int bestTime = myBase.getBestTimer();
            int bestSecond = bestTime % 60;
            int bestHour = bestTime / 3600;
            int bestMinute = (bestTime - bestHour * 3600) / 60;

            bestTimer.setText(String.format("%02d:%02d:%02d", bestHour, bestMinute, bestSecond));
        }
    }
}