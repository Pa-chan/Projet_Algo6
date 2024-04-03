package Modele;
//import Modele.Graph;
import java.util.*;

public class Dijkstra {
    public static List<Node> shortestPath(Graph graph, Node start, Node end) {
        Set<Node> visited = new HashSet<>();
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // Initialize distances
        for (Node node : graph.getAllNodes()) {
            if (node.equals(start)) {
                distance.put(node, 0);
            } else {
                distance.put(node, Integer.MAX_VALUE);
            }
        }

        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            visited.add(current);

            for (Edge edge : graph.getNeighbors(current)) {
                Node neighbor = edge.destination;
                int newDistance = distance.get(current) + edge.weight;

                if (!visited.contains(neighbor) && newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    parent.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        // Reconstruct shortest path
        List<Node> shortestPath = new ArrayList<>();
        Node current = end;
        while (current != null) {
            shortestPath.add(current);
            current = parent.get(current);
        }
        Collections.reverse(shortestPath);

        return shortestPath;
    }
}

 class Graph {
    private Map<Node, List<Edge>> adjacencyMap;

    public Graph() {
        adjacencyMap = new HashMap<>();
    }

    public void addNode(Node node) {
        // Vérifie si le nœud n'existe pas déjà dans le graphe
        if (!adjacencyMap.containsKey(node)) {
            // Si le nœud n'existe pas, initialise une nouvelle liste de voisins pour ce nœud
            adjacencyMap.put(node, new ArrayList<>());
        }
    }

    public void addEdge(Node source, Node destination, int weight) {
        adjacencyMap.get(source).add(new Edge(source, destination, weight));
        //adjacencyMap.get(destination).add(new Edge(destination, source, weight)); // Uncomment for undirected graph
    }

    public List<Edge> getNeighbors(Node node) {
        return adjacencyMap.get(node);
    }

    public Set<Node> getAllNodes() {
        return adjacencyMap.keySet();
    }
}



class Edge {
    Node source;
    Node destination;
    int weight;

    public Edge(Node source, Node destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
}

class Node {
    int row;
    int col;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Override equals and hashCode for proper functioning of HashMap
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return row == node.row && col == node.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}