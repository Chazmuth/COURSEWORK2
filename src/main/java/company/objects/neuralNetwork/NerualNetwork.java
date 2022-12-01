package company.objects.neuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class NerualNetwork {
    Matrix input;
    ArrayList<Matrix> weights = new ArrayList<>();
    ArrayList<Matrix> biases = new ArrayList<>();

    ArrayList<Matrix> hiddens = new ArrayList<>();

    Matrix output;
    //for each loop of the training process the result of each
    //hidden layer needs to be used to calculate the amounts each
    //weight matrix needs to change by

    public NerualNetwork(int inputSize, int hiddenAmount, int hiddenSize, int outputSize) {
        weights.add(new Matrix(hiddenSize, inputSize, "r"));
        weights.add(new Matrix(outputSize, hiddenSize, "r"));

        biases.add(new Matrix(hiddenSize, 1, "r"));
        biases.add(new Matrix(outputSize, 1, "r"));
        //add iteration here to define the layers
    }

    public List<Double> feedForward(double[] xInput) {
        Matrix input = Matrix.fromArray(xInput);
        hiddens.add(Matrix.multiply(weights.get(0), input));
        hiddens.get(0).add(biases.get(0));
        hiddens.get(0).sigmoid();

        output = Matrix.multiply(weights.get(1), hiddens.get(0));
        output.add(biases.get(1));
        output.sigmoid();

        return output.toArray();
    }

    public void backwardPropagation(Matrix result, Matrix expectedResult, double learningRate) {

    }

    public void train(int epochs, double learningRate, double[][] traingingDataX,
                      double[][] traingingDataY) {
        for (int i = 0; i < epochs; i++) {
            hiddens.clear();
            double error = 0;
            int sampleNumber = (int)(Math.random() * traingingDataX)
            System.out.println("INFO: EPOCH: " + i + 1 + " ERROR: " + error);
        }
    }

    public static void main(String[] args) {
        NerualNetwork net = new NerualNetwork(2, 0, 4, 1);

        ArrayList<Matrix> traingingDataX = new ArrayList<>();
        Matrix xin = Matrix.fromArray(new double[]{0, 0});
        traingingDataX.add(xin);
        xin = Matrix.fromArray(new double[]{0, 1});
        traingingDataX.add(xin);
        xin = Matrix.fromArray(new double[]{1, 0});
        traingingDataX.add(xin);
        xin = Matrix.fromArray(new double[]{1, 1});
        traingingDataX.add(xin);

        ArrayList<Matrix> traingingDataY = new ArrayList<>();
        Matrix yin = Matrix.fromArray(new double[]{0});
        traingingDataY.add(yin);
        yin = Matrix.fromArray(new double[]{1});
        traingingDataY.add(yin);
        yin = Matrix.fromArray(new double[]{1});
        traingingDataY.add(yin);
        yin = Matrix.fromArray(new double[]{0});
        traingingDataY.add(yin);

        net.train(10000, 0.5, traingingDataX, traingingDataY);
    }
}

//add a save function that saves the weights biases, layers, all thats needed
//to load in a trained neural network (and a constructor to train it and a
//function thats basically just feedforward to give an output from a trained network)

//expand the neural network so that one can control the size of each hidden layer
//and also add iteration that is based on the given size to all the fucntions so
//that the amount of layers isnt static

//the maths for this neural network (the most basic feedforward neural
//network) is from https://medium.com/swlh/mathematics-behind-basic-feed-forward-neural-network-3-layers-python-from-scratch-df88085c8049

