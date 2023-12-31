import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class that represents a Graph
 */
public class Graph {
    int amountNodes;
    int amountEdges;
    Node[] nodes;

    /**
     * Class that represents a node in a graph
     */
    static public class Node {
        public boolean visited;
        int value;
        int distance;
        Node previous;
        List<Integer> neighbours = new ArrayList<>();

        public Node(int value) {
            this.value = value;
        }

        public void addNeighbour(Integer integer) {
            neighbours.add(integer);
        }
    }

    /**
     * BreadthFirstSearch (BFS) method that starts
     * from a given Node
     * @param startNode the Node BFS starts from
     */
    public void breadthFirstSearch(Node startNode) {
        int distance = 0;
        Node[] searchedNodes = new Node[amountNodes];
        List<Node> queue = new ArrayList<>();

        startNode.visited = true;
        startNode.distance = distance;
        startNode.previous = null;
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.get(0);
            searchedNodes[currentNode.value] = currentNode;
            queue.remove(0);

            for (Integer integer : currentNode.neighbours) {
                if (!nodes[integer].visited) {
                    nodes[integer].visited = true;
                    nodes[integer].previous = currentNode;
                    nodes[integer].distance = currentNode.distance + 1;
                    queue.add(nodes[integer]);
                }
            }
        }
        System.out.printf("%-4s %-8s %6s %n", "NODE", "PREVIOUS", "DISTANCE");
        for (int i = 0; i < amountNodes; i++) {
            Node currentNode = searchedNodes[i];
            if(currentNode != null) {
                if (currentNode.previous == null) {
                    System.out.printf(" %-6s %-3s %6s %n", currentNode.value, " ", currentNode.distance);
                } else {
                    System.out.printf(" %-6s %-3s %6s %n", currentNode.value, currentNode.previous.value, currentNode.distance);
                }
            }
        }
    }

    /**
     * Method that starts a TopologicalSorting method
     * @return List that contains a valid order for the nodes
     */
    public List<Node> topologicalSorting() {
        List<Node> topologicalSortingList = new ArrayList<>();
        for (Node node : nodes) {
            topologicalSortingRecursive(node, topologicalSortingList);
        }
        return topologicalSortingList;
    }

    /**
     * The actual method for TopologicalSorting
     * @param currentNode the Node that is being checked
     * @param topologicalSortingList List that is being filled with a valid order for the nodes
     */
    private void topologicalSortingRecursive(Node currentNode, List<Node> topologicalSortingList) {
        if (!currentNode.visited) {
            currentNode.visited = true;
            for (Integer neighbour : currentNode.neighbours) {
                topologicalSortingRecursive(nodes[neighbour], topologicalSortingList);
            }
            topologicalSortingList.add(currentNode);
        }
    }

    /**
     * Method that sets the Graph from a file
     * @param fileName name of the file you want to set the Graph to
     */
    public void setGraphFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                if(i == 0) {
                    amountNodes = scanner.nextInt();
                    amountEdges = scanner.nextInt();
                    nodes = new Node[amountNodes];
                } else {
                    int currentNode = scanner.nextInt();
                    if(nodes[currentNode] == null) {
                        nodes[currentNode] = new Node(currentNode);
                        int neighbourNode = scanner.nextInt();
                        if(nodes[neighbourNode] == null) {
                            nodes[neighbourNode] = new Node(neighbourNode);
                        }

                        nodes[currentNode].addNeighbour(neighbourNode);
                    } else {
                        int neighbourNode = scanner.nextInt();
                        if(nodes[neighbourNode] == null) {
                            nodes[neighbourNode] = new Node(neighbourNode);
                        }
                        nodes[currentNode].addNeighbour(neighbourNode);
                    }
                }
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        System.out.println("BREADTH FIRST SEARCH\n");
        File f = new File("ø6g1");
        if(f.exists() && !f.isDirectory()) {
            int startNode_ø6g1 = 5;
            System.out.println("ø6g1 with start node: " + startNode_ø6g1);
            graph.setGraphFromFile("ø6g1");
            graph.breadthFirstSearch(graph.nodes[startNode_ø6g1]);
        }

        f = new File("ø6g2");
        if(f.exists() && !f.isDirectory()) {
            int startNode_ø6g2 = 0;
            System.out.println("\n----------------------------------\n");
            System.out.println("ø6g2 with start node: " + startNode_ø6g2);
            graph.setGraphFromFile("ø6g2");
            graph.breadthFirstSearch(graph.nodes[startNode_ø6g2]);
        }


        f = new File("ø6g5");
        if(f.exists() && !f.isDirectory()) {
            int startNode_ø6g5 = 0;
            System.out.println("\n----------------------------------\n");
            System.out.println("ø6g5 with start node: " + startNode_ø6g5);
            graph.setGraphFromFile("ø6g5");
            graph.breadthFirstSearch(graph.nodes[startNode_ø6g5]);
        }

        f = new File("ø6g7");
        if(f.exists() && !f.isDirectory()) {
            int startNode_ø6g7 = 0;
            System.out.println("\n----------------------------------\n");
            System.out.println("ø6g7 with start node: " + startNode_ø6g7);
            graph.setGraphFromFile("ø6g7");
            graph.breadthFirstSearch(graph.nodes[startNode_ø6g7]);
        }

        System.out.println("\nTOPOLOGICAL SORTING\n");
        f = new File("ø6g5");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("ø6g5");
            graph.setGraphFromFile("ø6g5");
            StringBuilder topologicalSortingString_ø6g5 = new StringBuilder();
            List<Node> listNode = graph.topologicalSorting();
            for(int i = listNode.size() - 1; i > -1; i--) {
                topologicalSortingString_ø6g5.append(listNode.get(i).value).append(" ");
            }
            System.out.println(topologicalSortingString_ø6g5);
        }

        f = new File("ø6g7");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("\n----------------------------------\n");
            System.out.println("ø6g7");
            graph.setGraphFromFile("ø6g7");
            StringBuilder topologicalSortingString_ø6g7 = new StringBuilder();
            List<Node> listNode = graph.topologicalSorting();
            for(int i = listNode.size() - 1; i > -1; i--) {
                topologicalSortingString_ø6g7.append(listNode.get(i).value).append(" ");
            }
            System.out.println(topologicalSortingString_ø6g7);
        }
    }
}
