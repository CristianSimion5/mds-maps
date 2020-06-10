package com.example.mindyourway;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class User {
    private static final String TAG = "User";
    private String name;
    private Integer level;
    private HashMap<String,Integer> status;
    private boolean admin = true;
    private HashMap<String,int[][][]> sudokuGames;
    private SharedPreferences.Editor editor;
    Gson g;

    public User(Activity context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        g = new Gson();

        Log.d(TAG, "Loading data from device...");
        this.name = sharedPreferences.getString("name", "");
        Log.d(TAG, "Name: " + this.name);
        this.level = sharedPreferences.getInt("level", 1);
        Log.d(TAG, "Level: " + this.level);

        this.status = new HashMap<>();
        this.sudokuGames = new HashMap<>();

        ArrayList<String> sectorList = new ArrayList<>(Arrays.asList(
                "Center", "Sector1", "Sector2", "Sector3", "Sector4", "Sector5", "Sector6"));

        for (String sector: sectorList) {
            for (int i = 1; i <= 3; i++) {
                String keyName = sector + '_' + i;
                if (keyName.equals("Center_1")) {
                    status.put(keyName, sharedPreferences.getInt(keyName + "Status", 2));
                }
                else
                    status.put(keyName, sharedPreferences.getInt(keyName + "Status", 1));

                if (sharedPreferences.contains(keyName + "Sudoku")) {
                    String gameState = sharedPreferences.getString(keyName + "Sudoku", "");
                    Log.d(TAG, "Sudoku game found for region " +  keyName + ": " + gameState);
                    this.sudokuGames.put(keyName, g.fromJson(gameState, int[][][].class));
                }
            }
        }
//        status.put("Center_1", 2);
//        status.put("Center_2", 1);
//        status.put("Center_3", 1);
//        status.put("Sector1_1", 1);
//        status.put("Sector1_2", 1);
//        status.put("Sector1_3", 1);
//        status.put("Sector3_1", 1);
//        status.put("Sector3_2", 1);
//        status.put("Sector3_3", 1);
//        status.put("Sector2_1", 1);
//        status.put("Sector2_2", 1);
//        status.put("Sector2_3", 1);
//        status.put("Sector4_1", 1);
//        status.put("Sector4_2", 1);
//        status.put("Sector4_3", 1);

    }

    public boolean isAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public Integer getLevel() {
        return level;
    }

    public int getStatus(String string){
        return status.get(string);
    }

    public void incrementStatus(String string){
        int newValue = status.get(string) + 1;
        status.put(string, newValue);
        Log.d(TAG, "incrementStatus for region " + string + " new value: " + newValue);
        editor.putInt(string + "Status", newValue);
        editor.apply();
    }

    public void setName(String name) {
        this.name = name;
        Log.d(TAG, "setName: " + name);
        editor.putString("name", name);
        editor.apply();
    }

    public void setSudokuGame(String name, int[][][] Table) {
        sudokuGames.put(name,Table);

        String array = g.toJson(Table);
        Log.d(TAG, "setSudokuGame for region " + name + ": " + array);
        editor.putString(name + "Sudoku", array);
        editor.apply();
    }

    public boolean checkSudokuGame(String name) {
        return sudokuGames.containsKey(name);
    }

    public int[][][] getSudokuGame(String name) {
        return sudokuGames.get(name);
    }
}
