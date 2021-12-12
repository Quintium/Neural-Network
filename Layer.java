public abstract class Layer {
    protected int nodes;
    protected Matrix values;

    public abstract void initializeWeights();
    public abstract Matrix feedforward();
    public abstract void backpropagate(Matrix dc_da, float learningRate);
    public abstract Matrix getValues();
}