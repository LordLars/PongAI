package Player;

import Main.Action;
import Main.Settings;

import java.io.*;

public class NN {

    private float[] input;
    private NNLayer[] layers;
    private final int NNLayerCount;
    private final int NNLayerLength;
    private final Action[] output;
    private final int outputLength;
    private final boolean withOutput;


    public NN(boolean withOutput) {
        NNLayerCount = Settings.NNLayerCount;
        NNLayerLength = Settings.NNLayerLength;
        output = new Action[]{Action.Up,Action.Down,Action.None};
        outputLength = output.length;
        this.withOutput = withOutput;
    }

    //region LAYER BUILDER
    /**
     * Builds Layers
     */
    public void buildLayers() {
        layers = new NNLayer[NNLayerCount + 2];
        layers[0] = new NNLayer(input.length, NNLayerLength, false);
        for(int i = 1; i < layers.length - 1; i++) {
            layers[i] = new NNLayer(NNLayerLength, NNLayerLength, false);
        }
        layers[layers.length - 1] = new NNLayer(NNLayerLength, outputLength, true);
    }
    //endregion

    //region PROPAGATION


    /**
     * Aktualisiert die Eingabe
     * @param pEingabe ist die aktualisierte Eingabe
     */
    public void setInput(float[] pEingabe){
        input = pEingabe;
    }

    /**
     * Aktualisiert die Werte der Neuronen
     */
    public void propagation() {
        layers[0].setPreNeurons(input);
        layers[0].propagation();
        for(int i = 1; i < layers.length; i++) {
            layers[i].setPreNeurons(layers[i-1].getNeurons());
            layers[i].propagation();
        }
    }


    //endregion

    //region BACKPROPAGATION
    /**
     * Backpropagation method
     */
    public void backPropagation(int ballY, float playerY) {
        float[] targets = new float[outputLength];
        int max = 50; //distance to the center of the player the smaller, the more precise the AI gets
        if(ballY < playerY - max) targets[0] = 1;
        else if(ballY > playerY + max) targets[1] = 1;
        else targets[2] = 1;
        if(withOutput) output(targets);

        layers[layers.length - 1].backPropagation(targets);
        for(int i = layers.length - 2; i >= 0; i--) {
            layers[i].backPropagation(layers[i + 1].getNextChain());
        }
    }

    //endregion

    //region SAVE & LOAD

    /**
     * Saves the AI
     */
    public void save(float accuracy, String path) {
        if(accuracy > getSavedAccuracy(path)){
            try {
                FileOutputStream fos = new FileOutputStream(path);
                BufferedOutputStream bf = new BufferedOutputStream(fos);
                ObjectOutputStream obj = new ObjectOutputStream(bf);

                NNData data = new NNData();
                NNLayerData[] layerData = new NNLayerData[layers.length];
                for(int i = 0; i < layers.length; i++){
                    layerData[i] = layers[i].save();
                }
                data.nnLayerData = layerData;
                data.accuracy = accuracy;
                obj.writeObject(data);
                obj.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads the AI
     */
    public void load(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            NNData data = (NNData) ois.readObject();
            for(int i = 0; i < layers.length; i++){
                layers[i].load(data.nnLayerData[i]);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //endregion

    //region GETTER & SETTER

    /**
     * @return Saved accuracy
     */
    public float getSavedAccuracy(String path){
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            NNData data = (NNData) ois.readObject();
            return data.accuracy;

        } catch (IOException | ClassNotFoundException ignored) {

        }
        return 0;
    }


    /**
     * @return Output of the AI
     */
    public Action getOutput(){
        float[] output = layers[layers.length - 1].getNeurons();
        int maxIndex = 0;
        for(int i = 1; i < output.length;i++) {
            if(output[i] > output[maxIndex]) maxIndex = i;
        }
        return this.output[maxIndex];
    }

    /**
     * Output of the last Layer
     * @param targets aims of the AI
     */
    private void output(float[] targets) {
        float[] neuronen = layers[layers.length-1].getNeurons();
        for(int i = 0; i < neuronen.length;i++) {
            System.out.println("geschätzt: " + neuronen[i] + ", ziel: " + targets[i]);
        }
    }

    //endregion
}
