package company.objects.neuralNetwork.trainingDataGeneration;

import company.objects.graph.Edge;
import company.objects.graph.Graph;
import company.objects.graph.Path;
import company.objects.graph.Vertex;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static company.DBUtils.SQLFunctions.readGraph;

public class DijkstraShortestPath {

    public static ArrayList<RoutingVertex> unvisited = new ArrayList<>();

    public static Path dijkstra(int source, int destination) {
        Path path = new Path();

        Graph graph = readGraph();

        Vertex sourceVertex = new Vertex(source);
        Vertex destinationVertex = new Vertex(destination);

        List<RoutingVertex> visited = new ArrayList<>();
        List<Vertex> graphVertecies = graph.getVertices();


        for (Vertex graphVertecy : graphVertecies) {
            if (graphVertecy.getId() == sourceVertex.getId()) {
                unvisited.add(new RoutingVertex(graphVertecy, 0, null));
            } else {
                unvisited.add(new RoutingVertex(graphVertecy, Integer.MAX_VALUE, null));
            }
        }

        //adds all vertecies to the unvisted graph, if
        //it is the sourceVertex, the cost is 0,
        //otherwise the cost is the max integer value
        //and all do not have a previous node yet

        /*
        //checks that the unvisted list is working and fully loaded
        Iterator unvisitedIterator = unvisited.iterator();
        while(unvisitedIterator.hasNext()) {
            System.out.println(unvisited.poll().toString());
            System.out.println(" ");
        }*/

        RoutingVertex current;

        int count = 0;
        while (unvisited.size() > 0) {
            count++;
            //Collections.sort(unvisited);
            mergeSort(0, unvisited.size());
            current = unvisited.get(0);
            //sorts the unvisited list so that the
            //Routing Vertex with the lowest cost from
            //sourceVertex is at index 0
            //and makes it the routing vertex current

            ArrayList<Edge> currentEdges = current.getVertex().getEdges();
            //makes an arraylist with all of the current vertex's edges
            for (int i = 0; i < currentEdges.size(); i++) {
                if (!(contains(visited, current.getVertex()))) {
                    int cost = currentEdges.get(i).getCost() + current.getCostFromSource();
                    int routingVertexLocation = getRoutingVertex(unvisited, currentEdges.get(i).getDestination().getId());

                    if (cost < unvisited.get(routingVertexLocation).getCostFromSource()) {
                        unvisited.get(routingVertexLocation).setCostFromSource(cost);
                        unvisited.get(routingVertexLocation).setPrevious(current.getVertex());
                    }
                }
            }
            visited.add(current);
            unvisited.remove(current);
            if (current.getVertex() == destinationVertex) {
                break;
            }
        }
        current = visited.get(getRoutingVertex(visited, destinationVertex.getId()));
        while (true) {
            path.addVertex(current.getVertex());
            if (current.getPrevious() != null) {
                current = visited.get(getRoutingVertex(visited, current.getPrevious().getId()));
            } else {
                break;
            }
        }
        return path;
    }

    public static boolean contains(List<RoutingVertex> list, Vertex target) {
        boolean containsVertex = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getVertex().getId() == target.getId()) {
                containsVertex = true;
                break;
            }
        }
        return containsVertex;
    }

    public static int getRoutingVertex(List<RoutingVertex> list, int targetId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getVertex().getId() == targetId) {
                return i;
            }
        }
        return 0;
    }

    public static ArrayList<double[][]> generateData() {
        System.out.println("Generating Training Data....");
        ArrayList<double[][]> trainingData = new ArrayList<>();
        ArrayList<double[]> xDataDynamic = new ArrayList<>();
        ArrayList<double[]> yDataDynamic = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 36; j++) {
                if (i != j) {
                    double[] Xtemp = new double[36];
                    Xtemp[i] = 1;
                    Xtemp[j] = 1;
                    xDataDynamic.add(Xtemp);
                    double[] Ytemp = new double[36];
                    ArrayList<Integer> route = dijkstra(i, j).getRoute();
                    for (int k = 0; k < route.size(); k++) {
                        Ytemp[route.get(k)] = 1;
                    }
                    yDataDynamic.add(Ytemp);
                }
            }
        }
        double[][] xData = new double[xDataDynamic.size()][];
        xData = xDataDynamic.toArray(xData);

        double[][] yData = new double[yDataDynamic.size()][];
        yData = yDataDynamic.toArray(yData);

        trainingData.add(xData);
        trainingData.add(yData);

        return trainingData;
    }

    public static void mergeSort(int start, int end) {
        if (start > end && (start - end) >= 1) {
            int midPoint = (start + end) / 2;
            mergeSort(start, midPoint);
            mergeSort(midPoint + 1, end);
            merge(start, midPoint, end);
        }
    }

    public static void merge(int start, int middle, int end) {
        ArrayList<RoutingVertex> tempArray = new ArrayList<>();

        int left = start;
        int right = middle + 1;


        while (left <= middle && right <= end) {
            if (unvisited.get(left).getCostFromSource() <= unvisited.get(right).getCostFromSource()) {
                tempArray.add(unvisited.get(left));
                left++;
            } else {
                tempArray.add(unvisited.get(right));
                right++;
            }
        }
        while (left <= middle) {
            tempArray.add(unvisited.get(left));
            left++;
        }
        while (right < end) {
            tempArray.add(unvisited.get(right));
            right++;
        }
        for (int i = 0; i < tempArray.size(); start++) {
            unvisited.set(start, tempArray.get(i++));
        }
    }

    public static void main(String[] args) {
    }
}
