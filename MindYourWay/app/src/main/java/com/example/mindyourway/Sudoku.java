package com.example.mindyourway;

import android.app.ActionBar;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Sudoku {
    private int[][] table;
    private int[][] table2;
    private int[][] table1;
    private int u = 1;
    private int numbersMissing=0;

    public Sudoku(int[][][] Table) {
        table = new int[9][9];
        table2 = new int[9][9];
        table1 = new int[9][9];
        copy(table1, Table[0]);
        copy(table, Table[1]);
        copy(table2, Table[2]);
    }

    public Sudoku(int difficulty) {
        table = new int[9][9];
        table2 = new int[9][9];
        table1 = new int[9][9];
        this.numbersMissing = difficulty;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                table[i][j] = 0;
            }
        }
        ArrayList<Integer> permutation = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
        Collections.shuffle(permutation);
        for(int i = 0; i < 9; i++){
            table[0][i] = permutation.get(i);
        }
        solve();
        copy(table2,table);
        removeElements(numbersMissing);
        copy(table1,table);
    }

    private void copy(int[][] table1, int[][] table2){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                table1[i][j] = table2[i][j];
            }
        }
    }


    public boolean isChangeable(int x, int y) {
        if (table1[x][y] != 0)
            return true;
        return false;
    }

    public int[][] getTable() {
        int[][] Table = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                Table[i][j] = this.table[i][j];
            }
        }
        return Table;
    }
    public int[][] getTableUnsolved(){
        int[][] Table = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                Table[i][j] = this.table1[i][j];
            }
        }
        return Table;
    }

    public int[][] getTableSolved(){
        int[][] Table = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                Table[i][j] = this.table2[i][j];
            }
        }
        return Table;
    }

    public int[][][] getCurrentState() {
        int[][][] Table = new int[3][9][9];
        Table[0] = getTableUnsolved();
        Table[1] = getTable();
        Table[2] = getTableSolved();
        return Table;
    }

    public int getElement(int i, int j) {
        return table[i][j];
    }

    public void setElement(int i, int j, int x) {
        this.table[i][j] = x;
    }

    private boolean checkElement1(int i, int j, int x, int[][] table){
        table[i][j] = 0;
        for (int k = 0; k < 9; k++){
            if(x == table[i][k] || x == table[k][j]) {
                return false;
            }
        }

        for (int k = (i / 3) * 3; k < (i / 3) * 3 + 3; k++){
            for (int l = (j / 3) * 3; l < (j / 3) * 3 + 3; l++){
                if(x == table[k][l]){
                    return false;
                }
            }
        }

        return true;
    }
    public boolean checkElement(int i, int j, int x){
        return checkElement1(i,j,x,this.table);
    }

    private void solve(){
        copy(table2,table);
        copy(table1,table);
        u=1;
        bkt(0,0);
    }

    public boolean checkIfSolved(){
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (table[i][j]!=table2[i][j])
                    return false;
            }
        }
        return true;
    }

    private void bkt(int x, int y){
        if(x==9 && this.u==1){
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    this.table[i][j]=this.table2[i][j];
                }
            }
            this.u=0;
        }
        else
        if(this.u==1)
        {
            int i,j;
            if(y==8){
                j=0;
                i=x+1;
            } else {
                i=x;
                j=y+1;
            }
            if(this.table1[x][y]==0){
                for(int k=1;k<=9;k++){
                    if(checkElement1(x,y,k,this.table2)){
                        this.table2[x][y]=k;
                        bkt(i,j);
                        this.table2[x][y]=0;
                    }
                    else{
                        //cout<<x<<" "<<y<<" "<<k<<"\n";
                    }
                }
            }
            else bkt(i,j);
        }
    }

    private void removeElements(int k){
        while(k!=0) {
            int i = Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
            int j =  Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
            while (table[i][j] == 0) {
                i =  Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
                j =  Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
            }
            table[i][j] = 0;
            k--;
        }
    }


    private boolean checkIfSolvable(){
        copy(table2,table);
        copy(table1,table);
        u=2;
        bkt1(0,0);
        if(u==1){
            return true;
        } else {
            return false;
        }
    }

    private void bkt1(int x, int y){
        if(x==9){
            if (this.u==2)
                this.u=1;
            else if(this.u==1){
                this.u=0;
            }
        }
        else
        if(this.u!=1)
        {
            int i,j;
            if(y==8){
                j=0;
                i=x+1;
            } else {
                i=x;
                j=y+1;
            }
            if(this.table1[x][y]==0){
                for(int k=1;k<=9;k++){
                    if(checkElement1(x,y,k,this.table2)){
                        this.table2[x][y]=k;
                        bkt(i,j);
                        this.table2[x][y]=0;
                    }
                    else{
                        //cout<<x<<" "<<y<<" "<<k<<"\n";
                    }
                }
            }
            else bkt(i,j);
        }
    }
}
