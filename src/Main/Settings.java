package Main;

import Player.Action;

public class Settings {

    public static boolean learn = false;
    public static int gameSpeed = 1;
    public static int aiCount = 1;
    public static boolean withInput = false;

    //How many pictures it can learn until gets saved
    public static int maxAttemptCount = 10000;
    //How many times it gets saved until it gets reset
    public static int maxSaveAttempts = 200;

    //region NEURAL NETWORK
    public static float nLernRate = .01f;
    public static int[] layerSizes = new int[]{6,16,16,3};
    public static Action[] output = new Action[]{Action.None,Action.Down,Action.Up};
    //endregion

}
