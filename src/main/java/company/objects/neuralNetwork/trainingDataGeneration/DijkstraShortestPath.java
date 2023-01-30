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

import static company.DBUtils.SQLFunctions.readGraph;

public class DijkstraShortestPath {

    public static Path dijkstra(int source, int destination) {
        Path path = new Path();

        Graph graph = readGraph();

        Vertex sourceVertex = new Vertex(source);
        Vertex destinationVertex = new Vertex(destination);

        ArrayList<RoutingVertex> visited = new ArrayList<>();
        ArrayList<RoutingVertex> unvisited = new ArrayList<>();
        ArrayList<Vertex> graphVertecies = graph.getVertices();


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
            Collections.sort(unvisited);
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

    public static boolean contains(ArrayList<RoutingVertex> list, Vertex target) {
        boolean containsVertex = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getVertex().getId() == target.getId()) {
                containsVertex = true;
                break;
            }
        }
        return containsVertex;
    }

    public static int getRoutingVertex(ArrayList<RoutingVertex> list, int targetId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getVertex().getId() == targetId) {
                return i;
            }
        }
        return 0;
    }

    public static ArrayList<double[][]> generateData() {
        ArrayList<double[][]> trainingData = new ArrayList<>();
        Graph graph = readGraph();
        graph.printGraph();
        System.out.println(graph.getVertexAmount() + "vertex amount ");
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

    public static void main(String[] args) {

    }
}
