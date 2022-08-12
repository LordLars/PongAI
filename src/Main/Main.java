package Main;

import GUI.GUIManager;
import Player.Player;
import Player.AI;
import Player.Ball;

public class Main{

    public Main(){
        BuildAI();
    }

    private void BuildAI(){
        GUIManager guiManager = new GUIManager();
        Player playerL = new Player(guiManager,0,500,false);
        Player playerR = new Player(guiManager,1650,500, Settings.withInput);
        Ball ball = new Ball(guiManager,1000,500,playerL,playerR);

        AI aiL = new AI(ball,playerL,true);
        aiL.start();
        ball.setAIL(aiL);

        if(!Settings.withInput){
            AI aiR = new AI(ball,playerR,false);
            aiR.start();
            ball.setAIR(aiR);
        }

    }

}
