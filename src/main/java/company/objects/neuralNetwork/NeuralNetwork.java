package company.objects.neuralNetwork;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NeuralNetwork {
    Matrix input;
    ArrayList<Matrix> weights = new ArrayList<>();
    ArrayList<Matrix> biases = new ArrayList<>();
    ArrayList<Matrix> hiddens = new ArrayList<>();
    Matrix output;

    double[][] X;
    double[][] Y;
    //for each loop of the training process the result of each
    //hidden layer needs to be used to calculate the amounts each
    //weight matrix needs to change by


    public ArrayList<Matrix> getWeights() {
        return weights;
    }

    public ArrayList<Matrix> getBiases() {
        return biases;
    }

    public double[][] getX() {
        return X;
    }

    public double[][] getY() {
        return Y;
    }

    public NeuralNetwork(int inputSize, int hiddenAmount, int hiddenSize,
                         int outputSize, double[][] X, double[][] Y) {
        this.weights.add(new Matrix(hiddenSize, inputSize, "r"));
        this.weights.add(new Matrix(outputSize, hiddenSize, "r"));

        this.biases.add(new Matrix(hiddenSize, 1, "r"));
        this.biases.add(new Matrix(outputSize, 1, "r"));

        this.X = X;
        this.Y = Y;
        //add iteration here to define the layers
    }

    public NeuralNetwork(int inputSize, int hiddenAmount, int hiddenSize,
                         int outputSize) {
        this.weights.add(new Matrix(hiddenSize, inputSize, "r"));
        this.weights.add(new Matrix(outputSize, hiddenSize, "r"));

        this.biases.add(new Matrix(hiddenSize, 1, "r"));
        this.biases.add(new Matrix(outputSize, 1, "r"));

        getTrainingData();
        //add iteration here to define the layers
    }

    public void getTrainingData() {
        ArrayList<double[]> XDynamic = new ArrayList<>();
        ArrayList<double[]> YDynamic = new ArrayList<>();

        try {
            File trainingData = new File("src/main/java/company/objects/neuralNetwork/trainingDataGeneration/trainingData");
            Scanner fileReader = new Scanner(trainingData);

            int arraySize = 0;
            boolean XReading = true;
            int lineNumber = 1;
            while (fileReader.hasNextLine()) {
                if (lineNumber == 1) {
                    arraySize = fileReader.nextInt();
                } else {
                    System.out.println("line number: " + lineNumber);
                    lineNumber++;
                    String data = fileReader.nextLine();
                    System.out.println("Data: " + data);
                    if (data.equals("Y")) {
                        XReading = false;
                    } else {
                        String[] dataLine = data.split(",");
                        double[] arrayToEnter = new double[arraySize];
                        for (int i = 0; i < dataLine.length; i++) {
                            arrayToEnter[Integer.parseInt(dataLine[i])] = 1;
                        }
                        if (XReading) {
                            System.out.println(Arrays.toString(arrayToEnter));
                            XDynamic.add(arrayToEnter);
                        } else {
                            System.out.println(Arrays.toString(arrayToEnter));
                            YDynamic.add(arrayToEnter);
                        }
                    }
                }
                lineNumber++;
            }
        } catch (Exception exception) {
            System.out.println("There was a file error");
            exception.printStackTrace();
        }
        System.out.println("Data formatted");
        System.out.println("X");
        for (int i = 0; i < XDynamic.size(); i++) {
            System.out.println(Arrays.toString(XDynamic.get(i)));
        }
        System.out.println("");
        System.out.println("Y");
        for (int i = 0; i < YDynamic.size(); i++) {
            System.out.println(Arrays.toString(YDynamic.get(i)));
        }
        double[][] X = new double[XDynamic.size()][];
        double[][] Y = new double[YDynamic.size()][];

        for (int i = 0; i < XDynamic.size(); i++) {
            X[i] = XDynamic.get(i);
            Y[i] = YDynamic.get(i);
        }
        this.X = X;
        this.Y = Y;
    }

    public void readFile() throws FileNotFoundException {
        File trainingData = new File("src/main/java/company/objects/neuralNetwork/trainingDataGeneration/trainingData");
        Scanner reader = new Scanner(trainingData);

        while (reader.hasNext()) {
            System.out.println(reader.nextLine());
        }
    }

    public NeuralNetwork(Matrix inputToHiddenWeights, Matrix inputToHiddenBiases,
                         Matrix hiddenToOuputWeights, Matrix hiddenToOutputBiases) {
        this.weights.add(inputToHiddenWeights);
        this.weights.add(hiddenToOuputWeights);

        this.biases.add(inputToHiddenBiases);
        this.biases.add(hiddenToOutputBiases);
    }

    private Matrix feedForward(double[] xInput) {
        Matrix input = Matrix.fromArray(xInput);

        this.input = input;

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

    private void backwardPropagation(Matrix output, double[] expectedResultY,
                                     double learningRate) {
        Matrix expectedResult = Matrix.fromArray(expectedResultY);
        Matrix error = Matrix.subtract(expectedResult, output);

        //calculates the gradient for biase 1
        Matrix biases1Gradient = output.dsigmoid();
        biases1Gradient.multiply(error);
        biases1Gradient.multiply(learningRate);

        //calculates the gradient for weight 1
        Matrix hiddenTransposed = this.hiddens.get(0);
        Matrix deltaWeight1 = Matrix.multiply(biases1Gradient, hiddenTransposed);

        //adds the gradient to the weight 1 and biase 1
        this.weights.get(1).add(deltaWeight1.toArray().get(0));
        this.biases.get(1).add(biases1Gradient);

        //calculates hidden errors
        Matrix weight1Transposed = Matrix.transpose(this.weights.get(1));
        Matrix hiddenErrors = Matrix.multiply(weight1Transposed, error);

        //calculates the gradient for biase 0
        Matrix hiddenGradient = this.hiddens.get(0).dsigmoid();
        hiddenGradient.multiply(hiddenErrors);
        hiddenGradient.multiply(learningRate);

        //calulates gradient for weight 0
        Matrix inputTransposed = Matrix.transpose(this.input);
        Matrix deltaWeight0 = Matrix.multiply(hiddenGradient, inputTransposed);

        //adds the gradient to the weight 0 and biase 0
        this.weights.get(0).add(deltaWeight0);
        this.biases.get(0).add(hiddenGradient);

    }

    public void fit(int epochs, double learningRate) {
        Scanner input = new Scanner(System.in);
        System.out.println("Fit with diagnostic information? [Y/N]");
        boolean withDiagnositic = false;
        if (input.next().equalsIgnoreCase("Y")) {
            withDiagnositic = true;
        }
        for (int i = 0; i < epochs; i++) {
            hiddens.clear();
            int sampleNumber = (int) (Math.random() * this.X.length);
            //does a training pass
            Matrix output = feedForward(this.X[sampleNumber]);
            backwardPropagation(output, this.Y[sampleNumber], learningRate);
            if (withDiagnositic) {
                System.out.println("INFO: EPOCH: " + i + 1 + " INPUT: " + Arrays.toString(this.X[sampleNumber]) + " OUTPUT: " + output.toString());
            }
        }
    }

    public List<Double> predict(double[] X) {
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(this.weights.get(0), input);
        hidden.add(this.biases.get(0));
        hidden.sigmoid();

        Matrix output = Matrix.multiply(this.weights.get(1), hidden);
        output.add(this.biases.get(1));
        output.sigmoid();

        return output.toArray();
    }

    public void saveNetwork(String filename) {
        File saveFile;
        try {
            saveFile = new File("src/main/java/company/objects/neuralNetwork/saveFiles/" + filename);
            System.out.println("Save File Created");
        } catch (Exception e1) {
            System.out.println("An error occured");
            e1.printStackTrace();
            saveFile = null;
        }
        try {
            assert saveFile != null;
            Writer fileWriter = new FileWriter(saveFile);
            for (int i = 0; i < this.weights.size(); i++) {
                String text = weights.get(i).toString();
                fileWriter.write(text);
            }
            System.out.println("Weights Written");
            for (int i = 0; i < this.biases.size(); i++) {
                String text = biases.get(i).toString();
                fileWriter.write(text);
            }
            System.out.println("Biases Written");
        } catch (Exception e2) {
            System.out.println("An error occured");
            e2.printStackTrace();
        }
        //idk why this wont work, will find out on wednesday
    }

    public static void main(String[] args) throws FileNotFoundException {
        double[][] X = {
                {0, 0},
                {1, 0},
                {0, 1},
                {1, 1}
        };
        double[][] Y = {
                {0}, {1}, {1}, {0}
        };/*
        NeuralNetwork nn = new NeuralNetwork(2, 0, 10, 1, X, Y);
        nn.fit(50000, 0.1);

        System.out.println(Arrays.toString(X[2]));
        System.out.println(nn.predict(X[2]));
        ArrayList<Matrix> weights = nn.getWeights();
        ArrayList<Matrix> biases = nn.getBiases();

        for (int i = 0; i < weights.size(); i++) {
            System.out.println("Weight " + i);
            System.out.println(weights.get(i));
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < biases.size(); i++) {
            System.out.println("Biase " + i);
            System.out.println(biases.get(i));
            System.out.println();
        }

        nn.saveNetwork("testSave");*/
        NeuralNetwork nn = new NeuralNetwork(2, 0, 10, 1, X, Y);

        nn.getTrainingData();
        System.out.println(Arrays.deepToString(nn.getX()));
        System.out.println(Arrays.deepToString(nn.getY()));

        nn.readFile();
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