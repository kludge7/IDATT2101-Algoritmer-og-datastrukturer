public class Node {
    int value;
    Node nextNode;
    public Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Node getNextNode() {
        return nextNode;
    }

    @Override
    public String toString() {
        return "Node: " + getValue();
    }
}