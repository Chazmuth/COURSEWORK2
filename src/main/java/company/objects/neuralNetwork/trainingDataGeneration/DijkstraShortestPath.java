package company.objects.neuralNetwork.trainingDataGeneration;

import company.objects.graph.Edge;
import company.objects.graph.Graph;
import company.objects.graph.Path;
import company.objects.graph.Vertex;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
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
            }else{
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

    public static void generateData() {
        File trainingData;
        try {
            trainingData = new File("src/com/company/objects/neuralNetwork/trainingDataGeneration/trainingData");
            System.out.println("file created");
        } catch (Exception e1) {
            System.out.println("An error occurred" + e1);
            trainingData = null;
        }
        try {
            assert trainingData != null;
            Writer fileWriter = new FileWriter(trainingData);
            Graph graph = readGraph();
            for (int i = 0; i < graph.getVertexAmount(); i++) {
                for (int j = 0; j < graph.getVertexAmount(); j++) {
                    if (i != j) {
                        System.out.println(dijkstra(i, j).getRoute());
                        fileWriter.write(dijkstra(i, j).getRoute() + "\n");
                        System.out.println("written");
                    }
                }
            }
            fileWriter.close();
        } catch (Exception e2) {
            System.out.println("there was an error" + e2);
        }
    }

    public static void main(String[] args) {
        System.out.println(dijkstra(0, 7).getRoute());
    }
}
