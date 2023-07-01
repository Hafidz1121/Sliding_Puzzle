package com.example.slidingpuzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private int emptyX = 3;
    private int emptyY =3;
    private RelativeLayout group;
    private Button[][] buttons;
    private String[] letters;
    private TextView textStep;
    private int stepCount = 0;
    private TextView textTime;
    private Timer timer;
    private int timeCount = 0;
    private Button buttonAcak;
    private Button buttonStop;
    private Boolean isTimeRunning;
    private MyBase myBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadViews();
        loadLatters();
        generateLatters();
        loadDataToViews();
    }

    private void loadDataToViews() {
        emptyX = 3;
        emptyY = 3;

        for (int i = 0; i < group.getChildCount() - 1; i++) {
            buttons[i/4][i%4].setText(String.valueOf(letters[i]));
            buttons[i/4][i%4].setBackgroundResource(android.R.drawable.btn_default);
        }

        buttons[emptyY][emptyY].setText("");
        buttons[emptyX][emptyY].setBackgroundColor(ContextCompat.getColor(this, R.color.freeButton));
    }

    private void generateLatters() {
        int n = 15;
        Random random = new Random();

        while (n > 1) {
            int randomIndex = random.nextInt(n--);
            String temp = letters[randomIndex];
            letters[randomIndex] = letters[n];
            letters[n] = temp;
        }

        if (!isSolvable()) {
            generateLatters();
        }
    }

    private boolean isSolvable() {
        int countInversions = 0;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < i; j++) {
                if (letters[j].compareTo(letters[i]) > 0)
                    countInversions++;
            }
        }

        return countInversions % 2 == 0;
    }

    private void loadLatters() {
        letters = new String[16];
        letters[0] = "A";
        letters[1] = "B";
        letters[2] = "C";
        letters[3] = "D";
        letters[4] = "E";
        letters[5] = "F";
        letters[6] = "G";
        letters[7] = "H";
        letters[8] = "I";
        letters[9] = "J";
        letters[10] = "K";
        letters[11] = "L";
        letters[12] = "M";
        letters[13] = "N";
        letters[14] = "O";
    }

    private void loadViews() {
        group = findViewById(R.id.group);
        textStep = findViewById(R.id.text_step);
        textTime = findViewById(R.id.text_time);
        buttonAcak = findViewById(R.id.btn_acak);
        buttonStop = findViewById(R.id.btn_stop);

        loadTimer();

        buttons = new Button[4][4];

        for (int i = 0; i < group.getChildCount(); i++) {
            buttons[i/4][i%4] = (Button) group.getChildAt(i);
        }

        buttonAcak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateLatters();
                loadDataToViews();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimeRunning) {
                    timer.cancel();
                    buttonStop.setText("Lanjutkan");
                    isTimeRunning = false;

                    for (int i = 0; i < group.getChildCount(); i++) {
                        buttons[i/4][i%4].setClickable(false);
                    }
                } else {
                    loadTimer();
                    buttonStop.setText("Berhenti");

                    for (int i = 0; i < group.getChildCount(); i++) {
                        buttons[i/4][i%4].setClickable(true);
                    }
                }
            }
        });
    }

    private void loadTimer() {
        isTimeRunning = true;
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeCount++;
                setTime(timeCount);
            }
        }, 1000, 1000);
    }

    private void setTime(int timeCount) {
        int second = timeCount % 60;
        int hour = timeCount / 60;
        int minute = (timeCount - hour * 3600) / 60;

        textTime.setText(String.format("Waktu : %02d:%02d:%02d", hour, minute, second));
    }

    public void buttonClick(View view) {
        Button button = (Button) view;

        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';

        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            buttons[emptyX][emptyY].setText(button.getText().toString());
            buttons[emptyX][emptyY].setBackgroundResource(android.R.drawable.btn_default);
            button.setText("");
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.freeButton));

            emptyX = x;
            emptyY = y;

            stepCount++;
            textStep.setText("Waktu : " + stepCount);

            checkWin();
        }
    }

    private void checkWin() {
        boolean isWin = true;

        for (int i = 0; i < group.getChildCount() - 1; i++) {
            String expectedLetter = String.valueOf((char) ('A' + i));;
            String actualLetter = buttons[i/4][i%4].getText().toString();

            if (!expectedLetter.equals(actualLetter)) {
                isWin = false;
                break;
            }
        }

        if (isWin) {
            Toast.makeText(this, "MENANG !!!\nLangkah : " + stepCount, Toast.LENGTH_LONG).show();

            for (int i = 0; i < group.getChildCount(); i++) {
                buttons[i/4][i%4].setClickable(false);
            }

            timer.cancel();

            buttonAcak.setClickable(false);

            buttonStop.setClickable(false);

            saveData();
        }
    }

    private void saveData() {
        myBase = new MyBase(GameActivity.this);
        myBase.saveLastStep(stepCount);
        myBase.saveLastTimer(timeCount);

        if (myBase.getBestStep() != 0) {
            if (myBase.getBestStep() > stepCount) {
                myBase.saveBestStep(stepCount);
            }
        } else {
            myBase.saveBestStep(stepCount);
        }

        if (myBase.getBestTimer() != 0) {
            if (myBase.getBestTimer() > timeCount) {
                myBase.saveBestTimer(timeCount);
            }
        } else {
            myBase.saveBestTimer(timeCount);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(MainActivity.REQUEST_CODE);
    }
}