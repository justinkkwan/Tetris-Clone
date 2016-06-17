import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Justin Kwan on 03/06/2016.
 */
public class GamePanel extends JPanel {
    private Tetris tetris;

    public GamePanel(Tetris t){
        tetris = t;

        this.setLayout(null);


        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "RotateRight");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "SoftDrop");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "Left");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "Right");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "RotateLeft");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "Hold");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("V"), "HardDrop");

        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "released RotateRight");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "released SoftDrop");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "released Left");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "released Right");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), "released RotateLeft");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "released Hold");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released V"), "released HardDrop");

        getActionMap().put("RotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[0]=true;
            }
        });

        getActionMap().put("SoftDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[1]=true;
            }
        });

        getActionMap().put("Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[2]=true;
            }
        });

        getActionMap().put("Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[3]=true;
            }
        });

        getActionMap().put("RotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[4]=true;
            }
        });

        getActionMap().put("Hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[5]=true;
            }
        });

        getActionMap().put("HardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[6]=true;
            }
        });

        getActionMap().put("Reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[7]=true;
            }
        });


        getActionMap().put("released RotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[0]=false;
            }
        });

        getActionMap().put("released SoftDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[1]=false;
            }
        });

        getActionMap().put("released Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[2]=false;
            }
        });

        getActionMap().put("released Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[3]=false;
            }
        });

        getActionMap().put("released RotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[4]=false;
            }
        });

        getActionMap().put("released Hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[5]=false;
            }
        });

        getActionMap().put("released HardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[6]=false;
            }
        });

        getActionMap().put("released Reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetris.keysPressed[7]=false;
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        //Main play field
        for(int i=0;i<10;i++){
            for(int j=0; j<20; j++){
                g.drawRect(20+ i*17 , 40+ j*17 ,17,17);
            }
        }


        //Preview pieces
        for(int i=0; i<4; i++){

            for(int j=0;j<4;j++){
                for(int k=0; k<4; k++){
                    g.drawRect(200 + j*17 , 40 + i*72 + k*17 ,17,17);
                }
            }

        }

        Color[][] cBoard = tetris.matrix;
        for(int i=0;i<10;i++){
            for(int j=2; j<22; j++){
                if(cBoard[j][i+1]!=null){
                    g.setColor(cBoard[j][i+1]);
                    g.fillRect(21+ i*17 , 41+ (j-2)*17 ,16,16);
                }
            }
        }

        g.setColor(tetris.activePiece.getColor());
        for(int i=0; i<4;i++){
            g.fillRect(21+ (tetris.activePieceLocations[2*i]-1)*17 , 41+ (tetris.activePieceLocations[2*i+1]-2)*17 ,16,16);
        }



    }

    public void paintPiece(Graphics g, Color c, int[]oldBlocks, int[] blocks) {

        for(int i=0;i<4;i++){
            g.clearRect(4+ oldBlocks[2*i]*17 , 7+ oldBlocks[2*i+1]*17 ,16,16);
        }

        g.setColor(c);

        for(int i=0;i<4;i++){
            g.fillRect(4+ blocks[2*i]*17 , 7+ blocks[2*i+1]*17 ,16,16);
        }

    }

    public void paintRemoveLine(Graphics g, int line){

    }

}
