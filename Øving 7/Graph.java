import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    int amountNodes;
    int amountEdges;
    Node[] nodes;
    Edge[] previousEdges;

    public class Edge {
        Edge nextEdge;
        Node endNode;
        int capacity;
        int flow;


        public Edge(Node endNode, Edge nextEdge, int capacity) {
            this.endNode = endNode;
            this.nextEdge = nextEdge;
            this.capacity = capacity;
        }

        public int getRestCapacity() {
            return capacity - flow;
        }
    }

    public class Node {
        Edge edgy;
        Object data;
        int value;

        public Node(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ""+value;
        }
    }

    public class Predecessor {
        int distance;
        Node predecessor;
        final static int INFINITE = 1_000_000_000;


        public Predecessor() {
            distance = INFINITE;
        }
    }

    /**
     * Edmund Karp-algorithm
     * @param source the source node (start node)
     * @param sink the sink node (end node)
     * @return maximum flow
     */
    public int edmundKarpAlgorithm(Node source, Node sink) {
        int maxFlow = 0;
        while (breadthFirstSearch(source, sink)) {
            List<Node> augmentedPath = getAugmentedPath(sink);
            int flow = getBottleneckValue(augmentedPath);
            updateFlowValues(augmentedPath, flow);
            System.out.println("Flytøkende vei: " + Arrays.toString(augmentedPath.toArray()) + " - Økning: " + flow);
            maxFlow += flow;
        }
        return maxFlow;
    }

    /**
     * Breadth First Search-algorithm
     * @param source the source node (start node)
     * @param sink the sink node (end node)
     * @return true or false depending on if it could reach sink
     */
    public boolean breadthFirstSearch(Node source, Node sink) {
        initializePredecessor(source);
        Queue<Node> queue = new ArrayDeque<>(amountNodes - 1);
        queue.offer(source);
        previousEdges = new Edge[amountEdges];
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            for (Edge edge = currentNode.edgy; edge != null; edge = edge.nextEdge) {
                Predecessor predecessor = (Predecessor) edge.endNode.data;
                if (predecessor.distance == Predecessor.INFINITE) {
                    predecessor.distance = ((Predecessor)currentNode.data).distance + 1;
                    predecessor.predecessor = currentNode;
                    previousEdges[edge.endNode.value] = edge;
                    queue.offer(edge.endNode);
                }
                if (edge.endNode == sink) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Initializing the Node´s Predecessors to prepare for Breadth First Search.
     * All Node´s get infinite distance, and no Predecessors.
     * @param source the source node (start node)
     */
    public void initializePredecessor(Node source) {
        for (int i = amountNodes; i-- > 0;) {
            nodes[i].data = new Predecessor();
        }
        ((Predecessor)source.data).distance = 0;
    }

    /**
     * Get augmented path based on Breadth First Search-method
     * @param endNode sink
     * @return augmented path
     */
    public List<Node> getAugmentedPath(Node endNode) {
        List<Node> augmentedPath = new ArrayList<>();
        for (Node node = endNode; node != null; node = ((Predecessor) node.data).predecessor) {
            augmentedPath.add(0, node);
        }
        return augmentedPath;
    }


    /**
     * Get bottleneck value based on augmented path from Breath First Search-method
     * @param augmentedPath augmented path
     * @return bottleneck value
     */
    public int getBottleneckValue(List<Node> augmentedPath) {
        int bottleNeck = Predecessor.INFINITE;
        for (int i = 0; i < augmentedPath.size()-1; i++) {
            int restCapacity = getEdgeBetweenTwoNodes(nodes[augmentedPath.get(i).value], nodes[augmentedPath.get(i + 1).value]).getRestCapacity();
            if (restCapacity < bottleNeck) {
                bottleNeck = restCapacity;
            }
        }
        return bottleNeck;
    }

    /**
     * Retrace augmented path and update flow values
     * @param augmentedPath augmented path based on Breadth First Search-method
     * @param bottleNeck bottleNeck value from augmented path
     */
    public void updateFlowValues(List<Node> augmentedPath, int bottleNeck) {
        for (int i = 0; i < augmentedPath.size() - 1; i++) {
            updateEdge(nodes[augmentedPath.get(i).value], nodes[augmentedPath.get(i+1).value], bottleNeck);
        }
    }

    /**
     * Get the Edge that is between two Node´s
     * @param fromNode the Node the Edge originates from
     * @param toNode the Node the Edge is going to
     * @return Edge or null if not found
     */
    public Edge getEdgeBetweenTwoNodes(Node fromNode, Node toNode) {
        for (Edge edge = fromNode.edgy; edge != null; edge = edge.nextEdge) {
            if (edge.endNode == toNode) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Update an Edge between two nodes based on bottleneck.
     * If Edge capacity is equal to bottleneck, remove the Edge
     * and replace it with one that goes in the opposite direction
     * with capacity equal to bottleneck.
     *
     * @param startNode Node the Edge is going from
     * @param endNode Node the edge is going to
     * @param bottleneck bottleneck value
     */
    public void updateEdge(Node startNode, Node endNode, int bottleneck) {
        Edge edge = getEdgeBetweenTwoNodes(startNode, endNode);

        if (edge.getRestCapacity() == bottleneck) {
            deleteEdge(startNode, edge);
            addEdge(endNode, startNode, bottleneck);
        } else {
            edge.flow += bottleneck;
            addEdge(endNode, startNode, bottleneck);
        }
    }

    /**
     * Delete a given Edge for a Node.
     * @param startNode Node the Edge is going from
     * @param edgeToDelete Edge you want to delete
     */
    public void deleteEdge(Node startNode, Edge edgeToDelete) {
        Edge previousEdge = null;
        Edge currentEdge = startNode.edgy;

        while (currentEdge != null && currentEdge != edgeToDelete) {
            previousEdge = currentEdge;
            currentEdge = currentEdge.nextEdge;
        }

        if (currentEdge != null) {
            if (previousEdge != null) {
                previousEdge.nextEdge = currentEdge.nextEdge;
            } else {
                startNode.edgy = currentEdge.nextEdge;
            }
            currentEdge.nextEdge = null;
        }
    }

    /**
     * Add an Edge from fromNode to toNode with the given capacity.
     * @param fromNode Node the edge is going from
     * @param toNode Node the Edge is going to
     * @param capacity capacity of the new Edge
     */
    public void addEdge(Node fromNode, Node toNode, int capacity) {
        fromNode.edgy = new Edge(toNode, fromNode.edgy, capacity);
    }


    /**
     * Set Graph from file with a specific format.
     * @param fileName name of the File
     */
    private void setGraphFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringTokenizer st = new StringTokenizer(bufferedReader.readLine());
        amountNodes = Integer.parseInt(st.nextToken());
        nodes = new  Node[amountNodes];
        for (int i = 0; i < amountNodes; ++i) nodes[i] = new Node(i);
        amountEdges = Integer.parseInt(st.nextToken());
        for (int i = 0; i < amountEdges; ++i) {
            st = new StringTokenizer(bufferedReader.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            nodes[from].edgy = new Edge(nodes[to], nodes[from].edgy, value);
        }
    }


    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();

        System.out.println("\n\nFlytgraf1");
        graph.setGraphFromFile("flytgraf1");
        System.out.println("Maksimal flyt ble " + graph.edmundKarpAlgorithm(graph.nodes[0], graph.nodes[7]));

        System.out.println("\n\nFlytgraf2");
        graph.setGraphFromFile("flytgraf2");
        System.out.println("Maksimal flyt ble " + graph.edmundKarpAlgorithm(graph.nodes[0], graph.nodes[1]));

        System.out.println("\n\nFlytgraf3");
        graph.setGraphFromFile("flytgraf3");
        System.out.println("Maksimal flyt ble " + graph.edmundKarpAlgorithm(graph.nodes[0], graph.nodes[1]));

        System.out.println("\n\nFlytgraf4");
        graph.setGraphFromFile("flytgraf4");
        System.out.println("Maksimal flyt ble " + graph.edmundKarpAlgorithm(graph.nodes[0], graph.nodes[7]));

        System.out.println("\n\nFlytgraf5");
        graph.setGraphFromFile("flytgraf5");
        System.out.println("Maksimal flyt ble " + graph.edmundKarpAlgorithm(graph.nodes[0], graph.nodes[7]));

    }
}