package Player;

import java.io.*;
import java.util.Arrays;

public class NN {

    private NNLayer[] layers;
    private final Action[] outPutActions;

    private float[] outputs;
    private float accuracy;
    private int guesses;
    private int rightGuesses;
    private final int[] layerSizes;

    private final ActivationFunction activationFunction;
    private final float nLernRate;

    public NN(int[] layerSizes, Action[] oOptions, ActivationType activation, float nLernRate) {
        this.layerSizes = layerSizes;
        this.outPutActions = oOptions;
        this.nLernRate = nLernRate;
        activationFunction = new ActivationFunction(activation);
        buildLayers();
    }

    //region LAYER BUILDER
    /**
     * Builds Layers
     */
    public void buildLayers() {
        layers = new NNLayer[layerSizes.length-1];
        for(int i = 0; i < layers.length; i++) {
            layers[i] = new NNLayer(layerSizes[i], layerSizes[i+1],activationFunction, nLernRate);
        }
    }
    //endregion

    //region PROPAGATION
    /**
     * Propagation Method
     */
    public float[] propagation(float[] inputs) {
        for(int i = 0; i < layers.length-1; i++){
            inputs = layers[i].propagation(inputs);
        }
        return outputs = layers[layers.length-1].propagation(inputs);
    }
    //endregion

    //region BACKPROPAGATION
    /**
     * Backpropagation method
     * @param target targets of the AI
     */
    public void backPropagation(Action target, boolean hit) {
        float[] targets = new float[outputs.length];
        int maxIndex = 0;
        for(int i = 1; i < outputs.length; i++) if(outputs[i] > outputs[maxIndex]) maxIndex = i;
        updateAccuracy(hit);
        targets[Arrays.asList(outPutActions).indexOf(target)] = 1;
        for(int i = 0; i < outputs.length; i++) targets[i] = 2*(outputs[i] - targets[i]);

        layers[layers.length - 1].backPropagation(targets);
        for(int i = layers.length - 2; i >= 0; i--) layers[i].backPropagation(layers[i + 1].getNextChain());
    }

    /**
     * Updates the Accuracy
     * @param hit if the player hit the Ball
     */
    private void updateAccuracy(boolean hit){
        guesses += 1;
        if(hit) rightGuesses += 1;
        accuracy = (float) rightGuesses / guesses * 100;
    }

    //endregion

    //region SAVE & LOAD

    /**
     * Saves the AI
     */
    public void save(String path) {
        if(accuracy > getSavedAccuracy(path)){
            try {
                FileOutputStream fos = new FileOutputStream(path);
                BufferedOutputStream bf = new BufferedOutputStream(fos);
                ObjectOutputStream obj = new ObjectOutputStream(bf);
                NNData data = new NNData();
                NNLayerData[] layerData = new NNLayerData[layers.length];
                for(int i = 0; i < layers.length; i++) layerData[i] = layers[i].save();
                data.nnLayerData = layerData;
                data.accuracy = accuracy;
                obj.writeObject(data);
                obj.close();
            } catch (IOException ignored) {}
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
            for(int i = 0; i < layers.length; i++) layers[i].load(data.nnLayerData[i]);
        } catch (IOException | ClassNotFoundException ignored){}
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

        } catch (IOException | ClassNotFoundException ignored) {}
        return 0;
    }

    /**
     * @return Output with the highest value
     */
    public Action getOutput(){
        int maxIndex = 0;
        for(int i = 1; i < outputs.length;i++) if(outputs[i] > outputs[maxIndex]) maxIndex = i;
        return outPutActions[maxIndex];
    }

    /**
     * @return Current accuracy
     */
    public float getCurrentAccuracy(){
        return accuracy;
    }

    //endregion
}
