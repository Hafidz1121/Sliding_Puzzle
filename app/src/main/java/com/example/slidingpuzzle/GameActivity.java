package com.example.slidingpuzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int emptyX = 3;
    private int emptyY =3;
    private RelativeLayout group;
    private Button[][] buttons;
    private String[] letters;

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
        buttons = new Button[4][4];

        for (int i = 0; i < group.getChildCount(); i++) {
            buttons[i/4][i%4] = (Button) group.getChildAt(i);
        }
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
            Toast.makeText(this, "MENANG !!!", Toast.LENGTH_SHORT).show();

            for (int i = 0; i < group.getChildCount(); i++) {
                buttons[i/4][i%4].setClickable(false);
            }
        }
    }
}