import java.util.function.*;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

public class Dense extends Layer {
    private Matrix weights;
    private Matrix biases;
    private List<Layer> inputLayers;
    private Function<Float, Float> activation;
    private Function<Float, Float> activationDerivative;
    private boolean initialized = false;

    public Dense(int nodes, String activation) {
        this.nodes = nodes;
        this.inputLayers = new ArrayList<Layer>();

        switch (activation) {
            case "sigmoid":
                this.activation = x -> (float)(1 / (1 + Math.exp(-x))); 
                this.activationDerivative = x -> x * (1 - x);
                break;
            case "tanh":
                this.activation = x -> (float)Math.tanh(x); 
                this.activationDerivative = x -> 1 - x * x;
                break;
            case "relu":
                this.activation = x -> (float)Math.max(0, x); 
                this.activationDerivative = x -> (x > 0 ? 1f : 0f);
                break;
            case "leaky_relu":
                this.activation = x -> (float)Math.max(0.01 * x, x); 
                this.activationDerivative = x -> (x > 0 ? 1f : 0.01f);
                break;
            case "identity":
                this.activation = x -> x; 
                this.activationDerivative = x -> 1f;
                break;
            default:
                throw new RuntimeException("The activation function " + activation + " doesn't exist.");
        }
    }

    public void addInput(Layer input) {
        this.inputLayers.add(input);
    }

    @Override
    public void initializeWeights() {
        if (!initialized) {
            initialized = true;

            int inputNodes = 0;
            for (Layer input : inputLayers) {
                input.initializeWeights();
                inputNodes += input.nodes;
            }
            final int totalNodes = inputNodes + nodes;

            // Xavier weight distribution
            Function<Float, Float> dist = x -> (float)(Math.random() * 2 / Math.sqrt(totalNodes) - 1 / Math.sqrt(totalNodes));

            weights = new Matrix(nodes, inputNodes);
            weights.map(dist);

            biases = new Matrix(nodes, 1);
            biases.map(dist);
        }
    }

    @Override
    public Matrix feedforward() {
        if (!initialized) {
            throw new RuntimeException("Layer not initialized.");
        }
        
        values = new Matrix(0, 1);
        for (Layer layer : inputLayers) {
            values = Matrix.vertCat(values, layer.feedforward());
        }
        values = Matrix.multiply(weights, values);
        values.add(biases);
        values.map(activation);

        return values;
    }

    @Override
    public void backpropagate(Matrix dc_da, float learningRate) {
        Matrix dz_dw = new Matrix(0, 1);
        for (Layer layer : inputLayers) {
            dz_dw = Matrix.vertCat(dz_dw, layer.values);
        }
        dz_dw = Matrix.transpose(dz_dw);

        Matrix da_dz = Matrix.map(values, activationDerivative);
        Matrix dc_dz = Matrix.elementMultiply(da_dz, dc_da);
        dc_dz.multiply(learningRate);
        
        Matrix dc_dw = Matrix.multiply(dc_dz, dz_dw);

        if (dc_dz.check(x -> Math.abs(x) > 1) || dc_dw.check(x -> Math.abs(x) > 1)) {
            System.out.println("Too big!");
        }

        weights.subtract(dc_dw);
        biases.subtract(dc_dz);
        
        int rows = 0;
        Matrix transposedWeights = Matrix.transpose(weights);
        for (Layer layer : inputLayers) {
            Matrix clippedWeights = Matrix.vertClip(transposedWeights, rows, layer.nodes);
            rows += layer.nodes;
            Matrix dc_da0 = Matrix.multiply(clippedWeights, Matrix.elementMultiply(da_dz, dc_da));
            layer.backpropagate(dc_da0, learningRate);
        }
    }

    @Override
    public Matrix getValues() {
        return values;
    };
}