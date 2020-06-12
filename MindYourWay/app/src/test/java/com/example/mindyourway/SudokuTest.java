package com.example.mindyourway;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SudokuTest {
    @Test
    public void testIsChangeable() {
        int[][][] table = new int[3][9][9];
        for(int k = 0; k < 3; k++){
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    table[k][i][j] = 0;
                }
            }
        }
        int x = 1;
        int y = 2;
        boolean expected = true;
        boolean output;
        Sudoku sudoku = new Sudoku(table);
        output = sudoku.isChangeable(x, y);
        assertEquals(expected,output);
    }

}
