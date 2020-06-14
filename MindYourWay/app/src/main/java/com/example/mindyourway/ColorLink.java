package com.example.mindyourway;

import android.util.Log;

public class ColorLink {

    //code for numberlink generator at https://github.com/abhishekpant93/numberlink-generator

    private int numSquares;
    private int[][] code;

    public ColorLink(int [][] code) {
        this.numSquares = code.length - 1;
        this.code = code;
    }

    public ColorLink(int numSquares){
        this.numSquares = numSquares;
        generateColorLink();
        numberToCode();
    }

    public void resetGame() {
        generateColorLink();
        numberToCode();
    }

    public void numberToCode() {
        for(int i = 0; i < numSquares; ++i)
            for(int j = 0; j < numSquares; ++j) {
                if(code[i][j] != 0) {
                    code[i][j] = code[i][j] << 5;
                    setSquare(i, j, 1);
                }
                setNext(i, j, new int[] {15, 15});
            }
    }

    // numberlink generator functions

    // Functions to generate a board along with its solution.
// ==================================================================
// Global variables storing intermediate state of the generator.
    private int pathClr = 0;
    private int covered = 0;

    // Returns a board of the specified size. Each cell of the board is empty.
    public int[][] GetEmptyBoard() {
        int[][] board = new int[numSquares + 1][numSquares];
        board[numSquares][0] = -1;
        return board;
    }

    // Returns the number of neighbours of cell (k, l) that have already been added
// to a path.
    public int NumAddedNeighbours(int[][] board, int k, int l) {
        int n = numSquares;
        int cnt = 0;
        if (k == 0) ++cnt;
        else if (board[k - 1][l] != 0) ++cnt;
        if (k == n - 1) ++cnt;
        else if (board[k + 1][l] != 0) ++cnt;
        if (l == 0) ++cnt;
        else if (board[k][l - 1] != 0) ++cnt;
        if (l == n - 1) ++cnt;
        else if (board[k][l + 1] != 0) ++cnt;
        return cnt;
    }

    // Returns the number of neighbours of cell (k, l) that are of color 'clr'.
    public int NumSameColoredNeighbours(int[][] board, int k, int l, int clr) {
        int n = numSquares;
        int cnt = 0;
        if (k != 0) {
            if (board[k - 1][l] == clr) ++cnt;
        }
        if (k != n - 1) {
            if (board[k + 1][l] == clr) ++cnt;
        }
        if (l != 0) {
            if (board[k][l - 1] == clr) ++cnt;
        }
        if (l != n - 1) {
            if (board[k][l + 1] == clr) ++cnt;
        }
        return cnt;
    }

    // Returns whether adding cell (k, l) to the path causes one or more isolated
// uncolored squares.
    public boolean HasIsolatedSquares(int[][] board, int k, int l, int clr, boolean isLastNode) {
        int n = numSquares;
        if (isLastNode) {
            if ((k != 0) && (board[k - 1][l] == 0) && (NumAddedNeighbours(board, k - 1, l) == 4) && (NumSameColoredNeighbours(board, k - 1, l, clr) > 1)) return true;
            if ((k != n - 1) && (board[k + 1][l] == 0) && (NumAddedNeighbours(board, k + 1, l) == 4) && (NumSameColoredNeighbours(board, k + 1, l, clr) > 1)) return true;
            if ((l != 0) && (board[k][l - 1] == 0) && (NumAddedNeighbours(board, k, l - 1) == 4) && (NumSameColoredNeighbours(board, k, l - 1, clr) > 1)) return true;
            if ((l != n - 1) && (board[k][l + 1] == 0) && (NumAddedNeighbours(board, k, l + 1) == 4) && (NumSameColoredNeighbours(board, k, l + 1, clr) > 1)) return true;
        } else {
            if ((k != 0) && (board[k - 1][l] == 0) && (NumAddedNeighbours(board, k - 1, l) == 4)) return true;
            if ((k != n - 1) && (board[k + 1][l] == 0) && (NumAddedNeighbours(board, k + 1, l) == 4)) return true;
            if ((l != 0) && (board[k][l - 1] == 0) && (NumAddedNeighbours(board, k, l - 1) == 4)) return true;
            if ((l != n - 1) && (board[k][l + 1] == 0) && (NumAddedNeighbours(board, k, l + 1) == 4)) return true;
        }
        return false;
    }

