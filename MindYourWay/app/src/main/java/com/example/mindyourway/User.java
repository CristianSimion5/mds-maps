package com.example.mindyourway;

import java.util.HashMap;

public class User {
    private String name;
    private Integer level;
    private HashMap<String,Integer> status;

    public User(){
        this.name = "";
        this.level = 1;
        this.status = new HashMap<>();
        status.put("Center_1", 1);
        status.put("Center_2", 1);
        status.put("Center_3", 1);
        status.put("Sector1_1", 1);
        status.put("Sector1_2", 1);
        status.put("Sector1_3", 1);
        status.put("Sector3_1", 1);
        status.put("Sector3_2", 1);
        status.put("Sector3_3", 1);
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
}
