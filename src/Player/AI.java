package Player;

import Main.Settings;

public class AI extends Thread {

    private final NN nn;
    private final Ball ball;
    private final Player player;
    private final boolean isLeft;
    private float currentAttemptCount;
    private final String path;

    public AI(Ball ball, Player player, boolean isLeft) {
        this.ball = ball;
        this.player = player;
        this.isLeft = isLeft;

        path = (isLeft) ? "Saves//LeftAI.dat" : "Saves//RightAI.dat";

        nn = new NN(Settings.layerSizes,Settings.output,ActivationType.ReLU, Settings.nLernRate);
        if(isLeft) System.out.println("Left: " +nn.getSavedAccuracy(path));
        else System.out.println("Right: " +nn.getSavedAccuracy(path));
        if(!Settings.learn)nn.load(path);
    }

    @Override
    public void run() {
        while(true){
            float[] input = new float[]{ball.getVelX(),ball.getVelY(),ball.getX(),ball.getY(), player.getX(), player.getY()};
            nn.propagation(input);
            if(nn.getOutput().equals(Action.Up)) player.moveUp();
            else if(nn.getOutput().equals(Action.Down)) player.moveDown();

            try { Thread.sleep(Settings.gameSpeed);}
            catch (InterruptedException ignored) {}
        }
    }


    //region BACK - PROPAGATION

    public void backPropagation(int y, boolean hit){
        Action action = Action.None;
        int distance = 50;
        if(y - distance > player.getY()) action = Action.Down;
        else if(y + distance < player.getY()) action = Action.Up;
        nn.backPropagation(action,hit);
        currentAttemptCount++;
        if(currentAttemptCount % Settings.maxSaveAttempts == 0){
            if(isLeft) System.out.println("Left Save: " + nn.getSavedAccuracy(path));
            else System.out.println("Right Save: " + nn.getSavedAccuracy(path));
            nn.save(path);
        }
        if(currentAttemptCount >= Settings.maxAttemptCount){
            if ((Math.random() > .5f)) nn.load(path);
            else reset();
        }
    }
    //endregion

    //region GETTER & SETTER

    /**
     * Resets the AI
     */
    private void reset(){
        nn.buildLayers();
        currentAttemptCount = 0;
    }

    //endregion

}
