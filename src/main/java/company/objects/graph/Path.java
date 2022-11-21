package company.objects.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;

public class Path {
    int cost;
    int time;
    ArrayList<Vertex> route;

    ArrayList<Integer> nodes;

    public Path() {
        this.cost = 0;
        this.time = 0;
        this.route = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public void addVertex(Vertex node) {
        route.add(node);
    }

    public String getRoute() {
        StringJoiner stringJoiner = new StringJoiner(",");
        Collections.reverse(route);
        for (int i = 0; i < this.route.size(); i++) {
            if (i < this.route.size() - 1) {
                Integer node = this.route.get(i).getId();
                stringJoiner.add(Integer.toString(node));
                nodes.add(node);
            } else {
                Integer node = this.route.get(i).getId();
                stringJoiner.add(Integer.toString(node));
                nodes.add(node);
            }
        }
        return stringJoiner.toString();
    }

    public ArrayList<Integer> getNodes() {
        return nodes;
    }
}

