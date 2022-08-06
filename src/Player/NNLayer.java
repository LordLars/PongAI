package Player;

import Main.Settings;

public class NNLayer {

    private float[][] weights;
    private float[] biases;
    private final float[] neurons;
    private int neuronCount;


    private int preNeuronCount;
    private float[] preNeurons;
    private float[] nextChain;


    private float[] chain;
    private final float lernRate;

    private final boolean isLastLayer;


    public NNLayer(int pPreNeuronCount, int pNeuronCount, boolean pIsLastLayer) {
        preNeuronCount = pPreNeuronCount;
        neuronCount = pNeuronCount;
        weights = new float[pNeuronCount][preNeuronCount];
        preNeurons = new float[preNeuronCount];
        neurons = new float[pNeuronCount];
        biases = new float[pNeuronCount];
        lernRate = Settings.nLernRate;
        isLastLayer = pIsLastLayer;
        buildLayer();
    }

    //region NN LAYER BUILDER
    /**
     * Builds a new Layer with random weights and biases
     */
    private void buildLayer() {
        for(int i = 0; i < neuronCount; i++) {
            for(int j = 0; j < preNeuronCount; j++) {
                weights[i][j] = (float) (Math.random() * 2 - 1);
            }
        }
        for(int i = 0; i < neuronCount; i++) {
            biases[i] = (float) (Math.random() * 2 - 1);
        }
    }
    //endregion

    //region PROPAGATION

    /**
     * Propagation method
     */
    public void propagation() {
        for(int i = 0; i < neuronCount; i++) {
            neurons[i] = reLU(add(i) + biases[i]);
        }
    }

    /**
     * Adds up all products of the weights and previous neurons
     * @param index index of the Neuron
     * @return sum
     */
    private float add(int index) {
        float sum = 0;
        for(int j = 0; j < preNeurons.length; j++) {
            sum += preNeurons[j] * weights[index][j];
        }
        return sum;
    }

    //endregion

    //region ACTIVATION

    /**
     * Activation Function: ReLU
     * @param neuron value to be calculated
     * @return result of the ReLU function
     */
    private float reLU(float neuron)
    {
        return Math.max(0,neuron * .1f);
        //return (float) (1 / (1 + Math.exp(-neuron)));
    }

    /**
     * Activation function: Derivative of ReLU
     * @param neuron value to be calculated
     * @return result of the derivative of the ReLU function
     */
    private float reLUDerivative(float neuron)
    {
        //return (sigMoid(neuron) * (1- sigMoid(neuron)));
        if(neuron < 0) return 0;
        else return .1f;
    }

    //endregion

    //region BACK - PROPAGATION

    /**
     * Backpropagation method
     * @param targets aims of the AI
     */
    public void backPropagation(float[] targets) {
        updateChain(targets);
        updateWeights();
        updateBiases();
    }

    /**
     * Updates the weights with the backpropagation method
     */
    private void updateWeights(){
        for(int i = 0; i < neuronCount; i++) {
            for(int j = 0; j < preNeuronCount; j++) {
                float preWeight = weights[i][j];
                float weightChain = chain[i] * preNeurons[j];
                weights[i][j] = preWeight - weightChain * lernRate;
            }
        }
    }

    /**
     * Updates the biases with the backpropagation method
     */
    private void updateBiases(){
        for(int i = 0; i < neuronCount; i++) {
            float preBias = biases[i];
            float biasChain = chain[i];
            biases[i] = preBias - biasChain * lernRate;
        }
    }

    /**
     * Updates the Chain Rule
     * @param targets aims of the AI
     */
    private void updateChain(float[] targets) {
        chain = new float[neuronCount];
        nextChain = new float[preNeuronCount];

        if(isLastLayer) {
            //"targets" are the aims of each neuron
            for(int i = 0; i < neuronCount; i++) {
                chain[i] = 2*(neurons[i] - targets[i]) * reLUDerivative(add(i) + biases[i]);
            }
        }else {
            //"targets" are the values of the previous Chain Rule
            for(int i = 0; i < neuronCount; i++) {
                chain[i] = targets[i] * reLUDerivative(add(i) + biases[i]);
            }
        }

        for(int i = 0; i < preNeuronCount; i++) {
            float chainSum = 0;
            for(int j = 0; j < neuronCount; j++) {
                chainSum += chain[j] * weights[j][i];
            }
            nextChain[i] = chainSum;
        }
    }

    //endregion

    //region GETTER & SETTER

    /**
     * Updates the previous Neuron values
     * @param pPreNeurons new previous Neuron values
     */
    public void setPreNeurons(float[] pPreNeurons) {
        preNeurons = pPreNeurons;
    }

    /**
     * @return Neurons
     */
    public float[] getNeurons() {
        return neurons;
    }

    /**
     * @return Values of the next Chain
     */
    public float[] getNextChain() {
        return nextChain;
    }

    //endregion

    //region SAVE & LOAD

    /**
     * Saves the values of the layer
     * @return Data of the layer
     */
    public NNLayerData save(){
        NNLayerData daten = new NNLayerData();
        daten.biases = biases;
        daten.weights = weights;
        daten.neuronCount = neuronCount;
        daten.preNeuronCount = preNeuronCount;
        return daten;
    }

    /**
     * Loads the saved Data
     * @param daten saved Data
     */
    public void load(NNLayerData daten){
        biases = daten.biases;
        weights = daten.weights;
        neuronCount = daten.neuronCount;
        preNeuronCount = daten.preNeuronCount;
    }

    //endregion
}
