package com.example.mindyourway;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorLinkTests {
    int[][] table = {{0, 32, 0, 0, 96},
            {0, 0, 0, 0, 0},
            {96, 0, 0, 0, 32},
            {0, 0, 0, 64, 0},
            {0, 64, 0, 0, 0}};
    @Test
    public void testGetColor() {
        ColorLink colorLink = new ColorLink(table);
        int x = 3;
        int y = 3;
        int expected = 2;
        int output = colorLink.getColor(x,y);
        assertEquals(expected,output);
    }

    @Test
    public void testGetNext() {
        int x = 2;
        int y = 0;
        table[x][y] = 33888;
        ColorLink colorLink = new ColorLink(table);
        int[] expected = {2,1};
        int[] output = colorLink.getNext(x,y);
        assertEquals(expected[0],output[0]);
    }


}
