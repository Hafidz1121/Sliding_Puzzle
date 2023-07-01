package com.example.slidingpuzzle;

import android.content.Context;
import android.content.SharedPreferences;

public class MyBase {
    public static final String SHARED_PREF = "sharedPref";
    public static final String LAST_STEP = "lastStep";
    public static final String LAST_TIMER = "lastTimer";
    public static final String BEST_STEP = "bestStep";
    public static final String BEST_TIMER = "bestTimer";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyBase(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLastStep(int step) {
        editor.putInt(LAST_STEP, step).commit();
    }

    public int getLastStep() {
        return sharedPreferences.getInt(LAST_STEP, 0);
    }

    public void saveLastTimer(int second) {
        editor.putInt(LAST_TIMER, second).commit();
    }

    public int getLastTimer() {
        return sharedPreferences.getInt(LAST_TIMER, 0);
    }

    public void saveBestStep(int step) {
        editor.putInt(BEST_STEP, step).commit();
    }

    public int getBestStep() {
        return sharedPreferences.getInt(BEST_STEP, 0);
    }

    public void saveBestTimer(int second) {
        editor.putInt(BEST_TIMER, second).commit();
    }

    public int getBestTimer() {
        return sharedPreferences.getInt(BEST_TIMER, 0);
    }
}