    // Locates and returns a random uncolored neighbour of cell (i, j). Additional
// constraints are enforced during path extension if a non-zero 'clr' is
// passed. This function ensures that the neighbour returned does not lead to
// any isolated uncolored squares.
    public int[] GetPathExtensionNeighbour(int[][] board, int i, int j, int clr) {
        int n = numSquares;
        int v, i1, j1;
        int u = (int) Math.floor(Math.random() * 4);
        for (v = 0; v < 4; ++v) {
            if (++u == 4) u = 0;
            i1 = i;
            j1 = j;
            switch (u) {
                case 0:
                    if (i == 0) continue;
                    i1 = i - 1;
                    break;
                case 1:
                    if (j == n - 1) continue;
                    j1 = j + 1;
                    break;
                case 2:
                    if (j == 0) continue;
                    j1 = j - 1;
                    break;
                case 3:
                    if (i == n - 1) continue;
                    i1 = i + 1;
                    break;
            }
            // Found an uncolored neighbour.
            if (board[i1][j1] == 0) {
                // Check the color constraint.
                if (clr != 0) {
                    if (NumSameColoredNeighbours(board, i1, j1, clr) > 1) continue;
                }
                board[i1][j1] = clr;
                // Check whether this neighbour causes isolated empty cells.
                if (HasIsolatedSquares(board, i, j, clr, false) || HasIsolatedSquares(board, i1, j1, clr, true)) {
                    board[i1][j1] = 0;
                    continue;
                }
                // This neighbour is suitable for path extension.
                return new int[] {i1, j1};
            }
        }
        // None of the 4 neighbours can extend the path, so return fail.
        return new int[] {0, 0};
    }

    // Tries to add a random path to the board, and returns whether it was
// successfull.
    public boolean AddPath(int[][] board_unsolved, int[][] board_solved) {
        int i, j, s, t;
        int[] nbr = new int[0];
        int n = numSquares;
        // Use the next color.
        ++pathClr;
        // Try and locate uncolored neighboring squares (i,j) and (k,l).
        s = (int) Math.floor(Math.random() * n * n);
        for (t = 0; t < n * n; ++t) {
            if (++s == n * n) s = 0;
            i = (int) Math.floor(s / n);
            j = s % n;
            if (board_solved[i][j] == 0) {
                board_unsolved[i][j] = pathClr;
                board_solved[i][j] = pathClr;
                if (HasIsolatedSquares(board_solved, i, j, pathClr, true)) {
                    board_solved[i][j] = 0;
                    board_unsolved[i][j] = 0;
                    continue;
                } else {
                    nbr = GetPathExtensionNeighbour(board_solved, i, j, pathClr);
                    if (nbr[0] == 0 && nbr[1] == 0) {
                        board_solved[i][j] = 0;
                        board_unsolved[i][j] = 0;
                        continue;
                    } else {
                        // Found path starting with (i, j) and nbr.
                        break;
                    }
                }
            }
        }
        if (t == n * n) {
            // Backtrack
            --pathClr;
            return false;
        }

        int pathlen = 2;
        covered += 2;
        int[] nextNbr;
        while (true) {
            i = nbr[0];
            j = nbr[1];
            nextNbr = GetPathExtensionNeighbour(board_solved, i, j, pathClr);
            if ((nextNbr[0] != 0 || nextNbr[1] != 0) && pathlen < n * n) {
                nbr = nextNbr;
            } else {
                board_unsolved[nbr[0]][nbr[1]] = pathClr;
                return true;
            }
            pathlen += 1;
            covered += 1;
        }
    }

