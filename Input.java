public class Input extends Layer {
    public Input(int nodes) {
        this.nodes = nodes;
    }

    @Override
    public void initializeWeights() {

    }

    @Override
    public Matrix feedforward() {
        return values;
    }

    public void setInput(float[] input) {
        if (input.length != nodes) {
            throw new RuntimeException("Input must have the same number of elements as the input layer.");
        }
        this.values = new Matrix(input);
    }

    @Override
    public void backpropagate(Matrix dc_da, float learningRate) {

    }

    @Override
    public Matrix getValues() {
        return values;
    };
}
