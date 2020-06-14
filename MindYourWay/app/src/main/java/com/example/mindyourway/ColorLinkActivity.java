package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ColorLinkActivity extends AppCompatActivity {

    private static final String TAG = "ColorLinkActivity";
    private String checkpointString;

    private Drawable cell_not_selected;
    private Drawable cell_selected;
    private Drawable first_layer;

    private float dim;
    private float biasdim;
    private int numColorPairs;

    private int xCurrent = -1;
    private int yCurrent = -1;

    private ColorLink game;
    private int[][][] colorPairs;
    private ConstraintSet cons;
    private ConstraintLayout colorLinkTable;
    private Button[][] cell;
    private Button buttonReset;
    private Button buttonComplete;
    private Button buttonBack;
    private Button buttonHelp;
    private ImageView[][][] layers;

    int[][] dir = new int[][] {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    private interface function{void setLine(int i, int j, int val);}
    private function[] sets = new function[] {
            new function() { public void setLine(int i, int j, int val) { game.setLeft(i, j, val); MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());} },
            new function() { public void setLine(int i, int j, int val) { game.setBottom(i, j, val); MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());} },
            new function() { public void setLine(int i, int j, int val) { game.setRight(i, j, val); MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());} },
            new function() { public void setLine(int i, int j, int val) { game.setTop(i, j, val); MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());} },
            new function() { public void setLine(int i, int j, int val) { game.setSquare(i, j, val); MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());} }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorlink);
        Intent intent = getIntent();
        checkpointString = intent.getStringExtra(CenterMapActivity.regionExtra);
        Log.d(TAG, "onCreate: "+ checkpointString);
        init();

    }

    public void init() {

        initFunctionalButtons();

        if(MainActivity.user.checkColorLinkGame(checkpointString)){
            game = new ColorLink(MainActivity.user.getColorLinkGame(checkpointString));
            Log.d(TAG, "init: was created");
        }
        else {
            Log.d(TAG, "init: was not created");
            int difficulty = 0;
            if (checkpointString.contains("Center")) {
                difficulty = 7;
            } else if (checkpointString.contains("Sector1") || checkpointString.contains("Sector3")) {
                difficulty = 9;
            } else if (checkpointString.contains("Sector2") || checkpointString.contains("Sector4")) {
                difficulty = 11;
            }

            game = new ColorLink(difficulty);
            MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());
        }

        initInterface();

    }

    public void initFunctionalButtons() {

        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.resetGame();
                MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());
                colorLinkTable.removeAllViews();
                initInterface();
            }
        });
        buttonHelp = (Button) findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.user.setColorLinkGame(checkpointString, game.getCurrentState());
                Intent intent = new Intent(ColorLinkActivity.this, GuideActivity.class);
                intent.putExtra(GuideActivity.guideExtra, "ColorLink-"+checkpointString);
                startActivity(intent);
            }
        });

        buttonComplete = (Button) findViewById(R.id.buttonComplete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(game.getNumRemainingPairs() == 0 || MainActivity.user.isAdmin()){
                    if(MainActivity.user.getStatus(checkpointString)==3) {
                        MainActivity.user.incrementStatus(checkpointString);
                        MainActivity.user.levelUp(2);
                    }
                    buttonBack.performClick();

                } else {
                    Toast.makeText(ColorLinkActivity.this, "Not all squares connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpointString.contains("Center")) {
                    Intent intent = new Intent(ColorLinkActivity.this, CenterMapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector1")) {
                    Intent intent = new Intent(ColorLinkActivity.this, Sector1MapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector2")) {
                    Intent intent = new Intent(ColorLinkActivity.this, Sector2MapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector3")) {
                    Intent intent = new Intent(ColorLinkActivity.this, Sector3MapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector4")) {
                    Intent intent = new Intent(ColorLinkActivity.this, Sector4MapActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void initInterface() {
        dim = (float) 1 / game.getNumSquares();
        biasdim = (float) 1 / (game.getNumSquares() - 1);

        cell_not_selected = getDrawable("colorlink_notselected");
        cell_selected = getDrawable("colorlink_selected");
        first_layer = getDrawable("colorlink_notcolored");

        colorLinkTable = (ConstraintLayout) findViewById(R.id.ColorLinkTable);
        cell = new Button[game.getNumSquares()][game.getNumSquares()];
        layers = new ImageView[game.getNumSquares()][game.getNumSquares()][2];
        cons = new ConstraintSet();

        initColors();

        for(int i = 0; i < game.getNumSquares(); ++i)
            for(int j = 0; j < game.getNumSquares(); ++j) {

                // add buttons
                cell[i][j] = new Button(ColorLinkActivity.this);

                cell[i][j].setBackground(cell_not_selected);
                cell[i][j].setId(View.generateViewId());

                giveCellOnClick(i, j);

                // add common background for cells
                addLayer(cell[i][j], i, j);

                ImageView image_layer = new ImageView(ColorLinkActivity.this);

                image_layer.setBackground(first_layer);
                image_layer.setId(View.generateViewId());

                addLayer(image_layer, i, j);

                // add layer images according to the cell's code
                addLayersByCode(i, j);

            }
    }

    public void initColors() {

        numColorPairs = 0;

        for(int i = 0; i < game.getNumSquares(); ++i)
            for(int j = 0; j < game.getNumSquares(); ++j)
                if(game.getColor(i, j) != 0)
                    ++numColorPairs;

        numColorPairs /= 2;
        if(game.getNumRemainingPairs() < 0)
            game.setNumRemainingPairs(numColorPairs);
        colorPairs = new int[numColorPairs][2][2];

        for(int i = 0; i < numColorPairs; ++i)
            for(int j = 0; j < 2; ++j)
                for(int k = 0; k < 2; ++k)
                    colorPairs[i][j][k] = -1;

        for(int i = 0; i < game.getNumSquares(); ++i)
            for(int j = 0; j < game.getNumSquares(); ++j) {

                int idx = game.getColor(i, j);

                if(idx != 0) {

                    if(colorPairs[idx - 1][0][0] < 0) {

                        colorPairs[idx - 1][0][0] = i;
                        colorPairs[idx - 1][0][1] = j;

                    }
                    else {

                        colorPairs[idx - 1][1][0] = i;
                        colorPairs[idx - 1][1][1] = j;

                    }
                }
            }
    }

    public void giveCellOnClick(final int i, final int j) {

        cell[i][j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(game.hasSquare(i, j) != 0 &&
                        (xCurrent < 0 ||
                                game.getColor(i, j) != game.getColor(xCurrent, yCurrent) ||
                                Math.abs(xCurrent - i) + Math.abs(yCurrent - j) != 1)) {
                    resetRoad(i, j);
                    int color = game.getColor(i, j) - 1;
                    int[] otherSquare = new int[2];

                    for(int k = 0; k < 2; ++k)
                        if(colorPairs[color][k][0] == i && colorPairs[color][k][1] == j) {
                            otherSquare = new int[] {colorPairs[color][1 - k][0], colorPairs[color][1 - k][1]};
                            break;
                        }
                    resetRoad(otherSquare[0], otherSquare[1]);
                }
                else {
                    resetRoad(i, j);
                }


                cell[i][j].setBackground(cell_selected);

                if(i == xCurrent && j == yCurrent) {
                    cell[i][j].setBackground(cell_not_selected);
                    xCurrent = -1;
                    yCurrent = -1;
                }
                else {

                    if(xCurrent >= 0) {
                        cell[xCurrent][yCurrent].setBackground(cell_not_selected);
                        boolean previousCellIsNotEmpty = game.getColor(xCurrent, yCurrent) != 0;
                        boolean nextToEachOther = Math.abs(xCurrent - i) + Math.abs(yCurrent - j) == 1;
                        boolean previousCellHasASquare = game.hasSquare(xCurrent, yCurrent) != 0;
                        boolean cellIsEmpty = game.getColor(i, j) == 0;

                        if(previousCellIsNotEmpty && nextToEachOther) {
                            if(previousCellHasASquare && cellIsEmpty) {
                                resetRoad(xCurrent, yCurrent);
                                int color = game.getColor(xCurrent, yCurrent) - 1;
                                int[] otherSquare = new int[2];

                                for(int k = 0; k < 2; ++k)
                                    if(colorPairs[color][k][0] == xCurrent && colorPairs[color][k][1] == yCurrent) {
                                        otherSquare = new int[] {colorPairs[color][1 - k][0], colorPairs[color][1 - k][1]};
                                        break;
                                    }
                                resetRoad(otherSquare[0], otherSquare[1]);
                            }

                            boolean cellIsTheOtherSquare =
                                    game.hasSquare(i, j) != 0 &&
                                            game.getColor(i, j) == game.getColor(xCurrent, yCurrent) &&
                                            game.hasTop(i, j) + game.hasRight(i, j) + game.hasBottom(i, j) + game.hasLeft(i, j) == 0;

                            if(cellIsEmpty || cellIsTheOtherSquare) {
                                for(int d = 0; d < 4; ++d)
                                    if(i + dir[d][0] == xCurrent && j + dir[d][1] == yCurrent) {
                                        game.setColor(i, j, game.getColor(xCurrent, yCurrent));
                                        addImageView(i, j, d);
                                        sets[d].setLine(i, j, 1);

                                        game.setNext(xCurrent, yCurrent, new int[] {i, j});
                                        addImageView(xCurrent, yCurrent, (d + 2) % 4);
                                        sets[(d + 2) % 4].setLine(xCurrent, yCurrent, 1);

                                        break;
                                    }
                            }

                            if(cellIsTheOtherSquare) {
                                game.setNumRemainingPairs(game.getNumRemainingPairs() - 1);

                                if(game.getNumRemainingPairs() == 0) {
                                    System.out.println("Game finished");
                                }
                            }
                        }
                    }
                    xCurrent = i;
                    yCurrent = j;
                }

                /**Toast.makeText(ColorLinkActivity.this,
                        "Cells: " + String.valueOf(i)+ " " + String.valueOf(j) +
                                "\n" + "Next: " + String.valueOf(game.getNext(i, j)[0]) + " " + String.valueOf(game.getNext(i, j)[1]) +
                                "\n" + "Color: " + String.valueOf(game.getColor(i, j)) +
                                "\n" + "Square: " + String.valueOf(game.hasSquare(i, j)) +
                                "\n" + "Top: " + String.valueOf(game.hasTop(i, j)) +
                                "\n" + "Right: " + String.valueOf(game.hasRight(i, j)) +
                                "\n" + "Bottom: " + String.valueOf(game.hasBottom(i, j)) +
                                "\n" + "Left: " + String.valueOf(game.hasLeft(i, j)),
                        Toast.LENGTH_SHORT).show();**/
            }
        });

    }

    public void resetRoad(int i, int j) {

        for(int k = 0; k < 4; ++k)
            if(i + dir[k][0] == game.getNext(i, j)[0] && j + dir[k][1] == game.getNext(i, j)[1]) {
                removeLayer(i, j, k);
                break;
            }

        if(game.getNext(i, j)[0] != 15)
            deleteRoad(game.getNext(i, j)[0], game.getNext(i, j)[1]);
        game.setNext(i, j, new int[] {15, 15});
    }

    public void deleteRoad(int i, int j) {
        if(game.getNext(i, j)[0] == 15){
            removeLayers(i, j);
            if(game.hasSquare(i, j) != 0)
                game.setNumRemainingPairs(game.getNumRemainingPairs() + 1);
            return;
        }
        deleteRoad(game.getNext(i, j)[0], game.getNext(i, j)[1]);
        game.setNext(i, j, new int[] {15, 15});
        removeLayers(i, j);
    }

    public void removeLayers(int i, int j) {

        for(int k = 0; k < 2; ++k)
            if(layers[i][j][k] != null &&
                    Integer.parseInt(layers[i][j][k].getContentDescription().toString()) < 4) {
                colorLinkTable.removeView(layers[i][j][k]);
                layers[i][j][k] = null;
            }

        if (layers[i][j][0] == null && layers[i][j][1] == null){
            game.setColor(i, j, 0);
        }

        for(int k = 0; k < 4; ++k)
            sets[k].setLine(i, j ,0);
    }

    public void removeLayer(int i, int j, int d) {
        sets[d].setLine(i, j ,0);

        for(int k = 0; k < 2; ++k)
            if(layers[i][j][k] != null &&
                    Integer.parseInt(layers[i][j][k].getContentDescription().toString()) == d) {
                colorLinkTable.removeView(layers[i][j][k]);
                layers[i][j][k] = null;
                return;
            }
    }

    public void addLayersByCode(int i, int j) {

        if(game.hasSquare(i, j) != 0)
            addImageView(i, j, 4);
        if(game.hasTop(i, j) != 0)
            addImageView(i, j, 3);
        if(game.hasRight(i, j) != 0)
            addImageView(i, j, 2);
        if(game.hasBottom(i, j) != 0)
            addImageView(i, j, 1);
        if(game.hasLeft(i, j) != 0)
            addImageView(i, j, 0);

    }

    // a color must be set beforehand
    public void addImageView(int i, int j, int d) {

        sets[d].setLine(i, j, 1);

        String background = "colorlink_" + String.format("%02d", game.getColor(i, j)) + "_" + String.valueOf(d);
        Drawable layer_image = getDrawable(background);
        ImageView image = new ImageView(ColorLinkActivity.this);

        image.setContentDescription(String.valueOf(d));
        image.setBackground(layer_image);
        image.setId(View.generateViewId());

        if(layers[i][j][0] == null)
            layers[i][j][0] = image;
        else
            layers[i][j][1] = image;

        addLayer(image, i, j);
    }

    public void addLayer(View cell, int pozi, int pozj) {

        colorLinkTable.addView(cell);

        cons.constrainWidth(cell.getId(), ConstraintSet.MATCH_CONSTRAINT);
        cons.constrainHeight(cell.getId(), ConstraintSet.MATCH_CONSTRAINT);
        cons.constrainPercentHeight(cell.getId(), dim);
        cons.constrainPercentWidth(cell.getId(), dim);
        cons.setVerticalBias(cell.getId(), biasdim * pozi);
        cons.setHorizontalBias(cell.getId(), biasdim * pozj);
        cons.connect(cell.getId(), ConstraintSet.START, colorLinkTable.getId(), ConstraintSet.START);
        cons.connect(cell.getId(), ConstraintSet.END, colorLinkTable.getId(), ConstraintSet.END);
        cons.connect(cell.getId(), ConstraintSet.TOP, colorLinkTable.getId(), ConstraintSet.TOP);
        cons.connect(cell.getId(), ConstraintSet.BOTTOM, colorLinkTable.getId(), ConstraintSet.BOTTOM);

        cons.applyTo(colorLinkTable);

    }

    public Drawable getDrawable(String drawable_name) {
        int resId = getResources().getIdentifier(drawable_name , "drawable", getPackageName());
        return getResources().getDrawable(resId);
    }
}
