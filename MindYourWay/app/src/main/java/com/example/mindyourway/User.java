package com.example.mindyourway;

import java.util.HashMap;

public class User {
    private String name;
    private Integer level;
    private HashMap<String,Integer> status;
    private boolean admin = true;
    private HashMap<String,int[][][]> sudokuGames;

    public User(){
        this.name = "";
        this.level = 1;
        this.status = new HashMap<>();
        this.sudokuGames = new HashMap<>();

        status.put("Center_1", 2);
        status.put("Center_2", 1);
        status.put("Center_3", 1);
        status.put("Sector1_1", 1);
        status.put("Sector1_2", 1);
        status.put("Sector1_3", 1);
        status.put("Sector3_1", 1);
        status.put("Sector3_2", 1);
        status.put("Sector3_3", 1);
        status.put("Sector2_1", 1);
        status.put("Sector2_2", 1);
        status.put("Sector2_3", 1);
        status.put("Sector4_1", 1);
        status.put("Sector4_2", 1);
        status.put("Sector4_3", 1);

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
        status.put(string, status.get(string) + 1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSudokuGame(String name, int[][][] Table) {
        sudokuGames.put(name,Table);
    }

    public boolean checkSudokuGame(String name) {
        return sudokuGames.containsKey(name);
    }

    public int[][][] getSudokuGame(String name) {
        return sudokuGames.get(name);
    }
}
