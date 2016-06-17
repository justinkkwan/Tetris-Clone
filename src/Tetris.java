import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Justin Kwan on 03/06/2016.
 */
public class Tetris {

    JLabel fpsLabel = new JLabel();
    GamePanel game = new GamePanel(this);
    Graphics g;
    boolean running;
    Color[][] matrix = new Color[23][12]; // 2 invisible rows on top (2 above for spawn position),
    // 1 invisible row at bottom, 2 invisible side walls
    // *** Changed matrix so that it goes boolean[rows][columns] so it is faster to clear lines (just change pointers)
    LinkedList<Pieces> bag = new LinkedList<Pieces>();
    Pieces activePiece;
    int activePieceState = 0;
    int[] activePieceLocations;
    int[] oldPieceLocations;
    int activePieceRelativeX;
    int activePieceRelativeY;

    private boolean goingDown = false;

    private static final int AutoRepeatRate = 1;
    private static final int DelayAutoShift = 3;

    private int ARRCount = 0;
    private int DASLeftCount = 0;
    private int DASRightCount = 0;

    boolean keysPressed[] = new boolean[8];
    boolean RotatedRight;
    boolean RotatedLeft;
    boolean justPlacedPiece;


    long time;


    public Tetris() {
        game.add(fpsLabel);


        activePieceRelativeX = 4;
        activePieceRelativeY = 0;

        RotatedRight = false;
        RotatedLeft = false;
    }

    public void main() {
        JFrame window = new JFrame("Tetris");
        window.add(game);
        window.setSize(300, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);


        g = game.getGraphics();

        running = true;
        setup();
        gameLoop();
    }

