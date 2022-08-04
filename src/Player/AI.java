package Player;

import Main.Action;
import Main.Settings;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AI{

    private NN neuralNetwork;
    private final Ball ball;
    private final Player player;
    private final String name;
    private final boolean withOutput;
    private final boolean isLeft;


    private final int maxAttemptCount;
    private final int maxLearningAttempts;
    private float currentAttemptCount;
    private int currentLearningAttempts;
    private float hitCount;
    private float accuracy;



    public AI(Ball ball, Player player, boolean withOutput, String name, boolean isLeft) {
        this.ball = ball;
        this.player = player;
        this.withOutput = withOutput;
        this.name = name;
        this.isLeft = isLeft;

        maxAttemptCount = Settings.maxAttemptCount;
        maxLearningAttempts = Settings.maxLearningAttempts;
        currentAttemptCount = 0;
        hitCount = 0;
        accuracy = 0;

        buildNN();

        if(!Settings.lernen) load();
        if(isLeft) System.out.println("left: " + savedAccuracy() + "%");
        else System.out.println("right: " + savedAccuracy()+ "%");

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::propagation, 0, Settings.gameSpeed, TimeUnit.MILLISECONDS);
    }

    //region AI BUILDER

    /**
     * Builds the Neural Network
     */
    private void buildNN(){
        neuralNetwork = new NN(withOutput);
        neuralNetwork.setInput(getInput());
        neuralNetwork.buildLayers();
    }
    //endregion

    //region PROPAGATION

    /**
     * Berechnet die Neuronen
     */
    private void propagation(){
        neuralNetwork.setInput(getInput());
        neuralNetwork.propagation();
        move();
    }

    /**
     * Moves the player
     */
    private void move() {
        Action output = neuralNetwork.getOutput();
        if(output.equals(Action.Up)) player.moveUp();
        else if(output.equals(Action.Down)) player.moveDown();
    }

    //endregion

    //region BACK - PROPAGATION


    /**
     * Backpropagation method
     */
    public void backPropagation(int y){
        if(Settings.lernen) neuralNetwork.backPropagation(y, player.getY());
    }

    /**
     * Updates the accuracy
     */
    public void updateAccuracy(boolean hit) {
        currentAttemptCount += 1;
        if(hit) hitCount += 1;
        accuracy = 100 * (hitCount / currentAttemptCount);
        if(currentAttemptCount > maxAttemptCount) {
            currentAttemptCount = 0;
            hitCount = 0;
            System.out.println("\n" + name + ":\nAccuracy: " + accuracy + " , saved: " + savedAccuracy() + " , learnCount " + currentLearningAttempts + " ---------------------------------------------");
            save();
            currentLearningAttempts += 1;
            if(currentLearningAttempts >= maxLearningAttempts) {
                load();
                currentLearningAttempts = 0;
            }
        }
    }

    //endregion

    //region SAVE & LOAD

    /**
     * Saves the AI
     */
    private void save() {
        neuralNetwork.save(accuracy,getPath());
    }

    /**
     * Loads the AI
     */
    private void load() {
        neuralNetwork.load(getPath());
    }

    //endregion

    //region GETTER & SETTER

    /**
     * Updates the input
     * @return input values
     */
    private float[] getInput() {
        float velX = ball.getVelX();
        float velY = ball.getVelY();
        float ballX = ball.getX();
        float ballY = ball.getY();
        float playerX = player.getX();
        float playerY = player.getY();

        return new float[] {velX, velY,ballX,ballY, playerX, playerY};
    }

    /**
     * @return savedAccuracy
     */
    private float savedAccuracy(){
        return neuralNetwork.getSavedAccuracy(getPath());
    }

    /**
     * @return save path
     */
    private String getPath(){
        String path = "Saves//RightAI.dat";
        if(isLeft) path = "Saves//LeftAI.dat";
        return path;
    }

    //endregion

}
