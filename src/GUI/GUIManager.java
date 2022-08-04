package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GUIManager extends JFrame {

    private JLabel scoresR;
    private JLabel scoresL;

    private int scoreNumR;
    private int scoreNumL;

    public GUIManager(){
        buildGUI();
    }

    //region GUI BUILDER

    /**
     * Builds the GUI
     */
    private void buildGUI(){
        this.setTitle("Pong");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setLayout(null);

        //Score Count
        scoresR = buildScoreCounter();
        scoresL = buildScoreCounter();
        scoresR.setLocation(1200,50);
        scoresL.setLocation(700,50);
        this.add(scoresR);
        this.add(scoresL);


    }

    /**
     * Builds new Score Counter
     * @return new JLabel
     */
    private JLabel buildScoreCounter(){
        JLabel jLabel = new JLabel("0");
        jLabel.setVisible(true);
        jLabel.setFont(new Font("tahoma", 20,100));
        jLabel.setSize(300, 100);
        return jLabel;
    }

    /**
     * Build a new player
     * @return new player
     */
    public JLabel buildPlayer(){
        ImageIcon icon = getImage("Pong-Spieler.png");
        JLabel jLabel = new JLabel();
        jLabel.setVisible(true);
        jLabel.setIcon(icon);
        jLabel.setSize(256, 256);
        return jLabel;
    }

    /**
     * Builds a new Ball
     */
    public JLabel buildBall(){
        ImageIcon icon = getImage("Pong-Ball.png");
        JLabel jLabel  = new JLabel();
        jLabel.setVisible(true);
        jLabel.setIcon(icon);
        jLabel.setSize(256, 256);
        return jLabel;
    }



    //endregion

    /**
     * @return Return image from file
     */
    public static ImageIcon getImage(String path) {
        File imageFile = new File(path);
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(imageFile));
            return  icon;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void movePlayerL(){

    }

    public void movePlayerR(){

    }

    public void addScoresR(){
        ++scoreNumR;
        scoresR.setText(scoreNumR + "");
    }

    public void addScoresL(){
        ++scoreNumL;
        scoresL.setText(scoreNumL + "");
    }

    public int getScoreL(){
        return scoreNumR;
    }
    public int getScoreR(){
        return scoreNumL;
    }


}