    public void gameLoop() {

        int targetFPS = 30;
        long lastRecordedTime = System.nanoTime();
        long currTime;
        long timeBetween;

        while (running) {
            currTime = System.nanoTime();
            timeBetween = currTime - lastRecordedTime;

            if (timeBetween < 1000000000 / targetFPS) {
                try {
                    Thread.sleep(1000 / targetFPS - timeBetween / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                update(timeBetween);
                render((int) ((long) 1000000000 / timeBetween));
                lastRecordedTime = currTime;
            }
        }
    }

    private void update(long t) {
        time += t;
        if (time >= 1000000000 || keysPressed[1]) {
            if (!keysPressed[1]) time = time - 1000000000;
            lowerPiece();
            goingDown = true;
        } else goingDown = false;

        if (bag.size() < 7) {
            LinkedList<Pieces> newBag = new LinkedList<>();
            newBag.add(Pieces.L);
            newBag.add(Pieces.J);
            newBag.add(Pieces.S);
            newBag.add(Pieces.Z);
            newBag.add(Pieces.T);
            newBag.add(Pieces.O);
            newBag.add(Pieces.I);
            Collections.shuffle(newBag);
            bag.addAll(newBag);
        }

        moveAccordingToKeyPresses();


    }

    private void lowerPiece() {
        Piece.lookup(activePiece, activePieceState);

        boolean okay = true;

        for (int i = 0; i < 4; i++) {
            if (matrix[activePieceLocations[2 * i + 1] + 1][activePieceLocations[2 * i]]!=null) {//check immediately  below space
                okay = false;
            }
        }

        if (okay) {
            for (int i = 0; i < 8; i++) {
                oldPieceLocations[i] = activePieceLocations[i];
            }

            for (int i = 0; i < 4; i++) {
                activePieceLocations[2 * i + 1]++;
            }
            activePieceRelativeY++;
        } else {
            placePiece();
        }

    }

    private void placePiece() {

        for (int i = 0; i < 4; i++) {
            matrix[activePieceLocations[2 * i + 1]][activePieceLocations[2 * i]] = activePiece.getColor();
        }


        game.paintPiece(g, activePiece.getColor(), oldPieceLocations, activePieceLocations);
        clearLines();
        bag.remove(0);
        activePiece = bag.get(0);
        activePieceState = 0;
        activePieceLocations = Piece.lookup(activePiece, activePieceState);
        activePieceRelativeX = 4;
        activePieceRelativeY = 0;
        for (int i = 0; i < 4; i++) {
            activePieceLocations[2 * i] += activePieceRelativeX;
        }
        oldPieceLocations = new int[8];
    }

    private void clearLines() {
        boolean clear;
        for (int i = 0; i < 4; i++) {
            clear = true;
            for (int j = 1; j < 10; j++) {
                if (matrix[activePieceLocations[2 * i + 1]][j]==null) clear = false;
            }
            if (clear) clearLine(activePieceLocations[2 * i + 1]);
        }
    }

    private void clearLine(int line) {
        for (int i = line; i > 0; i--) {
            matrix[i] = matrix[i - 1];
        }
        matrix[0] = new Color[12];
        for (int i = 1; i < 11; i++) {
            matrix[0][i] = null;
        }
        matrix[0][0] = Color.BLACK;
        matrix[0][11] = Color.BLACK;
        game.paintRemoveLine(g, line);
    }

    private void moveAccordingToKeyPresses() {

        if (!goingDown) {
            for (int i = 0; i < 8; i++) {
                oldPieceLocations[i] = activePieceLocations[i];
            }
        }

        if (keysPressed[2]) {
            boolean okay = true;
            if (0 < DASLeftCount && DASLeftCount < DelayAutoShift) {
                okay = false;
            }
            DASLeftCount++;
            for (int i = 0; i < 4; i++) {
                if (matrix[activePieceLocations[2 * i + 1]][activePieceLocations[2 * i] - 1]!=null) {//check immediately beside space
                    okay = false;
                }
            }
            if (okay) {
                for (int i = 0; i < 4; i++) {
                    activePieceLocations[2 * i]--;
                }
                activePieceRelativeX--;
            }
        }

        if (keysPressed[3]) {
            boolean okay = true;
            if (0 < DASRightCount && DASRightCount < DelayAutoShift) {
                okay = false;
            }
            DASRightCount++;
            for (int i = 0; i < 4; i++) {
                if (matrix[activePieceLocations[2 * i + 1]][activePieceLocations[2 * i] + 1]!=null) {//check immediately beside space
                    okay = false;
                }
            }
            if (okay) {
                for (int i = 0; i < 4; i++) {
                    activePieceLocations[2 * i]++;
                }
                activePieceRelativeX++;
            }
        }
        if (!keysPressed[2]) DASLeftCount = 0;
        if (!keysPressed[3]) DASRightCount = 0;

        if (keysPressed[0] && !RotatedRight) {
            RotatedRight = true;
            int potentialState;
            int[] potentialLocation;
            boolean okay = true;
            potentialState = (activePieceState + 1) % 4;
            potentialLocation = Piece.lookup(activePiece, potentialState);
            for (int i = 0; i < 4; i++) {
                potentialLocation[2 * i] += activePieceRelativeX;
                potentialLocation[2 * i + 1] += activePieceRelativeY;
            }
            for (int i = 0; i < 4; i++) {
                if (matrix[potentialLocation[2 * i + 1]][potentialLocation[2 * i]]!=null) {//check potential new location
                    okay = false;
                }
            }
            if (okay) {
                activePieceState = potentialState;
                System.arraycopy(potentialLocation, 0, activePieceLocations, 0, 8);
            }
        }
        if (!keysPressed[0]) RotatedRight = false;

        if (keysPressed[4] && !RotatedLeft) {
            RotatedLeft = true;
            boolean okay = true;
            int potentialState;
            int[] potentialLocation;
            potentialState = activePieceState - 1;
            if (potentialState < 0) potentialState += 4;
            potentialLocation = Piece.lookup(activePiece, potentialState);
            for (int i = 0; i < 4; i++) {
                potentialLocation[2 * i] += activePieceRelativeX;
                potentialLocation[2 * i + 1] += activePieceRelativeY;
            }
            for (int i = 0; i < 4; i++) {
                if (matrix[potentialLocation[2 * i + 1]][potentialLocation[2 * i]]!=null) {//check potential new location
                    okay = false;
                }
            }
            if (okay) {
                activePieceState = potentialState;
                System.arraycopy(potentialLocation, 0, activePieceLocations, 0, 8);
            }
        }
        if (!keysPressed[4]) RotatedLeft = false;

        if (keysPressed[6] && !justPlacedPiece) {
            justPlacedPiece = true;
            int addY = 0;
            while (matrix[activePieceLocations[1] + addY][activePieceLocations[0]]==null &&
                    matrix[activePieceLocations[3] + addY][activePieceLocations[2]]==null &&
                    matrix[activePieceLocations[5] + addY][activePieceLocations[4]]==null &&
                    matrix[activePieceLocations[7] + addY][activePieceLocations[6]]==null) addY++;
            addY--;
            for (int i = 0; i < 4; i++) {
                activePieceLocations[2 * i + 1] += addY;
            }
            activePieceRelativeY += addY;
            placePiece();
        }
        if (!keysPressed[6]) justPlacedPiece = false;

    }

    private void render(int fps) {
        fpsLabel.setText("FPS: " + Integer.toString(fps));

        //game.paintPiece(g, activePiece.getColor(), oldPieceLocations, activePieceLocations);
        game.repaint();
    }

    public void setup() {

        bag.add(Pieces.L);
        bag.add(Pieces.J);
        bag.add(Pieces.S);
        bag.add(Pieces.Z);
        bag.add(Pieces.T);
        bag.add(Pieces.O);
        bag.add(Pieces.I);
        Collections.shuffle(bag);

        activePiece = bag.get(0);
        activePieceLocations = Piece.lookup(activePiece, activePieceState);
        for (int i = 0; i < 4; i++) {
            activePieceLocations[2 * i] += activePieceRelativeX;
        }
        oldPieceLocations = new int[8];


        for (int i = 0; i < 22; i++) {
            for (int j = 1; j < 11; j++) {
                matrix[i][j] = null;
            }
        }

        for (int i = 0; i < 12; i++) {
            matrix[22][i] = Color.BLACK;
        }

        for (int i = 0; i < 22; i++) {
            matrix[i][0] = Color.BLACK;
            matrix[i][11] = Color.BLACK;
        }

        //game.paintComponent(g);
    }

//    private void movePiece(int[] sendTo){
//        boolean okay=true;
//        for(int i=0;i<4;i++){
//            if(matrix[activePieceLocations[2*i]][activePieceLocations[2*i+1]]){
//                okay = false;
//            }
//        }
//        if(okay) {
//            if(!goingDown) {
//                for (int i = 0; i < 8; i++) {
//                    oldPieceLocations[i] = activePieceLocations[i];
//                }
//            }
//            for (int i = 0; i < 8; i++) {
//                activePieceLocations[2 * i]++;
//            }
//        }
//    }
}
