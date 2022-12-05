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

    public Matrix feedForward(double[] xInput) {
        Matrix input = Matrix.fromArray(xInput);

        //input to hidden
        hiddens.add(Matrix.multiply(weights.get(0), input));
        hiddens.get(0).add(biases.get(0));
        hiddens.get(0).sigmoid();

        //hidden to output
        output = Matrix.multiply(weights.get(1), hiddens.get(0));
        output.add(biases.get(1));
        output.sigmoid();

        return output;
    }

    public void backwardPropagation(Matrix output, double [] expectedResultY, double learningRate) {
        Matrix expectedResult = Matrix.fromArray(expectedResultY);
        Matrix error = Matrix.subtract(expectedResult, output);

        Matrix biases1Gradient = output.dsigmoid();

    }

    public void fit(int epochs, double learningRate, double[][] traingingDataX,
                      double[][] traingingDataY) {
        for (int i = 0; i < epochs; i++) {
            hiddens.clear();
            double error = 0;
            int sampleNumber = (int)(Math.random() * traingingDataX.length);
            System.out.println("INFO: EPOCH: " + i + 1 + " ERROR: " + error);
        }
    }

    public static void main(String[] args) {

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