    // Returns a random permutation of array using Fisher-Yates method.
    public int[] Shuffle(int[] array) {
        int currentIndex = array.length, temporaryValue, randomIndex;

        // While there remain elements to shuffle
        while (0 != currentIndex) {

            // Pick a remaining element
            randomIndex = (int)Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            // And swap it with the current element.
            temporaryValue = array[currentIndex];
            array[currentIndex] = array[randomIndex];
            array[randomIndex] = temporaryValue;
        }

        return array;
    }

    // Shuffles the colors on the board since by this method, paths generated
// earlier will be longer.
    public void ShuffleColors(int[][] board_unsolved, int[][] board_solved, int numColors) {
        int[] colors = new int[numColors];
        for (int i = 0; i < numColors; i++) {
            colors[i] = i + 1;
        }
        colors = Shuffle(colors);
        int n = numSquares;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board_unsolved[i][j] != 0) {
                    board_unsolved[i][j] = colors[board_unsolved[i][j] - 1];
                }
                board_solved[i][j] = colors[board_solved[i][j] - 1];
            }
        }
    }

    public void generateColorLink() {
        int[][] board_unsolved = GetEmptyBoard();
        int[][] board_solved = GetEmptyBoard();
        // Randomized Numberlink board generation strategy. Repeat until all
        // squares are covered and satisfy the constraints.
        do {
            board_unsolved = GetEmptyBoard();
            board_solved = GetEmptyBoard();
            pathClr = 0;
            covered = 0;
            while (AddPath(board_unsolved, board_solved)) {}
        } while (covered < numSquares * numSquares);
        ShuffleColors(board_unsolved, board_solved, pathClr);
        code = board_unsolved;
    }

    //setters
    public void setNumRemainingPairs(int val) {
        code[numSquares][0] = val;
    }

    public void setNext(int i, int j, int[] next) {
        int mask1 = ((1 << 4) - 1) << 14;
        int mask2 = ((1 << 4) - 1) << 10;
        mask1 = ~mask1;
        mask2 = ~mask2;

        code[i][j] = (code[i][j] & mask1) | (next[0] << 14);
        code[i][j] = (code[i][j] & mask2) | (next[1] << 10);
    }

    public void setColor(int i, int j, int color) {
        int mask = ((1 << 5) - 1) << 5;
        mask = ~mask;
        code[i][j] = (mask & code[i][j]) | (color << 5);
    }

    public void setSquare(int i, int j, int square) {
        code[i][j] = (code[i][j] & ~(1 << 4)) | (square << 4);
    }

    public void setTop(int i, int j, int top) {
        code[i][j] = (code[i][j] & ~(1 << 3)) | (top << 3);
    }

    public void setRight(int i, int j, int right) {
        code[i][j] = (code[i][j] & ~(1 << 2)) | (right << 2);
    }

    public void setBottom(int i, int j, int bottom) {
        code[i][j] = (code[i][j] & ~(1 << 1)) | (bottom << 1);
    }

    public void setLeft(int i, int j, int left) {
        code[i][j] = (code[i][j] & ~1) | left;
    }

    // getters

    public int getNumRemainingPairs() {
        return code[numSquares][0];
    }

    public int[][] getCurrentState() {
        return code;
    }

    public int getNumSquares() { return numSquares;}

    public int[] getNext(int i, int j) {
        int mask1 = ((1 << 4) - 1) << 14;
        int mask2 = ((1 << 4) - 1) << 10;

        int[] next = new int[2];

        next[0] = (mask1 & code[i][j]) >> 14;
        next[1] = (mask2 & code[i][j]) >> 10;

        return next;
    }

    public int getColor(int i, int j) {
        int mask = ((1 << 5) - 1) << 5;
        return (mask & code[i][j]) >> 5;
    }

    public int hasSquare(int i, int j) {
        return ((1 << 4) & code[i][j]) >> 4;
    }

    public int hasTop(int i, int j) {
        return ((1 << 3) & code[i][j]) >> 3;
    }

    public int hasRight(int i, int j) {
        return ((1 << 2) & code[i][j]) >> 2;
    }

    public int hasBottom(int i, int j) {
        return ((1 << 1) & code[i][j]) >> 1;
    }

    public int hasLeft(int i, int j) {
        return 1 & code[i][j];
    }

}
