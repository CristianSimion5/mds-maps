package com.example.mindyourway;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SudokuTests {
    private int[][] table1 = {{9,0,0,1,0,0,0,0,5},
            {0,0,5,0,9,0,2,0,1},
            {8,0,0,0,4,0,0,0,0},
            {0,0,0,0,8,0,0,0,0},
            {0,0,0,7,0,0,0,0,0},
            {0,0,0,0,2,6,0,0,9},
            {2,0,0,3,0,0,0,0,6},
            {0,0,0,2,0,0,9,0,0},
            {0,0,1,9,0,4,5,7,0}};
    @Test
    public void testIsChangeable() {
        int[][][] table = new int[3][9][9];
        for(int k = 0; k < 3; k++){
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    table[k][i][j] = table1[i][j];
                }
            }
        }
        int x = 1;
        int y = 2;
        boolean expected = false;
        boolean output;
        Sudoku sudoku = new Sudoku(table);
        output = sudoku.isChangeable(x, y);
        assertEquals(expected,output);
    }

    @Test
    public void testCheckElement() {
        int[][][] table = new int[3][9][9];
        for(int k = 0; k < 3; k++){
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    table[k][i][j] = table1[i][j];
                }
            }
        }
        int x = 2;
        int y = 3;
        int val = 6;
        boolean expected = true;
        boolean output;
        Sudoku sudoku = new Sudoku(table);
        output = sudoku.checkElement(x, y, val);
        assertEquals(expected,output);
    }

}
