package Main;

public class Settings {

    public static boolean lernen = false;
    public static int gameSpeed = 1;
    public static int aiCount = 1;
    public static boolean withInput = false;

    //How many pictures it can learn until gets saved
    public static int maxAttemptCount = 400;
    //How many times it gets saved until it gets reset
    public static int maxLearningAttempts = 20;

    //region NEURAL NETWORK
    public static float nLernRate = .01f;
    public static int NNLayerCount = 2;
    public static int NNLayerLength = 16;
    //endregion

}
