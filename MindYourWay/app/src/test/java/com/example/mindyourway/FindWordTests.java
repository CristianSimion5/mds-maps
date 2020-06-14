package com.example.mindyourway;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindWordTests {
    char[][] table = {{'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','p','r','o','i','e','c','t','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'a','b','c','d','e','f','g','h','i','j'},};
    @Test
    public void testCheckPotrivireCuvant() {
        FindWordEngine findWordEngine = new FindWordEngine(table);
        int Sx = 3;
        int Sy = 1;
        int Fx = 3;
        int Fy = 7;
        String Cuvant = "proiect";
        int expected = 1;
        int output = findWordEngine.checkPotrivireCuvant(Sx,Sy,Fx,Fy,Cuvant);
        assertEquals(expected,output);
    }
}
