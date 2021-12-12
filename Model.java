import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Model {
    private List<Input> inputLayers = null;
    private Layer outputLayer = null;

    public Model() {

    }

    public Model(Input[] inputLayers, Layer outputLayer) {
        this.inputLayers = Arrays.asList(inputLayers);
        this.outputLayer = outputLayer;
    }
    public Model(Input inputLayer, Layer outputLayer) {
        this.inputLayers = new ArrayList<Input>();
        this.inputLayers.add(inputLayer);
        this.outputLayer = outputLayer;
    }


    public void addLayer(Layer layer) {
        if (outputLayer == null) {
            if (!(layer instanceof Input)) {
                throw new RuntimeException("The first layer has to be an input layer.");
            }
            inputLayers = new ArrayList<Input>();
            inputLayers.add((Input)layer);
        } else {
            if (layer instanceof Input) {
                throw new RuntimeException("An input layer can only be the first layer.");
            } else if (layer instanceof Dense) {
                ((Dense)layer).addInput(outputLayer);
            }
        }

        outputLayer = layer;
    }

    public void initialize() {
        outputLayer.initializeWeights();
    }

    public float[] predict(float[][] inputs) {
        if (inputs.length != inputLayers.size()) {
            throw new RuntimeException("Count of input layers doesn't match count of inputs.");
        }
        for (int i = 0; i < inputs.length; i++) {
            inputLayers.get(i).setInput(inputs[i]);;
        }

        float[] result = outputLayer.feedforward().toArray();

        return result;
    }

    public float[] predict(float[] input) {
        return predict(new float[][]{input});
    }

    public float[][] predictBatch(float[][][] data) {
        float[][] result = new float[data.length][];
        for (int i = 0; i < data.length; i++) {
            result[i] = predict(data[i]);
        }
        return result;
    }

    public float[][] predictBatch(float[][] data) {
        float[][] result = new float[data.length][];
        for (int i = 0; i < data.length; i++) {
            result[i] = predict(data[i]);
        }
        return result;
    }

    public float calculateLoss(float[][][] input, float[][] expected) {
        float[][] result = predictBatch(input);

        float sum = 0;
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                sum += (float)Math.pow(result[i][j]- expected[i][j], 2);
            }
        }

        return sum / (result.length * result[0].length);
    }

    public float calculateLoss(float[][] input, float[][] expected) {
        float[][] result = predictBatch(input);

        float sum = 0;
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                sum += (float)Math.pow(result[i][j] - expected[i][j], 2);
            }
        }

        return sum / (result.length * result[0].length);
    }

    public void train(float learningRate, float[] input, float[] expected) {
        predict(input);
        Matrix dc_da = Matrix.subtract(outputLayer.getValues(), new Matrix(expected));
        dc_da.multiply(2);
        outputLayer.backpropagate(dc_da, learningRate);
    }
}