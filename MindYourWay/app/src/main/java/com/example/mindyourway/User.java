package com.example.mindyourway;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

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
    private HashMap<String, FindWordEngine> findWordGames;
    private HashMap<String,int[][]> colorlinkGames;
    private SharedPreferences.Editor editor;
    Gson g;

    public User(Activity context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        g = new Gson();

        Log.d(TAG, "Loading data from device...");
        this.name = sharedPreferences.getString("name", "");
        Log.d(TAG, "Name: " + this.name);
        this.level = sharedPreferences.getInt("level", 0);
        Log.d(TAG, "Level: " + this.level);

        this.status = new HashMap<>();
        this.sudokuGames = new HashMap<>();
        this.findWordGames = new HashMap<>();
        this.colorlinkGames = new HashMap<>();

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
                if (sharedPreferences.contains(keyName + "ColorLink")) {
                    String gameState = sharedPreferences.getString(keyName + "ColorLink", "");
                    Log.d(TAG, "ColorLink game found for region " +  keyName + ": " + gameState);
                    this.colorlinkGames.put(keyName, g.fromJson(gameState, int[][].class));
                }
            }
        }
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

    public void levelUp(int x) {
        this.level += x;
        Log.d(TAG, "levelUP new value: " + this.level);
        editor.putInt("level", this.level);
        editor.apply();
    }

    public void reset() {
        editor.clear();
    }

    public void setFindWordGames(String name, FindWordEngine game) {
        findWordGames.put(name, game);
    }

    public boolean checkFindWordGame(String name) {
        return findWordGames.containsKey(name);
    }

    public FindWordEngine getFindWordGames(String name) {
        return findWordGames.get(name);
    }

    public void setColorLinkGame (String name,  int[][] Table) {
        colorlinkGames.put(name,Table);

        String array = g.toJson(Table);
        Log.d(TAG, "setColorLinkGame for region " + name + ": " + array);
        editor.putString(name + "ColorLink", array);
        editor.apply();
    }

    public boolean checkColorLinkGame(String name) {
        return colorlinkGames.containsKey(name);
    }

    public int[][] getColorLinkGame(String name) {
        return colorlinkGames.get(name);
    }

}
