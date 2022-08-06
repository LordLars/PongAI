package Player;

import GUI.GUIManager;
import Main.Settings;

import javax.swing.*;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ball {

    private final GUIManager guiManager;
    private JLabel ball;
    private int x;
    private int y;
    private final int startX;
    private final int startY;
    private int velY;
    private int velX;
    private int justCollided;


    private Rectangle2D border;
    private AI aiL;
    private AI aiR;
    private final Player playerL;
    private final Player playerR;



    public Ball(GUIManager guiManager, int x, int y, Player playerL, Player playerR) {
        startX = x;
        startY = y;
        this.guiManager = guiManager;
        this.x = x;
        this.y = y;
        this.playerL = playerL;
        this.playerR = playerR;

        buildBall();
        reset();

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::movement, 0, Settings.gameSpeed, TimeUnit.MILLISECONDS);
    }

    //region BALL BUILDER

    /**
     * Builds a new Ball
     */
    private void buildBall(){
        ball = guiManager.buildBall();
        ball.setLocation(x,y);
        guiManager.add(ball);
        border = guiManager.getBounds();
    }
    //endregion

    //region MOVEMENT

    /**
     * Moves the Ball and watches for collision
     */
    private void movement() {
        move();
        borderCollision();
        playerCollision();
    }

    /**
     * Moves the ball
     */
    private void move() {
        x += velX;
        y += velY;
        ball.setLocation(x, y);
    }

    //endregion

    //region COLLISION
    /**
     * Border Collision
     */
    private void borderCollision(){
        float maxX = (float) border.getMaxX() - 128;
        float minX = (float) border.getMinX() - 128;
        float maxY = (float) border.getMaxY() - 200;
        float minY = (float) border.getMinY() - 128;

        if(y <= minY || y >= maxY) velY = -velY;
        else if(x < minX || x > maxX){
            //Right
            if(velX > 0) {
                guiManager.addScoresL();
                if(aiR != null){
                    aiR.backPropagation(y);
                    aiR.updateAccuracy(false);
                }
                reset();
                //Left
            }else if(velX < 0){
                guiManager.addScoresR();
                if(aiL != null){
                    aiL.backPropagation(y);
                    aiL.updateAccuracy(false);
                }
                reset();
            }
        }
    }

    /**
     * Player Collision
     */
    private void playerCollision() {
        if((collidedWithPlayer(playerL) || collidedWithPlayer(playerR)) && justCollided < 0) {
            justCollided = 30;
            if(velX > 0) {
                if(aiR != null){
                    aiR.backPropagation(y);
                    aiR.updateAccuracy(true);
                }

            }else if(velX < 0){
                if(aiL != null){
                    aiL.backPropagation(y);
                    aiL.updateAccuracy(true);
                }
            }
            int temp = velX;
            setRandVel();
            if(temp > 0 && velX > 0 || temp < 0 && velX < 0) velX = -velX;
        }else{
            justCollided--;
        }

    }

    /**
     * Checks Player Collision
     * @return True if it hit a player
     */
    private Boolean collidedWithPlayer(Player player) {
        int width = 10;
        int height = 120;
        return (x > player.getX() - width && x < player.getX() + width && y < player.getY() + height && y > player.getY() - height);
    }

    //endregion

    //region RESET
    /**
     * Resets the ball
     */
    private void reset() {
        x = startX;
        y = startY;
        setRandVel();
    }

    /**
     * Sets the Balls velocity to random
     */
    private void setRandVel(){
        int minVel = 2;
        int maxVel = 5-minVel;
        int vel = (int) (Math.random()*maxVel+minVel);

        if(Math.random() > .5f) velY = -vel;
        else velY = vel;
        vel = (int) (Math.random()*maxVel + minVel);
        if(Math.random() > .5f) velX = vel;
        else velX = -vel;
    }
    //endregion

    //region GETTER & SETTER

    /**
     * @return X Velocity
     */
    public int getVelX() {
        return velX;
    }

    /**
     * @return Y Velocity
     */
    public int getVelY() {
        return velY;
    }

    /**
     * @return X Position
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y Position
     */
    public int getY() {
        return y;
    }

    public void setAIL(AI aiL){
        this.aiL = aiL;
    }

    public void setAIR(AI aiR){
        this.aiR = aiR;
    }
    //endregion


}
