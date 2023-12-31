/**
 * while-loopen til josephus solution vil loope (n-1) ganger, der n er antall personer,
 * det er fordi det er (n-1) som vil blir drept. I tillegg vil man måtte gange med m, som er intervallet,
 * dette er fordi man vil måtte kjøre loopen m flere ganger når intervallet øker. For eksempel
 * når intervallet går fra 1 til 2 vil man måtte kjøre gjennom loopen dobbelt så mange ganger.
 * Regnet ut at metoden josephusSolution vil ca. ha kompleksiteten 8*(n-1)^2 + 7*((n-1)*m)+ 9.
 */
public class CircularLinkedList {
    private Node head = null;
    private Node tail = null;
    public void addNode(int value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = newNode;
        } else {
            tail.nextNode = newNode;
        }
        tail = newNode;
        tail.nextNode = head;
    }

    /**
     * loops through linked list until it finds value it looks for
     * If I had used doublelink then the complexity would not be n, since
     * it then would not have to loop through the linked list.
     */
    public void deleteNode(int valueToDelete) {
        Node currentNode = head;                                            // 1 tilordning
        if (head == null) {                                                 // 1 sammenligning
            return;                                                         // 1 return
        }                                                                   // 3 dersom linked list er tom
        do {
            Node nextNode = currentNode.nextNode;                           // n tilordning, n oppslag
            if (nextNode.value == valueToDelete) {                          // n sammenligning
                if (tail == head) {                                         // n sammenligning
                    head = null;                                            // 1 tilordning
                    tail = null;                                            // 1 tilordning
                } else {
                    currentNode.nextNode = nextNode.nextNode;               // n tilordning
                    if (head == nextNode) {                                 // n sammenligning
                        head = head.nextNode;                               // 1 tilordning
                    }
                    if (tail == nextNode) {                                 // n sammenligning
                        tail = currentNode;                                 // 1 tilordning
                    }
                }
                break;
            }
            currentNode = nextNode;                                         // n tilordning
        } while (currentNode != head);                                      // n sammenligning
    }                                                                       // 8n + 5

    /**
     * Removed every element from the Linked List in
     * an interval.
     * @return the last Node standing
     */
    public Node josephusSolution(int interval) {
        Node currentNode = head;                            // 1 tilordning
        Node deathNode;                                     // 1 tilordning
        int integer = 1;                                    // 1 tilordning
        while(currentNode != currentNode.nextNode) {        // (n - 1) * m sammenligninger
            deathNode = currentNode;                        // (n - 1) * m tilordning
            currentNode = currentNode.getNextNode();        // (n - 1)* m tilordning, (n - 1) * m tilordning
            if(integer == interval) {                       // (n - 1) * m sammenligninger
                deleteNode(deathNode.value);                // 8*(n-1)^2 + 5 deleteNode
                integer = 0;                                // (n - 1) tilordning
            }
            integer++;                                      // (n - 1) * m inkrementer;
        }
        return head;                                        // 1 return
                                                            // 8*(n-1)^2 + 7*((n-1)*m)+ 9
    }

    public static void main(String[] args) {
        CircularLinkedList circularLinkedList = new CircularLinkedList();
        int amount = 10;
        int interval = 4;
        for(int i = 1; i <= amount; i++) {
            circularLinkedList.addNode(i);
        }
        System.out.println("Last node standing with " + amount + " nodes, when every " + interval + "th person killed, is " + circularLinkedList.josephusSolution(interval));
    }

}
