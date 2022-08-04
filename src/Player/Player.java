package Player;

import GUI.GUIManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player implements KeyListener{

    private GUIManager guiManager;
    private JLabel player;

    private int y;
    private int x;
    private int ySpeed;

    public Player(GUIManager guiManager, int x, int y, boolean withInput) {
        this.guiManager = guiManager;
        this.x = x;
        this.y = y;
        if(withInput) guiManager.addKeyListener(this);
        buildPlayer();

        ySpeed = 40;
    }

    //region PLAYER BUILDER

    private void buildPlayer(){
        player = guiManager.buildPlayer();
        player.setLocation(x,y);
        guiManager.add(player);
    }

    //endregion

    //region MOVEMENT

    /**
     * Moves the player up
     */
    public void moveUp() {
        if(y > -10) y -= ySpeed;
        player.setLocation(x, y);
    }

    /**
     * Moves the player down
     */
    public void moveDown() {
        if(y < 790) y += ySpeed;
        player.setLocation(x, y);
    }

    //endregion

    //region INPUT
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_UP) moveUp();
        if(keyCode == KeyEvent.VK_DOWN) moveDown();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //endregion

    //region GETTER & SETTER

    /**
     * @return y Position
     */
    public int getY() {
        return y;
    }

    /**
     * @return x Position
     */
    public int getX() {
        return x;
    }

    //endregion
}
