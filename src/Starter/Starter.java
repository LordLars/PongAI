package Starter;

import Main.Main;
import Main.Settings;

public class Starter {

    public static void main(String[] args) {
        for(int i = 0; i < Settings.aiCount; i++){
            new Main();
        }
    }
}
